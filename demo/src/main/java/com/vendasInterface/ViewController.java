package com.vendasInterface;

import javafx.application.Application;
import javafx.stage.Stage;

public class ViewController extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginRegisterView loginRegisterView = new LoginRegisterView(primaryStage);
        loginRegisterView.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
