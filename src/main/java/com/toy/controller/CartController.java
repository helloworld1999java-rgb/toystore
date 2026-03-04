package com.toy.controller;

import com.toy.model.Toy;
import com.toy.service.CartService;
import com.toy.util.Navigation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class CartController {
    @FXML private ListView<String> cartList;
    @FXML private Label totalLabel;

    private static ObservableList<Toy> cartItems = FXCollections.observableArrayList();
    private final CartService cartService = new CartService();

    public static void addItem(Toy toy) { cartItems.add(toy); }

    @FXML
    public void initialize() {
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
            double total = cartItems.stream().mapToDouble(t -> t.price).sum();
            if (cartItems.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Корзина пуста!").show();
                return;
            }
            if (cartService.placeOrder(cartItems, total)) {
                new Alert(Alert.AlertType.INFORMATION, "Заказ успешно оформлен!").showAndWait();
                cartItems.clear();
                Navigation.switchTo(event, "/views/main_menu.fxml");
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Ошибка при оформлении заказа").show();
            e.printStackTrace();
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        Navigation.switchTo(event, "/views/main_menu.fxml");
    }
}