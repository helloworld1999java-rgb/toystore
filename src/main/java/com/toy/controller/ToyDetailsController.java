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
     * Метод для инициализации данных извне (например, из MainController)
     */
    public void setToyData(Toy toy) {
        this.currentToy = toy;
        nameLabel.setText(toy.name);
        categoryLabel.setText("Категория: " + toy.category_id); // Здесь можно добавить маппинг имен категорий
        priceLabel.setText(toy.price + " ₽");
        descArea.setText(toy.description != null ? toy.description : "Описание отсутствует.");
    }

    @FXML
    void addToCart() {
        if (currentToy != null) {
            CartController.addItem(currentToy);
            System.out.println("Товар " + currentToy.name + " добавлен в корзину через детали.");
        }
    }
}