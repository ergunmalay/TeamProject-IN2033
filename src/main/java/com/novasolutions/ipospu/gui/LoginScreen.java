package com.novasolutions.ipospu.gui;

import com.novasolutions.ipospu.controller.LoginController;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;

public class LoginScreen extends VBox {

    LoginController loginController = new LoginController();

    TextField emailField;
    PasswordField passwordField;
    Button loginButton;
    Label messageLabel;

    //Constructor
    public LoginScreen() {
        emailField = new TextField();
        emailField.setPromptText("Enter email");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        loginButton = new Button("Login");
        messageLabel = new Label();

        setSpacing(10);
        setPadding(new Insets(20));
        this.getChildren().addAll(
                emailField,
                passwordField,
                loginButton,
                messageLabel
        );

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            boolean success = loginController.login(email, password);

            if (success) {
                messageLabel.setText("Login successful!");
            } else {
                messageLabel.setText("Login failed. Please check your credentials.");
            }
        });

    }

}
