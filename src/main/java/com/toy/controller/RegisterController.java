package com.toy.controller;

import com.toy.service.AuthService;
import com.toy.util.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController {
    @FXML private TextField nameField, emailField;
    @FXML private PasswordField passField;
    private AuthService authService = new AuthService();

    @FXML
    void handleRegister(ActionEvent event) {
        try {
            if (emailField.getText().isEmpty() || passField.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Заполните все поля!").show();
                return;
            }
            boolean success = authService.register(
                    emailField.getText(),
                    passField.getText(),
                    nameField.getText()
            );
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Регистрация успешна! Теперь войдите.");
                alert.showAndWait();
                Navigation.switchTo(event, "/views/login.fxml");
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Ошибка регистрации: " + e.getMessage()).show();
        }
    }

    @FXML
    void goToLogin(ActionEvent event) {
        Navigation.switchTo(event, "/views/login.fxml");
    }
}