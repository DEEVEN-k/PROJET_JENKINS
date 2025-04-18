package com.example.calculatrice;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CalculatriceApp extends Application {
    @Override
    public void start(Stage stage) {
        CalculatriceUI ui = new CalculatriceUI();
        Scene scene = new Scene(ui.createInterface(), 400, 600);
        stage.setTitle("Calculatrice DEEVEN");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
