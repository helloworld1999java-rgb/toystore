package com.toy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static String CSS = null;

    @Override
    public void start(Stage stage) throws Exception {
        // ← styles.css (с буквой s на конце)
        java.net.URL cssUrl = Main.class.getResource("/styles/styles.css");
        if (cssUrl != null) {
            CSS = cssUrl.toExternalForm();
            System.out.println("✅ CSS загружен: " + CSS);
        } else {
            System.out.println("⚠️ CSS не найден, запуск без стилей");
        }

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/views/login.fxml"));
        Scene scene = new Scene(loader.load());

        if (CSS != null) scene.getStylesheets().add(CSS);

        stage.setTitle("Toy Store 🧸");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
