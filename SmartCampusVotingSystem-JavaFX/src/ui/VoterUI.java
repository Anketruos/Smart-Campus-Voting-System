package ui;

import controller.AuthController;
import controller.VoterController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Election;
import java.util.List;

public class VoterUI {
    private final Stage stage;

    public VoterUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        VoterController controller = new VoterController(stage);
        AuthController authController = new AuthController(stage);

        Label title = new Label("Active Elections");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<Election> electionList = new ListView<>();
        List<Election> elections = controller.loadActiveElections();
        electionList.getItems().addAll(elections);
        electionList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Election item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitle());
            }
        });

        Button voteBtn = new Button("Vote");
        voteBtn.setMaxWidth(Double.MAX_VALUE);
        voteBtn.setOnAction(e -> {
            Election selected = electionList.getSelectionModel().getSelectedItem();
            if (selected != null) controller.openVoting(selected);
        });

        Button resultsBtn = new Button("View Results");
        resultsBtn.setMaxWidth(Double.MAX_VALUE);
        resultsBtn.setOnAction(e -> {
            Election selected = electionList.getSelectionModel().getSelectedItem();
            if (selected != null) new ResultUI(stage, selected).show();
        });

        Button logoutBtn = new Button("Logout");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> {
            authController.handleLogout();
            new LoginUI(stage).show();
        });

        VBox root = new VBox(12, title, electionList, voteBtn, resultsBtn, logoutBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 480, 450));
        stage.setTitle("Voter Dashboard");
        stage.show();
    }
}
