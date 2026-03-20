package com.novasolutions.ipospu.gui;

import com.novasolutions.ipospu.controller.RegistrationController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

        Button backButton = new Button("Back to Login");

        setSpacing(10);
        setPadding(new Insets(20));
        getChildren().addAll(
                new Label("Register - Non-Commercial Member"),
                nameField,
                emailField,
                registerButton,
                messageLabel,
                backButton
        );

        registerButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();

            String generatedPassword = registrationController.registerNonCommercial(name, email);

            if (generatedPassword != null) {
                messageLabel.setText("Registration successful!\nYour password: " + generatedPassword);
                nameField.clear();
                emailField.clear();
            } else {
                messageLabel.setText("Registration failed. Email may already be in use.");
            }
        });

        backButton.setOnAction(e -> {
            stage.getScene().setRoot(new LoginScreen());
            stage.setTitle("IPOS-PU | Login");
        });
    }
}