package com.example.calculatrice;


import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class CalculatriceUI {
    private TextField display = new TextField();

    public VBox createInterface() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1e1e1e;");

        display.setFont(Font.font(24));
        display.setStyle("-fx-control-inner-background: #333; -fx-text-fill: white;");
        display.setEditable(false);
        root.getChildren().add(display);

        String[][] buttons = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {"0", ".", "=", "+"},
                {"C", "(", ")", "^"}
        };

        for (String[] row : buttons) {
            HBox hBox = new HBox(10);
            for (String label : row) {
                Button btn = new Button(label);
                btn.setFont(Font.font(18));
                btn.setPrefSize(70, 50);
                btn.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: white;");
                btn.setOnAction(e -> handleInput(label));
                hBox.getChildren().add(btn);
            }
            root.getChildren().add(hBox);
        }

        return root;
    }

    private void handleInput(String value) {
        // Ajoute ici les opérations, le parsing ou l'évaluation
        if (value.equals("C")) {
            display.clear();
        } else if (value.equals("=")) {
            try {
                double result = eval(display.getText());
                display.setText(String.valueOf(result));
            } catch (Exception e) {
                display.setText("Erreur");
            }
        } else {
            display.appendText(value);
        }
    }

    private double eval(String expression) {
        // Une bibliothèque comme exp4j est recommandée ici
        return new net.objecthunter.exp4j.ExpressionBuilder(expression).build().evaluate();
    }
}
