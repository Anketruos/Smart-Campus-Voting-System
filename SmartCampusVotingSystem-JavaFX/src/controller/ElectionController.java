package controller;

import javafx.stage.Stage;
import model.Candidate;
import model.Election;
import service.ElectionService;
import util.AlertUtil;
import java.util.List;

public class ElectionController {
    private final ElectionService electionService = new ElectionService();
    private final Stage stage;

    public ElectionController(Stage stage) {
        this.stage = stage;
    }

    public List<Election> loadAll() {
        try {
            return electionService.getAllElections();
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
            return List.of();
        }
    }

    public void handleUpdate(Election election) {
        try {
            electionService.updateElection(election);
            AlertUtil.showInfo("Success", "Election updated.");
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }

    public List<Candidate> loadCandidates(int electionId) {
        try {
            return electionService.getCandidatesForElection(electionId);
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
            return List.of();
        }
    }
}
