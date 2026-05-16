package service;

import dao.CandidateDAO;
import dao.ElectionDAO;
import dao.VoteDAO;
import dao.VoterDAO;
import model.Candidate;
import model.Election;
import model.Vote;
import util.SessionManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Handles vote casting with thread-safety (Unit 6: Multithreading & Synchronization).
 */
public class VotingService {
    private final VoteDAO voteDAO = new VoteDAO();
    private final VoterDAO voterDAO = new VoterDAO();
    private final ElectionDAO electionDAO = new ElectionDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();

    public synchronized void castVoteForCurrentVoter(int electionId, int candidateId) throws Exception {
        if (!SessionManager.isVoterLoggedIn()) {
            throw new Exception("No voter is logged in.");
        }

        int voterId = SessionManager.getCurrentVoter().getId();
        castVote(voterId, electionId, candidateId);
        SessionManager.getCurrentVoter().setHasVoted(voterDAO.hasAnyVote(voterId));
    }

    public synchronized void castVote(int voterId, int electionId, int candidateId) throws Exception {
        Election election = electionDAO.findById(electionId);
        if (election == null) {
            throw new Exception("Election not found.");
        }
        if (!isElectionOpen(election)) {
            throw new Exception("Voting is closed for this election.");
        }

        Candidate candidate = candidateDAO.findById(candidateId);
        if (candidate == null || candidate.getElectionId() != electionId) {
            throw new Exception("Selected candidate does not belong to this election.");
        }

        if (voteDAO.hasVoted(voterId, electionId)) {
            throw new Exception("You have already voted in this election.");
        }

        Vote vote = new Vote(0, voterId, electionId, candidateId, LocalDateTime.now());
        try {
            voteDAO.insert(vote);
            voterDAO.markVoted(voterId);
        } catch (SQLException exception) {
            if (exception.getMessage() != null && exception.getMessage().toLowerCase().contains("unique")) {
                throw new Exception("You have already voted in this election.");
            }
            throw exception;
        }
    }

    public boolean hasCurrentVoterVotedInElection(int electionId) throws Exception {
        if (!SessionManager.isVoterLoggedIn()) {
            return false;
        }
        return voteDAO.hasVoted(SessionManager.getCurrentVoter().getId(), electionId);
    }

    private boolean isElectionOpen(Election election) {
        LocalDate today = LocalDate.now();
        return election.isActive()
            && !today.isBefore(election.getStartDate())
            && !today.isAfter(election.getEndDate());
    }
}
