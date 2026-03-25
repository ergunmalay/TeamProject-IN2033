package com.novasolutions.ipospu.gui;

import com.novasolutions.ipospu.controller.CommercialRegistrationController;
import com.novasolutions.ipospu.service.CommercialApplicationResult;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CommercialRegisterScreen extends VBox {

    private final CommercialRegistrationController commercialRegistrationController =
            new CommercialRegistrationController();

    public CommercialRegisterScreen(Stage stage) {
        Label title = new Label("Apply - Commercial Membership");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label info = new Label(
                """
                        Complete the form below to submit a commercial membership application.
                        Applications are reviewed by a System Administrator before approval.
                        No login-ready account will be created until your application is approved.""");
        info.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");
        info.setWrapText(true);

        TextField applicantNameField = new TextField();
        applicantNameField.setPromptText("Applicant full name");

        TextField companyNameField = new TextField();
        companyNameField.setPromptText("Company name");

        TextField companiesHouseNumberField = new TextField();
        companiesHouseNumberField.setPromptText("Companies House number");

        TextField directorNamesField = new TextField();
        directorNamesField.setPromptText("Director name(s)");

        TextField businessTypeField = new TextField();
        businessTypeField.setPromptText("Business type (e.g. Ltd, PLC, Partnership)");

        TextField businessAddressField = new TextField();
        businessAddressField.setPromptText("Business address");

        TextField emailField = new TextField();
        emailField.setPromptText("Contact email");

        Button submitButton = new Button("Submit Application");

        Label messageLabel = new Label();
        messageLabel.setWrapText(true);

        Button backButton = new Button("Back");

        setSpacing(10);
        setPadding(new Insets(24));
        getChildren().addAll(
                title,
                info,
                applicantNameField,
                companyNameField,
                companiesHouseNumberField,
                directorNamesField,
                businessTypeField,
                businessAddressField,
                emailField,
                submitButton,
                messageLabel,
                backButton
        );

        submitButton.setOnAction(e -> {
            String applicantName     = applicantNameField.getText();
            String companyName       = companyNameField.getText();
            String companiesHouseNum = companiesHouseNumberField.getText();
            String directorNames     = directorNamesField.getText();
            String businessType      = businessTypeField.getText();
            String businessAddress   = businessAddressField.getText();
            String email             = emailField.getText();

            CommercialApplicationResult result = commercialRegistrationController.submitCommercialApplication(
                    applicantName, companyName,
                    companiesHouseNum, directorNames,
                    businessType, businessAddress, email);

            if (result.isSuccess()) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(result.getMessage());
                // I disable the button after a successful submission to prevent duplicate
                // submissions within the same session.
                submitButton.setDisable(true);
                applicantNameField.clear();
                companyNameField.clear();
                companiesHouseNumberField.clear();
                directorNamesField.clear();
                businessTypeField.clear();
                businessAddressField.clear();
                emailField.clear();
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(result.getMessage());
            }
        });

        backButton.setOnAction(e -> {
            stage.getScene().setRoot(new RegistrationChoiceScreen(stage));
            stage.setTitle("IPOS-PU | Registration");
        });
    }
}
