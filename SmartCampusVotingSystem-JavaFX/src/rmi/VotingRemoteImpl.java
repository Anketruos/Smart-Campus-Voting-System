package rmi;

import dao.VoteDAO;
import dao.VoterDAO;
import model.Vote;
import service.ResultService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * RMI Server Implementation (Unit 5: Remote Method Invocation).
 * Student clients can invoke castVote() on this object remotely,
 * as if it were a local method call.
 */
public class VotingRemoteImpl extends UnicastRemoteObject implements VotingRemote {

    private final VoteDAO voteDAO = new VoteDAO();
    private final VoterDAO voterDAO = new VoterDAO();
    private final ResultService resultService = new ResultService();

    public VotingRemoteImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized String castVote(int voterId, int electionId, int candidateId) throws RemoteException {
        try {
            if (voteDAO.hasVoted(voterId, electionId))
                return "ERROR: You have already voted in this election.";

            Vote vote = new Vote(0, voterId, electionId, candidateId, LocalDateTime.now());
            voteDAO.insert(vote);
            voterDAO.markVoted(voterId);
            return "OK: Vote recorded successfully.";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @Override
    public String getResults(int electionId) throws RemoteException {
        try {
            Map<String, Integer> results = resultService.getResults(electionId);
            StringBuilder sb = new StringBuilder();
            results.forEach((name, count) -> sb.append(name).append(": ").append(count).append(" vote(s)\n"));
            return sb.isEmpty() ? "No votes cast yet." : sb.toString();
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
