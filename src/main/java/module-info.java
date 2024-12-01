module com.banuh.frologue {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;

    opens com.banuh.frologue to javafx.fxml;
    opens com.banuh.frologue.game.scenes to javafx.graphics;
    exports com.banuh.frologue;
    exports com.banuh.frologue.game.scenes;
}