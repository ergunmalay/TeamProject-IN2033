package com.novasolutions.ipospu.gui;

import com.novasolutions.ipospu.controller.RegistrationController;
import com.novasolutions.ipospu.service.RegistrationResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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

                showSuccessAlert(result.getPassword());

                nameField.clear();
                emailField.clear();

                messageLabel.setText("");
                copyButton.setVisible(false);

                stage.getScene().setRoot(new LoginScreen());
                stage.setTitle("IPOS-PU | Login");

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

    private void showSuccessAlert(String password) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setHeaderText("Your Account Has Been Created");

        alert.setContentText(
                "Your temporary password:\n\n" + password + "\n\n" +
                        "Important:\n" +
                        "- Copy this password now\n" +
                        "- You will be required to change it on first login"
        );

        ButtonType copyButtonType = new ButtonType("Copy Password");
        ButtonType okButtonType = new ButtonType("OK");

        alert.getButtonTypes().setAll(copyButtonType, okButtonType);

        alert.showAndWait().ifPresent(response -> {
            if (response == copyButtonType) {
                ClipboardContent content = new ClipboardContent();
                content.putString(password);
                Clipboard.getSystemClipboard().setContent(content);
            }
        });
    }
}