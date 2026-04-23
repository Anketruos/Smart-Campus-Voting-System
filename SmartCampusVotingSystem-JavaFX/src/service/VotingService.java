package service;

import dao.VoteDAO;
import dao.VoterDAO;
import model.Vote;
import util.SessionManager;
import java.time.LocalDateTime;

public class VotingService {
    private final VoteDAO voteDAO = new VoteDAO();
    private final VoterDAO voterDAO = new VoterDAO();

    public void castVote(int electionId, int candidateId) throws Exception {
        if (!SessionManager.isVoterLoggedIn())
            throw new Exception("No voter is logged in.");

        int voterId = SessionManager.getCurrentVoter().getId();

        if (voteDAO.hasVoted(voterId, electionId))
            throw new Exception("You have already voted in this election.");

        Vote vote = new Vote(0, voterId, electionId, candidateId, LocalDateTime.now());
        voteDAO.insert(vote);
        voterDAO.markVoted(voterId);

        // Update session
        SessionManager.getCurrentVoter().setHasVoted(true);
    }
}
