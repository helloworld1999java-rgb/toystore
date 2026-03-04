module com.toy.demo2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.toy.demo2 to javafx.fxml;
    exports com.toy.demo2;
}