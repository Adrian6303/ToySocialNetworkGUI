module com.example.toysocialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.toysocialnetworkgui to javafx.fxml;
    exports com.example.toysocialnetworkgui;
    opens com.example.toysocialnetworkgui.controller;
    exports com.example.toysocialnetworkgui.controller;
    opens com.example.toysocialnetworkgui.domain;
    exports com.example.toysocialnetworkgui.domain;
    opens com.example.toysocialnetworkgui.repository;
    exports com.example.toysocialnetworkgui.repository;
    opens com.example.toysocialnetworkgui.service;
    exports com.example.toysocialnetworkgui.service;
    opens com.example.toysocialnetworkgui.domain.validators;
    exports com.example.toysocialnetworkgui.domain.validators;
}