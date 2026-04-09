package com.novasolutions.ipospu.gui;

import com.novasolutions.ipospu.model.Member;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Shared navy sidebar for authenticated screens.
 * Pass one of "dashboard", "catalogue", "orders", "profile" as activeItem.
 */
public class SideBar extends VBox {

    public SideBar(Stage stage, Member member, String activeItem) {

        // ── Brand section ──────────────────────────────────────────────────
        Label title = new Label("Architectural Ledger");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("INTERNAL PROCUREMENT");
        subtitle.setStyle("-fx-font-size: 9px; -fx-text-fill: rgba(255,255,255,0.4);");

        VBox brand = new VBox(3, title, subtitle);
        brand.setPadding(new Insets(0, 0, 24, 4));

        // ── Nav items ─────────────────────────────────────────────────────
        VBox navCatalogue = navItem("View Catalogue", "catalogue".equals(activeItem));
        VBox navCart      = navItem("My Cart",        "cart".equals(activeItem));
        VBox navOrders    = navItem("My Orders",      "orders".equals(activeItem));
        VBox navProfile   = navItem("My Profile",     "profile".equals(activeItem));

        navCatalogue.setOnMouseClicked(e -> {
            stage.getScene().setRoot(new CatalogueScreen(stage, member));
            stage.setTitle("IPOS-PU | Catalogue");
        });

        navCart.setOnMouseClicked(e -> {
            stage.getScene().setRoot(new CartScreen(stage, member));
            stage.setTitle("IPOS-PU | My Cart");
        });

        navOrders.setOnMouseClicked(e -> {
            stage.getScene().setRoot(new OrderHistoryScreen(stage, member));
            stage.setTitle("IPOS-PU | My Orders");
        });

        navProfile.setOnMouseClicked(e -> {
            stage.getScene().setRoot(new MemberProfileScreen(stage, member));
            stage.setTitle("IPOS-PU | My Profile");
        });

        VBox nav = new VBox(4, navCatalogue, navCart, navOrders, navProfile);

        // ── Spacer ────────────────────────────────────────────────────────
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // ── User card ─────────────────────────────────────────────────────
        Label nameLabel = new Label(member != null ? member.fullName() : "");
        nameLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: white;");
        nameLabel.setWrapText(true);

        String roleStr = member != null
                ? ("COMMERCIAL".equals(member.memberType()) ? "Commercial Member" : "Non-Commercial Member")
                : "";
        Label roleLabel = new Label(roleStr);
        roleLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: rgba(255,255,255,0.45);");

        VBox userCard = new VBox(4, nameLabel, roleLabel);
        userCard.setStyle("-fx-background-color: rgba(255,255,255,0.06);" +
                          "-fx-background-radius: 10; -fx-padding: 14;");

        // ── Assembly ──────────────────────────────────────────────────────
        setPrefWidth(240);
        setMinWidth(240);
        setMaxWidth(240);
        setPadding(new Insets(24, 12, 16, 12));
        setSpacing(0);
        setStyle("-fx-background-color: " + AppStyles.NAVY + ";");

        getChildren().addAll(brand, nav, spacer, userCard);
    }

    private VBox navItem(String text, boolean active) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 13px;" +
                       "-fx-font-weight: " + (active ? "bold" : "normal") + ";" +
                       "-fx-text-fill: " + (active ? "white" : "rgba(255,255,255,0.55)") + ";");

        String activeStyle   = "-fx-background-color: " + AppStyles.PRIMARY + "; -fx-background-radius: 6; -fx-cursor: hand;";
        String inactiveStyle = "-fx-background-color: transparent; -fx-background-radius: 6; -fx-cursor: hand;";
        String hoverStyle    = "-fx-background-color: rgba(255,255,255,0.08); -fx-background-radius: 6; -fx-cursor: hand;";

        VBox box = new VBox(label);
        box.setPadding(new Insets(10, 14, 10, 14));
        box.setStyle(active ? activeStyle : inactiveStyle);

        if (!active) {
            box.setOnMouseEntered(e -> box.setStyle(hoverStyle));
            box.setOnMouseExited(e -> box.setStyle(inactiveStyle));
        }

        return box;
    }
}
