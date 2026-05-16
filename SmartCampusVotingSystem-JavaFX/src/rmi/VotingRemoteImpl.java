package rmi;

import service.ResultService;
import service.VotingService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class VotingRemoteImpl extends UnicastRemoteObject implements VotingRemote {

    private final VotingService votingService = new VotingService();
    private final ResultService resultService = new ResultService();

    public VotingRemoteImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized String castVote(int voterId, int electionId, int candidateId) throws RemoteException {
        try {
            votingService.castVote(voterId, electionId, candidateId);
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
