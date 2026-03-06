module com.toy.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;

    opens com.toy            to javafx.fxml;
    opens com.toy.controller to javafx.fxml;
    opens com.toy.model      to com.google.gson;

    exports com.toy;
}
