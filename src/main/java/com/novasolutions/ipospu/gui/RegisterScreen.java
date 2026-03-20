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

public class RegisterScreen extends VBox {

    private final RegistrationController registrationController = new RegistrationController();

    public RegisterScreen(Stage stage) {
        TextField nameField = new TextField();
        nameField.setPromptText("Full name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email address");

        Button registerButton = new Button("Register");
        Label messageLabel = new Label();
        Button copyButton = new Button("📋");
        copyButton.setVisible(false);

        HBox passwordRow = new HBox(10, messageLabel, copyButton);
        passwordRow.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("Back to Login");

        setSpacing(10);
        setPadding(new Insets(20));
        getChildren().addAll(
                new Label("Register - Non-Commercial Member"),
                nameField,
                emailField,
                registerButton,
                passwordRow,
                backButton
        );

        registerButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();

            RegistrationResult result = registrationController.registerNonCommercial(name, email);

            if (result.isSuccess()) {
                messageLabel.setText("Registration successful! Your password: " + result.getPassword());
                copyButton.setVisible(true);
                copyButton.setOnAction(ce -> {
                    ClipboardContent content = new ClipboardContent();
                    content.putString(result.getPassword());
                    Clipboard.getSystemClipboard().setContent(content);
                    copyButton.setText("📋");
                });
                nameField.clear();
                emailField.clear();
            } else {
                messageLabel.setText(result.getMessage());
                copyButton.setVisible(false);
            }
        });

        backButton.setOnAction(e -> {
            stage.getScene().setRoot(new LoginScreen());
            stage.setTitle("IPOS-PU | Login");
        });
    }
}