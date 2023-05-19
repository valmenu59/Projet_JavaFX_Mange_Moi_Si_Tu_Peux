package jeu;

import java.util.*;

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





    public void creerLabyrinthe() {
        //this.cases = new Case[lignes][colonnes];

        // Initialisation du labyrinthe avec des roches en bordure et de l'herbe à l'intérieur
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                //if (i == 0 || i == lignes - 1 || j == 0 || j == colonnes - 1) {
                   // this.cases[i][j] = new Case(new Roche(), null);
                if ((i % 2 == 1 && j % 2 == 1) || (i == lignes - 2 && j == colonnes - 2)){
                    this.cases[i][j].setContenuPlante(new Herbe());
                } else if (!(i == 0 || i == lignes - 1 || j == 0 || j == colonnes - 1)) {
                    this.cases[i][j].setContenuGeneral(new Roche());
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
        /*
        if (!verifPlateauCorrect(0,1)) {
            for (int[] c : casesPlante) {
                this.cases[c[0]][c[1]].setContenuGeneral(new Roche());
            }
        }

         */

    }





    /*
    public void creerPlateau() {
        this.cases = new Case[9][9];
        lignes = 9;
        colonnes = 9;
        for (int i = 0; i < lignes ; i++) {
            for (int j = 0; j < colonnes ; j++) {
                if (i == 0 || i == lignes - 1 || j == 0 || j == colonnes - 1) {
                    this.cases[i][j] = new Case(new Roche(), null);
                } else if ((i % 2 == 1 || j % 2 == 1) || (i == lignes - 2 && j == colonnes - 2)){
                    this.cases[i][j] = new Case(new Herbe(), null);
                } else {
                    this.cases[i][j] = new Case(new Roche(), null);
                }

            }
        }
        this.cases[0][2].setContenuGeneral(new Herbe());
        this.cases[7][6].setContenuGeneral(new Roche());
        this.cases[6][7].setContenuGeneral(new Roche());
        System.out.println(verifPlateauCorrect(0,2));
        for (int[] c : casesPlante){
            System.out.println(c[0]+" "+c[1]);
        }
        System.out.println();
    }

     */



    private void supprimerObstacle(int i, int j, char orientation){
        switch (orientation){
            case 'N' -> this.cases[i-1][j].setContenuGeneral(new Herbe());
            case 'E' -> this.cases[i][j+1].setContenuGeneral(new Herbe());
            case 'S' -> this.cases[i+1][j].setContenuGeneral(new Herbe());
            case 'O' -> this.cases[i][j-1].setContenuGeneral(new Herbe());
        }
    }





    public boolean verifPlateauCorrect(int iSortie, int jSortie){
        //D'abord on stocke dans une liste toutes les cases passées
        casesPlante.clear();
        listeCasePassees.clear();
        chemin.clear();

        completerListeCasesPlante();
        //On supprime l'autre liste tous les éléments
        //Maintenant on stocke dans une autre liste toutes les cases qui sont passables
        int iCourant = iSortie;
        int jCourant = jSortie;


        actionsCaseActuelle(iCourant, jCourant);

        while (!casesPlante.isEmpty()) {
            // Vérifier les cases voisines de la case courante
            boolean deplacementPossible = false;


            //Correspond à gauche
            if (jCourant - 1 > 0 && this.cases[iCourant][jCourant - 1].getContenu() instanceof Plante && !presentDansLaListe(listeCasePassees, new int[]{iCourant,jCourant-1})) {
                jCourant--;
                actionsCaseActuelle(iCourant, jCourant);
                deplacementPossible = true;
            }
            //Correspond à haut
            else if (iCourant - 1 > 0 && this.cases[iCourant - 1][jCourant].getContenu() instanceof Plante && !presentDansLaListe(listeCasePassees, new int[]{iCourant - 1,jCourant})) {
                iCourant--;
                actionsCaseActuelle(iCourant, jCourant);
                deplacementPossible = true;
            }
            //Correspond à droite
            else if (jCourant + 1 < colonnes - 1 && this.cases[iCourant][jCourant + 1].getContenu() instanceof Plante && !presentDansLaListe(listeCasePassees, new int[]{iCourant,jCourant + 1})) {
                jCourant++;
                actionsCaseActuelle(iCourant, jCourant);
                deplacementPossible = true;
            }
            //Correspond à bas
            else if (iCourant + 1 < lignes - 1 && this.cases[iCourant + 1][jCourant].getContenu() instanceof Plante && !presentDansLaListe(listeCasePassees, new int[]{iCourant + 1,jCourant})) {
                iCourant++;
                actionsCaseActuelle(iCourant, jCourant);
                deplacementPossible = true;
            }
            if (chemin.isEmpty()) {
                return false;
            }
            //System.out.println(iCourant+" "+jCourant+" "+deplacementPossible);
            // Si aucun déplacement n'est possible, on doit revenir en arrière
            if (!deplacementPossible) {
                int[] casePrecedente = chemin.peek();
                chemin.pop();
                iCourant = casePrecedente[0];
                jCourant = casePrecedente[1];

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


    private void supprimerTableauArrayList(ArrayList<int[]> arr, int[] l){
        for (int i=0; i < arr.size(); i++){
            if (l[0] == arr.get(i)[0] && l[1] == arr.get(i)[1]){
                arr.remove(i);
            }
        }
    }


    private void completerListeCasesPlante(){
        for (int i = 1; i < lignes-1; i++){
            for (int j=1; j < colonnes-1; j++){
                if (this.cases[i][j].getContenu() instanceof Plante){
                    casesPlante.add(new int[]{i,j});
                }
            }
        }
    }

    private boolean aBien4CasesVoisinesPlantes(int i, int j){
        /*
        Cette méthode permet de vérifier qu'il y a 4 voisins de type plante ET
        qu'il y a au moins 2 roches.
        En effet, le parcours en profondeur a pour défaut que s'il y a 4 cases possibles
        il ne fait au maximum que 2 cases au mieux de 3
         */
        int cmpt = 0;
        int roche = 0;
        if (!( i == 0 || j == 0 || i == lignes-1 || j == colonnes-1)){
            for (int a= -1; a<=1; a++){
                for (int b=-1; b<=1; b++){
                    if (Math.abs(a) != Math.abs(b)){
                        if (this.cases[i+a][j+b].getContenu() instanceof Plante){
                            cmpt++;
                        }
                    } else {
                        if (this.cases[i+a][j+b].getContenu() instanceof Roche){
                            roche++;
                        }
                    }
                }
            }
        }
        return cmpt == 4 && roche >= 2;
    }

    private void actionsCaseActuelle(int i, int j){
        int[] caseActuelle = new int[]{i, j};
        supprimerTableauArrayList(casesPlante, caseActuelle);
        listeCasePassees.add(caseActuelle);
        //En effet s'il y a 4 cases on fait 2 fois push
        if (aBien4CasesVoisinesPlantes(i,j)) {
            chemin.push(caseActuelle);
            chemin.push(caseActuelle);
        } else {
            chemin.push(caseActuelle);
        }
    }


    public boolean verifPresenceAnimal(String n){
        for (int i = 0; i < this.getLignes(); i++) {
            for (int j = 0; j < this.getColonnes(); j++) {
                if (this.cases[i][j].animalPresent()) {
                    if (this.cases[i][j].getAnimal().getNom().equals(n)) {
                        return true;
                    }
                }
            }
        }
        return false;
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





























/*
    public void creerPlateau(){
        this.cases = new Case[this.lignes][this.colonnes];
        // Étape 1 : Initialisation du labyrinthe avec des murs partout
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                this.cases[i][j] = new Case(new Roche(), null);
            }
        }

// Étape 2 : Choix d'une case de départ
        int startRow = 1;
        int startCol = 1;
        this.cases[startRow][startCol] = new Case(new Herbe(), null);

// Étape 3 : Liste des cases frontières
        ArrayList<int[]> frontierCells = new ArrayList<>();
        frontierCells.add(new int[]{startRow, startCol});

// Étape 4 : Expansion du labyrinthe
        while (!frontierCells.isEmpty()) {
            // Sélection aléatoire d'une case frontière
            int randomIndex = (int) (Math.random() * frontierCells.size());
            int[] currentCell = frontierCells.get(randomIndex);
            frontierCells.remove(randomIndex);

            int row = currentCell[0];
            int col = currentCell[1];

            // Vérification des cases voisines
            ArrayList<int[]> neighbors = new ArrayList<>();
            if (row >= 2) neighbors.add(new int[]{row - 2, col});
            if (col >= 2) neighbors.add(new int[]{row, col - 2});
            if (row < lignes - 3) neighbors.add(new int[]{row + 2, col});
            if (col < colonnes - 3) neighbors.add(new int[]{row, col + 2});

            for (int[] neighbor : neighbors) {
                int nRow = neighbor[0];
                int nCol = neighbor[1];

                if (this.cases[nRow][nCol].getContenu() instanceof Roche) {
                    // Suppression du mur entre la case courante et la case voisine
                    this.cases[nRow][nCol] = new Case(new Herbe(), null);
                    this.cases[nRow + (row - nRow) / 2][nCol + (col - nCol) / 2] = new Case(new Herbe(), null);

                    // Ajout de la case voisine à la liste des cases frontières
                    frontierCells.add(new int[]{nRow, nCol});
                }
            }
        }

// Étape 5 : Suppression des obstacles aléatoires à l'intérieur du labyrinthe
        ArrayList<Integer> listeColonnes = new ArrayList<>();
        for (int i = 1; i < lignes - 1; i++) {
            for (int j = 1; j < colonnes - 1; j++) {
                if (i % 2 == 1 || j % 2 == 1) {
                    System.out.println(i + "" + j + "" + this.cases[i][j].getContenu().getNom());
                    double random = Math.random();
                    if (random <= 0.5) {
                        if (j != colonnes - 2) {
                            listeColonnes.add(j);
                            supprimerObstacle(i, j, 'E');
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
        }
    }


 */



}
