package com.toy.controller;

import com.toy.model.Order;
import com.toy.service.OrderService;
import com.toy.util.Navigation;
import com.toy.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class OrdersController {

    @FXML private ListView<String> ordersList;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextField trackingField, deliveryDateField;

    private final OrderService orderService = new OrderService();
    private List<Order> orders;
    private int selectedOrderId = -1;

    @FXML
    public void initialize() {
        statusCombo.getItems().addAll("new", "pending", "shipped", "delivered", "cancelled");
        statusCombo.getSelectionModel().selectFirst();

        ordersList.getSelectionModel().selectedIndexProperty().addListener((obs, old, idx) -> {
            int i = idx.intValue();
            if (i >= 0 && orders != null && i < orders.size()) {
                Order o = orders.get(i);
                selectedOrderId = o.id;
                statusCombo.setValue(o.status != null ? o.status : "pending");
                trackingField.setText(o.tracking_number != null ? o.tracking_number : "");
                deliveryDateField.setText(o.delivery_date != null ? o.delivery_date : "");
            }
        });
        refreshOrders();
    }

    @FXML
    void refreshOrders() {
        String uid = UserSession.getInstance().getUserId();
        orders = orderService.getCustomerOrders(uid);
        ordersList.getItems().clear();
        if (orders.isEmpty()) {
            ordersList.getItems().add("Заказы отсутствуют");
        } else {
            for (Order o : orders) {
                ordersList.getItems().add(String.format(
                        "Заказ #%d | %.2f ₽ | %s | Оплата: %s | Трек: %s | Доставка: %s",
                        o.id, o.total_amount, o.status, o.payment_type,
                        o.tracking_number != null ? o.tracking_number : "—",
                        o.delivery_date != null ? o.delivery_date : "—"));
            }
        }
    }

    @FXML
    void updateStatus() {
        if (selectedOrderId < 0) { new Alert(Alert.AlertType.WARNING, "Выберите заказ!").show(); return; }
        if (orderService.updateOrderStatus(selectedOrderId, statusCombo.getValue())) {
            new Alert(Alert.AlertType.INFORMATION, "Статус обновлён!").show();
            refreshOrders();
        } else {
            new Alert(Alert.AlertType.ERROR, "Ошибка обновления статуса").show();
        }
    }

    @FXML
    void updateDelivery() {
        if (selectedOrderId < 0) { new Alert(Alert.AlertType.WARNING, "Выберите заказ!").show(); return; }
        String tracking = trackingField.getText();
        String date = deliveryDateField.getText();
        if (tracking.isEmpty() && date.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Введите трек-номер или дату!").show(); return;
        }
        if (orderService.setDeliveryInfo(selectedOrderId, tracking, date)) {
            new Alert(Alert.AlertType.INFORMATION, "Доставка обновлена!").show();
            refreshOrders();
        } else {
            new Alert(Alert.AlertType.ERROR, "Ошибка обновления доставки").show();
        }
    }

    @FXML
    void goBack(ActionEvent event) { Navigation.switchTo(event, "/views/main_menu.fxml"); }
}
