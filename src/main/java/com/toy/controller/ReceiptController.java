package com.toy.controller;

import com.toy.model.Receipt;
import com.toy.service.ReceiptService;
import com.toy.util.Navigation;
import com.toy.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ReceiptController {

    @FXML private ListView<String> receiptsList;
    private final ReceiptService receiptService = new ReceiptService();

    @FXML
    public void initialize() {
        receiptsList.getItems().clear();
        String uid = UserSession.getInstance().getUserId();
        for (Receipt r : receiptService.getReceiptsByCustomer(uid)) {
            receiptsList.getItems().add(String.format(
                    "Чек #%d | Заказ #%d | Сумма: %.2f ₽ | Оплата: %s | Дата: %s\n  Состав: %s",
                    r.id, r.order_id, r.total_amount, r.payment_type,
                    r.created_at != null ? r.created_at.substring(0, 10) : "—",
                    r.items_json));
        }
        if (receiptsList.getItems().isEmpty()) receiptsList.getItems().add("Чеки отсутствуют");
    }

    @FXML
    void goBack(ActionEvent event) { Navigation.switchTo(event, "/views/main_menu.fxml"); }
}
