module org.example.apploto {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.example.apploto to javafx.fxml;
    exports org.example.apploto;
    exports vue;
    exports module;
}