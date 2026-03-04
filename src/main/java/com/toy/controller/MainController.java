package com.toy.controller;

import com.toy.model.Toy;
import com.toy.service.ToyService;
import com.toy.util.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.util.List;

public class MainController {

    @FXML private FlowPane toyContainer; // Контейнер для карточек в FXML
    @FXML private TextField searchField;
    @FXML private Label welcomeLabel;

    private final ToyService toyService = new ToyService();

    @FXML
    public void initialize() {
        // Загружаем все товары при старте
        loadToys("");
    }

    /**
     * Метод для загрузки товаров и отображения их в интерфейсе
     */
    private void loadToys(String query) {
        try {
            List<Toy> toys;
            if (query == null || query.isEmpty()) {
                toys = toyService.getAllToys();
            } else {
                toys = toyService.searchToysByName(query);
            }

            toyContainer.getChildren().clear();

            for (Toy toy : toys) {
                toyContainer.getChildren().add(createToyCard(toy));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Ошибка загрузки", " не удалось получить список товаров.");
        }
    }

    /**
     * Создает визуальную карточку для товара
     */
    private VBox createToyCard(Toy toy) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #cccccc; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label nameLabel = new Label(toy.name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        nameLabel.setWrapText(true);

        Label priceLabel = new Label(toy.price + " ₽");
        priceLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 16px;");

        Button buyButton = new Button("В корзину");
        buyButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");

        // Обработка нажатия кнопки "Купить"
        buyButton.setOnAction(e -> {
            CartController.addItem(toy);
            showInfo("Добавлено", toy.name + " добавлен в корзину.");
        });

        card.getChildren().addAll(nameLabel, priceLabel, buyButton);
        return card;
    }

    @FXML
    void handleSearch() {
        loadToys(searchField.getText());
    }

    @FXML
    void goToCart(ActionEvent event) {
        Navigation.switchTo(event, "/views/cart.fxml");
    }

    @FXML
    void goToProfile(ActionEvent event) {
        Navigation.switchTo(event, "/views/profile.fxml");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}