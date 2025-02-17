module org.example.etlap {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml.crypto;


    opens org.example.etlap to javafx.fxml;
    exports org.example.etlap;
    exports org.example.etlap.controllers;
    opens org.example.etlap.controllers to javafx.fxml;
    exports org.example.etlap.models;
    opens org.example.etlap.models to javafx.fxml;
    exports org.example.etlap.services;
    opens org.example.etlap.services to javafx.fxml;
}