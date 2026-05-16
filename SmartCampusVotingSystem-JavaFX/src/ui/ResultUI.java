package ui;

import controller.ResultController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Election;
import util.SceneUtil;
import util.SessionManager;
import util.UiFactory;

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
        Map<String, Integer> results = controller.loadResults(election.getId());
        String winner = controller.loadWinner(election.getId());
        int total = controller.loadTotalVotes(election.getId());

        Button backBtn = UiFactory.ghostButton("Back");
        backBtn.setOnAction(event -> {
            if (SessionManager.isAdminLoggedIn()) {
                new ElectionUI(stage).show();
            } else {
                new VoterUI(stage).show();
            }
        });

        HBox topBar = new HBox(
            12,
            UiFactory.label("RESULTS", "eyebrow"),
            UiFactory.spacer(),
            backBtn
        );

        FlowPane heroChips = new FlowPane(10, 10,
            UiFactory.chip("Ranked totals"),
            UiFactory.chip("Instant summary"),
            UiFactory.chip("Election: " + election.getTitle())
        );

        VBox headline = UiFactory.commandPanel(
            UiFactory.label("RESULTS", "eyebrow"),
            UiFactory.label("Election results overview", "page-title"),
            UiFactory.label(election.getTitle(), "page-subtitle"),
            heroChips
        );

        HBox metrics = UiFactory.metricRow(
            UiFactory.metricCard(winner, "current winner"),
            UiFactory.metricCard(String.valueOf(total), "total votes cast"),
            UiFactory.metricCard(String.valueOf(results.size()), "candidates counted")
        );

        TableView<ResultRow> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(380);

        TableColumn<ResultRow, String> candidateColumn = new TableColumn<>("Candidate");
        candidateColumn.setCellValueFactory(data -> data.getValue().candidateProperty());

        TableColumn<ResultRow, Number> votesColumn = new TableColumn<>("Votes");
        votesColumn.setCellValueFactory(data -> data.getValue().votesProperty());

        tableView.getColumns().addAll(candidateColumn, votesColumn);
        results.forEach((candidate, votes) -> tableView.getItems().add(new ResultRow(candidate, votes)));

        VBox tablePanel = UiFactory.panel(
            UiFactory.label("Vote totals", "section-title"),
            UiFactory.label("Candidates are ordered by the number of votes they have received.", "section-copy"),
            tableView
        );
        VBox.setVgrow(tableView, Priority.ALWAYS);

        VBox root = new VBox(22, topBar, headline, metrics, tablePanel);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(UiFactory.scrollPage(root), 1000, 720);
        SceneUtil.applyStyles(scene);

        stage.setScene(scene);
        stage.setTitle("Election Results");
        stage.show();
    }

    private static class ResultRow {
        private final SimpleStringProperty candidate;
        private final SimpleIntegerProperty votes;

        private ResultRow(String candidate, int votes) {
            this.candidate = new SimpleStringProperty(candidate);
            this.votes = new SimpleIntegerProperty(votes);
        }

        private SimpleStringProperty candidateProperty() {
            return candidate;
        }

        private SimpleIntegerProperty votesProperty() {
            return votes;
        }
    }
}
