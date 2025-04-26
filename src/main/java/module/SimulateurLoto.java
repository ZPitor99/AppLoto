package module;

// Classe de simulation avec la console

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulateurLoto {
    private final List<GrilleLoto> grilles;
    private final List<Integer> numerosTires;
    private final int valeurMax;

    /**
     * Constructeur pour le simulateur de loto
     *
     * @param valeurMax La valeur maximale des numéros possibles
     */
    public SimulateurLoto(int valeurMax) {
        this.grilles = new ArrayList<>();
        this.numerosTires = new ArrayList<>();
        this.valeurMax = valeurMax;
    }

    /**
     * Ajoute une nouvelle grille au simulateur
     *
     * @param nbLignes   Nombre de lignes de la grille
     * @param nbColonnes Nombre de colonnes de la grille
     * @param nomGrille  Nom de la grille
     */
    public void creerGrille(int nbLignes, int nbColonnes, String nomGrille) {
        GrilleLoto grille = new GrilleLoto(nbLignes, nbColonnes, nomGrille);
        grilles.add(grille);
    }

    /**
     * Ajoute une nouvelle grille au simulateur (grille de loto)
     *
     * @param nomGrille Nom de la grille
     */
    public void creerGrille(String nomGrille) {
        GrilleLoto grille = new GrilleLoto(nomGrille, true);
        grilles.add(grille);
    }
/*
    public static void main(String[] args) {
        // Exemple d'utilisation
        SimulateurLoto simulateur = new SimulateurLoto(90); // Loto avec des numéros de 1 à 90

        // Créer quelques grilles
        simulateur.creerGrille("Grille 1");
        simulateur.creerGrille("Grille 2");
        int[][] carton1 = {
                {22, 40, 55, 79, 84},
                {7, 14, 48, 53, 72},
                {5, 25, 30, 57, 69}
        };
        simulateur.creerCarton(carton1, "Carton 1");


        System.out.println("État initial des grilles:");
        simulateur.afficherToutesLesGrilles();

        // Simuler quelques tirages
        System.out.println("\nDébut des tirages:");
        for (int i = 0; i < 70; i++) {
            int numero = simulateur.tirerNumero();
            if (numero == - 1) {
                System.out.println("Tous les numéros ont été tirés.");
                break;
            }

            // Vérifier après chaque tirage
            if (simulateur.verifierLignesCompletes()) {
                System.out.println("Nous avons un gagnant! (ligne)");
                break;
            }

            if (simulateur.verifierGrillesCompletes()) {
                System.out.println("Nous avons un gagnant! (grille)");
                break;
            }
        }

        System.out.println("\nÉtat final des grilles:");
        simulateur.afficherToutesLesGrilles();
    }

 */

    public void creerCarton(int[][] maGrille, String nomGrille) {
        boolean formatValide = maGrille != null && maGrille.length == 3;
        if (formatValide) {
            for (int i = 0; i < 3; i++) {
                if (maGrille[i] == null || maGrille[i].length != 5) {
                    formatValide = false;
                    break;
                }
            }
        }
        if (! formatValide) {
            System.out.println("Erreur de grille");
        }
        GrilleLoto grille = new GrilleLoto(maGrille, nomGrille);
        grilles.add(grille);
    }

    /**
     * Marque un numéro sur toutes les grilles
     *
     * @param numero Le numéro à marquer
     */
    public void marquerNumeroSurToutesLesGrilles(int numero) {
        for (GrilleLoto grille : grilles) {
            if (grille.marquerNumero(numero)) {
                System.out.println("Le numéro " + numero + " a été marqué sur " + grille.getNom());
            }
        }
    }

    /**
     * Vérifie si une des grilles a une ligne complète
     */
    public boolean verifierLignesCompletes() {
        for (int i = 0; i < grilles.size(); i++) {
            GrilleLoto grille = grilles.get(i);
            for (int ligne = 0; ligne < grille.getNbLignes(); ligne++) {
                if (grille.estLigneComplete(ligne)) {
                    System.out.println("La grille " + (i + 1) + " a une ligne complète (ligne " + (ligne + 1) + ")");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si une des grilles est complète
     *
     * @return true si au moins une grille est complète
     */
    public boolean verifierGrillesCompletes() {
        for (int i = 0; i < grilles.size(); i++) {
            if (grilles.get(i).estGrilleComplete()) {
                System.out.println("La grille " + (i + 1) + " est complète!");
                return true;
            }
        }
        return false;
    }

    /**
     * Réinitialise le jeu en effaçant les numéros tirés et les marques des grilles
     */
    public void reinitialiser() {
        numerosTires.clear();
        for (GrilleLoto grille : grilles) {
            grille.reinitialiserMarques();
        }
        System.out.println("Le jeu a été réinitialisé");
    }

    /**
     * Affiche l'état actuel de toutes les grilles
     */
    public void afficherToutesLesGrilles() {
        for (int i = 0; i < grilles.size(); i++) {
            System.out.println("Grille " + (i + 1) + ":");
            grilles.get(i).afficherGrille();
            System.out.println();
        }
    }

    /**
     * Retourne la liste des numéros déjà tirés
     */
    public List<Integer> getNumerosTires() {
        return new ArrayList<>(numerosTires);
    }

    /**
     * Récupère le nombre de grilles actuellement en jeu
     */
    public int getNombreGrilles() {
        return grilles.size();
    }

    /**
     * Tire un nouveau numéro aléatoire
     *
     * @return Le numéro tiré ou -1 si tous les numéros ont été tirés
     */
    public int tirerNumero() {
        if (numerosTires.size() >= valeurMax) {
            return - 1; // Tous les numéros ont été tirés
        }

        Random rand = new Random();
        int numero;
        do {
            numero = rand.nextInt(valeurMax) + 1;
        } while (numerosTires.contains(numero));

        numerosTires.add(numero);
        System.out.println("Numéro tiré: " + numero);

        // Marquer ce numéro sur toutes les grilles
        marquerNumeroSurToutesLesGrilles(numero);

        return numero;
    }
}

