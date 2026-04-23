package model;

import java.time.LocalDateTime;

public class Vote {
    private int id;
    private int voterId;
    private int electionId;
    private int candidateId;
    private LocalDateTime timestamp;

    public Vote() {}

    public Vote(int id, int voterId, int electionId, int candidateId, LocalDateTime timestamp) {
        this.id = id;
        this.voterId = voterId;
        this.electionId = electionId;
        this.candidateId = candidateId;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVoterId() { return voterId; }
    public void setVoterId(int voterId) { this.voterId = voterId; }

    public int getElectionId() { return electionId; }
    public void setElectionId(int electionId) { this.electionId = electionId; }

    public int getCandidateId() { return candidateId; }
    public void setCandidateId(int candidateId) { this.candidateId = candidateId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
