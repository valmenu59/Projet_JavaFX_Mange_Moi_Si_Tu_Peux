package jeu;

import java.io.*;
import java.util.*;

public class Plateau implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final int colonnes;
    private final int lignes;

    protected Case[][] cases;
    protected Jeu jeu;
    private final int[] caseSortie = new int[2];

    private transient ArrayList<int[]> casesPlante = new ArrayList<>();
    private transient ArrayList<int[]> listeCasePassees = new ArrayList<>();
    private transient Stack<int[]> chemin = new Stack<>();
    private int planteMangee;


    public Plateau(int l, int c){
        this.lignes = l;
        this.colonnes = c;
        this.jeu = new Jeu();
        this.planteMangee = 0;

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
                if ((i % 2 == 1 && j % 2 == 1) || (i == lignes - 2 && j == colonnes - 2)){
                    this.cases[i][j].setPlanteRandom();
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


        if (lignes % 2 == 0){
            for (int j = 0; j < colonnes; j++){
                this.cases[lignes - 1][j].setContenuGeneral(new Roche());
            }
        }
        if (colonnes % 2 == 0){
            for (int i = 0; i < lignes; i++){
                this.cases[i][colonnes - 1].setContenuGeneral(new Roche());
            }
        }

        if (caseSortie[0] == 0 || caseSortie[0] == lignes - 1) {
            int startRow = Math.max(0, caseSortie[0] - 1);
            int endRow = Math.min(lignes - 1, caseSortie[0] + 1);
            int column = caseSortie[1];

            for (int i = startRow; i <= endRow; i++) {
                this.cases[i][column].setPlanteRandom();
            }
            if (caseSortie[0] == 0){
                this.cases[getCaseSortie()[0] + 2][getCaseSortie()[1]].setPlanteRandom();
            } else {
                this.cases[getCaseSortie()[0] - 2][getCaseSortie()[1]].setPlanteRandom();
            }
        } else {
            int row = caseSortie[0];
            int startColumn = Math.max(0, caseSortie[1] - 1);
            int endColumn = Math.min(colonnes - 1, caseSortie[1] + 1);

            for (int j = startColumn; j <= endColumn; j++) {
                this.cases[row][j].setPlanteRandom();
            }
            if (caseSortie[1] == 0){
                this.cases[getCaseSortie()[0]][getCaseSortie()[1] + 2].setPlanteRandom();
            } else {
                this.cases[getCaseSortie()[0]][getCaseSortie()[1] - 2].setPlanteRandom();
            }
        }

        if (!verifPlateauCorrect()) {
            for (int[] c : casesPlante) {
                this.cases[c[0]][c[1]].setContenuGeneral(new Roche());
            }
        }
        System.out.println(verifPlateauCorrect());




    }


    public int getNbrPlanteMangee() {
        return this.planteMangee;
    }

    public void moutonAMangePlante(){
        this.planteMangee++;
    }


    /*
    //POUR TESTER
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
            case 'N' -> this.cases[i-1][j].setPlanteRandom();
            case 'E' -> this.cases[i][j+1].setPlanteRandom();
            case 'S' -> this.cases[i+1][j].setPlanteRandom();
            case 'O' -> this.cases[i][j-1].setPlanteRandom();
        }
    }





    public boolean verifPlateauCorrect(){
        //D'abord on stocke dans une liste toutes les cases passées
        casesPlante.clear();
        listeCasePassees.clear();
        chemin.clear();

        completerListeCasesPlante();
        //On supprime l'autre liste tous les éléments
        //Maintenant on stocke dans une autre liste toutes les cases qui sont passables
        int iCourant = this.getCaseSortie()[0];
        int jCourant = this.getCaseSortie()[1];


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
                break;
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
                } else if (this.cases[i][j].getContenu() instanceof Herbe) {
                    System.out.print("H" + "\t");
                } else if (this.cases[i][j].getContenu() instanceof Cactus){
                    System.out.print("C" + "\t");
                } else {
                    System.out.print("M" + "\t");
                }
            }
            System.out.println();
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

    public void setCaseSortie(int i, int j){
        caseSortie[0] = i;
        caseSortie[1] = j;
    }

    public int[] getCaseSortie(){
        return caseSortie;
    }

    public int[] getCaseMouton(){
        for (int i =0; i < lignes; i++){
            for (int j =0; j < colonnes; j++){
                if (this.cases[i][j].animalPresent()){
                    if (this.cases[i][j].getAnimal() instanceof Mouton){
                        return new int[]{i,j};
                    }
                }
            }
        }
        return new int[2];
    }

    public void planteQuiPousse(){
        for (int i = 0; i < lignes; i++){
            for (int j = 0; j < colonnes; j++){
                if (this.cases[i][j].getContenu() instanceof Terre){
                    ((Terre) this.cases[i][j].getContenu()).pasDePlantePlusUnJour();
                    if (((Terre) this.cases[i][j].getContenu()).getNbJourPasDePlante() > 2){
                        this.cases[i][j].setPlanteRandom();
                    }
                }
            }
        }
    }



    public int moutonMangePlante(int i, int j){
        if (!(this.cases[i][j].getContenu() instanceof Terre)){
            moutonAMangePlante();
            if (this.cases[i][j].getContenu() instanceof Herbe){
                this.cases[i][j].setContenuPlante(new Terre());
                return 2;
            } else if (this.cases[i][j].getContenu() instanceof Cactus) {
                this.cases[i][j].setContenuPlante(new Terre());
                return 1;
            } else {
                this.cases[i][j].setContenuPlante(new Terre());
                return 4;
            }
        } else {
            return 2;
        }
    }







    public void setCases(int i, int j, TypeTerrain t, Animal a){
        this.cases[i][j].setContenuGeneral(t);
        this.cases[i][j].setAnimal(a);
    }


    public void deplacerAnimal(String animal){
        int[] posAnimal = new int[2];
        //Détection de l'animal
        for (int i = 0; i < lignes; i++){
            for (int j = 0; j < colonnes; j++){
                if (this.cases[i][j].animalPresent()){
                    if (this.cases[i][j].getAnimal().getNom().equals(animal)){
                        posAnimal[0] = i;
                        posAnimal[1] = j;
                    }
                }
            }
        }
        // Méthode qui permet de vérifier si le loup et
        // mouton ont une distance Manhattan > 5
        if (animal.equals("Mouton")) {
            deplacementAnimalPassif(posAnimal[0], posAnimal[1], new Mouton());
        } else {
            deplacementAnimalPassif(posAnimal[0], posAnimal[1], new Loup());
        }
    }


    public void deplacementAnimalPassif(int i, int j, Animal a){
        ArrayList<int[]> casesPossibles = new ArrayList<>();
        if (this.cases[i][j-1].getContenu() instanceof Plante && j - 1 > 0){
            casesPossibles.add(new int[]{i,j-1});
        }
        if (this.cases[i-1][j].getContenu() instanceof Plante && i - 1 > 0){
            casesPossibles.add(new int[]{i-1,j});
        }
        if (this.cases[i][j+1].getContenu() instanceof Plante && j + 1 < colonnes - 1){
            casesPossibles.add(new int[]{i,j+1});
        }
        if (this.cases[i+1][j].getContenu() instanceof Plante && i + 1 < lignes - 1){
            casesPossibles.add(new int[]{i+1,j});
        }
        System.out.println(casesPossibles.size());
        Random random = new Random();
        int position = random.nextInt(casesPossibles.size());
        int[] deplacementChoisi = casesPossibles.get(position);
        this.cases[i][j].removeAnimal();
        this.cases[deplacementChoisi[0]][deplacementChoisi[1]].setAnimal(a);
    }



}
