package com.toy.controller;

import com.toy.model.Toy;
import com.toy.service.ToyService;
import com.toy.util.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminToysController {

    @FXML private TableView<Toy> toysTable;
    @FXML private TableColumn<Toy, Integer> colId;
    @FXML private TableColumn<Toy, String> colName;
    @FXML private TableColumn<Toy, Double> colPrice;
    @FXML private TableColumn<Toy, Integer> colStock;
    @FXML private TableColumn<Toy, Integer> colMinStock;

    @FXML private TextField nameField, colorField, materialField, sizeField;
    @FXML private TextField priceField, stockField, minStockField, ageField;
    @FXML private TextArea descField;
    @FXML private ComboBox<String> categoryCombo;

    private final ToyService toyService = new ToyService();
    private Toy selectedToy;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock_qty"));
        colMinStock.setCellValueFactory(new PropertyValueFactory<>("min_stock"));

        categoryCombo.getItems().addAll("Конструкторы", "Куклы", "Машинки", "Настольные игры");
        categoryCombo.getSelectionModel().selectFirst();

        toysTable.getSelectionModel().selectedItemProperty().addListener((obs, old, toy) -> {
            if (toy != null) fillForm(toy);
        });
        loadToys();
    }

    private void loadToys() { toysTable.getItems().setAll(toyService.getAllToys()); }

    private void fillForm(Toy toy) {
        selectedToy = toy;
        nameField.setText(toy.name);
        priceField.setText(String.valueOf(toy.price));
        stockField.setText(String.valueOf(toy.stock_qty));
        minStockField.setText(String.valueOf(toy.min_stock));
        ageField.setText(String.valueOf(toy.age_from));
        colorField.setText(toy.color != null ? toy.color : "");
        materialField.setText(toy.material != null ? toy.material : "");
        sizeField.setText(toy.size != null ? toy.size : "");
        descField.setText(toy.description != null ? toy.description : "");
    }

    private Toy buildToyFromForm() {
        Toy toy = new Toy();
        toy.name = nameField.getText();
        toy.price = Double.parseDouble(priceField.getText());
        toy.stock_qty = Integer.parseInt(stockField.getText().isEmpty() ? "0" : stockField.getText());
        toy.min_stock = Integer.parseInt(minStockField.getText().isEmpty() ? "5" : minStockField.getText());
        toy.age_from = Integer.parseInt(ageField.getText().isEmpty() ? "0" : ageField.getText());
        toy.color = colorField.getText();
        toy.material = materialField.getText();
        toy.size = sizeField.getText();
        toy.description = descField.getText();
        toy.category_id = toyService.getCategoryIdByName(categoryCombo.getValue());
        return toy;
    }

    @FXML
    void handleAdd() {
        try {
            if (nameField.getText().isEmpty() || priceField.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Заполните название и цену!").show();
                return;
            }
            if (toyService.addToy(buildToyFromForm())) {
                new Alert(Alert.AlertType.INFORMATION, "Товар добавлен!").show();
                loadToys(); clearForm();
            } else {
                new Alert(Alert.AlertType.ERROR, "Ошибка при добавлении").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Проверьте данные: " + e.getMessage()).show();
        }
    }

    @FXML
    void handleUpdate() {
        try {
            if (selectedToy == null) {
                new Alert(Alert.AlertType.WARNING, "Выберите товар из таблицы!").show();
                return;
            }
            Toy toy = buildToyFromForm();
            toy.id = selectedToy.id;
            if (toyService.updateToy(toy)) {
                new Alert(Alert.AlertType.INFORMATION, "Товар обновлён!").show();
                loadToys(); clearForm();
            } else {
                new Alert(Alert.AlertType.ERROR, "Ошибка при обновлении").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Проверьте данные: " + e.getMessage()).show();
        }
    }

    @FXML
    void handleDelete() {
        if (selectedToy == null) { new Alert(Alert.AlertType.WARNING, "Выберите товар!").show(); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Удалить «" + selectedToy.name + "»?");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                if (toyService.deleteToy(selectedToy.id)) {
                    new Alert(Alert.AlertType.INFORMATION, "Товар удалён!").show();
                    loadToys(); clearForm();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Ошибка при удалении").show();
                }
            }
        });
    }

    private void clearForm() {
        selectedToy = null;
        nameField.clear(); priceField.clear(); stockField.clear();
        minStockField.clear(); ageField.clear(); colorField.clear();
        materialField.clear(); sizeField.clear(); descField.clear();
    }

    @FXML
    void goBack(ActionEvent event) { Navigation.switchTo(event, "/views/main_menu.fxml"); }
}
