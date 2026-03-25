package com.novasolutions.ipospu;

import com.novasolutions.ipospu.gui.LoginScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginScreen root = new LoginScreen();
        Scene scene = new Scene(root, 420, 300);

        // I set a minimum size so that navigating between screens never clips content.
        primaryStage.setMinWidth(440);
        primaryStage.setMinHeight(320);
        primaryStage.setTitle("IPOS-PU | Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}