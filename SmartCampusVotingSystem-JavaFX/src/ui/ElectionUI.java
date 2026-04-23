package ui;

import controller.AdminController;
import controller.ElectionController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Candidate;
import model.Election;
import java.time.LocalDate;
import java.util.List;

public class ElectionUI {
    private final Stage stage;

    public ElectionUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        ElectionController electionController = new ElectionController(stage);
        AdminController adminController = new AdminController(stage);

        Label title = new Label("Manage Elections");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<Election> electionList = new ListView<>();
        electionList.getItems().addAll(electionController.loadAll());
        electionList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Election item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitle() + (item.isActive() ? " [Active]" : ""));
            }
        });

        Button createBtn = new Button("Create Election");
        createBtn.setOnAction(e -> showCreateDialog(adminController, electionList, electionController));

        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setOnAction(e -> {
            Election selected = electionList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                adminController.handleDeleteElection(selected.getId());
                electionList.getItems().setAll(electionController.loadAll());
            }
        });

        Button candidatesBtn = new Button("Manage Candidates");
        candidatesBtn.setOnAction(e -> {
            Election selected = electionList.getSelectionModel().getSelectedItem();
            if (selected != null) showCandidateDialog(adminController, electionController, selected);
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> new AdminUI(stage).show());

        HBox actions = new HBox(10, createBtn, deleteBtn, candidatesBtn, backBtn);
        actions.setAlignment(Pos.CENTER);

        VBox root = new VBox(12, title, electionList, actions);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 560, 450));
        stage.setTitle("Election Management");
        stage.show();
    }

    private void showCreateDialog(AdminController adminController, ListView<Election> list, ElectionController ec) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Election");

        TextField titleField = new TextField(); titleField.setPromptText("Title");
        TextField descField = new TextField(); descField.setPromptText("Description");
        DatePicker startPicker = new DatePicker(LocalDate.now());
        DatePicker endPicker = new DatePicker(LocalDate.now().plusDays(7));
        CheckBox activeBox = new CheckBox("Active");

        VBox content = new VBox(8,
            new Label("Title:"), titleField,
            new Label("Description:"), descField,
            new Label("Start Date:"), startPicker,
            new Label("End Date:"), endPicker,
            activeBox
        );
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                Election election = new Election(0, titleField.getText(), descField.getText(),
                    startPicker.getValue(), endPicker.getValue(), activeBox.isSelected());
                adminController.handleCreateElection(election);
                list.getItems().setAll(ec.loadAll());
            }
        });
    }

    private void showCandidateDialog(AdminController adminController, ElectionController ec, Election election) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Candidates: " + election.getTitle());

        ListView<Candidate> cList = new ListView<>();
        cList.getItems().addAll(ec.loadCandidates(election.getId()));
        cList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Candidate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " — " + item.getPosition());
            }
        });

        TextField nameField = new TextField(); nameField.setPromptText("Candidate Name");
        TextField posField = new TextField(); posField.setPromptText("Position");
        TextField bioField = new TextField(); bioField.setPromptText("Bio");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            Candidate c = new Candidate(0, election.getId(), nameField.getText(), posField.getText(), bioField.getText());
            adminController.handleAddCandidate(c);
            cList.getItems().setAll(ec.loadCandidates(election.getId()));
        });

        Button removeBtn = new Button("Remove Selected");
        removeBtn.setOnAction(e -> {
            Candidate selected = cList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                adminController.handleRemoveCandidate(selected.getId());
                cList.getItems().setAll(ec.loadCandidates(election.getId()));
            }
        });

        VBox content = new VBox(8, cList, new Label("Add Candidate:"), nameField, posField, bioField,
            new HBox(10, addBtn, removeBtn));
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }
}
