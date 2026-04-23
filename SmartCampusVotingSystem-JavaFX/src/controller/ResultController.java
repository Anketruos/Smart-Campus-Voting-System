package controller;

import javafx.stage.Stage;
import service.ResultService;
import util.AlertUtil;
import java.util.Map;

public class ResultController {
    private final ResultService resultService = new ResultService();
    private final Stage stage;

    public ResultController(Stage stage) {
        this.stage = stage;
    }

    public Map<String, Integer> loadResults(int electionId) {
        try {
            return resultService.getResults(electionId);
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
            return Map.of();
        }
    }

    public String loadWinner(int electionId) {
        try {
            return resultService.getWinner(electionId);
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
            return "Unknown";
        }
    }
}
