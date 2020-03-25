package dk.fitfit.solitaire;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LaunchFX extends Application {
    @Override
    public void start(Stage stage) {
        new FXApplication().start(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
