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
    private int[] caseSortie = new int[2];
    //Pour parcourir le labyrinthe :
    private final transient ArrayList<int[]> casesPlante = new ArrayList<>();
    //transient permet de ne pas sauvegarder l'attribut lors de la sauvegarde

    //Pour garder en mémoire les cases précédentes :
    private final transient ArrayList<int[]> casesLoupPassees = new ArrayList<>();
    private final transient ArrayList<int[]> casesMoutonPassees = new ArrayList<>();
    private transient int planteMangee;

    /**
     * @param l : nombre de lignes
     * @param c : nombre de colonnes
     * Constructeur du plateau
     */


    public Plateau(int l, int c){
        this.lignes = l;
        this.colonnes = c;
        this.jeu = new Jeu();
        this.planteMangee = 0;

    }

    /////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////GET, SET, IS//////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

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


    public int[] getCaseSortie(){
        for (int i = 0; i < lignes; i++){
            for (int j = 0; j < colonnes; j++) {
                if (i == 0 || j == 0 || i == lignes - 1 || j == colonnes - 1){
                    if (getCase(i,j).getContenu() instanceof Plante){
                        caseSortie[0] = i;
                        caseSortie[1] = j;
                        break;
                    }
                }
            }
        }
        System.out.println("Case sortie "+caseSortie[0]+"   "+caseSortie[1]);
        return caseSortie;
    }


    public int getNbrPlanteMangee() {
        return this.planteMangee;
    }

    public void moutonAMangePlante(){
        this.planteMangee++;
    }

    /**
     * @return : renvoie la case du mouton en un tableau de 2 entiers
     * Méthode qui permet d'obtenir la position du mouton
     */

    public int[] getCaseMouton(){
        for (int i =0; i < lignes; i++){
            for (int j =0; j < colonnes; j++){
                if (this.cases[i][j].isAnimalPresent()){
                    if (this.cases[i][j].getAnimal() instanceof Mouton){
                        return new int[]{i,j};
                    }
                }
            }
        }
        return new int[2];
    }

    /**
     * @return : renvoie la case du mouton en un tableau de 2 entiers
     * Méthode qui permet d'obtenir la position du mouton
     */

    public int[] getCaseLoup(){
        for (int i =0; i < lignes; i++){
            for (int j =0; j < colonnes; j++){
                if (this.cases[i][j].isAnimalPresent()){
                    if (this.cases[i][j].getAnimal() instanceof Loup){
                        return new int[]{i,j};
                    }
                }
            }
        }
        return new int[2];
    }

    /**
     * @param n : Nom de l'animal
     * @return : renvoie vrai si l'animal choisi est présent, sinon renvoie faux
     */


    public boolean isAnimalPresent(String n){
        for (int i = 0; i < this.getLignes(); i++) {
            for (int j = 0; j < this.getColonnes(); j++) {
                if (this.cases[i][j].isAnimalPresent()) {
                    if (this.cases[i][j].getAnimal().getNom().equals(n)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param liste : la collection choisie
     * @param position : un tableau de la position actuelle
     * @return : renvoie vrai si la collection contient les mêmes valeurs que les valeurs du tableau,
     * sinon renvoie faux
     */

    public boolean isPresentDansLaListe(Collection<int[]> liste, int[] position){
        for (int[] l : liste){
            //Permet de comparer que 2 tableaux sont identiques en valeurs
            if (Arrays.equals(l, position)){
                return true;
            }
        }
        return false;
    }

    public void setPositionAnimal(int posI, int PosJ, String animal){
        for (int i = 0; i < this.getLignes(); i++) {
            for (int j = 0; j < this.getColonnes(); j++) {
                if (this.cases[i][j].isAnimalPresent()) {
                    if (this.cases[i][j].getAnimal().getNom().equals(animal)) {
                        getCase(i,j).removeAnimal();
                        if (animal.equals("Mouton")) {
                            getCase(posI, PosJ).setAnimal(new Mouton());
                        } else {
                            getCase(posI, PosJ).setAnimal(new Loup());
                        }
                    }
                }
            }
        }
    }



    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////FIN GET, SET, IS////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////


    /**
     * Méthode permettant de créer un plateau vierge où toutes les extrémités sont des roches et le reste
     * des herbes
     */


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


    /**
     * Méthode permettant de créer un labyrinthe parfait très aléatoire donnant beaucoup de possibilité de
     * déplacement pour les animaux
     */


    public void creerLabyrinthe() {
        // Etape 1 :
        // Initialisation du plateau avec les cases des positions I et J impairs sont des plantes, OU que
        // I et J sont à l'avant-dernier lignes ou colonnes
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                if ((i % 2 == 1 && j % 2 == 1) || (i == lignes - 2 && j == colonnes - 2)){
                    this.cases[i][j].setPlanteRandom();
                } else if (!(i == 0 || i == lignes - 1 || j == 0 || j == colonnes - 1)) {
                    this.cases[i][j].setContenuGeneral(new Roche());
                }
            }
        }

        //Etape 2 :
        // Suppression de l'obstacle EST ou SUD à partir de la case actuelle (en boucle)
        ArrayList<Integer> listeColonnes = new ArrayList<>();
        for (int i = 1; i < lignes - 1; i++) {
            for (int j = 1; j < colonnes -1; j++) {
                double random = Math.random();
                if (random <= 0.5){
                    if (j != colonnes - 2) {
                        listeColonnes.add(j); //ajout dans la liste
                        supprimerObstacle(i, j, 'E'); //suppression de l'obstacle EST à la position i,j
                    }
                } else {
                    if (i != lignes - 2) {
                        listeColonnes.add(j);
                        Collections.shuffle(listeColonnes); //mélange de la liste
                        //suppression de l'obstacle SUD à la position i, élément 0 de la liste mélangée
                        supprimerObstacle(i, listeColonnes.get(0), 'S');
                        listeColonnes.clear(); // effacement des positions de la liste
                    }
                }
            }
        }

        //Ajout des roches si le nombre de lignes ou/et de colonnes est pair
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

        //Etape 3 :
        //Libération des cases aux alentours de la sortie
        caseSortie = getCaseSortie();
        if (caseSortie[0] == 0 || caseSortie[0] == lignes - 1) {
            int depart = Math.max(0, caseSortie[0] - 1);
            int arrivee = Math.min(lignes - 1, caseSortie[0] + 1);
            int colonne = caseSortie[1];

            for (int i = depart; i <= arrivee; i++) {
                this.cases[i][colonne].setPlanteRandom();
            }
            if (caseSortie[0] == 0){
                this.cases[getCaseSortie()[0] + 2][getCaseSortie()[1]].setPlanteRandom();
            } else {
                this.cases[getCaseSortie()[0] - 2][getCaseSortie()[1]].setPlanteRandom();
            }
        } else {
            int depart = caseSortie[0];
            int colonneDepart = Math.max(0, caseSortie[1] - 1);
            int colonneArrivee = Math.min(colonnes - 1, caseSortie[1] + 1);

            for (int j = colonneDepart; j <= colonneArrivee; j++) {
                this.cases[depart][j].setPlanteRandom();
            }
            if (caseSortie[1] == 0){
                this.cases[getCaseSortie()[0]][getCaseSortie()[1] + 2].setPlanteRandom();
            } else {
                this.cases[getCaseSortie()[0]][getCaseSortie()[1] - 2].setPlanteRandom();
            }
        }

        //Etape 4
        //Si labyrinthe imparfait (très rare) remplacement des cases non visitées par de la roche
        if (!verifPlateauCorrect()) {
            for (int[] c : casesPlante) {
                this.cases[c[0]][c[1]].setContenuGeneral(new Roche());
            }
        }
    }


    /**
     * @param i : position i du plateau
     * @param j : position j du plateau
     * @param orientation : orientation choisie (N, S, E ou O)
     * Cette méthode permet supprimer une roche afin de la remplacer par une plante aléatoire
     */

    public void supprimerObstacle(int i, int j, char orientation){
        switch (orientation){
            case 'N' -> this.cases[i-1][j].setPlanteRandom();
            case 'E' -> this.cases[i][j+1].setPlanteRandom();
            case 'S' -> this.cases[i+1][j].setPlanteRandom();
            case 'O' -> this.cases[i][j-1].setPlanteRandom();
        }
    }

    /**
     * @return : renvoie vrai si le labyrinthe est parfait, sinon renvoie faux
     * Cette méthode permet de vérifier si le plateau est un labyrinthe est porfait ou pas
     * Cette méthode contient 2 arraylists : un qui contient les cases plantes, l'autre le marqueur
     * Contient aussi une pile (parcours en profondeur) contenant le chemin
     * Tant qu'on a pas visité toutes les cases plantes, vérifie les 4 positions si :
     * la case est bien une plante ET n'est pas à l'extrémité ET n'est pas marquée
     * Si aucune des 4 positions n'est possible, on reprend le dernier élément de chemin et on recommence
     * Si la pile est vide, renvoie faux car labyrinthe est imparfait
     * Si l'algorithme a visité toutes les cases plantes, renvoie vrai car labyrinthe parfait
     */


    public boolean verifPlateauCorrect(){
        //D'abord on stocke dans une liste toutes les cases passées
        casesPlante.clear();
        ArrayList<int[]> listeCasePassees = new ArrayList<>();
        Stack<int[]> chemin = new Stack<>();

        //On complète la liste des cases plantes
        completerListeCasesPlante();
        //On supprime l'autre liste tous les éléments
        //Maintenant on stocke dans une autre liste toutes les cases qui sont passables
        int iCourant = this.getCaseSortie()[0];
        int jCourant = this.getCaseSortie()[1];


        actionsCaseActuelle(iCourant, jCourant, listeCasePassees, chemin);
        //parcours en profondeur
        while (!casesPlante.isEmpty()) {
            // Vérifier les cases voisines de la case courante
            boolean deplacementPossible = false;


            // Vérification des cases voisines
            int[] dI = {-1, 1, 0, 0}; // Déplacements en ligne (haut, bas)
            int[] dJ = {0, 0, -1, 1}; // Déplacements en colonne (gauche, droite)

            for (int k = 0; k < 4; k++) {
                int nextI = iCourant + dI[k];
                int nextJ = jCourant + dJ[k];

                // Vérification que c'est pas l'extrémité, que la case est une plante et que
                // le tableau n'est pas présent dans la liste des cases passées
                if (nextI > 0 && nextI < lignes - 1 && nextJ > 0 && nextJ < colonnes - 1 &&
                    cases[nextI][nextJ].getContenu() instanceof Plante &&
                    !isPresentDansLaListe(listeCasePassees, new int[]{nextI, nextJ})) {

                   actionsCaseActuelle(nextI, nextJ, listeCasePassees, chemin);
                   deplacementPossible = true;
                }
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



    /**
     * @param arr : une Arraylist choisie
     * @param l : un tableau d'entier
     *          Cette méthode permet de supprimer une valeur dans l'arraylist si elle correspond aux mêmes valeurs
     *          du tableau contenant 2 entiers (spécifique à ce programme)
     */


    public void removeTableauArrayList(ArrayList<int[]> arr, int[] l){
        for (int i=0; i < arr.size(); i++){
            if (l[0] == arr.get(i)[0] && l[1] == arr.get(i)[1]){
                arr.remove(i);
                break;
            }
        }
    }

    /**
     * Méthode qui permet une liste contenant la position des cases plantes
     */

    private void completerListeCasesPlante(){
        for (int i = 1; i < lignes-1; i++){
            for (int j=1; j < colonnes-1; j++){
                if (this.cases[i][j].getContenu() instanceof Plante){
                    casesPlante.add(new int[]{i,j});
                }
            }
        }
    }

    /**
     * @param i : position i du plateau
     * @param j : position j du plateau
     * @return : renvoie vrai si la case actuelle a 4 voisins de type plante ET qu'il y a au moins 2 roches.
     * En effet, le parcours en profondeur a pour défaut que s'il y a 4 cases possibles
     * il ne fait au maximum que 2 cases au mieux de 3
     */

    public boolean aBien4CasesVoisinesPlantes(int i, int j){
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

    /**
     * @param i : position i du plateau
     * @param j : position j du plateau
     * Méthode qui permet de supprimer la position actuelle de la liste casesPlantes
     * Ajoute la position à la liste des cases passées et push 1 ou 2 fois le chemin
     */

    public void actionsCaseActuelle(int i, int j, ArrayList<int[]> casesPasses, Stack<int[]> chemin){
        int[] caseActuelle = new int[]{i, j};
        removeTableauArrayList(casesPlante, caseActuelle);
        casesPasses.add(caseActuelle);
        //En effet s'il y a 4 cases on fait 2 fois push
        if (aBien4CasesVoisinesPlantes(i,j)) {
            //chemin.push(caseActuelle);
            chemin.push(caseActuelle);
        } else {
            chemin.push(caseActuelle);
        }
    }




    /**
     * Méthode qui permet d'afficher à la console un plateau représentant une lettre en fonction
     * du type de terrain
     */

    public void printMatrices () {
        for (int i = 0; i < this.lignes; i++) {
            for (int j = 0; j < this.colonnes; j++) {
                if (getCase(i,j).isAnimalPresent()){
                    if (getCase(i,j).getAnimal() instanceof Loup){
                        System.out.print("w" + "\t");
                    } else {
                        System.out.print("m" + "\t");
                    }
                } else {
                    if (this.cases[i][j].getContenu() instanceof Roche) {
                        System.out.print("R" + "\t");
                    } else if (this.cases[i][j].getContenu() instanceof Herbe) {
                        System.out.print("H" + "\t");
                    } else if (this.cases[i][j].getContenu() instanceof Cactus) {
                        System.out.print("C" + "\t");
                    } else {
                        System.out.print("M" + "\t");
                    }
                }
            }
            System.out.println();
        }
    }







    /**
     * Méthode qui permet d'augmenter la valeur du nombre de jours où la case est de type terre et si la valeur
     * est supérieure à 2 permet de faire pousser une nouvelle plante aléatoire
     */

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

    /**
     * @param i : position i du plateau
     * @param j : position j du plateau
     * @return : retourne un entier en fonction de la plante où le mouton arrête son tour
     * Renvoie 4 si c'est une marguerite, 2 si c'est une terre ou une herbe, 1 si c'est un cactus
     * Si c'est n'importe quelle plante sauf de la terre, le mouton la mange et crée une case de type terre
     */

    public int moutonMangePlante(int i, int j){
        //Permet d'obtenir le nombre de déplacements du mouton en fonction de la case
        if (!(this.cases[i][j].getContenu() instanceof Terre)){
            moutonAMangePlante(); //permet d'augmenter le compteur du nombre de plantes mangées
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



    /**
     * @param animal : Nom de l'animal choisi
     * Méthode qui permet de détecter la case de l'animal choisi et le déplace avec un algorithme choisi
     * s'il y est menacé/menaçant, sinon le déplacer aléatoirement
     */


    public void deplacerAnimal(String animal, boolean danger){
        int[] posAnimal;
        if (!danger) {
            if (animal.equals("Mouton")) {
                posAnimal = getCaseMouton();
                if (!deplacementAnimalPassif(posAnimal[0], posAnimal[1], new Mouton())) {
                    deplacerAnimal("Mouton", false);
                }
            } else {
                posAnimal = getCaseLoup();
                if (!deplacementAnimalPassif(posAnimal[0], posAnimal[1], new Loup())) {
                    deplacerAnimal("Loup", false);
                }
            }
        } else {
            System.out.println("ici");
            List<int[]> chemin;
            if (animal.equals("Mouton")) {
                //parcoursProfondeur(true);
                //dijkstra();
                chemin = aStarSimple(true);
                int [] caseMouton = getCaseMouton();
                if (Arrays.equals(caseMouton, getCaseLoup())){
                    this.cases[caseMouton[0]][caseMouton[1]].removeAnimal();
                    this.cases[caseMouton[0]][caseMouton[1]].removeAnimal();
                    this.cases[caseMouton[0]][caseMouton[1]].setAnimal(new Loup());
                } else {
                    this.cases[caseMouton[0]][caseMouton[1]].removeAnimal();
                    this.cases[chemin.get(0)[0]][chemin.get(0)[1]].setAnimal(new Mouton());
                }
            } else {
                //parcoursProfondeur(false);
                //dijkstra();
                chemin = aStarSimple(false);
                int [] caseLoup = getCaseLoup();
                this.cases[caseLoup[0]][caseLoup[1]].removeAnimal();
                this.cases[chemin.get(0)[0]][chemin.get(0)[1]].setAnimal(new Loup());

            }
        }
    }

    /**
     *
     * @param i : position i du plateau
     * @param j : position j du plateau
     * @param a : animal actuel : loup ou mouton
     * @return : renvoie vrai si le déplacement est possible, sinon renvoie faux
     * Le but de cette méthode est de regarder les 4 positions possibles de déplacement
     * Une liste prend les cases auquel l'animal peut se déplacer ET qu'il n'est pas passé dessus dernièrement
     * (sauf dans le cas où aucun déplacement n'était possible, permet de favoriser le déplacement des animaux)
     * Si déplacement possible, prend une des cases possibles au hasard et déplace l'animal vers cette case
     */

    public boolean deplacementAnimalPassif(int i, int j, Animal a){
        //Reprise d'une arraylist déjà créée
        ArrayList<int[]> listeCasePasses;
        int[] posActuelle;
        if (a.getNom().equals("Mouton")){
            listeCasePasses = casesMoutonPassees;
            posActuelle = getCaseMouton();
        } else {
            listeCasePasses = casesLoupPassees;
            posActuelle = getCaseLoup();
        }
        //Création d'une liste stockant les cases possibles
        ArrayList<int[]> casesPossibles = new ArrayList<>();
        //Regarde la position gauche
        //casesPossibles prend la valeur si la case est une plante ET n'est pas présent dans la liste
        //des cases passées
        int I = posActuelle[0]; // Ligne de la position actuelle
        int J = posActuelle[1]; // Colonne de la position actuelle

        // Vérification des cases voisines
        int[] dI = {-1, 1, 0, 0}; // Déplacements en ligne (haut, bas)
        int[] dJ = {0, 0, -1, 1}; // Déplacements en colonne (gauche, droite)


        for (int k = 0; k < 4; k++) {
            int nextI = I + dI[k];
            int nextJ = J + dJ[k];

            if (nextI > 0 && nextI < lignes - 1 && nextJ > 0 && nextJ < colonnes - 1){
                int[] caseActuelle = new int[]{nextI, nextJ};
                if (cases[nextI][nextJ].getContenu() instanceof Plante &&
                !isPresentDansLaListe(listeCasePasses, caseActuelle)){
                    casesPossibles.add(caseActuelle);
                }
            }
        }

        //Si aucune possibilité
        if (casesPossibles.isEmpty()){
            //On efface l'une des 2 listes en fonction de l'animal
            if (a.getNom().equals("Mouton")){
                casesMoutonPassees.clear();
            } else {
                casesLoupPassees.clear();
            }
            return false; //retourne faux
        }
        //Sinon, on initialise une méthode Random
        Random random = new Random();
        //On prend la taille de la liste
        int position = random.nextInt(casesPossibles.size());
        //Choisi une case aléatoirement
        int[] deplacementChoisi = casesPossibles.get(position);
        //Supprime l'animal à la case initiale et déplace l'animal en fonction de la sélection
        this.cases[i][j].removeAnimal();
        this.cases[deplacementChoisi[0]][deplacementChoisi[1]].setAnimal(a);
        if (a.getNom().equals("Mouton")){
            casesMoutonPassees.add(deplacementChoisi);
        } else {
            casesLoupPassees.add(deplacementChoisi);
        }
        return true;
    }


    /*
    Différentes méthodes pour la saé 202 :
    Manhattan pour détecter que le mouton et loup sont bien à 5 cases de distances de manhattan
    parcours en largeur
    parcours en profondeur
    A*
    Dijkstra
     */

    public int[][] initialiserMatricePoids(){
        //Création d'une matrice ayant des entiers : -1 pour les plantes -2 pour les roches
        //Sert pour le marquage
        int[][] casesNumero = new int[lignes][colonnes];
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                if (getCase(i,j).getContenu() instanceof Plante) {
                    casesNumero[i][j] = -1;
                } else {
                    casesNumero[i][j] = -2;
                }
            }
        }
        return casesNumero;
    }

    public int[][] donnerPoidsCase(int[] position0){
        int[][] casesNumero = initialiserMatricePoids();

        casesNumero[position0[0]][position0[1]] = 0;

        //Création d'une file (parcours en largeur)
        Queue<int[]> file = new ArrayDeque<>();
        //Ajout de la position mouton
        file.add(position0);

        while (!file.isEmpty()) {
            int[] posActuelle = file.poll(); //Récupération et suppression de la 1ère valeur de la file
            //I et J : valeur prise de la file
            int I = posActuelle[0];
            int J = posActuelle[1];


            int[] dI = {-1, 1, 0, 0}; // Déplacements en ligne (haut, bas)
            int[] dJ = {0, 0, -1, 1}; // Déplacements en colonne (gauche, droite)

            //Permet de vérifier les 4 côtés

            for (int k = 0; k < 4; k++) {
                int nextI = I + dI[k];
                int nextJ = J + dJ[k];

                // Vérification des limites de la grille
                if (nextI >= 0 && nextI < lignes && nextJ >= 0 && nextJ < colonnes) {
                    //vérification que la case n'a pas encore été visitée
                    if (casesNumero[nextI][nextJ] == -1) {
                        file.add(new int[]{nextI, nextJ});
                        casesNumero[nextI][nextJ] = casesNumero[I][J] + 1;
                    }
                }
            }
        }

        printNumeroCase(casesNumero);

        return casesNumero;
    }

    /**
     * Cette méthode permet de calculer la distance de Manhanttan entre le loup et le mouton
     * Si la distance est inférieure ou égale à 5 renvoie vrai, sinon renvoie faux.
     * Utilise l'algorithme de parcours en largeur
     */

    public boolean manhattan() {
        int[][] poids = donnerPoidsCase(getCaseMouton());

        int[] caseLoup = getCaseLoup();

        return poids[caseLoup[0]][caseLoup[1]] <= 5;
    }

    /**
     * Méthode privée permettant de visualiser la distance des cases par rapport à la position du mouton en
     * distance de Manhattan
     * -1 représente les cases non visitées, -2 représente les roches
     */

    private void printNumeroCase(int[][] plateau){
        System.out.println("Manhanttan : ");
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                String numero = String.format("%2d", plateau[i][j]); //Permet de décaler s'il y a un seul caractère
                System.out.print(numero + "\t");
            }
            System.out.println();
        }
    }




    public List<int[]> aStarSimple(boolean isAuTourMouton){
        int[][] poids;
        int[] posDepart;
        int[] posArrivee;

        if (isAuTourMouton){
            posDepart = getCaseMouton();
            posArrivee = getCaseSortie();
        } else {
            posDepart = getCaseLoup();
            posArrivee = getCaseMouton();
        }

        poids = donnerPoidsCase(posArrivee);

        printNumeroCase(poids);

        // Création d'une file de priorité (parcours en largeur)
        Queue<int[]> queue = new ArrayDeque<>();
        // Ajout de la position mouton
        queue.offer(posDepart);

        List<int[]> leChemin = new ArrayList<>();



        int poidsActuel = poids[posDepart[0]][posDepart[1]];

        poids[posDepart[0]][posDepart[1]] = -1;

        while (!queue.isEmpty()) {
            int[] posActuelle = queue.poll(); // Récupération et suppression de la première valeur de la file
            int I = posActuelle[0]; // Ligne de la position actuelle
            int J = posActuelle[1]; // Colonne de la position actuelle

            // Vérification des cases voisines
            int[] dI = {-1, 1, 0, 0}; // Déplacements en ligne (haut, bas)
            int[] dJ = {0, 0, -1, 1}; // Déplacements en colonne (gauche, droite)

            List<int[]> casesPossibles = new ArrayList<>();

            for (int k = 0; k < 4; k++) {
                int nextI = I + dI[k];
                int nextJ = J + dJ[k];

                // Vérification des limites de la grille
                if (nextI >= 0 && nextI < lignes && nextJ >= 0 && nextJ < colonnes && poids[nextI][nextJ] >= 0) {
                    // Mise à jour de la distance si elle est plus courte
                    if (poidsActuel > poids[nextI][nextJ] && poids[nextI][nextJ] >= 0) {
                        casesPossibles.add(new int[]{nextI, nextJ});
                    }
                }
            }

            double plusCourteDistance = Integer.MAX_VALUE;
            int[] positionLaPlusCourte = new int[2];

            for (int[] c : casesPossibles){
                double distance = distanceAStar(c[0], c[1], posArrivee[0], posArrivee[1]);
                if (plusCourteDistance > distance){
                    plusCourteDistance = distance;
                    positionLaPlusCourte = c;
                }
            }

            leChemin.add(positionLaPlusCourte);
            queue.offer(positionLaPlusCourte);

            //Permet de marquer la case comme visitée
            poids[positionLaPlusCourte[0]][positionLaPlusCourte[1]] = -1;

            if (Arrays.equals(posArrivee, positionLaPlusCourte)){
                break;
            }
        }

        System.out.println("je suis l'algo A*");
        printNumeroCase(poids);
        return leChemin;

    }

    public double distanceAStar(int iDepart, int jDepart, int iArrivee, int jArrivee){
        return Math.sqrt(Math.pow((iDepart - iArrivee), 2) + Math.pow((jDepart - jArrivee), 2));
    }

























    public ArrayList<int[]> parcoursLargeur(boolean isAuTourMouton){
        //Sert pour le marquage
        int[][] casesNumero = initialiserMatricePoids();

        int[] posAnimalEnCours;
        int[] posDestination;
        int posIAnimal;
        int posJAnimal;
        int posIArrive;
        int posJArrive;

        if (isAuTourMouton){
            //Récupération de la case mouton
            posAnimalEnCours = getCaseMouton();
            posIAnimal = posAnimalEnCours[0];
            posJAnimal = posAnimalEnCours[1];
            //Récupération de la case sortie
            posDestination = getCaseSortie();
            posIArrive = posDestination[0];
            posJArrive = posDestination[1];
        } else {
            //Récupération de la case loup
            posAnimalEnCours = getCaseLoup();
            posIAnimal = posAnimalEnCours[0];
            posJAnimal = posAnimalEnCours[1];
            //Récupération de la case mouton
            posDestination = getCaseSortie();
            posIArrive = posDestination[0];
            posJArrive = posDestination[1];
        }

        //Valeur de la case mouton = 0
        casesNumero[posIAnimal][posJAnimal] = 0;

        // Liste pour stocker le chemin parcouru
        ArrayList<int[]> cheminParcouru = new ArrayList<>();

        //Création d'une file (parcours en largeur)
        Queue<int[]> file = new ArrayDeque<>();
        //Ajout de la position de l'animal actuel
        file.add(posAnimalEnCours);

        while (!file.isEmpty()) {
            //Récupération et suppression de la 1ère valeur de la file
            int[] posActuelle;

            posActuelle = file.poll();

            //I et J : valeur prise de la file
            int I = posActuelle[0];
            int J = posActuelle[1];

            cheminParcouru.add(posActuelle);

            int nbrPossibiliteCase = 0;
            //Vérification en haut
            if (I - 1 >= 0 && casesNumero[I - 1][J] == -1) {
                file.add(new int[]{I - 1, J});
                casesNumero[I - 1][J] = 0;
                nbrPossibiliteCase++;
                //Si la case possède un loup
                if ( I - 1 == posIArrive && J == posJArrive) {
                    break;
                }
            }

            // Vérification des autres directions
            // Bas
            if (I + 1 < lignes && casesNumero[I + 1][J] == -1) {
                file.add(new int[]{I + 1, J});
                casesNumero[I + 1][J] = 0;
                nbrPossibiliteCase++;
                if ( I + 1 == posIArrive && J == posJArrive) {
                    break;
                }
            }

            // Gauche
            if (J - 1 >= 0 && casesNumero[I][J - 1] == -1) {
                file.add(new int[]{I, J - 1});
                casesNumero[I][J - 1] = 0;
                nbrPossibiliteCase++;
                if ( I == posIArrive && J - 1 == posJArrive) {
                    break;
                }
            }

            // Droite
            if (J + 1 < colonnes && casesNumero[I][J + 1] == -1) {
                file.add(new int[]{I, J + 1});
                casesNumero[I][J + 1] = 0;
                nbrPossibiliteCase++;
                if ( I == posIArrive && J + 1 == posJArrive) {
                    break;
                }
            }

            if (nbrPossibiliteCase == 0) {
                cheminParcouru.remove(cheminParcouru.size() - 1);
                int index = cheminParcouru.indexOf(posActuelle);
                if (index != -1) {
                    cheminParcouru.subList(index + 1, cheminParcouru.size()).clear();
                }
            }

        }
        printNumeroCase(casesNumero);
        //cheminParcouru.remove(0);
        for (int[] c : cheminParcouru){
            System.out.println(c[0]+" "+c[1]);
        }
        return cheminParcouru;
    }

    public ArrayList<int[]> parcoursProfondeur(boolean isAuTourMouton){
        //Sert pour le marquage
        int[][] casesNumero = initialiserMatricePoids();

        int[] posAnimalEnCours;
        int[] posDestination;
        int posIAnimal;
        int posJAnimal;
        int posIArrive;
        int posJArrive;

        if (isAuTourMouton){
            //Récupération de la case mouton
            posAnimalEnCours = getCaseMouton();
            posIAnimal = posAnimalEnCours[0];
            posJAnimal = posAnimalEnCours[1];
            //Récupération de la case sortie
            posDestination = getCaseSortie();
            posIArrive = posDestination[0];
            posJArrive = posDestination[1];
        } else {
            //Récupération de la case loup
            posAnimalEnCours = getCaseLoup();
            posIAnimal = posAnimalEnCours[0];
            posJAnimal = posAnimalEnCours[1];
            //Récupération de la case mouton
            posDestination = getCaseSortie();
            posIArrive = posDestination[0];
            posJArrive = posDestination[1];
        }

        //Valeur de la case mouton = 0
        casesNumero[posIAnimal][posJAnimal] = 0;

        // Liste pour stocker le chemin parcouru
        ArrayList<int[]> cheminParcouru = new ArrayList<>();

        //Création d'une file (parcours en largeur)
        Stack<int[]> pile = new Stack<>();
        //Ajout de la position de l'animal actuel
        pile.push(posAnimalEnCours);

        while (!pile.isEmpty()) {
            //Récupération et suppression de la 1ère valeur de la file
            int[] posActuelle;

            posActuelle = pile.peek();
            pile.pop();

            //I et J : valeur prise de la file
            int I = posActuelle[0];
            int J = posActuelle[1];

            cheminParcouru.add(posActuelle);

            int nbrPossibiliteCase = 0;
            //Vérification en haut
            if (I - 1 >= 0 && casesNumero[I - 1][J] == -1) {
                pile.add(new int[]{I - 1, J});
                casesNumero[I - 1][J] = 0;
                nbrPossibiliteCase++;
                //Si la case possède un loup
                if ( I - 1 == posIArrive && J == posJArrive) {
                    break;
                }
            }

            // Vérification des autres directions
            // Bas
            if (I + 1 < lignes && casesNumero[I + 1][J] == -1) {
                pile.add(new int[]{I + 1, J});
                casesNumero[I + 1][J] = 0;
                nbrPossibiliteCase++;
                if ( I + 1 == posIArrive && J == posJArrive) {
                    break;
                }
            }

            // Gauche
            if (J - 1 >= 0 && casesNumero[I][J - 1] == -1) {
                pile.add(new int[]{I, J - 1});
                casesNumero[I][J - 1] = 0;
                nbrPossibiliteCase++;
                if ( I == posIArrive && J - 1 == posJArrive) {
                    break;
                }
            }

            // Droite
            if (J + 1 < colonnes && casesNumero[I][J + 1] == -1) {
                pile.add(new int[]{I, J + 1});
                casesNumero[I][J + 1] = 0;
                nbrPossibiliteCase++;
                if ( I == posIArrive && J + 1 == posJArrive) {
                    break;
                }
            }

            if (nbrPossibiliteCase == 0) {
                cheminParcouru.remove(cheminParcouru.size() - 1);
                int index = cheminParcouru.indexOf(posActuelle);
                if (index != -1) {
                    cheminParcouru.subList(index + 1, cheminParcouru.size()).clear();
                }
            }

        }
        printNumeroCase(casesNumero);
        //cheminParcouru.remove(0);
        for (int[] c : cheminParcouru){
            System.out.println(c[0]+" "+c[1]);
        }
        return cheminParcouru;
    }

    //Optionnel
    public int[][] dijkstra(){
        int[][] distance  = new int[lignes][colonnes];

        // Initialisation des distances à une valeur maximale (infini)
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                if (getCase(i,j).getContenu() instanceof Plante) {
                    distance[i][j] = Integer.MAX_VALUE;
                } else {
                    distance[i][j] = -1 ; //Permet de différencier les cases
                }
            }
        }

        // Récupération de la case mouton
        int[] posMouton = getCaseMouton();
        int posI = posMouton[0];
        int posJ = posMouton[1];

        // Valeur de la case mouton = 0
        distance[posI][posJ] = 0;

        // Création d'une file de priorité (parcours en largeur)
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o -> distance[o[0]][o[1]]));
        // Ajout de la position mouton
        queue.offer(posMouton);

        while (!queue.isEmpty()) {
            int[] posActuelle = queue.poll(); // Récupération et suppression de la première valeur de la file
            int I = posActuelle[0]; // Ligne de la position actuelle
            int J = posActuelle[1]; // Colonne de la position actuelle

            // Vérification des cases voisines
            int[] dI = {-1, 1, 0, 0}; // Déplacements en ligne (haut, bas)
            int[] dJ = {0, 0, -1, 1}; // Déplacements en colonne (gauche, droite)

            for (int k = 0; k < 4; k++) {
                int nextI = I + dI[k];
                int nextJ = J + dJ[k];

                // Vérification des limites de la grille
                if (nextI >= 0 && nextI < lignes && nextJ >= 0 && nextJ < colonnes) {
                    // Calcul du poids de la case voisine
                    int weight = 0;
                    if (getCase(nextI, nextJ).getContenu() instanceof Plante) {
                        weight = 1; // Poids pour les plantes
                    }

                    // Mise à jour de la distance si elle est plus courte
                    if (distance[I][J] + weight < distance[nextI][nextJ]) {
                        distance[nextI][nextJ] = distance[I][J] + weight;
                        queue.offer(new int[]{nextI, nextJ});
                    }
                }
            }
        }
        System.out.println("je suis algo de dijstra");
        printNumeroCase(distance);


        return distance;


    }



}
