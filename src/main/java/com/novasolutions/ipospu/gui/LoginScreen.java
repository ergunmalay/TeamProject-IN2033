package com.novasolutions.ipospu.gui;

import com.novasolutions.ipospu.controller.LoginController;
import com.novasolutions.ipospu.db.DatabaseResetDAO;
import com.novasolutions.ipospu.model.Member;
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
        Button registerButton = new Button("Register");
        Button resetDatabaseButton = new Button("Reset Database");
        Label messageLabel = new Label();

        setSpacing(10);
        setPadding(new Insets(20));
        getChildren().addAll(
                emailField,
                passwordField,
                loginButton,
                messageLabel,
                registerButton,
                resetDatabaseButton
        );

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            LoginResult result = loginController.login(email, password);

            if (result.isSuccess()) {
                Stage stage = (Stage) getScene().getWindow();
                Member member = result.getMember();

                if (member.isFirstLogin()) {
                    stage.getScene().setRoot(new ChangePasswordScreen(stage, member));
                    stage.setTitle("IPOS-PU | Change Password");
                } else {
                    stage.getScene().setRoot(new DashboardScreen(stage, member));
                    stage.setTitle("IPOS-PU | Dashboard");
                }
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(result.getMessage());
            }
        });

        registerButton.setOnAction(e -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.getScene().setRoot(new RegistrationChoiceScreen(stage));
            stage.setTitle("IPOS-PU | Registration");
        });

        resetDatabaseButton.setOnAction(e -> {
            boolean success = DatabaseResetDAO.resetMembersAndCommercialApplications();

            if (success) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Development database reset successfully.");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Failed to reset development database.");
            }
        });
    }
}