package ui;

import controller.CandidateController;
import controller.VotingController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Candidate;
import model.Election;
import util.SceneUtil;
import util.UiFactory;

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
        List<Candidate> candidates = candidateController.loadByElection(election.getId());
        boolean alreadyVoted = votingController.hasCurrentVoterVotedInElection(election.getId());

        Button backBtn = UiFactory.ghostButton("Back to Dashboard");
        backBtn.setOnAction(event -> new VoterUI(stage).show());

        HBox topBar = new HBox(
            12,
            UiFactory.label("BALLOT VIEW", "eyebrow"),
            UiFactory.spacer(),
            backBtn
        );
        topBar.setAlignment(Pos.CENTER_LEFT);

        HBox heroChips = new HBox(
            10,
            UiFactory.chip("Read candidate profiles"),
            UiFactory.chip("Review before submit"),
            UiFactory.chip(alreadyVoted ? "Ballot closed" : "Ballot open")
        );

        VBox headline = UiFactory.commandPanel(
            UiFactory.label("BALLOT VIEW", "eyebrow"),
            UiFactory.label("Cast your vote carefully.", "page-title"),
            UiFactory.label(
                election.getTitle() + " | " + election.getStartDate() + " to " + election.getEndDate(),
                "page-subtitle"
            ),
            heroChips
        );

        HBox metrics = UiFactory.metricRow(
            UiFactory.metricCard(String.valueOf(candidates.size()), "candidates on this ballot"),
            UiFactory.metricCard(alreadyVoted ? "Closed" : "Open", "your voting status")
        );

        ListView<Candidate> candidateList = new ListView<>();
        candidateList.getItems().setAll(candidates);
        candidateList.setPrefHeight(360);
        candidateList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Candidate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + "\n" + item.getPosition());
                }
            }
        });

        VBox ballotPanel = UiFactory.panel(
            UiFactory.label("Candidate list", "section-title"),
            UiFactory.label("Select one candidate before submitting your final vote.", "section-copy"),
            candidateList
        );
        VBox.setVgrow(candidateList, Priority.ALWAYS);

        VBox profileCard = UiFactory.detailCard(
            "Candidate profile",
            "Choose a candidate to read the campaign summary and review your selection."
        );

        Button castBtn = new Button("Submit Vote");
        castBtn.setDisable(alreadyVoted);
        castBtn.setOnAction(event -> {
            Candidate selected = candidateList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                votingController.handleCastVote(election.getId(), selected.getId());
            }
        });

        Button resultsBtn = UiFactory.secondaryButton("View Current Results");
        resultsBtn.setOnAction(event -> new ResultUI(stage, election).show());

        UiFactory.makeWide(castBtn, resultsBtn, backBtn);

        candidateList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            castBtn.setDisable(selected == null || alreadyVoted);
            if (selected == null) {
                profileCard.getChildren().setAll(
                    UiFactory.label("Candidate profile", "section-title"),
                    UiFactory.label("Choose a candidate to read the campaign summary and review your selection.", "section-copy")
                );
            } else {
                profileCard.getChildren().setAll(
                    UiFactory.label(selected.getName(), "section-title"),
                    UiFactory.label(selected.getPosition(), "section-copy"),
                    UiFactory.label(
                        selected.getBio() == null || selected.getBio().isBlank()
                            ? "No campaign statement provided."
                            : selected.getBio(),
                        "section-copy"
                    ),
                    UiFactory.label(
                        alreadyVoted
                            ? "You have already submitted your vote in this election."
                            : "Your choice becomes final once you click Submit Vote.",
                        "section-copy"
                    )
                );
            }
        });

        VBox actionPanel = UiFactory.commandPanel(
            UiFactory.label("Selection details", "section-title"),
            profileCard,
            castBtn,
            resultsBtn,
            backBtn
        );
        actionPanel.setPrefWidth(360);

        HBox content = new HBox(20, ballotPanel, actionPanel);
        content.setAlignment(Pos.TOP_CENTER);
        HBox.setHgrow(ballotPanel, Priority.ALWAYS);

        VBox root = new VBox(22, topBar, headline, metrics, content);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(UiFactory.scrollPage(root), 1160, 760);
        SceneUtil.applyStyles(scene);

        stage.setScene(scene);
        stage.setTitle("Cast Vote");
        stage.show();
    }
}
