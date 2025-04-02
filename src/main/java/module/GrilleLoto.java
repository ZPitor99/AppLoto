package module;

// Classe qui implémente la grille et son marquage

import java.util.*;

public class GrilleLoto {
    private String nom;
    private int[][] grille;        // Tableau 2D pour représenter la grille
    private boolean[][] marques;   // Tableau 2D pour suivre les numéros cochés
    private final int nbLignes;
    private final int nbColonnes;

    /**
     * Constructeur pour une grille de loto avec dimensions spécifiées
     *
     * @param nbLignes   Nombre de lignes de la grille
     * @param nbColonnes Nombre de colonnes de la grille
     * @param nomGrille  Nom de la grille
     */
    public GrilleLoto(int nbLignes, int nbColonnes, String nomGrille) {
        this.nom = nomGrille;
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.grille = new int[nbLignes][nbColonnes];
        this.marques = new boolean[nbLignes][nbColonnes];

        initialiserGrille();
    }

    /**
     * Constructeur pour une grille de loto avec format spécifique (3 lignes, 9 colonnes)
     * Format spécifique correspond à un carton de loto
     *
     * @param nomGrille        Nom de la grille
     * @param formatSpecifique Si true, utilise le format spécifique (3 lignes x 5 nombres)
     */
    public GrilleLoto(String nomGrille, boolean formatSpecifique) {
        this.nom = nomGrille;
        this.nbLignes = 3;
        this.nbColonnes = 9;
        this.grille = new int[nbLignes][nbColonnes];
        this.marques = new boolean[nbLignes][nbColonnes];

        if (formatSpecifique) {
            initialiserCarton();
        } else {
            initialiserGrille();
        }
    }

    public GrilleLoto(int[][] nums, String nomGrille) {
        this.nom = nomGrille;
        this.nbLignes = 3;
        this.nbColonnes = 9;

        this.grille = new int[nbLignes][nbColonnes];
        this.marques = new boolean[nbLignes][nbColonnes];
        initialiserCarton();
    }

    /**
     * Initialise la grille avec des nombres aléatoires
     * Pour un loto standard
     */
    private void initialiserGrille() {

        // Pour chaque colonne, définir la plage de nombres possible
        for (int col = 0; col < nbColonnes; col++) {
            int min = col * 10 + 1;                // Ex : première colonne : 1-10
            int max = Math.min(90, (col + 1) * 10); // Ex : dernière colonne : jusqu'à 90

            // Générer des nombres uniques pour chaque case dans cette colonne
            List<Integer> nombresPossibles = new ArrayList<>();
            for (int n = min; n <= max; n++) {
                nombresPossibles.add(n);
            }
            Collections.shuffle(nombresPossibles);

            // Remplir la colonne avec des nombres aléatoires
            for (int ligne = 0; ligne < nbLignes; ligne++) {
                if (ligne < nombresPossibles.size()) {
                    grille[ligne][col] = nombresPossibles.get(ligne);
                }
            }
        }
    }

    /**
     * Initialise la grille avec des nombres aléatoires
     * Pour un carton de loto
     * - Ligne de cinq nombres
     * - Colonne avec au moins un nombre
     */
    private void initialiserCarton() {
        // Réinitialiser la grille
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                grille[i][j] = 0;
                marques[i][j] = false;
            }
        }

        Random random = new Random();

        // Créer les plages de nombres pour chaque colonne
        List<List<Integer>> colonnes = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            List<Integer> colonne = new ArrayList<>();
            int min;
            if (i == 0) min = 1;
            else min = i * 10;
            int max;
            if (i == 8) max = 90;
            else max = ((i + 1) * 10) - 1;

            for (int num = min; num <= max; num++) {
                colonne.add(num);
            }
            Collections.shuffle(colonne); // Mélanger les nombres de chaque colonne
            colonnes.add(colonne);
        }

        // Première étape : s'assurer qu'aucune colonne n'est vide
        // en distribuant au moins un nombre par colonne
        for (int col = 0; col < nbColonnes; col++) {
            // Choisir une ligne aléatoire pour cette colonne
            int ligne = random.nextInt(nbLignes);
            if (colonnes.get(col).size() > 0) {
                grille[ligne][col] = colonnes.get(col).remove(0);
            }
        }

        // Deuxième étape : compléter chaque ligne pour avoir exactement 5 nombres
        for (int ligne = 0; ligne < nbLignes; ligne++) {
            // Compter combien de nombres sont déjà sur cette ligne
            int nombresDansLigne = 0;
            for (int col = 0; col < nbColonnes; col++) {
                if (grille[ligne][col] > 0) {
                    nombresDansLigne++;
                }
            }

            // Compléter jusqu'à 5 nombres par ligne
            int nombresAjouter = 5 - nombresDansLigne;
            if (nombresAjouter > 0) {
                // Collecter les colonnes disponibles (vides) sur cette ligne
                List<Integer> colonnesDisponibles = new ArrayList<>();
                for (int col = 0; col < nbColonnes; col++) {
                    if (grille[ligne][col] == 0 && colonnes.get(col).size() > 0) {
                        colonnesDisponibles.add(col);
                    }
                }

                // Mélanger pour une sélection aléatoire
                Collections.shuffle(colonnesDisponibles);

                // Ajouter des nombres aux colonnes disponibles
                for (int i = 0; i < nombresAjouter && i < colonnesDisponibles.size(); i++) {
                    int col = colonnesDisponibles.get(i);
                    if (colonnes.get(col).size() > 0) {
                        grille[ligne][col] = colonnes.get(col).remove(0);
                    }
                }
            }
            // Si trop de nombres, en retirer
            else if (nombresDansLigne > 5) {
                List<Integer> colonnesAvecNombres = new ArrayList<>();
                for (int col = 0; col < nbColonnes; col++) {
                    if (grille[ligne][col] > 0) {
                        colonnesAvecNombres.add(col);
                    }
                }

                Collections.shuffle(colonnesAvecNombres);

                for (int i = 0; i < nombresDansLigne - 5; i++) {
                    int col = colonnesAvecNombres.get(i);
                    // Remettre le nombre dans la liste des disponibles
                    colonnes.get(col).add(grille[ligne][col]);
                    grille[ligne][col] = 0;
                }
            }
        }
        verifierCarton(random);
    }

    private void verifierCarton(Random random) {
        // Vérification finale et correction
        // S'assurer qu'aucune colonne n'est vide
        for (int col = 0; col < nbColonnes; col++) {
            boolean colonneVide = true;
            for (int ligne = 0; ligne < nbLignes; ligne++) {
                if (grille[ligne][col] > 0) {
                    colonneVide = false;
                    break;
                }
            }

            if (colonneVide) {
                // Trouver une ligne avec le moins de modifications nécessaires
                for (int ligne = 0; ligne < nbLignes; ligne++) {
                    int nombresDansLigne = 0;
                    for (int c = 0; c < nbColonnes; c++) {
                        if (grille[ligne][c] > 0) {
                            nombresDansLigne++;
                        }
                    }

                    if (nombresDansLigne == 5) {
                        // Cette ligne a déjà 5 nombres, on va remplacer un nombre
                        List<Integer> colonnesAvecNombres = new ArrayList<>();
                        for (int c = 0; c < nbColonnes; c++) {
                            if (grille[ligne][c] > 0 && c != col) {
                                colonnesAvecNombres.add(c);
                            }
                        }

                        if (!colonnesAvecNombres.isEmpty()) {
                            int colARemplacer = colonnesAvecNombres.get(random.nextInt(colonnesAvecNombres.size()));

                            // Générer un nouveau nombre pour la colonne vide
                            int min;
                            if (col == 0) min = 1;
                            else min = col * 10;
                            int max;
                            if (col == 8) max = 90;
                            else max = ((col + 1) * 10) - 1;

                            int nouveauNombre = min + random.nextInt(max - min + 1);

                            // Remplacer un nombre existant par ce nouveau nombre
                            grille[ligne][col] = nouveauNombre;
                            grille[ligne][colARemplacer] = 0;

                            break; // Sortir de la boucle, la colonne n'est plus vide
                        }
                    }
                }
            }
        }

        // Vérifier une dernière fois que chaque ligne a exactement 5 nombres
        for (int ligne = 0; ligne < nbLignes; ligne++) {
            int count = 0;
            for (int col = 0; col < nbColonnes; col++) {
                if (grille[ligne][col] > 0) {
                    count++;
                }
            }

            if (count != 5) {
                // Ajuster si nécessaire (rarement requis à ce stade)
                if (count < 5) {
                    // Ajouter des nombres si possible
                    List<Integer> colonnesVides = new ArrayList<>();
                    for (int col = 0; col < nbColonnes; col++) {
                        if (grille[ligne][col] == 0) {
                            colonnesVides.add(col);
                        }
                    }

                    Collections.shuffle(colonnesVides);

                    for (int i = 0; i < 5 - count && i < colonnesVides.size(); i++) {
                        int col = colonnesVides.get(i);
                        int min;
                        if (col == 0) min = 1;
                        else min = col * 10;
                        int max;
                        if (col == 8) max = 90;
                        else max = ((col + 1) * 10) - 1;

                        grille[ligne][col] = min + random.nextInt(max - min + 1);
                    }
                } else {
                    // Retirer des nombres en excès
                    List<Integer> colonnesAvecNombres = new ArrayList<>();
                    for (int col = 0; col < nbColonnes; col++) {
                        if (grille[ligne][col] > 0) {
                            colonnesAvecNombres.add(col);
                        }
                    }

                    Collections.shuffle(colonnesAvecNombres);

                    for (int i = 0; i < count - 5; i++) {
                        int col = colonnesAvecNombres.get(i);
                        grille[ligne][col] = 0;
                    }
                }
            }
        }
    }

    /**
     * Marque un numéro sur la grille s'il existe
     *
     * @param numero Le numéro à marquer
     * @return true si le numéro a été trouvé et marqué, false sinon
     */
    public boolean marquerNumero(int numero) {
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                if (grille[i][j] == numero) {
                    marques[i][j] = true;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si une ligne complete est marquée
     *
     * @param ligne Le numéro de la ligne à vérifier
     * @return true si tous les numéros de la ligne sont marqués
     */
    public boolean estLigneComplete(int ligne) {
        if (ligne < 0 || ligne >= this.nbLignes) {
            return false;
        }
        int compteur = 0;
        for (int j = 0; j < this.nbColonnes; j++) {
            if (this.estMarque(ligne, j)) {
                compteur++;
            }
        }
        return compteur == 5;
    }

    /**
     * Vérifie si la grille entière est marquée
     *
     * @return true si tous les numéros de la grille sont marqués, false sinon
     */
    public boolean estGrilleComplete() {
        for (int i = 0; i < this.nbLignes; i++) {
            if (!this.estLigneComplete(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Réinitialise toutes les marques sur la grille
     */
    public void reinitialiserMarques() {
        for (int i = 0; i < this.nbLignes; i++) {
            for (int j = 0; j < this.nbColonnes; j++) {
                this.marques[i][j] = false;
            }
        }
    }

    /**
     * Récupère le numéro à une position donnée
     *
     * @param ligne   le numero de la ligne
     * @param colonne le numero de la colonne
     * @return le numéro à la position dans la grille, -1 sinon
     */
    public int getNumero(int ligne, int colonne) {
        if (ligne >= 0 && ligne < nbLignes && colonne >= 0 && colonne < nbColonnes) {
            return grille[ligne][colonne];
        }
        return -1; // Valeur invalide
    }

    /**
     * Vérifie si un numéro à une position donnée est marqué
     *
     * @param ligne   le numero de la ligne
     * @param colonne le numero de la colone
     * @return true si la case est marque, false sinon
     */
    public boolean estMarque(int ligne, int colonne) {
        if (ligne >= 0 && ligne < nbLignes && colonne >= 0 && colonne < nbColonnes) {
            return marques[ligne][colonne];
        }
        return false;
    }

    /**
     * Affiche la grille dans la console
     */
    public void afficherGrille() {
        System.out.print("+");
        for (int i = 0; i < 9; i++) {
            System.out.print("---+");
        }
        System.out.println();

        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 9; j++) {
                if (this.getNumero(i, j) == 0) {
                    System.out.print("   |");
                } else {
                    if (j == 0) {
                        if (this.estMarque(i, j)) {
                            System.out.print(this.getNumero(i, j) + "X |");
                        } else {
                            System.out.print(" " + this.getNumero(i, j) + " |");
                        }
                    } else {
                        if (this.estMarque(i, j)) {
                            System.out.print(this.getNumero(i, j) + "X|");
                        } else {
                            System.out.print(this.getNumero(i, j) + " |");
                        }
                    }
                }
            }
            System.out.println();
            System.out.print("+");
            for (int y = 0; y < 9; y++) {
                System.out.print("---+");
            }
            System.out.println();
        }
    }

    /**
     * Accesseur du nombre de ligne
     *
     * @return nbLignes le nombre de lignes de la grilleLoto
     */
    public int getNbLignes() {
        return nbLignes;
    }

    /**
     * Accesseur du nombre de colones
     *
     * @return nbColonnes le nombre de colones de la grilleLoto
     */
    public int getNbColonnes() {
        return nbColonnes;
    }

    /**
     * Accesseur du nom de la grille
     *
     * @return nom le nom de la grilleLoto
     */
    public String getNom() {
        return nom;
    }

    /**
     * Accesseur de la grilleLoto en elle-meme
     *
     * @return grille de la grilleLoto
     */
    public int[][] getGrille() {
        return grille;
    }

    /**
     * Accesseur des nombres marqués
     *
     * @return marques la grille des nombres marqués
     */
    public boolean[][] getMarques() {
        return marques;
    }
}
