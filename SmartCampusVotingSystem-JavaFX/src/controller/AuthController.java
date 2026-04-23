package controller;

import javafx.stage.Stage;
import model.Admin;
import model.Voter;
import service.AuthService;
import ui.AdminUI;
import ui.VoterUI;
import util.AlertUtil;

public class AuthController {
    private final AuthService authService = new AuthService();
    private final Stage stage;

    public AuthController(Stage stage) {
        this.stage = stage;
    }

    public void handleVoterLogin(String studentId, String password) {
        try {
            Voter voter = authService.loginVoter(studentId, password);
            new VoterUI(stage).show();
        } catch (Exception e) {
            AlertUtil.showError("Login Failed", e.getMessage());
        }
    }

    public void handleAdminLogin(String username, String password) {
        try {
            Admin admin = authService.loginAdmin(username, password);
            new AdminUI(stage).show();
        } catch (Exception e) {
            AlertUtil.showError("Login Failed", e.getMessage());
        }
    }

    public void handleRegister(String studentId, String name, String email, String password) {
        try {
            authService.register(studentId, name, email, password);
            AlertUtil.showInfo("Registration Successful", "You can now log in with your student ID.");
        } catch (Exception e) {
            AlertUtil.showError("Registration Failed", e.getMessage());
        }
    }

    public void handleLogout() {
        authService.logout();
    }
}
