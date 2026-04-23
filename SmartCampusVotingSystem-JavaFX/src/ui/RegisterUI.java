package ui;

import controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterUI {
    private final Stage stage;

    public RegisterUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        AuthController controller = new AuthController(stage);

        Label title = new Label("Voter Registration");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Student ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerBtn = new Button("Register");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setOnAction(e -> controller.handleRegister(
            studentIdField.getText(), nameField.getText(),
            emailField.getText(), passwordField.getText()
        ));

        Button backBtn = new Button("Back to Login");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setOnAction(e -> new LoginUI(stage).show());

        VBox root = new VBox(12, title, studentIdField, nameField, emailField, passwordField, registerBtn, backBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setMaxWidth(320);

        VBox wrapper = new VBox(root);
        wrapper.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(wrapper, 480, 420));
        stage.setTitle("Register");
        stage.show();
    }
}
