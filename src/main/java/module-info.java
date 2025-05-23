module com.mycompany.simple_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires fontawesomefx;
    requires javafx.graphics;
    opens com.mycompany.simple_project to javafx.fxml;
    opens models to javafx.base;
    exports com.mycompany.simple_project;
}
