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
    private final transient Queue<int[]> filePheromonesMouton = new ArrayDeque<>();
    private final transient Queue<int[]> filePheromonesLoup = new ArrayDeque<>();
    private final transient Queue<Integer> fileNbDeplacementMouton = new ArrayDeque<>();

    private final transient ArrayList<int[]> leCheminMouton = new ArrayList<>();
    private final transient ArrayList<int[]> leCheminLoup = new ArrayList<>();

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
        return caseSortie;
    }


    public int getNbrPlanteMangee() {
        return this.planteMangee;
    }

    public void isMoutonAMangePlante(){
        this.planteMangee++;
    }


    public Queue<int[]> getFilePheromonesMouton(){
        return filePheromonesMouton;
    }

    public Queue<int[]> getFilePheromonesLoup(){
        return filePheromonesLoup;
    }

    public Queue<Integer> getFileNbDeplacementMouton(){
        return fileNbDeplacementMouton;
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
     * @return : renvoie vrai si l'animal choisi est présent qu'une fois, sinon renvoie faux
     */


    public boolean isAnimalPresent(String n){
        int cmpt = 0;
        for (int i = 0; i < this.getLignes(); i++) {
            for (int j = 0; j < this.getColonnes(); j++) {
                if (this.cases[i][j].isAnimalPresent()) {
                    if (this.cases[i][j].getAnimal().getNom().equals(n)) {
                        cmpt++;
                    }
                }
            }
        }
        return cmpt == 1;
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
        System.out.println(getCaseSortie()[0]+" "+getCaseSortie()[1]);
    }


    /**
     * Méthode permettant de créer un labyrinthe parfait très aléatoire donnant beaucoup de possibilité de
     * déplacement pour les animaux
     */


    public void creerLabyrinthe() {
        // Etape 1 :
        // Initialisation du plateau avec les cases des positions I et J impairs sont des plantes, OU que
        // I et J sont à l'avant-dernier lignes ou colonnes
        for (int i = 1; i < lignes - 1; i++) {
            for (int j = 1; j < colonnes - 1; j++) {
                if ((i % 2 == 1 && j % 2 == 1) || (i == lignes - 2 && j == colonnes - 2)){
                    this.cases[i][j].setPlanteRandom();
                } else {
                    this.cases[i][j].setContenuGeneral(new Roche());
                }
            }
        }

        caseSortie = getCaseSortie();


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
        System.out.println(caseSortie[0]+"    "+caseSortie[1]);
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
     * Méthode qui permet de supprimer la position actuelle de la liste casesPlantes
     * Ajoute la position à la liste des cases passées et push 1 ou 2 fois le chemin
     */

    public void actionsCaseActuelle(int i, int j, ArrayList<int[]> casesPasses, Stack<int[]> chemin){
        int[] caseActuelle = new int[]{i, j};
        removeTableauArrayList(casesPlante, caseActuelle);
        casesPasses.add(caseActuelle);
        chemin.push(caseActuelle);
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
     * * Renvoie 4 si c'est une marguerite, 2 si c'est une terre ou une herbe, 1 si c'est un cactus
     * Si c'est n'importe quelle plante sauf de la terre, le mouton la mange et crée une case de type terre
     * Afin de favoriser les chances que le mouton gagne, j'ai pris une initiative où si le mouton est en danger :
     * Il y a 80% de chance que le mouton avance de 3 s'il mange une herbe, 1% de chance qu'il avance de 4
     * Il y a 60% de chance que le mouton avance de 2 s'il mange un cactus, 15% de chance qu'il avance de 3
     * Il y a 10% de chance que le mouton avance de 5 s'il mange une marguerite
     * @param i : position i du plateau
     * @param j : position j du plateau
     * @return : retourne un entier en fonction de la plante où le mouton arrête son tour
     */

    public int moutonMangePlante(int i, int j){
        double rand = Math.random();
        //Permet d'obtenir le nombre de déplacements du mouton en fonction de la case
        if (!(this.cases[i][j].getContenu() instanceof Terre)){
            isMoutonAMangePlante(); //permet d'augmenter le compteur du nombre de plantes mangées
            if (this.cases[i][j].getContenu() instanceof Herbe){
                this.cases[i][j].setContenuPlante(new Terre());
                if (jeu.isMoutonEnDanger()){
                    if (rand <= 0.80) {
                        return 3;
                    } else if (rand <= 0.81){
                        return 4;
                    }
                }
                return 2;
            } else if (this.cases[i][j].getContenu() instanceof Cactus) {
                this.cases[i][j].setContenuPlante(new Terre());
                if (jeu.isMoutonEnDanger()){
                    if (rand <= 0.60){
                        return 2;
                    } else if (rand <= 0.75){
                        return 3;
                    }
                }
                return 1;
            } else {
                this.cases[i][j].setContenuPlante(new Terre());
                if (jeu.isMoutonEnDanger()){
                    if (rand <= 0.10){
                        return 5;
                    }
                }
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


    public void deplacerAnimal(String animal, boolean danger, String algo){
        int[] posAnimal;
        int[] caseMouton;
        if (!danger) {
            if (animal.equals("Mouton")) {
                posAnimal = getCaseMouton();
                if (!deplacementAnimalPassif(posAnimal[0], posAnimal[1], new Mouton())) {
                    deplacerAnimal("Mouton", false, algo);
                }
            } else {
                posAnimal = getCaseLoup();
                if (!deplacementAnimalPassif(posAnimal[0], posAnimal[1], new Loup())) {
                    deplacerAnimal("Loup", false, algo);
                }
            }
        } else {
            if (animal.equals("Mouton")) {
                if (jeu.getDeplacementMouton() == jeu.getPlanteDeplacementMouton()) {
                    leCheminMouton.clear();
                    if (algo.equals("Dijkstra")) {
                        dijkstra(true);
                    } else if (algo.equals("A*")) {
                        aStarSimple(true);
                    } else {
                        aStarComplexeMouton();
                    }
                }
                caseMouton = getCaseMouton();
                this.cases[caseMouton[0]][caseMouton[1]].removeAnimal();
                this.cases[leCheminMouton.get(0)[0]][leCheminMouton.get(0)[1]].setAnimal(new Mouton());
                filePheromonesMouton.add(caseMouton);
                leCheminMouton.remove(0);
            } else {
                if (jeu.getDeplacementLoup() == 3) {
                    leCheminLoup.clear();
                    if (algo.equals("Dijkstra")) {
                        dijkstra(false);
                    } else {
                        aStarSimple(false);
                    }
                }
                int [] caseLoup = getCaseLoup();
                this.cases[caseLoup[0]][caseLoup[1]].removeAnimal();
                this.cases[leCheminLoup.get(0)[0]][leCheminLoup.get(0)[1]].setAnimal(new Loup());
                filePheromonesLoup.add(caseLoup);
                leCheminLoup.remove(0);
            }
        }
        if (!isAnimalPresent("Loup")){
            caseMouton = getCaseMouton();
            System.out.println("le mouton s'est dirigé vers le loup");
            this.cases[caseMouton[0]][caseMouton[1]].removeAnimal();
            this.cases[caseMouton[0]][caseMouton[1]].setAnimal(new Loup());
        }
    }

    public void supprimerPheromones(boolean isAutourDuMouton){
        if (isAutourDuMouton){
            int nbPheromonesASupprimer = fileNbDeplacementMouton.poll();
            for (int i = 0; i < nbPheromonesASupprimer; i++){
                filePheromonesMouton.poll();
            }
        } else {
            for (int i = 0; i < 3; i++){
                filePheromonesLoup.poll();
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

            /*
            Au tour du mouton :
                0% de chance que casesPossibles prenne une case qui est stockée dans caseActuelle
                100% de chance que casesPossibles prenne une case qui n'a ni phéromones loup ni phéromones mouton
                50% de chance que casesPossibles prenne une case qui a des phéromones mouton mais pas de phéromones loup
                35% de chance que casesPossibles prenne une case qui a des phéromones loup mais pas de phéromones mouton
                20% de chance que casesPossibles prenne une case qui a des phéromones loup et mouton

             Au tour du loup :
                0% de chance que casesPossibles prenne une case qui est stockée dans caseActuelle
                100% de chance que casesPossibles prenne une case qui n'a ni phéromones loup ni phéromones mouton
                100% de chance que casesPossibles prenne une case qui a des phéromones mouton mais pas de phéromones loup
                    dont 60% de chance qu'il stocke une deuxième fois dans casesPossibles
                40% de chance que casesPossibles prenne une case qui a des phéromones loup mais pas de phéromones mouton
                100% de chance que casesPossibles prenne une case qui a des phéromones loup et mouton
                    dont 20% de chance qu'il stocke une deuxième fois dans casesPossibles

                Permet de favoriser le déplacement et développer des spécificités pour les animaux
             */

            if (nextI > 0 && nextI < lignes - 1 && nextJ > 0 && nextJ < colonnes - 1){
                int[] caseActuelle = new int[]{nextI, nextJ};
                if (cases[nextI][nextJ].getContenu() instanceof Plante){
                   if (!isPresentDansLaListe(listeCasePasses, caseActuelle)){
                       if (a.getNom().equals("Mouton")){
                           if (!isPresentDansLaListe(filePheromonesMouton, caseActuelle) && !isPresentDansLaListe(filePheromonesLoup, caseActuelle)){
                               casesPossibles.add(caseActuelle);
                           } else if (isPresentDansLaListe(filePheromonesMouton, caseActuelle) && !isPresentDansLaListe(filePheromonesLoup, caseActuelle)){
                               if (Math.random() <= 0.60){
                                   casesPossibles.add(caseActuelle);
                               }
                           } else if (isPresentDansLaListe(filePheromonesLoup, caseActuelle)){
                               if (Math.random() <= 0.50){
                                   casesPossibles.add(caseActuelle);
                               }
                           } else {
                               if (Math.random() <= 0.35){
                                   casesPossibles.add(caseActuelle);
                               }
                           }
                       } else {
                           if (!isPresentDansLaListe(filePheromonesMouton, caseActuelle) && !isPresentDansLaListe(filePheromonesLoup, caseActuelle)){
                               casesPossibles.add(caseActuelle);
                           } else if (isPresentDansLaListe(filePheromonesMouton, caseActuelle) && !isPresentDansLaListe(filePheromonesLoup, caseActuelle)){
                               casesPossibles.add(caseActuelle);
                               if (Math.random() <= 0.60){
                                   casesPossibles.add(caseActuelle);
                               }
                           } else if (isPresentDansLaListe(filePheromonesLoup, caseActuelle)){
                               if (Math.random() <= 0.40){
                                   casesPossibles.add(caseActuelle);
                               }
                           } else {
                               casesPossibles.add(caseActuelle);
                               if (Math.random() <= 0.20){
                                   casesPossibles.add(caseActuelle);
                               }
                           }
                       }
                    }
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
            filePheromonesMouton.add(deplacementChoisi);
        } else {
            casesLoupPassees.add(deplacementChoisi);
            filePheromonesLoup.add(deplacementChoisi);
        }
        return true;
    }


    /*
    Différentes méthodes pour la saé 202
     */

    /**
     * Permet d'initialiser une matrice de poids négatif : -1 si la case est une plante, -2 si la case est une roche
     * @return : retourne une matrice d'entiers
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

    /**
     * Utilise un parcours en largeur
     * Cet algorithme permet de donner des poids croissants à partir d'un point de départ
     * @param position0 : point de départ
     * @param casesNumero : matrice d'entiers à donner des poids croissants
     * @return : retourne une matrice d'entiers
     */

    public int[][] donnerPoidsCase(int[] position0, int[][] casesNumero){

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


        return casesNumero;
    }

    /**
     * Cette méthode permet de calculer la distance de Manhanttan entre le loup et le mouton
     * Si la distance est inférieure ou égale à 5 renvoie vrai, sinon renvoie faux.
     */

    public boolean manhattan() {
        int[][] poids = donnerPoidsCase(getCaseMouton(), initialiserMatricePoids());

        int[] caseLoup = getCaseLoup();

        //return poids[caseLoup[0]][caseLoup[1]] <= 5;

        if (poids[caseLoup[0]][caseLoup[1]] > 5){
            return false;
        }

        int[] caseMouton = getCaseMouton();


        //Pour la vision j'utilise l'algoritme de tracé de segment de Bresenham :
        // https://fr.wikipedia.org/wiki/Algorithme_de_trac%C3%A9_de_segment_de_Bresenham
        //Permet de voir si le loup voit le mouton ou inversement. Si une des cases stockées est
        //Une roche, la vision est obstruée donc renvoie faux, sinon renvoie vrai

        //Calcule la différence entre les coordonnées du loup et du mouton
        double dx = caseLoup[0] - caseMouton[0];
        double dy = caseLoup[1] - caseMouton[1];
        //Calcul du nombre d'itérations
        double pas = Math.max(Math.abs(dx), Math.abs(dy));

        //Permet incrémenter x et y
        double xIncrementation = dx / pas;
        double yIncrementation = dy / pas;

        ArrayList<int[]> listeCasePassantParDroite = new ArrayList<>();

        double x = caseMouton[0];
        double y = caseMouton[1];
        
        //Boucle d'itération
        for (int i = 0; i < pas; i++) {
            int posI = (int) Math.round(x);
            int posJ = (int) Math.round(y);
            //Si parmi une itération est une case roche : renvoie faux
            if (getCase(posI, posJ).getContenu() instanceof Roche){
                return false;
            }

            x+= xIncrementation;
            y+= yIncrementation;
        }

        return true;
    }

    /**
     * Méthode privée permettant de visualiser la distance des cases par rapport à la position du mouton en
     * distance de Manhattan
     * -1 représente les cases non visitées, -2 représente les roches
     */

    private void printNumeroCase(int[][] plateau){
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                String numero = String.format("%2d", plateau[i][j]); //Permet de décaler s'il y a un seul caractère
                System.out.print(numero + "\t");
            }
            System.out.println();
        }
    }


    /**
     * Reprend la base de l'algorithme de Dijsktra sauf qu'au mieux de partir d'un point 0 à un point x
     * avec des poids croissants, nous faisons l'inverse à partir d'un point x pour aller vers un point 0
     * avec des poids décroissants
     * @param isAuTourMouton : si c'est bien au tour du mouton
     */



    public void dijkstra(boolean isAuTourMouton){
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

        poids = donnerPoidsCase(posArrivee, initialiserMatricePoids());

        printNumeroCase(poids);

        // Création d'une file de priorité (parcours en largeur)
        Queue<int[]> queue = new ArrayDeque<>();
        // Ajout de la position mouton
        queue.offer(posDepart);

        //List<int[]> leChemin = new ArrayList<>();

        int poidsActuel = poids[posDepart[0]][posDepart[1]];

        poids[posDepart[0]][posDepart[1]] = -1;



        while (!queue.isEmpty()) {
            int[] posActuelle = queue.poll(); // Récupération et suppression de la première valeur de la file
            int I = posActuelle[0]; // Ligne de la position actuelle
            int J = posActuelle[1]; // Colonne de la position actuelle

            // Vérification des cases voisines
            int[] dI = {-1, 1, 0, 0}; // Déplacements en ligne (haut, bas)
            int[] dJ = {0, 0, -1, 1}; // Déplacements en colonne (gauche, droite)


            int plusPetitPoids = Integer.MAX_VALUE;
            int[] caseAPasser = new int[2];


            for (int k = 0; k < 4; k++) {
                int nextI = I + dI[k];
                int nextJ = J + dJ[k];

                // Vérification des limites de la grille
                if (nextI >= 0 && nextI < lignes && nextJ >= 0 && nextJ < colonnes && poids[nextI][nextJ] >= 0) {
                    // Mise à jour de la distance si elle est plus courte
                    if (poidsActuel > poids[nextI][nextJ] && poids[nextI][nextJ] >= 0) {
                        if (poids[nextI][nextJ] <= plusPetitPoids){
                            caseAPasser = new int[]{nextI, nextJ};
                            plusPetitPoids = poids[nextI][nextJ];
                        }
                    }
                }
            }

            if (isAuTourMouton) {
                leCheminMouton.add(caseAPasser);
            } else {
                leCheminLoup.add(caseAPasser);
            }
            queue.offer(caseAPasser);
            poids[caseAPasser[0]][caseAPasser[1]] = -1;

            if (Arrays.equals(posArrivee, caseAPasser)){
                break;
            }
        }
        System.out.println("je suis Dijkstra");

    }



    /**
     * Reprend exactement le même principe que l'algorithme de Dijkstra sauf qu'en plus les cases
     * ayant les petits poids on prend la case ayant la plus petite distance par rapport au point d'arrivé
     * @param isAuTourMouton
     */


    public void aStarSimple(boolean isAuTourMouton){
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

        poids = donnerPoidsCase(posArrivee, initialiserMatricePoids());

        //printNumeroCase(poids);

        // Création d'une file de priorité (parcours en largeur)
        Queue<int[]> file = new ArrayDeque<>();
        // Ajout de la position mouton
        file.offer(posDepart);

        //List<int[]> leChemin = new ArrayList<>();



        int poidsActuel = poids[posDepart[0]][posDepart[1]];

        poids[posDepart[0]][posDepart[1]] = -1;



        while (!file.isEmpty()) {
            System.out.println("A* Simple");
            int[] posActuelle = file.poll(); // Récupération et suppression de la première valeur de la file
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

             //Permet de gérer les cas rare où casesPossible récupère aucune case non visitée
             if (!casesPossibles.isEmpty())   {
                // pour vérifier la distance
                double plusCourteDistance = Double.MAX_VALUE;
                int[] positionLaPlusCourte = new int[2];

                for (int[] c : casesPossibles) {
                    double distance = distanceAStar(c[0], c[1], posArrivee[0], posArrivee[1]);
                    if (plusCourteDistance > distance) {
                        plusCourteDistance = distance;
                        positionLaPlusCourte = c;
                    }
                }

                if (isAuTourMouton) {
                    leCheminMouton.add(positionLaPlusCourte);
                } else {
                    leCheminLoup.add(positionLaPlusCourte);
                }
                file.offer(positionLaPlusCourte);


                //Permet de marquer la case comme visitée
                poids[positionLaPlusCourte[0]][positionLaPlusCourte[1]] = -1;

                if (Arrays.equals(posArrivee, positionLaPlusCourte)) {
                    break;
                }
            }
        }
        System.out.println("je suis l'algo A*");
    }

    /**
     * Cet algorithme reprend le principe de aStarSimple mais qu'en plus on a rajouté des conditions supplémentaires :
     * D'abord on rend les cases aux alentours du loup inaccessibles. Si le loup si situe à côté de la case d'arrivée,
     * le mouton prend la direction opposée
     * Si le nombre de déplacements du mouton est égal à 0, le mouton priorise à aller sur les cases Marguerites s'il en a.
     * Si c'est le cas, il peut exceptionnellement revenir en arrière.
     * Si aucun déplacement n'est possible sauf à aller vers le loup, le mouton est condamné. Il va prendre le chemin créé
     * par aStarSimple et donc va se diriger vers le loup
     * Cet algorithme ne concerne que le mouton
     */




    public void aStarComplexeMouton(){
        int[][] poids;
        int[] posDepart;
        int[] posArrivee;
        int[] posLoup;
        int nombreDeplacement;

        posDepart = getCaseMouton();
        posArrivee = getCaseSortie();
        nombreDeplacement = jeu.getDeplacementMouton();


        poids = initialiserMatricePoids();

        posLoup = getCaseLoup();

        poids[posLoup[0]][posLoup[1]]= -3; //Rend la case inaccessible

        // Déplacement de tous les côtés d'une case
        int[] depI = {-1, 1, 0, 0, -1, -1, 1, 1}; // Déplacements en ligne (haut, bas)
        int[] depJ = {0, 0, -1, 1, -1, 1, -1, 1}; // Déplacements en colonne (gauche, droite)

        boolean loupACoteDeLaSortie = false;

        // Parcourir les cases adjacentes au loup
        for (int k = 0; k < 8; k++) {
            int iActuel = posLoup[0] + depI[k];
            int jActuel = posLoup[1] + depJ[k];

            // Vérification des limites de la grille
            if (iActuel >= 0 && iActuel < lignes && jActuel >= 0 && jActuel < colonnes ) {
                if (poids[iActuel][jActuel] == - 1 && !Arrays.equals(posLoup, new int[]{iActuel, jActuel}) &&
                !Arrays.equals(posDepart, new int[]{iActuel, jActuel})) {
                    poids[iActuel][jActuel] = -3; //Rend les cases inaccessibles
                }
                //Vérification que le loup n'est pas à l'opposé de la sortie
                if (Arrays.equals(new int[]{iActuel, jActuel}, posArrivee)){
                    loupACoteDeLaSortie = true;
                }
            }
        }

        //Le loup est à côté de la case de sortie
        if (loupACoteDeLaSortie){
            //Le mouton cherche à aller vers l'opposé de la sortie
            posArrivee = new int[]{lignes - posLoup[0], colonnes - posLoup[1]};
        }

        donnerPoidsCase(caseSortie, poids);


        //printNumeroCase(poids);

        // Création d'une file de priorité (parcours en largeur)
        Queue<int[]> queue = new ArrayDeque<>();
        // Ajout de la position mouton
        queue.offer(posDepart);



        int poidsActuel = poids[posDepart[0]][posDepart[1]];
        System.out.println(poidsActuel);

        poids[posDepart[0]][posDepart[1]] = -1;


        while (!queue.isEmpty()) {
            System.out.println("A* Complexe");
            int[] posActuelle = queue.poll(); // Récupération et suppression de la première valeur de la file
            int I = posActuelle[0]; // Ligne de la position actuelle
            int J = posActuelle[1]; // Colonne de la position actuelle

            // Vérification des cases voisines
            int[] dI = {-1, 1, 0, 0}; // Déplacements en ligne (haut, bas)
            int[] dJ = {0, 0, -1, 1}; // Déplacements en colonne (gauche, droite)

            List<int[]> casesPossibles = new ArrayList<>();
            List<int[]> casesMargueritesTour0 = new ArrayList<>();

            for (int k = 0; k < 4; k++) {
                int nextI = I + dI[k];
                int nextJ = J + dJ[k];

                // Vérification des limites de la grille
                if (nextI >= 0 && nextI < lignes && nextJ >= 0 && nextJ < colonnes) {
                    if (poidsActuel > poids[nextI][nextJ] && poids[nextI][nextJ] >= 0) {
                        casesPossibles.add(new int[]{nextI, nextJ});
                    }
                    //Si le nombre de tour restant du mouton = 0 ET que la case est une marguerite,
                    //peut exceptionnellement revenir en arrière
                     if (nombreDeplacement == 0) {
                        if (poids[nextI][nextJ] >= -1) {
                            if (getCase(nextI, nextJ).getContenu() instanceof Marguerite) {
                                casesMargueritesTour0.add(new int[]{nextI, nextJ});
                            }
                        }
                    }
                }
            }

            //Uniquement au tour 0 du mouton && qu'il y a au moins une case marguerite aux alentours du mouton
            if (!casesMargueritesTour0.isEmpty()){
                casesPossibles.clear();
                casesPossibles = casesMargueritesTour0;
                casesMargueritesTour0.clear();
            }

            //Cas rare où le mouton est dans tous les cas condamnés
            if (casesPossibles.isEmpty() && leCheminMouton.isEmpty()) {
                //Dans le cas où il n'y a plus aucune possiblité
                System.out.println("Je rentre dans A* simple");

                aStarSimple(true);
            } else if (casesPossibles.isEmpty()){ //Cas où il n'y a plus d'autre chemin possible
                break;
            }

            //Maintenant prend le chemin le faible en poids ET en distance
            double plusCourteDistance = Double.MAX_VALUE;
            int[] positionLaPlusCourte = new int[2];

            for (int[] c : casesPossibles) {
                double distance = distanceAStar(c[0], c[1], posArrivee[0], posArrivee[1]);
                if (plusCourteDistance > distance) {
                    plusCourteDistance = distance;
                    positionLaPlusCourte = c;
                }
            }

            leCheminMouton.add(positionLaPlusCourte);
            queue.offer(positionLaPlusCourte);

            //Permet de marquer la case comme visitée
            poids[positionLaPlusCourte[0]][positionLaPlusCourte[1]] = -1;

            nombreDeplacement--;

            if (Arrays.equals(posArrivee, positionLaPlusCourte)) {
                break;
            }


        }

        System.out.println("je suis l'algo A* Complexe");

    }


    /**
     * Permet de calculer une distance entre 2 points dans un plan 2D avec la formule :
     * sqrt( (x0 - x1)² + (y0 - y1)² )
     * @param iDepart : y0
     * @param jDepart : x0
     * @param iArrivee : y1
     * @param jArrivee : x1
     * @return : retourne une distance
     */


    public double distanceAStar(int iDepart, int jDepart, int iArrivee, int jArrivee){
        return Math.sqrt(Math.pow((iDepart - iArrivee), 2) + Math.pow((jDepart - jArrivee), 2));
    }




















    /////////////////////////////////////////////////////////////////////////
    ////////////////////// METHODES NON UTILISEES ///////////////////////////
    /////////////////////////////////////////////////////////////////////////







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



}
