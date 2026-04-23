package ui;

import controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginUI {
    private final Stage stage;

    public LoginUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        AuthController controller = new AuthController(stage);

        Label title = new Label("Smart Campus Voting System");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Student ID");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login as Voter");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnAction(e -> controller.handleVoterLogin(studentIdField.getText(), passwordField.getText()));

        Button adminBtn = new Button("Login as Admin");
        adminBtn.setMaxWidth(Double.MAX_VALUE);
        adminBtn.setOnAction(e -> new AdminLoginUI(stage).show());

        Button registerBtn = new Button("Register");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setOnAction(e -> new RegisterUI(stage).show());

        VBox root = new VBox(12, title, studentIdField, passwordField, loginBtn, adminBtn, registerBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setMaxWidth(320);

        VBox wrapper = new VBox(root);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.getStylesheets().add(getClass().getResource("/styles.css") != null
            ? getClass().getResource("/styles.css").toExternalForm() : "");

        stage.setScene(new Scene(wrapper, 480, 400));
        stage.setTitle("Login");
        stage.show();
    }
}
