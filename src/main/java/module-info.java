module com.banuh.frologue {
    requires javafx.controls;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
  
    exports com.banuh.frologue;
    exports com.banuh.frologue.game.scenes;
  
    opens com.banuh.frologue to javafx.fxml;
    opens com.banuh.frologue.game.scenes to javafx.graphics;
    opens com.banuh.frologue.core.tilemap to com.fasterxml.jackson.databind;
}