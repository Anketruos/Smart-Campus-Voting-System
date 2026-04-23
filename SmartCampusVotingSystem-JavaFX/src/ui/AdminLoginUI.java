package ui;

import controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminLoginUI {
    private final Stage stage;

    public AdminLoginUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        AuthController controller = new AuthController(stage);

        Label title = new Label("Admin Login");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnAction(e -> controller.handleAdminLogin(usernameField.getText(), passwordField.getText()));

        Button backBtn = new Button("Back");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setOnAction(e -> new LoginUI(stage).show());

        VBox root = new VBox(12, title, usernameField, passwordField, loginBtn, backBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setMaxWidth(320);

        VBox wrapper = new VBox(root);
        wrapper.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(wrapper, 480, 350));
        stage.setTitle("Admin Login");
        stage.show();
    }
}
