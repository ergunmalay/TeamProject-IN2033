package com.novasolutions.ipospu.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrationChoiceScreen extends VBox {

    public RegistrationChoiceScreen(Stage stage) {
        Label title = new Label("Join IPOS-PU");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label subtitle = new Label("Select the type of membership that applies to you.");
        subtitle.setStyle("-fx-text-fill: gray;");

        Label nonCommercialDesc = new Label(
                "Non-Commercial: For individual members.\n" +
                "Your account is created immediately with a system-generated password.");
        nonCommercialDesc.setStyle("-fx-text-fill: #555; -fx-font-size: 12px;");
        nonCommercialDesc.setWrapText(true);

        Button nonCommercialButton = new Button("Register as Non-Commercial Member");
        nonCommercialButton.setMaxWidth(Double.MAX_VALUE);

        Label commercialDesc = new Label(
                "Commercial: For businesses and organisations.\n" +
                "Your application is submitted for review by a System Administrator.");
        commercialDesc.setStyle("-fx-text-fill: #555; -fx-font-size: 12px;");
        commercialDesc.setWrapText(true);

        Button commercialButton = new Button("Apply as Commercial Member");
        commercialButton.setMaxWidth(Double.MAX_VALUE);

        Button backButton = new Button("Back to Login");

        setSpacing(10);
        setPadding(new Insets(30));
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(
                title,
                subtitle,
                new Separator(),
                nonCommercialDesc,
                nonCommercialButton,
                new Separator(),
                commercialDesc,
                commercialButton,
                new Separator(),
                backButton
        );

        nonCommercialButton.setOnAction(e -> {
            stage.getScene().setRoot(new NonCommercialRegisterScreen(stage));
            stage.setTitle("IPOS-PU | Register - Non-Commercial");
        });

        commercialButton.setOnAction(e -> {
            stage.getScene().setRoot(new CommercialRegisterScreen(stage));
            stage.setTitle("IPOS-PU | Apply - Commercial");
        });

        backButton.setOnAction(e -> {
            stage.getScene().setRoot(new LoginScreen());
            stage.setTitle("IPOS-PU | Login");
        });
    }
}
