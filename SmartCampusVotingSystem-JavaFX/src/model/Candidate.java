package model;

import java.io.Serializable;

/** Serializable so candidate data can be saved/transmitted (Unit 2). */
public class Candidate implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int electionId;
    private String name;
    private String position;
    private String bio;

    public Candidate() {}

    public Candidate(int id, int electionId, String name, String position, String bio) {
        this.id = id;
        this.electionId = electionId;
        this.name = name;
        this.position = position;
        this.bio = bio;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getElectionId() { return electionId; }
    public void setElectionId(int electionId) { this.electionId = electionId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
