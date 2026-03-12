package com.novasolutions.ipospu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * IPOS-PU Main Application Entry Point.
 *
 * Launches the JavaFX desktop application for the InfoPharma
 * Online Purchasing System - Public Portal.
 *
 * Team 24/C - Nova Solutions
 * IN2033 Team Project
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // TODO: Replace with LoginScreen once Hassan/Marwan complete it
        Label placeholder = new Label("IPOS-PU - Nova Solutions\nProject scaffold loaded successfully.");
        placeholder.setStyle("-fx-font-size: 18px; -fx-text-alignment: center;");

        StackPane root = new StackPane(placeholder);
        Scene scene = new Scene(root, 900, 600);

        primaryStage.setTitle("IPOS-PU | InfoPharma Online Purchasing System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
