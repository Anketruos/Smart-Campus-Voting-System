package service;

import dao.PreapprovedVoterDAO;
import dao.VoterDAO;
import model.Admin;
import model.Voter;
import util.PasswordUtil;
import util.SessionManager;
import util.ValidationUtil;

public class AuthService {
    private final VoterDAO voterDAO = new VoterDAO();
    private final PreapprovedVoterDAO preapprovedVoterDAO = new PreapprovedVoterDAO();

    // Hardcoded admin credentials (replace with DB-backed admin in production)
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD_HASH;

    static {
        String hash = "";
        try { hash = PasswordUtil.hash("admin123"); } catch (Exception ignored) {}
        ADMIN_PASSWORD_HASH = hash;
    }

    public Voter loginVoter(String studentId, String password) throws Exception {
        if (!ValidationUtil.isNotEmpty(studentId) || !ValidationUtil.isNotEmpty(password))
            throw new IllegalArgumentException("Student ID and password are required.");

        Voter voter = voterDAO.findByStudentId(studentId);
        if (voter == null || !PasswordUtil.verify(password, voter.getPasswordHash()))
            throw new Exception("Invalid student ID or password.");

        SessionManager.setCurrentVoter(voter);
        return voter;
    }

    public Admin loginAdmin(String username, String password) throws Exception {
        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(password))
            throw new IllegalArgumentException("Username and password are required.");

        if (!username.equals(ADMIN_USERNAME) || !PasswordUtil.verify(password, ADMIN_PASSWORD_HASH))
            throw new Exception("Invalid admin credentials.");

        Admin admin = new Admin(1, username, ADMIN_PASSWORD_HASH);
        SessionManager.setCurrentAdmin(admin);
        return admin;
    }

    public Voter register(String studentId, String name, String email, String password) throws Exception {
        if (!ValidationUtil.isValidStudentId(studentId)) throw new IllegalArgumentException("Invalid student ID.");
        if (!ValidationUtil.isValidEmail(email)) throw new IllegalArgumentException("Invalid email address.");
        if (!ValidationUtil.isValidPassword(password)) throw new IllegalArgumentException("Password must be at least 8 characters with letters and numbers.");
        if (!ValidationUtil.isNotEmpty(name)) throw new IllegalArgumentException("Name is required.");

        if (!preapprovedVoterDAO.isPreapproved(studentId, email))
            throw new Exception("You are not on the approved voter list.");

        if (voterDAO.findByStudentId(studentId) != null)
            throw new Exception("Student ID is already registered.");

        Voter voter = new Voter(0, studentId, name, email, PasswordUtil.hash(password), false);
        voterDAO.insert(voter);
        preapprovedVoterDAO.markRegistered(studentId);
        return voter;
    }

    public void logout() {
        SessionManager.logout();
    }
}
