module org.game.snakek {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;

    opens org.game.snakek.gui to javafx.fxml;
    exports org.game.snakek.gui;
    exports org.game.snakek;
    opens org.game.snakek to javafx.fxml;
}