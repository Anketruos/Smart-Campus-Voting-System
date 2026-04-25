package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMI Remote Interface (Unit 5: Remote Method Invocation).
 * Defines methods that can be called remotely by student client machines.
 */
public interface VotingRemote extends Remote {

    /**
     * Cast a vote remotely.
     * @param voterId    the voter's database ID
     * @param electionId the election ID
     * @param candidateId the candidate ID
     * @return confirmation message
     */
    String castVote(int voterId, int electionId, int candidateId) throws RemoteException;

    /**
     * Get vote results for an election remotely.
     * @param electionId the election ID
     * @return formatted results string
     */
    String getResults(int electionId) throws RemoteException;
}
