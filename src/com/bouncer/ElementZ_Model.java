//--------------------------------------------------------------------------
// Projet : ElementZ Lite
// Version Beta
//--------------------------------------------------------------------------
// Cours : IHM
// 2009
//--------------------------------------------------------------------------
// On appellera dans ce qui suit famille à n boules un groupe de n boules
// voisines ayant la même couleur
// Famille en ligne : famille qui se trouve sur la même ligne
// Famille en colonne : famille qui se trouve sur la même colonne
//--------------------------------------------------------------------------
// But du jeu :
// Il faut permuter deux boules voisines de telle sorte à former
// des familles en lignes et/ou en colonne à 3, 4 ou 5 boules.
//--------------------------------------------------------------------------

package com.bouncer;
import fr.pierrezemb.isen.elementz.ElementZ_Model_Listener;
import fr.pierrezemb.isen.elementz.ModelChangedEvent;

import javax.swing.event.EventListenerList;
import java.util.Random;
/**
 * @author Ahcene Bounceur
 */

public class ElementZ_Model {

    //--------------------------------------------------------------------------
    // Ajout par Pierre Zemb

    // Adding a listener
    //--------------------------------------------------------------------------

    private EventListenerList listeners;
    //--------------------------------------------------------------------------
    // Les variables
    //--------------------------------------------------------------------------
    private int[][] matrix = new int[8][8];             // Matrice du jeu
    private boolean[][] matrixb = new boolean[8][8];    // Matrice des familles
    private int score;                                  // Le score

    //--------------------------------------------------------------------------
    // Les constructeurs
    //--------------------------------------------------------------------------
    public ElementZ_Model() {
        listeners = new EventListenerList();

        merge();        // Mélanger les boules
        prepar();       // Préparer le plateau sans qu'il y ait de familles
        // en ligne ou en colonne
        score = 0;      // Initialiser le score
    }

    //--------------------------------------------------------------------------
    // Les accesseurs
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Le score
    //--------------------------------------------------------------------------
    public int getScore() {
        return score;
    }

    //--------------------------------------------------------------------------
    // Affecter une couleur à la boule de la ligne x et de la colonne y
    // Valeurs de value : de 1 à 6
    //--------------------------------------------------------------------------
    public void setXY(int x, int y, int value) {
        matrix[x][y] = value;
    }

    //--------------------------------------------------------------------------
    // Récuperer la couleur de la boule de la ligne x et de la colonne y
    // Cette valeur se situe entre 1 et 6
    //--------------------------------------------------------------------------
    public int getXY(int x, int y) {
        return matrix[x][y];
    }

    //--------------------------------------------------------------------------
    // Les méthodes
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Initialiser la matrice des familles
    // Au debut aucune famille n'est formée
    //--------------------------------------------------------------------------
    private void initMatrixb() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                matrixb[i][j] = false;
            }
        }
    }

    //--------------------------------------------------------------------------
    // Trouver toutes les familles en lignes à n boules
    // Ceci met a jour la matrice des familles matrixb de la maniere suivante :
    // On met la valeur VRAI dans la matrice matrixb dans les cases
    // correspondant aux boules faisant partie d'une famille à n boules
    //--------------------------------------------------------------------------
    public void detectLine(int n) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < (8 - n + 1); j++) {
                boolean lineOk = true;
                int first = matrix[i][j];
                int k = j;
                while (k < j + n && lineOk) {
                    if (matrix[i][k] != first && matrix[i][k] != 0) lineOk = false;
                    k++;
                }
                if (lineOk) {
                    for (k = j; k < (j + n); k++) matrixb[i][k] = true;
                    j += n + 1;
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    // Même chose que la méthode detectLine() : mais pour les colonnes
    //--------------------------------------------------------------------------
    public void detectCol(int n) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < (8 - n + 1); j++) {
                boolean lineOk = true;
                int first = matrix[j][i];
                int k = j;
                while (k < j + n && lineOk) {
                    if (matrix[k][i] != first && matrix[k][i] != 0) lineOk = false;
                    k++;
                }
                if (lineOk) {
                    for (k = j; k < (j + n); k++) matrixb[k][i] = true;
                    j += n + 1;
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    // Détecte toutes les familles en ligne à 3, 4 et 5 boules
    //--------------------------------------------------------------------------
    public void detectLines() {
        for (int i = 5; i > 2; i--) {
            detectLine(i);
        }
    }

    //--------------------------------------------------------------------------
    // Détecte toutes les familles en colonne à 3, 4 et 5 boules
    //--------------------------------------------------------------------------
    public void detectCols() {
        for (int i = 5; i > 2; i--) {
            detectCol(i);
        }
    }

    //--------------------------------------------------------------------------
    // Détecte toutes les familles en ligne et en colonne à 3, 4 et 5 boules
    //--------------------------------------------------------------------------
    public void detectLinesAndCols() {
        for (int i = 5; i > 2; i--) {
            detectLine(i);
            detectCol(i);
        }
    }

    //--------------------------------------------------------------------------
    // Supprime toutes les familles à 3, 4 ou 5 boules
    // Mettre 0 dans les cases correspondantes de la matrice du jeu matrix
    //--------------------------------------------------------------------------
    public void deleteTheSame() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (matrixb[i][j]) matrix[i][j] = 0;
            }
        }
        initMatrixb();
    }

    //--------------------------------------------------------------------------
    // Occuper les vides apres avoir supprimer les boules
    // Les boules d'en haut descendent en bas pour occuper les vides
    // Tous les vides se trouveront en haut
    // C'est à dire, tous les zéros de la matrice du jeu matrix se trouveront
    // dans la partie supérieure de cette matrice
    //--------------------------------------------------------------------------
    public void gravity() {
        for (int j = 0; j < 8; j++) {
            int k = 7;
            for (int i = 7; i >= 0; i--) {
                if (matrix[i][j] != 0) {
                    matrix[k][j] = matrix[i][j];
                    k--;
                }
            }
            for (int i = k; i >= 0; i--) {
                matrix[i][j] = 0;
            }
        }
    }

    //--------------------------------------------------------------------------
    // Remplacer les vides par de nouvelles boules
    // C'est a dire, remplacer les zéros de la matrice matrix par des boules
    // (numeros de 1 à 6)
    //--------------------------------------------------------------------------
    public void fillGaps() {
        Random x = new Random();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (matrix[i][j] == 0) matrix[i][j] = x.nextInt(6) + 1;
            }
        }
    }

    //--------------------------------------------------------------------------
    // Apres avoir remplacer les vides, vérifier s'il y a des familles
    // à 3, 4 ou 5 boules
    // S'il y a des familles qui se créent, il faut les supprimer à nouveau
    // avant de laisse la main au joueur
    // Remarque : les familles créées automatiquement seront contabilisées
    // dans le score
    //--------------------------------------------------------------------------
    public boolean notReadyToPlay() {
        detectLinesAndCols();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (matrixb[i][j]) return true;
        return false;
    }

    //--------------------------------------------------------------------------
    // Après une permutation du joueur :
    // 1. on vérifie toutes les familles créées
    // 2. si au moins une famille est créée (notReadyToPlay = VRAI)
    // 3. on met à jour le score par rapport aux familles créées
    // 4. supprimer toutes ses familles
    // 5. faire descendre les boules pour occuper les vides
    // 6. remplacer les vides par de nouvelles boules
    // 7. revenir au point 2
    // 8. sinon on redonne la main au joueur pour faire une autre permutation
    //--------------------------------------------------------------------------
    public void prepar() {
        while (notReadyToPlay()) {
            score += somme();
            deleteTheSame();
            gravity();
            fillGaps();
        }
    }

    //--------------------------------------------------------------------------
    // Permuter deux boules voisine (verticalement ou horizontalement)
    //--------------------------------------------------------------------------
    public void permut(int i, int j, int ip, int jp) {
        if (((i == ip) && (j == (jp + 1)))
                ||
                ((i == ip) && (j == (jp - 1)))
                ||
                ((j == jp) && (i == (ip + 1)))
                ||
                ((j == jp) && (i == (ip - 1)))
                ) {
            int s = matrix[i][j];
            matrix[i][j] = matrix[ip][jp];
            matrix[ip][jp] = s;
        }
        fireModelChanged();
    }

    //--------------------------------------------------------------------------
    // Jouer :
    // Après une permutation, on vérifie si une famille est créée ou non
    // Si oui, on la supprime du jeu, on remplace le vide par de nouvelles
    // boules et voir si à nouveau d'autres familles sont créées sinon
    // on laisse la main au joueur
    // Si la permutation n'engendre pas de famille, on ne fait rien
    //--------------------------------------------------------------------------
    public void play(int x, int y, int xp, int yp) {
        permut(x, y, xp, yp);
        detectLinesAndCols();
        if (matrixb[x][y] || matrixb[xp][yp])
            prepar();
        else
            permut(x, y, xp, yp);
    }


    //--------------------------------------------------------------------------
    // Créer une matrice de jeu (matrix) avec des boules aléatoires
    //--------------------------------------------------------------------------
    public void merge() {
        Random x = new Random();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                matrix[i][j] = x.nextInt(6) + 1;
            }
        }
    }

    //--------------------------------------------------------------------------
    // Le score est égale au nombre de boules créées par le nombre de familles
    //--------------------------------------------------------------------------
    public int somme() {
        int s = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                s += (matrixb[i][j] ? 1 : 0);
            }
        }
        return s;
    }

    //--------------------------------------------------------------------------
    // Voir l'état des matrices (de jeu et de famille)
    //--------------------------------------------------------------------------
    public String toString() {
        String s = "";
        String s1;
        String s2;
        for (int i = 0; i < 8; i++) {
            s1 = "";
            s2 = "";
            for (int j = 0; j < 8; j++) {
                s1 += matrix[i][j] + " ";
                s2 += (matrixb[i][j] ? 1 : 0) + " ";
            }
            s += s1 + "            " + s2 + '\n';
        }
        return s;
    }

//--------------------------------------------------------------------------
// Copyright (c) 2009 - Ahcene Bounceur
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
// New methods by Pierre Zemb
//--------------------------------------------------------------------------

    private void fireModelChanged() {
        ElementZ_Model_Listener[] listenerList = (ElementZ_Model_Listener[]) listeners.getListeners(ElementZ_Model_Listener.class);
        for (ElementZ_Model_Listener listener : listenerList) {

            listener.modelChanged(new ModelChangedEvent(this));
        }
    }

    public void addModelListener(ElementZ_Model_Listener listener) {
        listeners.add(ElementZ_Model_Listener.class, listener);
    }

    public void addScore() {
        this.score += somme();
    }


    public void newGame() {
        merge();        // Mélanger les boules
        prepar();       // Préparer le plateau sans qu'il y ait de familles
        // en ligne ou en colonne
        score = 0;      // Initialiser le score
        fireModelChanged();
    }
}