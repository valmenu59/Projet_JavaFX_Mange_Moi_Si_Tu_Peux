package jeu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Plateau {

    private int colonnes;
    private int lignes;

    protected Case[][] cases;

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
                } else if ((i % 2 == 1 && j % 2 == 1)){ //|| i == lignes - 2 || j == colonnes - 2){
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
    }



    private void supprimerObstacle(int i, int j, char orientation){
        switch (orientation){
            case 'E' -> this.cases[i][j+1].setContenuGeneral(new Herbe());
            case 'S' -> this.cases[i+1][j].setContenuGeneral(new Herbe());
        }
    }

    public boolean verifAccederPosition(int iDepart, int jDepart, int iSortie, int jSortie){
        ArrayList<int[]> listeCasePassees = new ArrayList<>();
        Stack<int[]> pileCase = new Stack<>();
        int[] tableau = new int[]{iDepart, jDepart};
        listeCasePassees.add(tableau);
        pileCase.push(tableau);
        System.out.println(pileCase);
        boolean pilePossedePositionSortie = false;
        while (!pilePossedePositionSortie){
            iDepart+=1;
            if (this.cases[iDepart][jDepart].getContenu().getNom().equals("Herbe")){
                int[] tableau2 = new int[]{iDepart,jDepart};
                listeCasePassees.add(tableau2);
                pileCase.push(tableau2);
            } else {

            }
        }
    }













    public void detruireMur(){

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
