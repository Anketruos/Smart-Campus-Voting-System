package controller;

import javafx.stage.Stage;
import model.Candidate;
import service.ElectionService;
import util.AlertUtil;
import java.util.List;

public class CandidateController {
    private final ElectionService electionService = new ElectionService();
    private final Stage stage;

    public CandidateController(Stage stage) {
        this.stage = stage;
    }

    public List<Candidate> loadByElection(int electionId) {
        try {
            return electionService.getCandidatesForElection(electionId);
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
            return List.of();
        }
    }

    public void handleAdd(Candidate candidate) {
        try {
            electionService.addCandidate(candidate);
            AlertUtil.showInfo("Success", "Candidate added.");
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }

    public void handleRemove(int candidateId) {
        try {
            electionService.removeCandidate(candidateId);
            AlertUtil.showInfo("Success", "Candidate removed.");
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }
}
