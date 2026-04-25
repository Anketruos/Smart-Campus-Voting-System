package service;

import dao.VoteDAO;
import dao.VoterDAO;
import model.Vote;
import util.SessionManager;
import java.time.LocalDateTime;

/**
 * Handles vote casting with thread-safety (Unit 6: Multithreading & Synchronization).
 * The castVote method is synchronized to prevent race conditions when multiple
 * students vote at the exact same time.
 */
public class VotingService {
    private final VoteDAO voteDAO = new VoteDAO();
    private final VoterDAO voterDAO = new VoterDAO();

    /**
     * Casts a vote for the logged-in voter.
     * Synchronized to ensure two simultaneous votes don't corrupt data (Unit 6).
     */
    public synchronized void castVote(int electionId, int candidateId) throws Exception {
        if (!SessionManager.isVoterLoggedIn())
            throw new Exception("No voter is logged in.");

        int voterId = SessionManager.getCurrentVoter().getId();

        // Check double-voting before inserting (data integrity — Unit 3: JDBC)
        if (voteDAO.hasVoted(voterId, electionId))
            throw new Exception("You have already voted in this election.");

        // Vote is immutable once created (Unit 1: Immutability)
        Vote vote = new Vote(0, voterId, electionId, candidateId, LocalDateTime.now());
        voteDAO.insert(vote);
        voterDAO.markVoted(voterId);

        SessionManager.getCurrentVoter().setHasVoted(true);
    }
}
