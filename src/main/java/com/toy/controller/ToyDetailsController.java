package com.toy.controller;

import com.toy.model.Toy;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ToyDetailsController {

    @FXML private Label nameLabel;
    @FXML private Label categoryLabel;
    @FXML private Label priceLabel;
    @FXML private TextArea descArea;

    private Toy currentToy;

    /**
     * Инициализация данных игрушки извне (например, из MainController при выборе товара)
     */
    public void setToyData(Toy toy) {
        this.currentToy = toy;
        nameLabel.setText(toy.name);
        categoryLabel.setText("Категория: " + (toy.category_id != null ? toy.category_id : "Не указана"));
        priceLabel.setText(toy.price + " ₽");
        descArea.setText(toy.description != null ? toy.description : "Описание отсутствует.");
    }

    /**
     * Добавить текущую игрушку в корзину
     */
    @FXML
    void addToCart() {
        if (currentToy != null) {
            CartController.addItem(currentToy);
            System.out.println("Товар " + currentToy.name + " добавлен в корзину через детали.");
        }
    }
}
