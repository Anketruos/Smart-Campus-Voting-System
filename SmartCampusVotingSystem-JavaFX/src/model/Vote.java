package model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Immutable Vote object — once a vote is cast its data cannot be altered,
 * ensuring election integrity (Unit 1: Immutability).
 * Implements Serializable for object serialization (Unit 2).
 */
public final class Vote implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final int voterId;
    private final int electionId;
    private final int candidateId;
    private final LocalDateTime timestamp;

    public Vote(int id, int voterId, int electionId, int candidateId, LocalDateTime timestamp) {
        this.id = id;
        this.voterId = voterId;
        this.electionId = electionId;
        this.candidateId = candidateId;
        this.timestamp = timestamp;
    }

    public int getId()           { return id; }
    public int getVoterId()      { return voterId; }
    public int getElectionId()   { return electionId; }
    public int getCandidateId()  { return candidateId; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Vote{id=" + id + ", voter=" + voterId + ", election=" + electionId
             + ", candidate=" + candidateId + ", time=" + timestamp + "}";
    }
}
