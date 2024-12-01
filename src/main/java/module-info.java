module com.banuh.frologue {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;

    opens com.banuh.frologue to javafx.fxml;
    opens com.banuh.frologue.core.tilemap to com.fasterxml.jackson.databind;
    exports com.banuh.frologue;
}