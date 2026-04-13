package com.novasolutions.ipospu.gui;

import com.novasolutions.ipospu.controller.PromotionsController;
import com.novasolutions.ipospu.model.Member;
import com.novasolutions.ipospu.model.PromotionCampaign;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class CampaignAdminScreen extends BorderPane {

    private final PromotionsController promotionsController = new PromotionsController();
    private PromotionCampaign selectedCampaign;

    public CampaignAdminScreen(Stage stage, Member member) {
        setTop(new TopBar(stage, member));
        setLeft(new SideBar(stage, member, "admin"));
        if (member == null || !"ADMIN".equals(member.memberType())) {
            setCenter(buildAccessDenied());
            return;
        }
        setCenter(buildContent());
    }

    private ScrollPane buildAccessDenied() {
        Label title = new Label("Access denied");
        title.setStyle(AppStyles.headline());

        Label sub = new Label("Only admin accounts can create, edit, or deactivate promotion campaigns.");
        sub.setStyle(AppStyles.bodyMuted());
        sub.setWrapText(true);

        VBox box = new VBox(12, title, sub);
        box.setPadding(new Insets(32));
        box.setAlignment(Pos.CENTER_LEFT);

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + AppStyles.SURFACE + "; -fx-background: " + AppStyles.SURFACE + ";");
        return scroll;
    }

    private ScrollPane buildContent() {
        Label title = new Label("Campaign Administration");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + AppStyles.ON_SURFACE + ";");

        Label sub = new Label("Create, edit, and deactivate promotion campaigns.");
        sub.setStyle(AppStyles.bodyMuted());

        TextField nameField = new TextField();
        nameField.setPromptText("Campaign name");
        nameField.setStyle(AppStyles.inputField());

        DatePicker startPicker = new DatePicker(LocalDate.now());
        DatePicker endPicker = new DatePicker(LocalDate.now().plusDays(7));

        TextField discountField = new TextField();
        discountField.setPromptText("Discount percent");
        discountField.setStyle(AppStyles.inputField());

        TextField productsField = new TextField();
        productsField.setPromptText("Product IDs, comma separated");
        productsField.setStyle(AppStyles.inputField());

        Label statusLabel = new Label();
        statusLabel.setWrapText(true);

        Button createBtn = new Button("Create Campaign");
        createBtn.setStyle(AppStyles.ghostGradBtn());

        Button updateBtn = new Button("Update Selected");
        updateBtn.setStyle(AppStyles.secondaryBtn());

        Button deactivateBtn = new Button("Deactivate Selected");
        deactivateBtn.setStyle("-fx-background-color: " + AppStyles.ERROR_CONT + "; -fx-text-fill: " + AppStyles.ON_ERROR_CONT + ";");

        VBox form = new VBox(12,
                fieldBlock("Campaign Name", nameField),
                fieldBlock("Start Date", startPicker),
                fieldBlock("End Date", endPicker),
                fieldBlock("Discount %", discountField),
                fieldBlock("Product IDs", productsField),
                statusLabel,
                new HBox(10, createBtn, updateBtn, deactivateBtn)
        );
        form.setPadding(new Insets(24));
        form.setStyle("-fx-background-color: " + AppStyles.SURFACE_LOWEST + "; -fx-background-radius: 12;");
        form.setEffect(AppStyles.subtleShadow());
        HBox.setHgrow(form, Priority.ALWAYS);

        ListView<PromotionCampaign> campaignsList = new ListView<>();
        campaignsList.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(PromotionCampaign item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.name() + " [" + item.status() + "]");
            }
        });
        refreshCampaigns(campaignsList);

        campaignsList.getSelectionModel().selectedItemProperty().addListener((obs, old, campaign) -> {
            selectedCampaign = campaign;
            if (campaign != null) {
                nameField.setText(campaign.name());
                startPicker.setValue(campaign.startDate());
                endPicker.setValue(campaign.endDate());
                discountField.setText(String.valueOf(campaign.discountPercent()));
                productsField.setText(campaign.products().stream().map(p -> String.valueOf(p.productId())).reduce((a, b) -> a + "," + b).orElse(""));
            }
        });

        createBtn.setOnAction(e -> {
            try {
                promotionsController.createCampaign(
                        nameField.getText().trim(),
                        startPicker.getValue(),
                        endPicker.getValue(),
                        Double.parseDouble(discountField.getText().trim()),
                        parseProductIds(productsField.getText())
                );
                statusLabel.setStyle(AppStyles.successStyle());
                statusLabel.setText("Campaign created successfully.");
                refreshCampaigns(campaignsList);
            } catch (Exception ex) {
                statusLabel.setStyle(AppStyles.errorStyle());
                statusLabel.setText(ex.getMessage());
            }
        });

        updateBtn.setOnAction(e -> {
            if (selectedCampaign == null) {
                statusLabel.setStyle(AppStyles.errorStyle());
                statusLabel.setText("Select a campaign to update.");
                return;
            }
            try {
                promotionsController.updateCampaign(
                        selectedCampaign.id(),
                        nameField.getText().trim(),
                        startPicker.getValue(),
                        endPicker.getValue(),
                        Double.parseDouble(discountField.getText().trim()),
                        parseProductIds(productsField.getText())
                );
                statusLabel.setStyle(AppStyles.successStyle());
                statusLabel.setText("Campaign updated successfully.");
                refreshCampaigns(campaignsList);
            } catch (Exception ex) {
                statusLabel.setStyle(AppStyles.errorStyle());
                statusLabel.setText(ex.getMessage());
            }
        });

        deactivateBtn.setOnAction(e -> {
            if (selectedCampaign == null) {
                statusLabel.setStyle(AppStyles.errorStyle());
                statusLabel.setText("Select a campaign to deactivate.");
                return;
            }
            try {
                promotionsController.deactivateCampaign(selectedCampaign.id());
                statusLabel.setStyle(AppStyles.successStyle());
                statusLabel.setText("Campaign deactivated.");
                refreshCampaigns(campaignsList);
            } catch (Exception ex) {
                statusLabel.setStyle(AppStyles.errorStyle());
                statusLabel.setText(ex.getMessage());
            }
        });

        VBox listCard = new VBox(12,
                new Label("Campaigns"),
                campaignsList
        );
        listCard.setPadding(new Insets(24));
        listCard.setMinWidth(300);
        listCard.setStyle("-fx-background-color: " + AppStyles.SURFACE_LOWEST + "; -fx-background-radius: 12;");
        listCard.setEffect(AppStyles.subtleShadow());

        HBox content = new HBox(18, form, listCard);
        content.setAlignment(Pos.TOP_LEFT);

        VBox page = new VBox(20, title, sub, content);
        page.setPadding(new Insets(32));
        page.setStyle("-fx-background-color: " + AppStyles.SURFACE + ";");

        ScrollPane scroll = new ScrollPane(page);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + AppStyles.SURFACE + "; -fx-background: " + AppStyles.SURFACE + ";");
        return scroll;
    }

    private VBox fieldBlock(String labelText, javafx.scene.Node input) {
        Label label = new Label(labelText);
        label.setStyle(AppStyles.fieldLabel());
        VBox box = new VBox(6, label, input);
        return box;
    }

    private void refreshCampaigns(ListView<PromotionCampaign> campaignsList) {
        campaignsList.getItems().setAll(promotionsController.getAllCampaigns());
    }

    private List<Integer> parseProductIds(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("At least one product ID is required");
        }
        // Admin input stays lightweight: comma-separated product IDs map directly to campaign_products.
        return java.util.Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .toList();
    }
}
