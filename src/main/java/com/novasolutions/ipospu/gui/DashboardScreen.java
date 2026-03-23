package com.novasolutions.ipospu.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardScreen extends BorderPane {

    private VBox createTopBar() {
        Label title = new Label("IPOS-PU Dashboard");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #fbfbfb;");


        VBox topBar = new VBox(title);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");

        return topBar;
    }

    private VBox createMainContent(Stage stage, String email) {
        Label welcomeLabel = new Label("Welcome, " + email + "!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("You are now logged in.");
        subtitleLabel.setStyle("-fx-text-fill: gray;");

        Button logoutButton = new Button("Logout");

        VBox content = new VBox(12, welcomeLabel, subtitleLabel, logoutButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));

        logoutButton.setOnAction(e -> {
            stage.getScene().setRoot(new LoginScreen());
            stage.setTitle("IPOS-PU | Login");
        });

        return content;
    }

    public DashboardScreen(Stage stage, String email) {
        setTop(createTopBar());
        setCenter(createMainContent(stage, email));
    }
}
