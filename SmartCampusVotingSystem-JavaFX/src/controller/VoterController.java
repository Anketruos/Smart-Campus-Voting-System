package controller;

import javafx.stage.Stage;
import service.ElectionService;
import model.Election;
import ui.VoteUI;
import util.AlertUtil;
import java.util.List;

public class VoterController {
    private final ElectionService electionService = new ElectionService();
    private final Stage stage;

    public VoterController(Stage stage) {
        this.stage = stage;
    }

    public List<Election> loadActiveElections() {
        try {
            return electionService.getActiveElections();
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
            return List.of();
        }
    }

    public void openVoting(Election election) {
        new VoteUI(stage, election).show();
    }
}
