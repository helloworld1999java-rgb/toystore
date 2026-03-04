module com.toy.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;
    requires java.desktop;
    requires com.google.gson;

    // Открываем пакеты для Reflection (FXMLLoader и Gson)
    opens com.toy.controller to javafx.fxml;
    opens com.toy.model to com.google.gson;

    exports com.toy;
}