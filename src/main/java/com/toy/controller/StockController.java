package com.toy.controller;

import com.toy.model.Reservation;
import com.toy.model.StockMovement;
import com.toy.model.Toy;
import com.toy.service.StockService;
import com.toy.service.ToyService;
import com.toy.util.Navigation;
import com.toy.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class StockController {

    @FXML private ListView<String> movementsList;
    @FXML private ListView<String> lowStockList;
    @FXML private ListView<String> reservationsList;
    @FXML private ComboBox<String> toyCombo;
    @FXML private ComboBox<String> moveTypeCombo;
    @FXML private TextField qtyField, noteField;

    private final StockService stockService = new StockService();
    private final ToyService toyService = new ToyService();
    private List<Toy> allToys;

    @FXML
    public void initialize() {
        moveTypeCombo.getItems().addAll(
                "arrival", "transfer_to_shop", "transfer_to_warehouse");
        moveTypeCombo.getSelectionModel().selectFirst();

        allToys = toyService.getAllToys();
        for (Toy t : allToys) toyCombo.getItems().add(t.id + " — " + t.name);
        if (!toyCombo.getItems().isEmpty()) toyCombo.getSelectionModel().selectFirst();

        refresh();
    }

    @FXML
    void refresh() {
        loadMovements();
        loadLowStock();
        loadReservations();
    }

    private void loadMovements() {
        movementsList.getItems().clear();
        for (StockMovement m : stockService.getAllMovements()) {
            movementsList.getItems().add(String.format("[%s] Товар #%d | %s | кол-во: %d | %s → %s | %s",
                    m.created_at != null ? m.created_at.substring(0, 10) : "—",
                    m.toy_id, m.movement_type, m.quantity,
                    m.from_location, m.to_location, m.note));
        }
        if (movementsList.getItems().isEmpty()) movementsList.getItems().add("Нет перемещений");
    }

    private void loadLowStock() {
        lowStockList.getItems().clear();
        for (Toy t : stockService.getLowStockToys())
            lowStockList.getItems().add("⚠️ " + t.name + " — остаток: " + t.stock_qty + " (мин: " + t.min_stock + ")");
        if (lowStockList.getItems().isEmpty()) lowStockList.getItems().add("✅ Все товары в норме");
    }

    private void loadReservations() {
        reservationsList.getItems().clear();
        for (Reservation r : stockService.getActiveReservations())
            reservationsList.getItems().add(String.format("Бронь #%d | Товар #%d | Клиент: %s | кол-во: %d",
                    r.id, r.toy_id, r.customer_id, r.quantity));
        if (reservationsList.getItems().isEmpty()) reservationsList.getItems().add("Нет активных бронирований");
    }

    @FXML
    void handleMovement() {
        try {
            int idx = toyCombo.getSelectionModel().getSelectedIndex();
            if (idx < 0 || qtyField.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Выберите товар и введите количество!").show(); return;
            }
            Toy toy = allToys.get(idx);
            String type = moveTypeCombo.getValue();
            int qty = Integer.parseInt(qtyField.getText());
            String from = type.contains("to_shop") ? "warehouse" : "shop";
            String to = type.contains("to_shop") ? "shop" : type.contains("to_warehouse") ? "warehouse" : "—";

            if (stockService.createMovement(toy.id, type, qty, from, to, noteField.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Перемещение зафиксировано!").show();
                qtyField.clear(); noteField.clear(); refresh();
            } else {
                new Alert(Alert.AlertType.ERROR, "Ошибка при создании перемещения").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Количество должно быть числом!").show();
        }
    }

    @FXML
    void handleReserve() {
        try {
            int idx = toyCombo.getSelectionModel().getSelectedIndex();
            if (idx < 0 || qtyField.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Выберите товар и введите количество!").show(); return;
            }
            Toy toy = allToys.get(idx);
            int qty = Integer.parseInt(qtyField.getText());
            String cid = UserSession.getInstance().getUserId();

            if (stockService.reserveToy(toy.id, cid, qty)) {
                new Alert(Alert.AlertType.INFORMATION, "Товар забронирован!").show();
                qtyField.clear(); refresh();
            } else {
                new Alert(Alert.AlertType.ERROR, "Ошибка при бронировании").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Количество должно быть числом!").show();
        }
    }

    @FXML
    void goBack(ActionEvent event) { Navigation.switchTo(event, "/views/main_menu.fxml"); }
}
