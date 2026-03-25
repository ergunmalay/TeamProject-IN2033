package com.novasolutions.ipospu.gui;

import com.novasolutions.ipospu.controller.RegistrationController;
import com.novasolutions.ipospu.service.RegistrationResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NonCommercialRegisterScreen extends VBox {

    private final RegistrationController registrationController = new RegistrationController();

    public NonCommercialRegisterScreen(Stage stage) {
        Label title = new Label("Register - Non-Commercial Member");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label info = new Label(
                "A temporary password will be generated for you.\n" +
                "You will be required to change it on your first login.");
        info.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");
        info.setWrapText(true);

        TextField nameField = new TextField();
        nameField.setPromptText("Full name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email address");

        Button registerButton = new Button("Register");

        Label messageLabel = new Label();
        messageLabel.setWrapText(true);

        Button copyButton = new Button("Copy Password");
        copyButton.setVisible(false);

        HBox passwordRow = new HBox(10, messageLabel, copyButton);
        passwordRow.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("Back");

        setSpacing(10);
        setPadding(new Insets(24));
        getChildren().addAll(title, info, nameField, emailField, registerButton, passwordRow, backButton);

        registerButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();

            RegistrationResult result = registrationController.registerNonCommercial(name, email);

            if (result.isSuccess()) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(
                        "Registration successful!\n" +
                        "Your temporary password: " + result.getPassword() + "\n\n" +
                        "Important: You will be asked to change this password when you first log in.");
                copyButton.setVisible(true);
                copyButton.setText("Copy Password");
                copyButton.setOnAction(ce -> {
                    ClipboardContent content = new ClipboardContent();
                    content.putString(result.getPassword());
                    Clipboard.getSystemClipboard().setContent(content);
                    copyButton.setText("Copied!");
                });
                nameField.clear();
                emailField.clear();
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(result.getMessage());
                copyButton.setVisible(false);
            }
        });

        backButton.setOnAction(e -> {
            stage.getScene().setRoot(new RegistrationChoiceScreen(stage));
            stage.setTitle("IPOS-PU | Registration");
        });
    }
}
