package com.toy.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.toy.service.AuthService;
import com.toy.util.ApiClient;
import com.toy.util.Navigation;
import com.toy.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passField;

    private final AuthService authService = new AuthService();
    private final Gson gson = new Gson();

    @FXML
    void handleLogin(ActionEvent event) {
        try {
            String email    = emailField.getText().trim();
            String password = passField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Введите email и пароль").show();
                return;
            }

            String response = authService.login(email, password);
            JsonObject json = gson.fromJson(response, JsonObject.class);

            if (json.has("access_token")) {
                String accessToken = json.get("access_token").getAsString();
                String userId = json.get("user").getAsJsonObject()
                        .get("id").getAsString();

                // Временно инициализируем сессию с токеном, чтобы fetchRole сработал
                UserSession.init(userId, email, "client", accessToken);

                String role = fetchRole(userId);
                UserSession.init(userId, email, role, accessToken);

                if ("admin".equalsIgnoreCase(role)) {
                    Navigation.switchTo(event, "/views/admin_main.fxml");
                } else {
                    Navigation.switchTo(event, "/views/main_menu.fxml");
                }
            } else {
                String error = json.has("error_description")
                        ? json.get("error_description").getAsString()
                        : "Ошибка входа";
                new Alert(Alert.AlertType.ERROR, error).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Ошибка соединения с сервером").show();
        }
    }

    private String fetchRole(String userId) {
        try {
            HttpResponse<String> r = ApiClient.sendRequest(
                    "/rest/v1/customers?id=eq." + userId + "&select=role", "GET", "");
            if (r.statusCode() == 200) {
                List<Map<String, Object>> list = gson.fromJson(
                        r.body(), new TypeToken<List<Map<String, Object>>>(){}.getType());
                if (!list.isEmpty() && list.get(0).get("role") != null)
                    return list.get(0).get("role").toString();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "client";
    }

    @FXML
    void goToRegister(ActionEvent event) {
        Navigation.switchTo(event, "/views/register.fxml");
    }
}
