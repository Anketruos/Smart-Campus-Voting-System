package ui;

import controller.AdminController;
import controller.AuthController;
import dao.VoterDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Voter;
import java.util.List;

public class AdminUI {
    private final Stage stage;

    public AdminUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        AdminController adminController = new AdminController(stage);
        AuthController authController = new AuthController(stage);

        Label title = new Label("Admin Dashboard");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button manageElectionsBtn = new Button("Manage Elections");
        manageElectionsBtn.setMaxWidth(Double.MAX_VALUE);
        manageElectionsBtn.setOnAction(e -> adminController.openElectionManagement());

        Button preapproveBtn = new Button("Pre-approve Voter");
        preapproveBtn.setMaxWidth(Double.MAX_VALUE);
        preapproveBtn.setOnAction(e -> showPreapproveDialog(adminController));

        Button viewVotersBtn = new Button("View Registered Voters");
        viewVotersBtn.setMaxWidth(Double.MAX_VALUE);
        viewVotersBtn.setOnAction(e -> showVoterList());

        Button logoutBtn = new Button("Logout");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> {
            authController.handleLogout();
            new LoginUI(stage).show();
        });

        VBox root = new VBox(14, title, manageElectionsBtn, preapproveBtn, viewVotersBtn, logoutBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setMaxWidth(320);

        VBox wrapper = new VBox(root);
        wrapper.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(wrapper, 480, 380));
        stage.setTitle("Admin Dashboard");
        stage.show();
    }

    private void showPreapproveDialog(AdminController controller) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Pre-approve Voter");

        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Student ID");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        VBox content = new VBox(10, new Label("Student ID:"), studentIdField, new Label("Email:"), emailField);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK)
                controller.handleAddPreapprovedVoter(studentIdField.getText(), emailField.getText());
        });
    }

    private void showVoterList() {
        try {
            VoterDAO voterDAO = new VoterDAO();
            List<Voter> voters = voterDAO.findAll();

            // Use ArrayList (java.util) to manage voter list (Unit 2: Collections)
            ListView<String> listView = new ListView<>();
            voters.forEach(v -> listView.getItems().add(
                v.getStudentId() + " — " + v.getName() + (v.isHasVoted() ? " [Voted]" : "")
            ));

            Stage voterStage = new Stage();
            VBox root = new VBox(10, new Label("Registered Voters (" + voters.size() + ")"), listView);
            root.setPadding(new Insets(15));
            voterStage.setScene(new Scene(root, 420, 350));
            voterStage.setTitle("Registered Voters");
            voterStage.show();
        } catch (Exception e) {
            util.AlertUtil.showError("Error", e.getMessage());
        }
    }
}
