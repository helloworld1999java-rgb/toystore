package com.toy.controller;

import com.toy.util.ApiClient;
import com.toy.util.Navigation;
import com.toy.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProfileController {
    @FXML private TextField nameField, phoneField;
    @FXML private TextArea addressField;

    @FXML
    void saveChanges() {
        try {
            String json = "{\"full_name\":\"" + nameField.getText() + "\", \"phone\":\"" + phoneField.getText() + "\"}";
            // PATCH запрос в таблицу customers по ID пользователя
            ApiClient.sendRequest("/rest/v1/customers?id=eq." + UserSession.getInstance().getUserId(), "PATCH", json);
            System.out.println("Профиль обновлен");
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void handleLogout(javafx.event.ActionEvent event) {
        UserSession.logout();
        Navigation.switchTo(event, "/views/login.fxml");
    }
    @FXML
    void goBack(javafx.event.ActionEvent event) {
        Navigation.switchTo(event, "/views/main_menu.fxml");
    }
}