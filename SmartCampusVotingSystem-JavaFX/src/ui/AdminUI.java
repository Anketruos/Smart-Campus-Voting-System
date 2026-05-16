package ui;

import controller.AdminController;
import controller.AuthController;
import dao.VoterDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.PreapprovedVoter;
import model.Voter;
import util.AlertUtil;
import util.SceneUtil;
import util.UiFactory;

import java.util.List;

public class AdminUI {
    private final Stage stage;

    public AdminUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        AdminController adminController = new AdminController(stage);
        AuthController authController = new AuthController(stage);

        List<PreapprovedVoter> approvedStudents = adminController.loadPreapprovedVoters();
        int totalApproved = approvedStudents.size();
        long pendingApprovals = approvedStudents.stream().filter(voter -> !voter.isRegistered()).count();

        int totalRegistered = 0;
        int participated = 0;
        try {
            List<Voter> voters = new VoterDAO().findAll();
            totalRegistered = voters.size();
            participated = (int) voters.stream().filter(Voter::isHasVoted).count();
        } catch (Exception exception) {
            AlertUtil.showWarning("Warning", "Could not load voter summary: " + exception.getMessage());
        }

        Button manageElectionsBtn = new Button("Manage Elections and Candidates");
        manageElectionsBtn.setOnAction(event -> adminController.openElectionManagement());

        Button preapproveBtn = UiFactory.secondaryButton("Pre-approve Student");
        preapproveBtn.setOnAction(event -> showPreapproveDialog(adminController));

        Button viewVotersBtn = UiFactory.secondaryButton("View Registered Students");
        viewVotersBtn.setOnAction(event -> showVoterList());

        Button approvedListBtn = UiFactory.secondaryButton("View Approved List");
        approvedListBtn.setOnAction(event -> showApprovedVoters(approvedStudents));

        Button logoutBtn = UiFactory.ghostButton("Logout");
        logoutBtn.setOnAction(event -> {
            authController.handleLogout();
            new LoginUI(stage).show();
        });

        UiFactory.makeWide(manageElectionsBtn, preapproveBtn, viewVotersBtn, approvedListBtn, logoutBtn);

        Button backBtn = UiFactory.ghostButton("Back to Login");
        backBtn.setOnAction(event -> {
            authController.handleLogout();
            new LoginUI(stage).show();
        });

        HBox header = new HBox(
            16,
            UiFactory.label("ADMIN DASHBOARD", "eyebrow"),
            UiFactory.spacer(),
            backBtn
        );

        HBox heroChips = new HBox(
            10,
            UiFactory.chip("Election control"),
            UiFactory.chip("Student approvals"),
            UiFactory.chip("Live participation")
        );

        VBox headline = UiFactory.commandPanel(
            UiFactory.label("ADMIN CONTROL ROOM", "eyebrow"),
            UiFactory.label("Run campus elections from one high-visibility dashboard.", "page-title"),
            UiFactory.label(
                "Approve students, manage election periods, update candidates, and review live results from a single admin workspace.",
                "page-subtitle"
            ),
            heroChips
        );
        headline.setSpacing(10);

        HBox metrics = UiFactory.metricRow(
            UiFactory.metricCard(String.valueOf(totalApproved), "approved students"),
            UiFactory.metricCard(String.valueOf(pendingApprovals), "waiting to register"),
            UiFactory.metricCard(String.valueOf(totalRegistered), "registered voters"),
            UiFactory.metricCard(String.valueOf(participated), "ballots already cast")
        );

        VBox actionPanel = UiFactory.commandPanel(
            UiFactory.label("Admin actions", "section-title"),
            UiFactory.label("Move through the workflow in order and your project demo will feel much more professional.", "section-copy"),
            manageElectionsBtn,
            preapproveBtn,
            viewVotersBtn,
            approvedListBtn,
            logoutBtn
        );
        actionPanel.setPrefWidth(320);

        ListView<String> checklist = new ListView<>();
        checklist.getItems().addAll(
            "Create the election and define the start and end dates.",
            "Add candidates with position and campaign summary.",
            "Pre-approve students before they attempt registration.",
            "Let students vote during the active election window.",
            "Monitor results and participation from the admin screens."
        );
        checklist.setPrefHeight(260);

        VBox guidePanel = UiFactory.panel(
            UiFactory.label("Recommended workflow", "section-title"),
            UiFactory.label("This sequence is easy to explain to your lecturer while you demonstrate the system.", "section-copy"),
            checklist
        );

        VBox notesPanel = UiFactory.panel(
            UiFactory.label("System notes", "section-title"),
            UiFactory.label("Students can only register if their student ID and email are pre-approved.", "section-copy"),
            UiFactory.label("Votes are stored in the database and counted again for results, so totals stay consistent.", "section-copy"),
            UiFactory.label("The socket server and RMI server are included for your networking units, but the main app runs directly through JavaFX.", "section-copy")
        );

        VBox rightColumn = new VBox(18, guidePanel, notesPanel);

        HBox content = new HBox(20, actionPanel, rightColumn);
        content.setAlignment(Pos.TOP_CENTER);

        VBox root = new VBox(22, header, headline, metrics, content);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(UiFactory.scrollPage(root), 1180, 760);
        SceneUtil.applyStyles(scene);

        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }

    private void showPreapproveDialog(AdminController controller) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Pre-approve Student");

        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Student ID");

        TextField emailField = new TextField();
        emailField.setPromptText("Campus Email");

        dialog.getDialogPane().setContent(UiFactory.dialogContent(
            UiFactory.label("Add a student to the approved registration list.", "section-copy"),
            UiFactory.fieldBlock("Student ID", studentIdField),
            UiFactory.fieldBlock("Campus email", emailField)
        ));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                controller.handleAddPreapprovedVoter(studentIdField.getText(), emailField.getText());
            }
        });
    }

    private void showVoterList() {
        try {
            VoterDAO voterDAO = new VoterDAO();
            List<Voter> voters = voterDAO.findAll();

            ListView<String> listView = new ListView<>();
            voters.forEach(voter -> listView.getItems().add(
                voter.getStudentId() + " | " + voter.getName() + " | " + voter.getEmail()
                    + (voter.isHasVoted() ? " | Participated" : " | Not yet voted")
            ));

            VBox root = new VBox(
                14,
                UiFactory.label("Registered students", "section-title"),
                UiFactory.label("Total: " + voters.size(), "section-copy"),
                listView
            );
            root.setPadding(new Insets(18));

            Stage voterStage = new Stage();
            Scene scene = new Scene(UiFactory.scrollPage(root), 700, 460);
            SceneUtil.applyStyles(scene);
            voterStage.setScene(scene);
            voterStage.setTitle("Registered Students");
            voterStage.show();
        } catch (Exception e) {
            AlertUtil.showError("Error", e.getMessage());
        }
    }

    private void showApprovedVoters(List<PreapprovedVoter> approvedVoters) {
        Stage listStage = new Stage();
        ListView<String> listView = new ListView<>();
        approvedVoters.forEach(voter -> listView.getItems().add(voter.toString()));

        VBox root = new VBox(
            14,
            UiFactory.label("Approved students", "section-title"),
            UiFactory.label("Total: " + approvedVoters.size(), "section-copy"),
            listView
        );
        root.setPadding(new Insets(18));

        Scene scene = new Scene(UiFactory.scrollPage(root), 700, 460);
        SceneUtil.applyStyles(scene);
        listStage.setScene(scene);
        listStage.setTitle("Approved Students");
        listStage.show();
    }
}
