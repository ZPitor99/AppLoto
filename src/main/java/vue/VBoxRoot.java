package vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VBoxRoot extends VBox {
    private final VBox grillesContainer;

    public VBoxRoot() {
        super();
        // Création d'un conteneur pour vos grilles
        grillesContainer = new VBox(10); // 10 pixels d'espacement entre les grilles
        grillesContainer.setPadding(new Insets(10));

        // Création du ScrollPane qui contiendra toutes les grilles
        ScrollPane scrollPane = new ScrollPane(grillesContainer);
        scrollPane.setFitToWidth(true);

        // Bouton pour ajouter une nouvelle grille
        Button ajouterGrilleBtn = new Button("Ajouter une grille");
        ajouterGrilleBtn.setId("ajouterGrille");
        ajouterGrilleBtn.setOnAction(e -> {
            // Ouvrir une nouvelle fenêtre pour créer une grille
            ajouterGrille();
        });

        // Ajouter les éléments au VBox principal
        this.getChildren().addAll(scrollPane, ajouterGrilleBtn);

        // Définir les marges et espacements
        this.setPadding(new Insets(15));
        this.setSpacing(10);
    }

    public void ajouterGrille() {
        // Créer une nouvelle fenêtre pour saisir les numéros
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL); // Bloque l'interaction avec la fenêtre principale
        popup.setTitle("Nouvelle grille de loto");

        VBox popupRoot = new VBox(15);
        popupRoot.setPadding(new Insets(20));
        popupRoot.setAlignment(Pos.CENTER);

        Label nomLabel = new Label("Saisissez le nom de votre grille");
        nomLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        popupRoot.getChildren().addAll(nomLabel);

        TextField nomGrille = new TextField();
        nomGrille.setAlignment(Pos.CENTER);
        nomGrille.setPrefWidth(200);
        nomGrille.setMaxWidth(200);
        nomGrille.setPromptText("Nom de la grille");
        popupRoot.getChildren().addAll(nomGrille);

        Label titreLabel = new Label("Saisissez les numéros de votre grille");
        titreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Grille de saisie avec 3 lignes de 5 champs
        GridPane gridSaisie = new GridPane();
        gridSaisie.setHgap(10);
        gridSaisie.setVgap(10);
        gridSaisie.setAlignment(Pos.CENTER);

        // Créer un tableau pour stocker tous les champs de texte
        TextField[][] champsNumeros = new TextField[3][5];

        // Remplir la grille avec des champs de texte
        for (int ligne = 0; ligne < 3; ligne++) {
            for (int col = 0; col < 5; col++) {
                TextField champNumero = new TextField();
                champNumero.setPrefWidth(50);
                champNumero.setMaxWidth(50);
                champNumero.setPromptText("N°");
                champNumero.setStyle("-fx-focus-color: #3c5a73;");

                // Limiter la saisie à 2 chiffres seulement
                champNumero.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.length() > 2) {
                        champNumero.setText(oldValue);
                    } else if (! newValue.isEmpty() && ! newValue.matches("\\d*")) {
                        champNumero.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                });

                gridSaisie.add(champNumero, col, ligne);
                champsNumeros[ligne][col] = champNumero;
            }
        }

        // Boutons pour valider ou annuler
        HBox boutons = new HBox(15);
        boutons.setAlignment(Pos.CENTER);

        Button btnAleatoire = new Button("Aleatoire");
        btnAleatoire.setStyle("-fx-focus-color: #3c5a73;");
        Button btnValider = new Button("Valider");
        btnValider.setStyle("-fx-focus-color: #3c5a73;");
        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.setStyle("-fx-focus-color: #3c5a73;");

        btnAnnuler.setOnAction(e -> popup.close());

        btnValider.setOnAction(e -> {
            // Vérifier que tous les champs sont remplis
            boolean champVide = false;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    if (champsNumeros[i][j].getText().trim().isEmpty()) {
                        champVide = true;
                        break;
                    }
                }
            }

            if (champVide) {
                // Afficher un message d'erreur
                Label erreur = new Label("Tous les champs doivent être remplis");
                erreur.setStyle("-fx-text-fill: red;");

                // Vérifier si le message d'erreur existe déjà
                if (! popupRoot.getChildren().stream().anyMatch(node -> node instanceof Label &&
                        ((Label) node).getText().equals("Tous les champs doivent être remplis"))) {
                    popupRoot.getChildren().add(erreur);
                }
            } else {
                // Créer une nouvelle grille et l'ajouter au ScrollPane
                ajouterGrilleAuScrollPane(champsNumeros);
                popup.close();
            }
        });

        btnAleatoire.setOnAction(e -> {
            //Aleatory
        });

        boutons.getChildren().addAll(btnValider, btnAnnuler, btnAleatoire);

        popupRoot.getChildren().addAll(titreLabel, gridSaisie, boutons);

        Scene popupScene = new Scene(popupRoot, 400, 300);
        popup.setScene(popupScene);
        popup.showAndWait();
    }

    private void ajouterGrilleAuScrollPane(TextField[][] champsNumeros) {
        // Créer un BorderPane pour contenir la grille
        BorderPane grillePane = new BorderPane();
        grillePane.getStyleClass().add("grille-loto");
        grillePane.setPadding(new Insets(10));

        // Créer un en-tête pour la grille
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Grille " + (grillesContainer.getChildren().size() + 1));
        titleLabel.getStyleClass().add("grille-title");

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setOnAction(e -> {
            grillesContainer.getChildren().remove(grillePane);
        });

        header.getChildren().addAll(titleLabel, supprimerBtn);
        grillePane.setTop(header);

        // Créer la grille avec les numéros saisis
        GridPane numeroGrid = new GridPane();
        numeroGrid.setHgap(10);
        numeroGrid.setVgap(10);
        numeroGrid.setPadding(new Insets(15));
        numeroGrid.setAlignment(Pos.CENTER);

        // Remplir la grille avec les numéros saisis
        for (int ligne = 0; ligne < 3; ligne++) {
            for (int col = 0; col < 5; col++) {
                String numero = champsNumeros[ligne][col].getText();
                Label numeroLabel = new Label(numero);
                numeroLabel.setStyle("-fx-padding: 10px; -fx-background-color: #f2f2f2; " +
                        "-fx-border-radius: 5px; -fx-background-radius: 5px; " +
                        "-fx-min-width: 40px; -fx-min-height: 40px; " +
                        "-fx-alignment: center; -fx-font-weight: bold;");

                numeroGrid.add(numeroLabel, col, ligne);
            }
        }

        grillePane.setCenter(numeroGrid);

        // Ajouter la grille au conteneur
        grillesContainer.getChildren().add(grillePane);
    }
}