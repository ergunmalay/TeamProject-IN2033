package com.novasolutions.ipospu.gui;

import com.novasolutions.ipospu.model.Member;
import com.novasolutions.ipospu.service.ChangePasswordResult;
import com.novasolutions.ipospu.service.MembershipService;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChangePasswordScreen extends VBox {

    private final MembershipService membershipService = new MembershipService();

    public ChangePasswordScreen(Stage stage, Member member) {

        Label title = new Label("Change Your Password");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");

        Button resetPasswordButton = new Button("Reset Password");
        Label messageLabel = new Label();

        setSpacing(10);
        setPadding(new Insets(20));

        getChildren().addAll(
                title,
                newPasswordField,
                confirmPasswordField,
                resetPasswordButton,
                messageLabel
        );

        resetPasswordButton.setOnAction(e -> {
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            ChangePasswordResult result =
                    membershipService.changePassword(
                            member.email(),
                            newPassword,
                            confirmPassword
                    );

            if (result.isSuccess()) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(result.getMessage());

                // Navigate to dashboard after success
                stage.getScene().setRoot(new DashboardScreen(stage, member));
                stage.setTitle("IPOS-PU | Dashboard");

            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(result.getMessage());
            }
        });
    }
}