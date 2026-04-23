package controller;

import javafx.stage.Stage;
import service.VotingService;
import ui.VoterUI;
import util.AlertUtil;

public class VotingController {
    private final VotingService votingService = new VotingService();
    private final Stage stage;

    public VotingController(Stage stage) {
        this.stage = stage;
    }

    public void handleCastVote(int electionId, int candidateId) {
        try {
            votingService.castVote(electionId, candidateId);
            AlertUtil.showInfo("Vote Cast", "Your vote has been recorded successfully.");
            new VoterUI(stage).show();
        } catch (Exception e) {
            AlertUtil.showError("Voting Error", e.getMessage());
        }
    }
}
