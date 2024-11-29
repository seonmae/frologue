module com.banuh.frologue {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    
    opens com.banuh.frologue to javafx.fxml;
    exports com.banuh.frologue;
}