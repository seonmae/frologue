module com.banuh.frologue {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.banuh.frologue to javafx.fxml;
    exports com.banuh.frologue;
}