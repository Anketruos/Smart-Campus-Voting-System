package ui;

import controller.ResultController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Election;
import java.util.Map;

public class ResultUI {
    private final Stage stage;
    private final Election election;

    public ResultUI(Stage stage, Election election) {
        this.stage = stage;
        this.election = election;
    }

    public void show() {
        ResultController controller = new ResultController(stage);

        Label title = new Label("Results: " + election.getTitle());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Map<String, Integer> results = controller.loadResults(election.getId());
        String winner = controller.loadWinner(election.getId());
        int total = controller.loadTotalVotes(election.getId());

        ListView<String> resultList = new ListView<>();
        results.forEach((name, count) -> resultList.getItems().add(name + ": " + count + " vote(s)"));

        Label totalLabel = new Label("Total votes cast: " + total);
        Label winnerLabel = new Label("Winner: " + winner);
        winnerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c5fb3;");

        Button backBtn = new Button("Back");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setOnAction(e -> new VoterUI(stage).show());

        VBox root = new VBox(12, title, resultList, totalLabel, winnerLabel, backBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 480, 420));
        stage.setTitle("Election Results");
        stage.show();
    }
}
