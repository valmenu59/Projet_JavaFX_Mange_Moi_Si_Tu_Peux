package jeu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

public class Plateau {

    private int colonnes;
    private int lignes;

    protected Case[][] cases;

    ArrayList<int[]> casesPlante = new ArrayList<>();
    ArrayList<int[]> listeCasePassees = new ArrayList<>();
    Stack<int[]> chemin = new Stack<>();


    public Plateau(int c, int l){
        this.lignes = c;
        this.colonnes = l;
    }

    /*
    public void creerPlateau(){
        //Permet d'initialiser le plateau
        this.cases = new Case[this.lignes][this.colonnes];
        for (int i = 0; i < this.lignes; i++){
            for (int j = 0; j < this.colonnes; j++){
                if (i == 0 || i == this.lignes - 1 || j == 0 || j == this.colonnes - 1) {
                    this.cases[i][j] = new Case(new Roche(), null);
                } else {
                    this.cases[i][j] = new Case(new Herbe(), null);
                }
            }
        }
    }
*/

    public void creerPlateau() {
        this.cases = new Case[lignes][colonnes];

        // Initialisation du labyrinthe avec des roches en bordure et de l'herbe à l'intérieur
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                if (i == 0 || i == lignes - 1 || j == 0 || j == colonnes - 1) {
                    this.cases[i][j] = new Case(new Roche(), null);
                } else if ((i % 2 == 1 && j % 2 == 1) || (i == lignes - 2 && j == colonnes - 2)){
                    this.cases[i][j] = new Case(new Herbe(), null);
                } else {
                    this.cases[i][j] = new Case(new Roche(), null);
                }
            }
        }

        printMatrices();

        ArrayList<Integer> listeColonnes = new ArrayList<>();
        for (int i = 1; i < lignes - 1; i++) {
            for (int j = 1; j < colonnes -1; j++) {
                System.out.println(i+""+j+""+this.cases[i][j].getContenu().getNom());
                double random = Math.random();
                if (random <= 0.5){
                    if (j != colonnes - 2) {
                        listeColonnes.add(j);
                        supprimerObstacle(i, j, 'E');
                    }
                } else {
                    if (i != lignes - 2) {
                        listeColonnes.add(j);
                        Collections.shuffle(listeColonnes);
                        supprimerObstacle(i, listeColonnes.get(0), 'S');
                        listeColonnes.clear();
                    }
                }
            }
        }


        System.out.println(verifPlateauCorrect(0,1));
        System.out.println(casesPlante.toString());
        System.out.println();
    }



    private void supprimerObstacle(int i, int j, char orientation){
        System.out.println("j'ai remplacé l'herbe par roche"+" "+i+" "+j);
        switch (orientation){
            case 'N' -> this.cases[i-1][j].setContenuGeneral(new Herbe());
            case 'E' -> this.cases[i][j+1].setContenuGeneral(new Herbe());
            case 'S' -> this.cases[i+1][j].setContenuGeneral(new Herbe());
            case 'O' -> this.cases[i][j-1].setContenuGeneral(new Herbe());
        }
    }


    /*
    public boolean verifPlateauValide(int iSortie, int jSortie){
        Stack<int[]> pileCase = new Stack<>();
        int[] tableau = new int[]{1,1};
        listeCasePassees.add(tableau);
        pileCase.push(tableau);
        while (!pileCase.isEmpty()) {
            int[] caseCourante = pileCase.peek();
            int iCourant = caseCourante[0];
            int jCourant = caseCourante[1];
            System.out.println("je suis ici avec la position i et j "+iCourant+" "+jCourant+" "+pileCase.size());

            // Vérification si la position courante est la position de sortie
            if (iCourant == iSortie && jCourant == jSortie) {
                return true;
            }

            // Vérification des cases voisines non visitées
            boolean deplacementPossible = false;
            if (jCourant > 0 && !presentDansLaListe(new int[]{iCourant, jCourant - 1})) {
                if (this.cases[iCourant][jCourant - 1].getContenu().getNom().equals("Herbe")) {
                    int[] nouvelleCase = new int[]{iCourant, jCourant - 1};
                    pileCase.push(nouvelleCase);
                    listeCasePassees.add(nouvelleCase);
                    deplacementPossible = true;
                }
            }
            if (jCourant < colonnes - 1 && !presentDansLaListe(new int[]{iCourant, jCourant + 1})){
                if (this.cases[iCourant][jCourant + 1].getContenu().getNom().equals("Herbe")) {
                    int[] nouvelleCase = new int[]{iCourant, jCourant + 1};
                    pileCase.push(nouvelleCase);
                    listeCasePassees.add(nouvelleCase);
                    deplacementPossible = true;
                }
            }
            if (iCourant > 0 && !presentDansLaListe(new int[]{iCourant - 1, jCourant})) {
                if (this.cases[iCourant - 1][jCourant].getContenu().getNom().equals("Herbe")) {
                    int[] nouvelleCase = new int[]{iCourant - 1, jCourant};
                    pileCase.push(nouvelleCase);
                    listeCasePassees.add(nouvelleCase);
                    deplacementPossible = true;
                }
            }
            if (iCourant < lignes - 1 && !presentDansLaListe(new int[]{iCourant + 1, jCourant})) {
                if (this.cases[iCourant + 1][jCourant].getContenu().getNom().equals("Herbe")) {
                    int[] nouvelleCase = new int[]{iCourant + 1, jCourant};
                    pileCase.push(nouvelleCase);
                    listeCasePassees.add(nouvelleCase);
                    deplacementPossible = true;
                }
            }

            // Si aucun déplacement possible à partir de la case courante, on la supprime de la pile
            if (!deplacementPossible) {
                pileCase.pop();
            }
        }
        return false;
    }
    */


    public boolean verifPlateauCorrect(int iSortie, int jSortie){
        //D'abord on stocke dans une liste toutes les cases passées
        casesPlante.clear();
        listeCasePassees.clear();
        chemin.clear();
        for (int i = 1; i < lignes-1; i++){
            for (int j=1; j < colonnes-1; j++){
                if (this.cases[i][j].getContenu() instanceof Plante){
                    casesPlante.add(new int[]{i,j});
                }
            }
        }
        //On supprime l'autre liste tous les éléments
        //Maintenant on stocke dans une autre liste toutes les cases qui sont passables
        int iCourant = iSortie;
        int jCourant = jSortie;

        if (iSortie == 0){
            iCourant++;
        } else if (jSortie == 0){
            jCourant++;
        } else if (iCourant == lignes-1){
            iCourant--;
        } else if (jCourant == colonnes-1){
            jCourant--;
        }
        int[] caseDepart = new int[]{iCourant, jCourant};
        supprimerTableauListePlante(caseDepart);
        listeCasePassees.add(caseDepart);
        chemin.push(caseDepart);


        while (!casesPlante.isEmpty()) {
            // Vérifier les cases voisines de la case courante
            boolean deplacementPossible = false;
            //Correspond à gauche
            if (jCourant - 1 > 0 && this.cases[iCourant][jCourant - 1].getContenu() instanceof Plante && !presentDansLaListe(listeCasePassees, new int[]{iCourant,jCourant-1})) {
                jCourant--;
                int[] caseActuelle = new int[]{iCourant, jCourant};
                //Regarde si l'élément est disponible dans la liste
                supprimerTableauListePlante(caseActuelle);
                listeCasePassees.add(caseActuelle);
                chemin.push(caseActuelle);
                deplacementPossible = true;
            }
            //Correspond à haut
            else if (iCourant - 1 > 0 && this.cases[iCourant - 1][jCourant].getContenu() instanceof Plante && !presentDansLaListe(listeCasePassees, new int[]{iCourant - 1,jCourant})) {
                iCourant--;
                int[] caseActuelle = new int[]{iCourant, jCourant};
                //Regarde si l'élément est disponible dans la liste
                supprimerTableauListePlante(caseActuelle);
                listeCasePassees.add(caseActuelle);
                chemin.push(caseActuelle);
                deplacementPossible = true;
            }
            //Correspond à droite
            else if (jCourant + 1 < colonnes - 1 && this.cases[iCourant][jCourant + 1].getContenu() instanceof Plante && !presentDansLaListe(listeCasePassees, new int[]{iCourant,jCourant + 1})) {
                jCourant++;
                int[] caseActuelle = new int[]{iCourant, jCourant};
                //Regarde si l'élément est disponible dans la liste
                supprimerTableauListePlante(caseActuelle);
                listeCasePassees.add(caseActuelle);
                chemin.push(caseActuelle);
                deplacementPossible = true;
            }
            //Correspond à bas
            else if (iCourant + 1 < lignes - 1 && this.cases[iCourant + 1][jCourant].getContenu() instanceof Plante && !presentDansLaListe(listeCasePassees, new int[]{iCourant + 1,jCourant})) {
                iCourant++;
                int[] caseActuelle = new int[]{iCourant, jCourant};
                //Regarde si l'élément est disponible dans la liste
                supprimerTableauListePlante(caseActuelle);
                listeCasePassees.add(caseActuelle);
                chemin.push(caseActuelle);
                deplacementPossible = true;
            }

            System.out.println(iCourant+" "+jCourant+" "+deplacementPossible);
            // Si aucun déplacement n'est possible, on doit revenir en arrière
            if (!deplacementPossible) {
                int[] casePrecedente = chemin.peek();
                chemin.pop();
                iCourant = casePrecedente[0];
                jCourant = casePrecedente[1];
                if (chemin.isEmpty()){
                    return false;
                }
            }
        }


        return true;
    }



    private boolean presentDansLaListe(Collection<int[]> liste, int[] position){
        for (int[] l : liste){
            if (l[0] == position[0] && l[1] == position[1]){
                return true;
            }
        }
        return false;
    }


    private void supprimerTableauListePlante(int[] l){
        for (int i=0; i < casesPlante.size(); i++){
            if (l[0] == casesPlante.get(i)[0] && l[1] == casesPlante.get(i)[1]){
                casesPlante.remove(i);
            }
        }
    }


















    public void printMatrices () {
        for (int i = 0; i < this.lignes; i++) {
            for (int j = 0; j < this.colonnes; j++) {
                if (this.cases[i][j].getContenu().getNom().equals("Roche")) {
                    System.out.print("R" + "\t");
                } else {
                    System.out.print("H" + "\t");
                }
            }
            System.out.println("");
        }
    }



    public int getColonnes(){
        return this.colonnes;
    }

    public int getLignes(){
        return this.lignes;
    }


    public Case getCase(int i, int j){
        //sert uniquement pour les tests
        return this.cases[i][j];
    }



    public void setColonnes(int c){
        this.lignes = c;
    }

    public void setLignes(int l){
        this.lignes = l;
    }

}
