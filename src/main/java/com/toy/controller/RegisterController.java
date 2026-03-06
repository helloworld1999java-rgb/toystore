package com.toy.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.toy.service.AuthService;
import com.toy.util.ApiClient;
import com.toy.util.Navigation;
import com.toy.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController {
    @FXML private TextField nameField, emailField;
    @FXML private PasswordField passField;

    private final AuthService authService = new AuthService();
    private final Gson gson = new Gson();

    @FXML
    void handleRegister(ActionEvent event) {
        try {
            if (emailField.getText().isEmpty() || passField.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Заполните все поля!").show();
                return;
            }

            String response = authService.registerRaw(
                    emailField.getText().trim(),
                    passField.getText(),
                    nameField.getText()
            );

            JsonObject json = gson.fromJson(response, JsonObject.class);
            if (json.has("id") || json.has("user")) {
                // Получаем id и токен из ответа
                JsonObject userObj = json.has("user") ? json.get("user").getAsJsonObject() : json;
                String userId = userObj.get("id").getAsString();

                // Если пришёл access_token — сразу создаём запись customer
                if (json.has("access_token")) {
                    String token = json.get("access_token").getAsString();
                    UserSession.init(userId, emailField.getText(), "client", token);
                    createCustomer(userId, nameField.getText(), emailField.getText());
                }

                new Alert(Alert.AlertType.INFORMATION,
                        "Регистрация успешна! Проверьте почту или войдите.").showAndWait();
                Navigation.switchTo(event, "/views/login.fxml");
            } else {
                String err = json.has("msg") ? json.get("msg").getAsString() :
                        json.has("error_description") ? json.get("error_description").getAsString() :
                                "Не удалось зарегистрироваться";
                new Alert(Alert.AlertType.ERROR, err).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Ошибка регистрации: " + e.getMessage()).show();
        }
    }

    private void createCustomer(String userId, String name, String email) {
        try {
            String json = gson.toJson(java.util.Map.of(
                    "id", userId,
                    "full_name", name,
                    "email", email,
                    "role", "client"
            ));
            ApiClient.sendRequest("/rest/v1/customers", "POST", json);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void goToLogin(ActionEvent event) {
        Navigation.switchTo(event, "/views/login.fxml");
    }
}
