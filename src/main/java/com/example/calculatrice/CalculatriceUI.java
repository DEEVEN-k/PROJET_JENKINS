package com.example.calculatrice;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

import java.util.ArrayList;
import java.util.List;

public class CalculatriceUI {
    private final TextField affichage = new TextField();
    private final StringBuilder saisie = new StringBuilder();
    private final ListView<String> historiqueList = new ListView<>();
    private final List<String> historique = new ArrayList<>();
    private boolean themeSombre = false;
    private double memoire = 0.0;

    public BorderPane createInterface(Scene scene) {
        VBox racine = new VBox(10);
        racine.setPadding(new Insets(10));

        affichage.setEditable(false);
        affichage.setStyle("-fx-font-size: 24px;");
        affichage.setFocusTraversable(false);
        affichage.setMaxHeight(50);

        HBox menu = new HBox(5);
        Button themeBtn = new Button("Thème");
        Button memPlusBtn = new Button("M+");
        Button memRBtn = new Button("MR");
        Button memClearBtn = new Button("MC");
        menu.getChildren().addAll(themeBtn, memPlusBtn, memRBtn, memClearBtn);

        GridPane clavier = creerClavier();
        VBox.setVgrow(clavier, Priority.ALWAYS);

        historiqueList.setPrefHeight(100);
        historiqueList.setPlaceholder(new Label("Historique vide"));

        racine.getChildren().addAll(menu, affichage, clavier, new Label("Historique :"), historiqueList);

        appliquerTheme(scene);

        // Clavier physique
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ENTER) evaluer();
            else if (e.getCode() == KeyCode.BACK_SPACE) supprimerDernierCaractere();
            else {
                String texte = e.getText();
                if ("0123456789.+-*/()%".contains(texte)) ajouterInput(texte);
            }
        });

        // Mémoire
        memPlusBtn.setOnAction(e -> {
            try {
                memoire = Double.parseDouble(affichage.getText());
            } catch (Exception ignored) {}
        });
        memRBtn.setOnAction(e -> {
            saisie.append(memoire);
            affichage.setText(saisie.toString());
        });
        memClearBtn.setOnAction(e -> memoire = 0.0);

        // Thème
        themeBtn.setOnAction(e -> {
            themeSombre = !themeSombre;
            appliquerTheme(scene);
        });

        BorderPane layout = new BorderPane();
        layout.setCenter(racine);
        return layout;
    }

    private void appliquerTheme(Scene scene) {
        scene.getStylesheets().clear();
        if (themeSombre) {
            scene.getRoot().setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: #222;");
            affichage.setStyle("-fx-font-size: 24px; -fx-text-fill: #222; -fx-background-color: #3c3f41;");
        } else {
            scene.getRoot().setStyle("-fx-background-color: white;");
            affichage.setStyle("-fx-font-size: 24px;");
        }
    }

    private GridPane creerClavier() {
        GridPane clavier = new GridPane();
        clavier.setHgap(5);
        clavier.setVgap(5);
        clavier.setPadding(new Insets(10));

        String[][] touches = {
                {"sin", "cos", "tan", "√"},
                {"ln", "log", "^", "%"},
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {"0", ".", "(", ")"},
                {"C", "⌫", "=", "+"}
        };

        for (int i = 0; i < touches.length; i++) {
            for (int j = 0; j < touches[i].length; j++) {
                String valeur = touches[i][j];
                Button bouton = new Button(valeur);
                bouton.setMinSize(60, 60);
                bouton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                bouton.setStyle("-fx-font-size: 16px;");
                bouton.setOnAction(e -> handleInput(valeur));
                GridPane.setHgrow(bouton, Priority.ALWAYS);
                GridPane.setVgrow(bouton, Priority.ALWAYS);
                clavier.add(bouton, j, i);
            }
        }
        return clavier;
    }

    private void handleInput(String input) {
        switch (input) {
            case "C" -> {
                saisie.setLength(0);
                affichage.clear();
            }
            case "⌫" -> supprimerDernierCaractere();
            case "=" -> evaluer();
            case "√" -> saisie.append("sqrt(");
            case "sin", "cos", "tan", "log", "ln" -> saisie.append(input).append("(");
            case "^" -> saisie.append("^");
            default -> saisie.append(input);
        }
        affichage.setText(saisie.toString());
    }

    private void ajouterInput(String input) {
        saisie.append(input);
        affichage.setText(saisie.toString());
    }

    private void supprimerDernierCaractere() {
        int len = saisie.length();
        if (len > 0) {
            saisie.deleteCharAt(len - 1);
            affichage.setText(saisie.toString());
        }
    }

    private void evaluer() {
        try {
            String expTexte = saisie.toString().replace("%", "mod");

            Function mod = new Function("mod", 2) {
                @Override
                public double apply(double... args) {
                    return args[0] % args[1];
                }
            };

            Expression exp = new ExpressionBuilder(expTexte)
                    .functions(mod)
                    .build();

            double resultat = exp.evaluate();
            affichage.setText(String.valueOf(resultat));
            historique.add(saisie + " = " + resultat);
            historiqueList.getItems().setAll(historique);
            saisie.setLength(0);
            saisie.append(resultat);
        } catch (Exception e) {
            affichage.setText("Erreur");
            saisie.setLength(0);
        }
    }
}
