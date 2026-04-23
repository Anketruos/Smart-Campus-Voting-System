package model;

public class PreapprovedVoter {
    private int id;
    private String studentId;
    private String email;
    private boolean registered;

    public PreapprovedVoter() {}

    public PreapprovedVoter(int id, String studentId, String email, boolean registered) {
        this.id = id;
        this.studentId = studentId;
        this.email = email;
        this.registered = registered;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isRegistered() { return registered; }
    public void setRegistered(boolean registered) { this.registered = registered; }
}
