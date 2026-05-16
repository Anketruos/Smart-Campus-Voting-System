package ui;

import controller.AuthController;
import controller.VoterController;
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
import model.Election;
import model.Voter;
import util.SceneUtil;
import util.SessionManager;
import util.UiFactory;

import java.util.List;

public class VoterUI {
    private final Stage stage;

    public VoterUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        VoterController controller = new VoterController(stage);
        VotingController votingController = new VotingController(stage);
        AuthController authController = new AuthController(stage);
        Voter voter = SessionManager.getCurrentVoter();
        List<Election> elections = controller.loadActiveElections();

        Button refreshBtn = UiFactory.secondaryButton("Refresh");
        refreshBtn.setOnAction(event -> new VoterUI(stage).show());

        Button logoutBtn = UiFactory.ghostButton("Logout");
        logoutBtn.setOnAction(event -> {
            authController.handleLogout();
            new LoginUI(stage).show();
        });

        HBox topBar = new HBox(
            12,
            UiFactory.label("STUDENT DASHBOARD", "eyebrow"),
            UiFactory.spacer(),
            refreshBtn,
            logoutBtn
        );
        topBar.setAlignment(Pos.CENTER_LEFT);

        HBox heroChips = new HBox(
            10,
            UiFactory.chip("Active ballots"),
            UiFactory.chip("Candidate previews"),
            UiFactory.chip("One submission only")
        );

        VBox headline = UiFactory.commandPanel(
            UiFactory.label("STUDENT DASHBOARD", "eyebrow"),
            UiFactory.label("Open elections and cast your ballot with confidence.", "page-title"),
            UiFactory.label(
                voter == null
                    ? "Select an active election to continue."
                    : "Welcome, " + voter.getName() + " (" + voter.getStudentId() + ")",
                "page-subtitle"
            ),
            heroChips
        );

        HBox metrics = UiFactory.metricRow(
            UiFactory.metricCard(String.valueOf(elections.size()), "active elections"),
            UiFactory.metricCard(voter == null ? "-" : voter.getStudentId(), "student ID"),
            UiFactory.metricCard(voter != null && voter.isHasVoted() ? "Yes" : "No", "has voted before")
        );

        ListView<Election> electionList = new ListView<>();
        electionList.getItems().setAll(elections);
        electionList.setPrefHeight(360);
        electionList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Election item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle() + "\n" + item.getStartDate() + " to " + item.getEndDate());
                }
            }
        });

        VBox listPanel = UiFactory.panel(
            UiFactory.label("Available ballots", "section-title"),
            UiFactory.label("Pick one election to see details and continue.", "section-copy"),
            electionList
        );
        VBox.setVgrow(electionList, Priority.ALWAYS);

        VBox detailsCard = UiFactory.detailCard(
            "Election details",
            "Choose an election from the list to see its description, date range, and whether you can still vote."
        );
        detailsCard.setPrefWidth(330);

        Button voteBtn = new Button("Open Ballot");
        Button resultsBtn = UiFactory.secondaryButton("View Results");
        Button backBtn = UiFactory.ghostButton("Back to Login");
        backBtn.setOnAction(event -> {
            authController.handleLogout();
            new LoginUI(stage).show();
        });

        voteBtn.setDisable(true);
        resultsBtn.setDisable(true);

        voteBtn.setOnAction(event -> {
            Election selected = electionList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.openVoting(selected);
            }
        });

        resultsBtn.setOnAction(event -> {
            Election selected = electionList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                new ResultUI(stage, selected).show();
            }
        });

        UiFactory.makeWide(voteBtn, resultsBtn, backBtn);

        electionList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            boolean hasSelection = selected != null;
            resultsBtn.setDisable(!hasSelection);
            voteBtn.setDisable(!hasSelection);

            if (!hasSelection) {
                detailsCard.getChildren().setAll(
                    UiFactory.label("Election details", "section-title"),
                    UiFactory.label("Choose an election from the list to see its description, date range, and whether you can still vote.", "section-copy")
                );
                return;
            }

            boolean alreadyVoted = votingController.hasCurrentVoterVotedInElection(selected.getId());
            voteBtn.setDisable(alreadyVoted);
            detailsCard.getChildren().setAll(
                UiFactory.label(selected.getTitle(), "section-title"),
                UiFactory.label(selected.getDescription(), "section-copy"),
                UiFactory.label("Voting window: " + selected.getStartDate() + " to " + selected.getEndDate(), "section-copy"),
                UiFactory.label(
                    alreadyVoted ? "Status: you have already voted in this election." : "Status: ballot available for you to submit.",
                    "section-copy"
                )
            );
        });

        VBox actionPanel = UiFactory.commandPanel(
            UiFactory.label("Selected election", "section-title"),
            detailsCard,
            voteBtn,
            resultsBtn,
            backBtn
        );
        actionPanel.setPrefWidth(350);

        HBox content = new HBox(20, listPanel, actionPanel);
        content.setAlignment(Pos.TOP_CENTER);
        HBox.setHgrow(listPanel, Priority.ALWAYS);

        VBox root = new VBox(22, topBar, headline, metrics, content);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(UiFactory.scrollPage(root), 1160, 760);
        SceneUtil.applyStyles(scene);

        stage.setScene(scene);
        stage.setTitle("Student Dashboard");
        stage.show();
    }
}
