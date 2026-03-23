package com.novasolutions.ipospu;

import com.novasolutions.ipospu.gui.DashboardScreen;
import com.novasolutions.ipospu.gui.LoginScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * IPOS-PU Main Application Entry Point.
 * Launches the JavaFX desktop application for the InfoPharma
 * Online Purchasing System - Public Portal.
 * Team 24/C - Nova Solutions
 * IN2033 Team Project
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load the first real screen of the application
        // LoginScreen root = new LoginScreen();

        //Skip login for now and go straight to dashboard
        DashboardScreen root = new DashboardScreen(primaryStage, "dev@test.com");
        // Keep the window small and simple until JavaFX is stable
        Scene scene = new Scene(root, 400, 250);

        primaryStage.setTitle("IPOS-PU | Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}