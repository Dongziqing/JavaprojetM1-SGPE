module com.example.studentgrade {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.sgms.controller to javafx.fxml;
    exports com.sgms.controller;
    exports com.sgms.pojo;
    opens com.sgms.pojo to javafx.fxml;
    exports com.sgms.app;
    opens com.sgms.app to javafx.fxml;
}