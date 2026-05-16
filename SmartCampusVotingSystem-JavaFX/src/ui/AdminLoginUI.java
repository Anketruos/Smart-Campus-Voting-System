package ui;

import controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.SceneUtil;
import util.UiFactory;

public class AdminLoginUI {
    private final Stage stage;

    public AdminLoginUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        AuthController controller = new AuthController(stage);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Open Admin Dashboard");
        loginBtn.setOnAction(event -> controller.handleAdminLogin(usernameField.getText(), passwordField.getText()));

        Button backBtn = UiFactory.ghostButton("Back to Home");
        backBtn.setOnAction(event -> new LoginUI(stage).show());

        UiFactory.makeWide(loginBtn, backBtn);

        VBox hero = UiFactory.authHero(
            "ADMIN CONSOLE",
            "Run the election room like a real command center.",
            "The admin workspace handles approvals, election timing, candidate records, and live monitoring without spreadsheet chaos.",
            "Create and update election periods.",
            "Add candidates and campaign descriptions.",
            "Monitor participation and results in real time."
        );
        HBox heroStats = new HBox(
            12,
            UiFactory.heroStat("Approve", "student access"),
            UiFactory.heroStat("Manage", "candidates and dates"),
            UiFactory.heroStat("Review", "live totals")
        );
        hero.getChildren().add(heroStats);

        VBox card = UiFactory.authCard(
            "AUTHORIZED STAFF",
            "Administrator login",
            "Use the admin account to control the election cycle and maintain voting integrity.",
            UiFactory.fieldBlock("Username", usernameField),
            UiFactory.fieldBlock("Password", passwordField),
            loginBtn,
            backBtn
        );

        HBox shell = new HBox(26, hero, card);
        shell.setAlignment(Pos.CENTER);
        HBox.setHgrow(hero, Priority.ALWAYS);
        HBox.setHgrow(card, Priority.ALWAYS);

        StackPane root = new StackPane(shell);
        root.setPadding(new Insets(32));

        Scene scene = new Scene(UiFactory.scrollPage(root), 1100, 700);
        SceneUtil.applyStyles(scene);

        stage.setScene(scene);
        stage.setTitle("Admin Login");
        stage.show();
    }
}
