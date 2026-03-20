package com.novasolutions.ipospu.gui;

import com.novasolutions.ipospu.controller.LoginController;
import com.novasolutions.ipospu.service.LoginResult;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen extends VBox {

    private final LoginController loginController = new LoginController();

    public LoginScreen() {
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Label messageLabel = new Label();
        Button registerButton = new Button("Register as Non-Commercial Member");

        setSpacing(10);
        setPadding(new Insets(20));
        getChildren().addAll(
                emailField,
                passwordField,
                loginButton,
                messageLabel,
                registerButton
        );

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            LoginResult result = loginController.login(email, password);

            if (result.isSuccess()) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(result.getMessage());
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(result.getMessage());
            }
        });

        registerButton.setOnAction(e -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.getScene().setRoot(new RegisterScreen(stage));
            stage.setTitle("IPOS-PU | Register");
        });
    }
}