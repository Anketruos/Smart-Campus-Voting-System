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

public class RegisterUI {
    private final Stage stage;

    public RegisterUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        AuthController controller = new AuthController(stage);

        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Student ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Campus Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerBtn = new Button("Create Student Account");
        registerBtn.setOnAction(event -> controller.handleRegister(
            studentIdField.getText(),
            nameField.getText(),
            emailField.getText(),
            passwordField.getText()
        ));

        Button backBtn = UiFactory.ghostButton("Back to Home");
        backBtn.setOnAction(event -> new LoginUI(stage).show());

        UiFactory.makeWide(registerBtn, backBtn);

        VBox hero = UiFactory.authHero(
            "NEW STUDENT ACCOUNT",
            "Register once. Vote once. Count once.",
            "The registration gate is tied to pre-approved campus details so only valid student voters can enter the election system.",
            "Use the same student ID and email that the admin approved.",
            "Choose a password with letters and numbers.",
            "After registration, log in from the main student screen."
        );
        HBox heroStats = new HBox(
            12,
            UiFactory.heroStat("Approved", "ID plus email"),
            UiFactory.heroStat("Personal", "student password"),
            UiFactory.heroStat("Ready", "for active elections")
        );
        hero.getChildren().add(heroStats);

        VBox card = UiFactory.authCard(
            "REGISTRATION",
            "Create your account",
            "Your registration works only if the admin already added your student ID and campus email.",
            UiFactory.fieldBlock("Student ID", studentIdField),
            UiFactory.fieldBlock("Full name", nameField),
            UiFactory.fieldBlock("Campus email", emailField),
            UiFactory.fieldBlock("Password", passwordField),
            registerBtn,
            backBtn
        );

        HBox shell = new HBox(26, hero, card);
        shell.setAlignment(Pos.CENTER);
        HBox.setHgrow(hero, Priority.ALWAYS);
        HBox.setHgrow(card, Priority.ALWAYS);

        StackPane root = new StackPane(shell);
        root.setPadding(new Insets(32));

        Scene scene = new Scene(UiFactory.scrollPage(root), 1120, 720);
        SceneUtil.applyStyles(scene);

        stage.setScene(scene);
        stage.setTitle("Register");
        stage.show();
    }
}
