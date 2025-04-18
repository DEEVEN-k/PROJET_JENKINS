package com.example.calculatrice;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CalculatriceApp extends Application {
    @Override
    public void start(Stage stage) {
        CalculatriceUI ui = new CalculatriceUI();

        Scene scene = new Scene(new BorderPane(), 400, 600);
        BorderPane root = ui.createInterface(scene);
        scene.setRoot(root);

        stage.setTitle("Calculatrice DEEVEN");
        stage.setScene(scene);
        stage.setMinWidth(300);
        stage.setMinHeight(500);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
