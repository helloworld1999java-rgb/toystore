package com.toy.controller;

import com.toy.service.StockService;
import com.toy.service.ToyService;
import com.toy.util.Navigation;
import com.toy.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class AdminMainController {

    @FXML private Label welcomeLabel;
    @FXML private Label statsLabel;

    private final ToyService  toyService  = new ToyService();
    private final StockService stockService = new StockService();

    @FXML
    public void initialize() {
        welcomeLabel.setText("Добро пожаловать, " + UserSession.getInstance().getEmail());
        int total   = toyService.getAllToys().size();
        int lowStock = stockService.getLowStockToys().size();
        statsLabel.setText("Товаров в базе: " + total + " | Требуют пополнения: " + lowStock);
    }

    @FXML void goToToys(ActionEvent e)    { Navigation.switchTo(e, "/views/admin_toys.fxml"); }
    @FXML void goToStock(ActionEvent e)   { Navigation.switchTo(e, "/views/stock.fxml"); }
    @FXML void goToOrders(ActionEvent e)  { Navigation.switchTo(e, "/views/orders.fxml"); }
    @FXML void goToReceipts(ActionEvent e){ Navigation.switchTo(e, "/views/receipts.fxml"); }

    @FXML
    void handleLogout(ActionEvent e) {
        UserSession.logout();
        Navigation.switchTo(e, "/views/login.fxml");
    }
}
