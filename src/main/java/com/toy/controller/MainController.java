package com.toy.controller;

import com.toy.model.Toy;
import com.toy.service.ToyService;
import com.toy.util.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class MainController {

    @FXML private FlowPane toyContainer;
    @FXML private TextField searchField;
    @FXML private VBox categoriesBox; // ← теперь VBox, не HBox

    private final ToyService toyService = new ToyService();
    private Button activeCatBtn = null;

    private static final String[][] CATEGORIES = {
            {"Все",             "🎯"},
            {"Конструкторы",    "🧱"},
            {"Куклы",           "🪆"},
            {"Машинки",         "🚗"},
            {"Настольные игры", "♟️"}
    };

    @FXML
    public void initialize() {
        if (categoriesBox != null) setupCategories();
        loadToys("");
    }

    private void setupCategories() {
        // Оставляем Label "КАТЕГОРИИ", добавляем кнопки после него
        categoriesBox.getChildren().removeIf(n -> n instanceof Button);

        for (String[] cat : CATEGORIES) {
            Button btn = new Button(cat[1] + "  " + cat[0]);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setPadding(new Insets(10, 14, 10, 14));
            btn.setStyle(
                    "-fx-background-color:transparent;" +
                            "-fx-text-fill:rgba(255,255,255,0.75);" +
                            "-fx-font-size:13px;" +
                            "-fx-cursor:hand;" +
                            "-fx-background-radius:8;"
            );
            btn.setOnMouseEntered(e -> {
                if (btn != activeCatBtn)
                    btn.setStyle(
                            "-fx-background-color:rgba(255,255,255,0.08);" +
                                    "-fx-text-fill:white;" +
                                    "-fx-font-size:13px;" +
                                    "-fx-cursor:hand;" +
                                    "-fx-background-radius:8;"
                    );
            });
            btn.setOnMouseExited(e -> {
                if (btn != activeCatBtn)
                    btn.setStyle(
                            "-fx-background-color:transparent;" +
                                    "-fx-text-fill:rgba(255,255,255,0.75);" +
                                    "-fx-font-size:13px;" +
                                    "-fx-cursor:hand;" +
                                    "-fx-background-radius:8;"
                    );
            });
            btn.setOnAction(e -> {
                setActiveCategory(btn);
                if ("Все".equals(cat[0])) loadToys("");
                else displayToys(
                        toyService.getToysByCategory(
                                toyService.getCategoryIdByName(cat[0])));
            });
            categoriesBox.getChildren().add(btn);
            if ("Все".equals(cat[0])) setActiveCategory(btn);
        }
    }

    private void setActiveCategory(Button btn) {
        if (activeCatBtn != null) {
            activeCatBtn.setStyle(
                    "-fx-background-color:transparent;" +
                            "-fx-text-fill:rgba(255,255,255,0.75);" +
                            "-fx-font-size:13px;" +
                            "-fx-cursor:hand;" +
                            "-fx-background-radius:8;"
            );
        }
        btn.setStyle(
                "-fx-background-color:rgba(255,255,255,0.15);" +
                        "-fx-text-fill:white;" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-cursor:hand;" +
                        "-fx-background-radius:8;" +
                        "-fx-border-color:rgba(255,255,255,0.3);" +
                        "-fx-border-radius:8; -fx-border-width:1;"
        );
        activeCatBtn = btn;
    }

    private void loadToys(String query) {
        List<Toy> toys = (query == null || query.isEmpty())
                ? toyService.getAllToys()
                : toyService.searchToysByName(query);
        displayToys(toys);
    }

    private void displayToys(List<Toy> toys) {
        toyContainer.getChildren().clear();
        if (toys == null || toys.isEmpty()) {
            VBox empty = new VBox(10);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(80));
            Label icon = new Label("🔍");
            icon.setStyle("-fx-font-size:48px;");
            Label msg  = new Label("Товары не найдены");
            msg.setStyle("-fx-font-size:16px; -fx-text-fill:#95a5a6;");
            empty.getChildren().addAll(icon, msg);
            toyContainer.getChildren().add(empty);
            return;
        }
        for (Toy toy : toys) toyContainer.getChildren().add(createToyCard(toy));
    }

    private VBox createToyCard(Toy toy) {
        VBox card = new VBox(0);
        card.setPrefWidth(200);
        card.setMaxWidth(200);
        card.setStyle(
                "-fx-background-color:white;" +
                        "-fx-border-color:#e8ecef;" +
                        "-fx-border-width:1;" +
                        "-fx-border-radius:12;" +
                        "-fx-background-radius:12;" +
                        "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.07),8,0,0,3);"
        );

        // Цветная шапка
        String[] colors = {"#3498db","#e74c3c","#27ae60","#e67e22","#8e44ad","#16a085"};
        String color = colors[Math.abs(toy.name.hashCode()) % colors.length];
        VBox header = new VBox();
        header.setPrefHeight(75);
        header.setAlignment(Pos.CENTER);
        header.setStyle(
                "-fx-background-color:" + color + ";" +
                        "-fx-background-radius:12 12 0 0;"
        );
        Label emoji = new Label(getCategoryEmoji(toy.category_id));
        emoji.setStyle("-fx-font-size:34px;");
        header.getChildren().add(emoji);

        // Тело
        VBox body = new VBox(6);
        body.setPadding(new Insets(10, 12, 12, 12));

        Label name = new Label(toy.name);
        name.setWrapText(true);
        name.setMaxWidth(175);
        name.setStyle("-fx-font-weight:bold; -fx-font-size:13px; -fx-text-fill:#2c3e50;");

        VBox specs = new VBox(2);
        if (toy.material != null && !toy.material.isEmpty())
            specs.getChildren().add(specRow("🧱", toy.material));
        if (toy.color != null && !toy.color.isEmpty())
            specs.getChildren().add(specRow("🎨", toy.color));
        if (toy.size != null && !toy.size.isEmpty())
            specs.getChildren().add(specRow("📐", toy.size));

        Separator sep = new Separator();
        sep.setPadding(new Insets(3,0,3,0));

        HBox priceRow = new HBox();
        priceRow.setAlignment(Pos.CENTER_LEFT);
        Label price = new Label(String.format("%.0f ₽", toy.price));
        price.setStyle("-fx-font-size:16px; -fx-font-weight:bold; -fx-text-fill:#27ae60;");
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        boolean low = toy.stock_qty <= toy.min_stock;
        Label stock = new Label(toy.stock_qty > 0 ? "✓" + toy.stock_qty : "✗");
        stock.setStyle(low || toy.stock_qty <= 0
                ? "-fx-font-size:11px; -fx-text-fill:#e74c3c;"
                : "-fx-font-size:11px; -fx-text-fill:#27ae60;");
        priceRow.getChildren().addAll(price, sp, stock);

        HBox btns = new HBox(6);
        btns.setPadding(new Insets(6,0,0,0));
        Button buy = new Button("🛒 В корзину");
        buy.setDisable(toy.stock_qty <= 0);
        buy.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(buy, Priority.ALWAYS);
        buy.setStyle("-fx-background-color:#27ae60; -fx-text-fill:white;" +
                "-fx-background-radius:7; -fx-cursor:hand; -fx-font-weight:bold;");
        buy.setOnAction(e -> {
            CartController.addItem(toy);
            Alert a = new Alert(Alert.AlertType.INFORMATION, toy.name + " добавлен ✓");
            a.setHeaderText(null); a.setTitle("Корзина"); a.show();
        });
        Button det = new Button("ℹ");
        det.setStyle("-fx-background-color:#ecf0f1; -fx-text-fill:#2c3e50;" +
                "-fx-background-radius:7; -fx-cursor:hand;");
        det.setOnAction(e -> Navigation.switchToWithToy("/views/toy_details.fxml", toy));
        btns.getChildren().addAll(buy, det);

        body.getChildren().addAll(name, specs, sep, priceRow, btns);
        card.getChildren().addAll(header, body);
        return card;
    }

    private HBox specRow(String icon, String text) {
        HBox row = new HBox(4);
        row.setAlignment(Pos.CENTER_LEFT);
        Label ic  = new Label(icon);
        ic.setStyle("-fx-font-size:10px;");
        Label lb  = new Label(text);
        lb.setStyle("-fx-font-size:11px; -fx-text-fill:#95a5a6;");
        lb.setWrapText(true);
        lb.setMaxWidth(150);
        row.getChildren().addAll(ic, lb);
        return row;
    }

    private String getCategoryEmoji(Integer catId) {
        if (catId == null) return "🎁";
        return switch (catId) {
            case 1 -> "🧱"; case 2 -> "🪆";
            case 3 -> "🚗"; case 4 -> "♟️";
            default -> "🎁";
        };
    }

    @FXML void handleSearch()              { loadToys(searchField.getText()); }
    @FXML void goToCart(ActionEvent e)     { Navigation.switchTo(e, "/views/cart.fxml"); }
    @FXML void goToProfile(ActionEvent e)  { Navigation.switchTo(e, "/views/profile.fxml"); }
    @FXML void goToOrders(ActionEvent e)   { Navigation.switchTo(e, "/views/orders.fxml"); }
    @FXML void goToReceipts(ActionEvent e) { Navigation.switchTo(e, "/views/receipts.fxml"); }
}
