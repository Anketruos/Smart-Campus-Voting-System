package controller;

import dao.PreapprovedVoterDAO;
import javafx.stage.Stage;
import model.PreapprovedVoter;
import service.ElectionService;
import model.Election;
import model.Candidate;
import ui.ElectionUI;
import util.AlertUtil;
import java.util.List;

public class AdminController {
    private final ElectionService electionService = new ElectionService();
    private final PreapprovedVoterDAO preapprovedVoterDAO = new PreapprovedVoterDAO();
    private final Stage stage;

    public AdminController(Stage stage) {
        this.stage = stage;
    }

    public void handleCreateElection(Election election) {
        try {
            electionService.createElection(election);
            AlertUtil.showInfo("Success", "Election created successfully.");
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }

    public void handleDeleteElection(int electionId) {
        try {
            electionService.deleteElection(electionId);
            AlertUtil.showInfo("Success", "Election deleted.");
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }

    public void handleAddCandidate(Candidate candidate) {
        try {
            electionService.addCandidate(candidate);
            AlertUtil.showInfo("Success", "Candidate added.");
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }

    public void handleRemoveCandidate(int candidateId) {
        try {
            electionService.removeCandidate(candidateId);
            AlertUtil.showInfo("Success", "Candidate removed.");
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }

    public List<Election> loadElections() {
        try {
            return electionService.getAllElections();
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
            return List.of();
        }
    }

    public void handleAddPreapprovedVoter(String studentId, String email) {
        try {
            preapprovedVoterDAO.insert(new PreapprovedVoter(0, studentId, email, false));
            AlertUtil.showInfo("Success", "Voter pre-approved.");
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }

    public void openElectionManagement() {
        new ElectionUI(stage).show();
    }
}
