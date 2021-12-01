package org.game.snakek.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application implements Runnable{
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Snake Game");
        stage.setResizable(false);

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                getClass().getResource("game.fxml")
        ));

        Scene scene = new Scene(loader.load());
        stage.setWidth(500);
        stage.setHeight(625);
        stage.setScene(scene);
        stage.getIcons().add(
                new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("snake-game.png"))
                )
        );
        stage.show();
    }

    @Override
    public void run() {
        Application.launch();
    }
}
