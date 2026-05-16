package app;

import javafx.application.Application;
import javafx.stage.Stage;
import dao.DBConnection;
import ui.LoginUI;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DBConnection.initializeDatabase();
        primaryStage.setMinWidth(860);
        primaryStage.setMinHeight(620);
        new LoginUI(primaryStage).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
