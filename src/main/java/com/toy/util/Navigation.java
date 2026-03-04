package com.toy.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class Navigation {
    public static void switchTo(ActionEvent event, String fxmlPath) {
        try {
            // Загрузка через Navigation.class гарантирует поиск в папке ресурсов
            FXMLLoader loader = new FXMLLoader(Navigation.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Ошибка: Не найден FXML файл по пути " + fxmlPath);
            e.printStackTrace();
        }
    }
}