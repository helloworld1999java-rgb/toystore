package com.toy.controller;

import com.toy.model.Toy;
import com.toy.service.CartService;
import com.toy.util.Navigation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CartController {
    @FXML private ListView<String> cartList;
    @FXML private Label totalLabel;
    @FXML private ComboBox<String> paymentCombo;

    private static ObservableList<Toy> cartItems = FXCollections.observableArrayList();
    private final CartService cartService = new CartService();

    public static void addItem(Toy toy) { cartItems.add(toy); }

    @FXML
    public void initialize() {
        paymentCombo.setItems(FXCollections.observableArrayList("Наличные", "Карта", "Онлайн"));
        paymentCombo.getSelectionModel().selectFirst();
        refreshCart();
    }

    private void refreshCart() {
        double total = 0;
        cartList.getItems().clear();
        for (Toy toy : cartItems) {
            cartList.getItems().add(toy.name + " — " + toy.price + " ₽");
            total += toy.price;
        }
        totalLabel.setText("Итого: " + total + " ₽");
    }

    @FXML
    void placeOrder(ActionEvent event) {
        try {
            if (cartItems.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Корзина пуста!").show();
                return;
            }

            String paymentType = paymentCombo.getValue().toLowerCase();
            double total = cartItems.stream().mapToDouble(t -> t.price).sum();

            if (cartService.placeOrder(cartItems, total, paymentType)) {
                StringBuilder sb = new StringBuilder();
                sb.append("=== ЧЕК ===\n\n");
                for (Toy t : cartItems)
                    sb.append("  • ").append(t.name).append(" — ").append(t.price).append(" ₽\n");
                sb.append("\n─────────────────\n");
                sb.append("ИТОГО: ").append(total).append(" ₽\n");
                sb.append("Оплата: ").append(paymentCombo.getValue());

                Alert receipt = new Alert(Alert.AlertType.INFORMATION, sb.toString());
                receipt.setTitle("Чек");
                receipt.setHeaderText("✅ Спасибо за покупку!");
                receipt.showAndWait();

                cartItems.clear();
                Navigation.switchTo(event, "/views/main_menu.fxml");
            } else {
                new Alert(Alert.AlertType.ERROR, "Не удалось оформить заказ").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Ошибка при оформлении заказа").show();
        }
    }

    @FXML
    void goBack(ActionEvent event) { Navigation.switchTo(event, "/views/main_menu.fxml"); }
}
