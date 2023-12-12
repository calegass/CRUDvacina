module poov.praticavacinajavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    requires org.controlsfx.controls;

    opens poov.praticavacinajavafx to javafx.fxml;
    exports poov.praticavacinajavafx;
    exports poov.praticavacinajavafx.controller;
    exports poov.praticavacinajavafx.modelo;
    opens poov.praticavacinajavafx.controller to javafx.fxml;
}