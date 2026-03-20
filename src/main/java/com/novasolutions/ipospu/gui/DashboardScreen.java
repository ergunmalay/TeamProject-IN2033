package com.novasolutions.ipospu.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardScreen extends VBox {

    public DashboardScreen(Stage stage, String email) {
        Label welcomeLabel = new Label("Welcome, " + email + "!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("You are now logged in.");
        subtitleLabel.setStyle("-fx-text-fill: gray;");

        Button logoutButton = new Button("Logout");

        setSpacing(12);
        setPadding(new Insets(30));
        setAlignment(Pos.CENTER);
        getChildren().addAll(welcomeLabel, subtitleLabel, logoutButton);

        logoutButton.setOnAction(e -> {
            stage.getScene().setRoot(new LoginScreen());
            stage.setTitle("IPOS-PU | Login");
        });
    }
}
