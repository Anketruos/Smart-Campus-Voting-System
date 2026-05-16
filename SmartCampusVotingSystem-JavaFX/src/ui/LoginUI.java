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

public class LoginUI {
    private final Stage stage;

    public LoginUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        AuthController controller = new AuthController(stage);

        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Student ID");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login as Student");
        loginBtn.setOnAction(event -> controller.handleVoterLogin(studentIdField.getText(), passwordField.getText()));

        Button adminBtn = UiFactory.secondaryButton("Open Admin Login");
        adminBtn.setOnAction(event -> new AdminLoginUI(stage).show());

        Button registerBtn = UiFactory.ghostButton("Register New Student");
        registerBtn.setOnAction(event -> new RegisterUI(stage).show());

        UiFactory.makeWide(loginBtn, adminBtn, registerBtn);

        VBox hero = UiFactory.authHero(
            "SMART CAMPUS ELECTIONS",
            "Campus democracy, designed like it matters.",
            "A sharper student election experience with secure access, one-person-one-vote rules, and instant result tracking.",
            "Students register only after approval from the election office.",
            "Each ballot is stored securely and every voter can vote only once per election.",
            "Admins can manage candidates, elections, and results in one place."
        );
        HBox heroStats = new HBox(
            12,
            UiFactory.heroStat("1 voter", "one final ballot"),
            UiFactory.heroStat("Live", "result tracking"),
            UiFactory.heroStat("Secure", "approved access only")
        );
        hero.getChildren().add(heroStats);

        VBox card = UiFactory.authCard(
            "STUDENT ACCESS",
            "Welcome back",
            "Sign in to open the current ballot, review candidates, and submit your vote once.",
            UiFactory.fieldBlock("Student ID", studentIdField),
            UiFactory.fieldBlock("Password", passwordField),
            loginBtn,
            adminBtn,
            registerBtn,
            UiFactory.chip("Default admin: admin / admin123")
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
        stage.setTitle("Campus Voting System");
        stage.show();
    }
}
