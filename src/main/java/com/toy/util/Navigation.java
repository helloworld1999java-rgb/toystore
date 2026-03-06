package com.toy.util;

import com.toy.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigation {

    public static void switchTo(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Navigation.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            if (Main.CSS != null) {
                scene.getStylesheets().add(Main.CSS);
            }

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchToWithToy(String fxmlPath, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Navigation.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            if (Main.CSS != null) {
                scene.getStylesheets().add(Main.CSS);
            }

            Object controller = loader.getController();
            try {
                controller.getClass()
                        .getMethod("setToyData", data.getClass())
                        .invoke(controller, data);
            } catch (NoSuchMethodException ignored) {}

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
