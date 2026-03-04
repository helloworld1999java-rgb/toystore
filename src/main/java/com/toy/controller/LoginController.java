package com.toy.controller;

import com.toy.service.AuthService;
import com.toy.util.Navigation;
import com.toy.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    private AuthService authService = new AuthService();

    @FXML
    void handleLogin(ActionEvent event) {
        try {
            String response = authService.login(emailField.getText(), passField.getText());
            if (response.contains("access_token")) {
                // В идеале распарсить UUID из ответа
                UserSession.init("uuid-from-db", emailField.getText(), "client");
                Navigation.switchTo(event, "/views/main_menu.fxml");
            } else {
                new Alert(Alert.AlertType.ERROR, "Ошибка входа!").show();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void goToRegister(ActionEvent event) {
        Navigation.switchTo(event, "/views/register.fxml");
    }
}