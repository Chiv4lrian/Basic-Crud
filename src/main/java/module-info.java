module com.example.crud {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;


    opens com.example.crud to javafx.fxml;
    exports com.example.crud;
}