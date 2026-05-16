package ui;

import controller.AdminController;
import controller.ElectionController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Candidate;
import model.Election;
import util.AlertUtil;
import util.SceneUtil;
import util.UiFactory;

import java.time.LocalDate;

public class ElectionUI {
    private final Stage stage;

    public ElectionUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        ElectionController electionController = new ElectionController(stage);
        AdminController adminController = new AdminController(stage);

        ListView<Election> electionList = new ListView<>();
        electionList.getItems().setAll(electionController.loadAll());
        electionList.setPrefHeight(390);
        electionList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Election item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle() + "\n" + item.getStartDate() + " to " + item.getEndDate()
                        + (item.isActive() ? " | Active" : " | Inactive"));
                }
            }
        });

        Button createBtn = new Button("Create Election");
        createBtn.setOnAction(event -> showElectionDialog(null, adminController, electionController, electionList));

        Button editBtn = UiFactory.secondaryButton("Edit Selected");
        editBtn.setDisable(true);
        editBtn.setOnAction(event -> {
            Election selected = electionList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showElectionDialog(selected, adminController, electionController, electionList);
            }
        });

        Button deleteBtn = UiFactory.dangerButton("Delete Selected");
        deleteBtn.setDisable(true);
        deleteBtn.setOnAction(event -> {
            Election selected = electionList.getSelectionModel().getSelectedItem();
            if (selected != null && AlertUtil.confirm("Delete Election", "Delete " + selected.getTitle() + "?")) {
                adminController.handleDeleteElection(selected.getId());
                refreshElections(electionController, electionList);
            }
        });

        Button candidatesBtn = UiFactory.secondaryButton("Manage Candidates");
        candidatesBtn.setDisable(true);
        candidatesBtn.setOnAction(event -> {
            Election selected = electionList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showCandidateDialog(adminController, electionController, selected);
            }
        });

        Button resultsBtn = UiFactory.secondaryButton("View Results");
        resultsBtn.setDisable(true);
        resultsBtn.setOnAction(event -> {
            Election selected = electionList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                new ResultUI(stage, selected).show();
            }
        });

        Button backBtn = UiFactory.ghostButton("Back to Admin");
        backBtn.setOnAction(event -> new AdminUI(stage).show());

        HBox topBar = new HBox(
            12,
            UiFactory.label("ELECTION CONTROL", "eyebrow"),
            UiFactory.spacer(),
            backBtn
        );
        topBar.setAlignment(Pos.CENTER_LEFT);

        HBox heroChips = new HBox(
            10,
            UiFactory.chip("Election dates"),
            UiFactory.chip("Candidate management"),
            UiFactory.chip("Result review")
        );

        VBox headline = UiFactory.commandPanel(
            UiFactory.label("ELECTION CONTROL", "eyebrow"),
            UiFactory.label("Manage elections, dates, candidates, and results.", "page-title"),
            UiFactory.label("Select an election from the list to edit it or view its candidates and results.", "page-subtitle"),
            heroChips
        );

        HBox metrics = UiFactory.metricRow(
            UiFactory.metricCard(String.valueOf(electionList.getItems().size()), "elections in system"),
            UiFactory.metricCard("Admin", "current workspace")
        );

        FlowPane actionBar = new FlowPane(10, 10, createBtn, editBtn, deleteBtn, candidatesBtn, resultsBtn);

        VBox listPanel = UiFactory.panel(
            UiFactory.label("Election list", "section-title"),
            UiFactory.label("Each record stores title, description, dates, and active status.", "section-copy"),
            electionList,
            actionBar
        );
        VBox.setVgrow(electionList, Priority.ALWAYS);

        VBox detailsCard = UiFactory.detailCard(
            "Election details",
            "Select an election to see its description and current status."
        );

        VBox rightPanel = UiFactory.commandPanel(
            UiFactory.label("Selection summary", "section-title"),
            detailsCard,
            UiFactory.label("Tip: keep only the currently running election marked active during your demonstration.", "section-copy")
        );
        rightPanel.setPrefWidth(360);

        electionList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            boolean hasSelection = selected != null;
            editBtn.setDisable(!hasSelection);
            deleteBtn.setDisable(!hasSelection);
            candidatesBtn.setDisable(!hasSelection);
            resultsBtn.setDisable(!hasSelection);

            if (!hasSelection) {
                detailsCard.getChildren().setAll(
                    UiFactory.label("Election details", "section-title"),
                    UiFactory.label("Select an election to see its description and current status.", "section-copy")
                );
            } else {
                detailsCard.getChildren().setAll(
                    UiFactory.label(selected.getTitle(), "section-title"),
                    UiFactory.label(selected.getDescription(), "section-copy"),
                    UiFactory.label("Dates: " + selected.getStartDate() + " to " + selected.getEndDate(), "section-copy"),
                    UiFactory.label("Status: " + (selected.isActive() ? "Active" : "Inactive"), "section-copy")
                );
            }
        });

        HBox content = new HBox(20, listPanel, rightPanel);
        content.setAlignment(Pos.TOP_CENTER);
        HBox.setHgrow(listPanel, Priority.ALWAYS);

        VBox root = new VBox(22, topBar, headline, metrics, content);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(UiFactory.scrollPage(root), 1220, 780);
        SceneUtil.applyStyles(scene);

        stage.setScene(scene);
        stage.setTitle("Election Management");
        stage.show();
    }

    private void showElectionDialog(Election existing, AdminController adminController, ElectionController electionController, ListView<Election> electionList) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Create Election" : "Edit Election");

        TextField titleField = new TextField(existing == null ? "" : existing.getTitle());
        titleField.setPromptText("Election title");

        TextArea descriptionArea = new TextArea(existing == null ? "" : existing.getDescription());
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setPromptText("Election description");

        DatePicker startPicker = new DatePicker(existing == null ? LocalDate.now() : existing.getStartDate());
        DatePicker endPicker = new DatePicker(existing == null ? LocalDate.now().plusDays(7) : existing.getEndDate());

        CheckBox activeBox = new CheckBox("Mark as active");
        activeBox.setSelected(existing == null || existing.isActive());

        dialog.getDialogPane().setContent(UiFactory.dialogContent(
            UiFactory.label("Basic election setup", "section-copy"),
            UiFactory.fieldBlock("Election title", titleField),
            UiFactory.fieldBlock("Description", descriptionArea),
            UiFactory.fieldBlock("Start date", startPicker),
            UiFactory.fieldBlock("End date", endPicker),
            activeBox
        ));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                Election election = new Election(
                    existing == null ? 0 : existing.getId(),
                    titleField.getText(),
                    descriptionArea.getText(),
                    startPicker.getValue(),
                    endPicker.getValue(),
                    activeBox.isSelected()
                );

                if (existing == null) {
                    adminController.handleCreateElection(election);
                } else {
                    electionController.handleUpdate(election);
                }
                refreshElections(electionController, electionList);
            }
        });
    }

    private void showCandidateDialog(AdminController adminController, ElectionController electionController, Election election) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Candidates for " + election.getTitle());

        ListView<Candidate> candidateList = new ListView<>();
        candidateList.getItems().setAll(electionController.loadCandidates(election.getId()));
        candidateList.setPrefHeight(240);

        TextField nameField = new TextField();
        nameField.setPromptText("Candidate name");

        TextField positionField = new TextField();
        positionField.setPromptText("Position");

        TextArea bioField = new TextArea();
        bioField.setPromptText("Short bio or campaign statement");
        bioField.setPrefRowCount(4);

        Button addBtn = new Button("Add Candidate");
        addBtn.setOnAction(event -> {
            Candidate candidate = new Candidate(0, election.getId(), nameField.getText(), positionField.getText(), bioField.getText());
            adminController.handleAddCandidate(candidate);
            candidateList.getItems().setAll(electionController.loadCandidates(election.getId()));
            nameField.clear();
            positionField.clear();
            bioField.clear();
        });

        Button removeBtn = UiFactory.dangerButton("Remove Selected");
        removeBtn.setOnAction(event -> {
            Candidate selected = candidateList.getSelectionModel().getSelectedItem();
            if (selected != null && AlertUtil.confirm("Remove Candidate", "Remove " + selected.getName() + "?")) {
                adminController.handleRemoveCandidate(selected.getId());
                candidateList.getItems().setAll(electionController.loadCandidates(election.getId()));
            }
        });

        dialog.getDialogPane().setContent(UiFactory.dialogContent(
            UiFactory.label("Current candidates", "section-copy"),
            candidateList,
            UiFactory.fieldBlock("Candidate name", nameField),
            UiFactory.fieldBlock("Position", positionField),
            UiFactory.fieldBlock("Campaign summary", bioField),
            new HBox(10, addBtn, removeBtn)
        ));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void refreshElections(ElectionController controller, ListView<Election> electionList) {
        electionList.getItems().setAll(controller.loadAll());
    }
}
