package ui;

import controller.VotingController;
import controller.CandidateController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Candidate;
import model.Election;
import java.util.List;

public class VoteUI {
    private final Stage stage;
    private final Election election;

    public VoteUI(Stage stage, Election election) {
        this.stage = stage;
        this.election = election;
    }

    public void show() {
        VotingController votingController = new VotingController(stage);
        CandidateController candidateController = new CandidateController(stage);

        Label title = new Label("Vote: " + election.getTitle());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<Candidate> candidateList = new ListView<>();
        List<Candidate> candidates = candidateController.loadByElection(election.getId());
        candidateList.getItems().addAll(candidates);
        candidateList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Candidate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " — " + item.getPosition());
            }
        });

        Button castBtn = new Button("Cast Vote");
        castBtn.setMaxWidth(Double.MAX_VALUE);
        castBtn.setOnAction(e -> {
            Candidate selected = candidateList.getSelectionModel().getSelectedItem();
            if (selected != null) votingController.handleCastVote(election.getId(), selected.getId());
        });

        Button backBtn = new Button("Back");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setOnAction(e -> new VoterUI(stage).show());

        VBox root = new VBox(12, title, candidateList, castBtn, backBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 480, 420));
        stage.setTitle("Cast Vote");
        stage.show();
    }
}
