package service;

import dao.AdminDAO;
import dao.PreapprovedVoterDAO;
import dao.VoterDAO;
import model.Admin;
import model.Voter;
import util.PasswordUtil;
import util.SessionManager;
import util.ValidationUtil;

public class AuthService {
    private final AdminDAO adminDAO = new AdminDAO();
    private final VoterDAO voterDAO = new VoterDAO();
    private final PreapprovedVoterDAO preapprovedVoterDAO = new PreapprovedVoterDAO();

    public Voter loginVoter(String studentId, String password) throws Exception {
        if (!ValidationUtil.isNotEmpty(studentId) || !ValidationUtil.isNotEmpty(password)) {
            throw new IllegalArgumentException("Student ID and password are required.");
        }

        Voter voter = voterDAO.findByStudentId(studentId.trim().toUpperCase());
        if (voter == null || !PasswordUtil.verify(password, voter.getPasswordHash())) {
            throw new Exception("Invalid student ID or password.");
        }

        SessionManager.setCurrentVoter(voter);
        return voter;
    }

    public Admin loginAdmin(String username, String password) throws Exception {
        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(password)) {
            throw new IllegalArgumentException("Username and password are required.");
        }

        Admin admin = adminDAO.findByUsername(username.trim());
        if (admin == null || !PasswordUtil.verify(password, admin.getPasswordHash())) {
            throw new Exception("Invalid admin credentials.");
        }

        SessionManager.setCurrentAdmin(admin);
        return admin;
    }

    public Voter register(String studentId, String name, String email, String password) throws Exception {
        if (!ValidationUtil.isValidStudentId(studentId)) {
            throw new IllegalArgumentException("Invalid student ID.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address.");
        }
        if (!ValidationUtil.isValidPassword(password)) {
            throw new IllegalArgumentException("Password must be at least 8 characters with letters and numbers.");
        }
        if (!ValidationUtil.isNotEmpty(name)) {
            throw new IllegalArgumentException("Name is required.");
        }

        String normalizedStudentId = studentId.trim().toUpperCase();
        String normalizedEmail = email.trim().toLowerCase();

        if (!preapprovedVoterDAO.isPreapproved(normalizedStudentId, normalizedEmail)) {
            throw new Exception("You are not on the approved voter list or your registration has already been used.");
        }

        if (voterDAO.findByStudentId(normalizedStudentId) != null) {
            throw new Exception("Student ID is already registered.");
        }

        if (voterDAO.findByEmail(normalizedEmail) != null) {
            throw new Exception("Email address is already registered.");
        }

        Voter voter = new Voter(
            0,
            normalizedStudentId,
            name.trim(),
            normalizedEmail,
            PasswordUtil.hash(password),
            false
        );
        voterDAO.insert(voter);
        preapprovedVoterDAO.markRegistered(normalizedStudentId);
        return voter;
    }

    public void logout() {
        SessionManager.logout();
    }
}
