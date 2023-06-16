package test;

import jeu.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
class JeuTest {

    protected Plateau plateau;



    @BeforeEach
    void setUp() {
        plateau = new Plateau(12,9);
        plateau.creerPlateau();
    }

    @AfterEach
    void tearDown() {
        plateau = null;
    }

    /*
    Le but des méthodes Test testPlateauJeu est de vérifier si sur les extrémités il contient bien de la roche et qu'à l'intérieur de l'herbe
     */

    @Test
    void testPlateauJeu (){
        assertEquals(new Roche().getNom(), plateau.getCase(0,0).getContenu().getNom(), "Cette case contient bien de la roche");

    }

    @Test
    void testPlateauJeu2(){
        assertEquals(new Herbe().getNom(), plateau.getCase(1,1).getContenu().getNom(), "Cette case contient bien de l'herbe");
    }

    @Test
    void testPlateauJeu3(){
        assertEquals(new Roche().getNom(), plateau.getCase(plateau.getLignes()-1,plateau.getColonnes()-1).getContenu().getNom(), "Cette case contient bien de la roche");
    }

    //Pour tester si l'algo verifPlateauCorrect affiche bien vrai si c'est un labyrinthe parfait
    @Test
    void testMethodeLabyrintheParfait(){
        plateau.getCase(0,1).setContenuPlante(new Herbe()); //permet de remplacer la case roche par herbe
        assertTrue(plateau.verifPlateauCorrect(),"Le labyrinthe créé est bien parfait");
    }

    //Pour tester si l'algo verifPlateauCorrect affiche faux si c'est un labyrinthe imparfait
    @Test
    void testMethodeLabyrintheImparfait(){
        plateau.getCase(0,1).setContenuPlante(new Herbe()); //permet de remplacer la case roche par herbe
        //on veut que la case[2][2] soit entourée de roche
        plateau.getCase(3,2).setContenuGeneral(new Roche());
        plateau.getCase(1,2).setContenuGeneral(new Roche());
        plateau.getCase(2,1).setContenuGeneral(new Roche());
        plateau.getCase(2,3).setContenuGeneral(new Roche());
        assertFalse(plateau.verifPlateauCorrect(),"Le labyrinthe créé est bien imparfait");
    }


    //on teste que l'algo de labyrinthe créé est parfait
    @Test
    void testAlgoLabyrinthe(){
        plateau.creerLabyrinthe();
        assertTrue(plateau.verifPlateauCorrect(),"L'algorithme est bien fonctionnel");
    }

    //Pour vérifier que la méthode presentDansLaListe fonctionne bien
    @Test
    void testMethodePresentDansLaListe1(){
        ArrayList<int[]> arr = new ArrayList<>();
        arr.add(new int[]{1,34});
        arr.add(new int[]{-4,12});
        arr.add(new int[]{3,3});
        assertTrue(plateau.isPresentDansLaListe(arr,new int[]{-4,12}),"Cette méthode est fonctionnelle");
    }

    @Test
    void testMethodePresentDansLaListe2(){
        ArrayList<int[]> arr = new ArrayList<>();
        arr.add(new int[]{1,34});
        arr.add(new int[]{-4,12});
        arr.add(new int[]{3,3});
        assertFalse(plateau.isPresentDansLaListe(arr,new int[]{-10,12}),"Cette méthode est fonctionnelle");
    }




    @Test
    void testVerifPresenceAnimal(){
        plateau.getCase(2,2).setAnimal(new Loup());
        assertFalse(plateau.isAnimalPresent("Mouton"), "Le plateau ne contient pas de mouton");
    }

    @Test
    void testVerifPresenceAnimal2(){
        plateau.getCase(2,2).setAnimal(new Mouton());
        assertTrue(plateau.isAnimalPresent("Mouton"), "Le plateau ne contient pas de mouton");
    }

    @Test
    void testVerifPresenceAnimal3(){
        plateau.getCase(2,2).setAnimal(new Mouton());
        plateau.getCase(2,4).setAnimal(new Mouton());
        assertFalse(plateau.isAnimalPresent("Mouton"), "Le plateau contient trop de moutons");
    }

    @Test
    void testMoutonMangePlante(){
        int i,j;
        //Remplacement de toutes les cases herbes par une case marguerite
        for (i = 0; i < plateau.getLignes(); i++){
            for (j = 0; j < plateau.getColonnes(); j++){
                if (plateau.getCase(i,j).getContenu() instanceof Herbe){
                    plateau.getCase(i,j).setContenuPlante(new Marguerite());
                }
            }
        }
        //ajout du mouton
        plateau.getCase(3,3).setAnimal(new Mouton());
        assertEquals(4,plateau.moutonMangePlante(3,3),"Le mouton a bien mangé " +
                "une marguerite et donc renvoie la valeur 4");
    }

    @Test
    void testMoutonMangePlante2(){
        int i,j;
        //Remplacement de toutes les cases herbes par une case cactus
        for (i = 0; i < plateau.getLignes(); i++){
            for (j = 0; j < plateau.getColonnes(); j++){
                if (plateau.getCase(i,j).getContenu() instanceof Herbe){
                    plateau.getCase(i,j).setContenuPlante(new Cactus());
                }
            }
        }
        //ajout du mouton
        plateau.getCase(3,3).setAnimal(new Mouton());
        assertNotEquals(4,plateau.moutonMangePlante(3,3),"Le mouton a bien mangé " +
                "une marguerite et donc renvoie la valeur 4");
    }


    @Test
    void testerManhanttan1(){
        int i, j;
        plateau.getCase(3,3).setContenuGeneral(new Roche());
        plateau.getCase(3,2 ).setAnimal(new Mouton());
        plateau.getCase(3, 5).setAnimal(new Loup());
        assertFalse(plateau.manhattan(), "La distance de Manhanttan entre le loup et le mouton est " +
                "égal à 5 donc doit renvoyer mais il y a une roche devant donc renvoie faux");
    }

    @Test
    void testerManhanttan2(){
        plateau.getCase(3,3).setContenuGeneral(new Roche());
        plateau.getCase(3,2 ).setAnimal(new Mouton());
        plateau.getCase(3, 6).setAnimal(new Loup());
        assertFalse(plateau.manhattan(), "La distance de Manhanttan entre le loup et le mouton est " +
                "égal à 6 donc doit renvoyer faux");
    }

    @Test
    void testerManhanttan3(){
        plateau.getCase(3,3).setContenuGeneral(new Roche());
        plateau.getCase(3,2 ).setAnimal(new Mouton());
        plateau.getCase(7, 3).setAnimal(new Loup());
        assertTrue(plateau.manhattan(), "La distance de Manhanttan entre le loup et le mouton est " +
                "égal à 5 mais il y a aucun obstacle roche");
    }


    @Test
    void testerPoidsCase1(){
        plateau.getCase(0,1).setContenuGeneral(new Herbe()); //créée une case de sortie
        plateau.getCase(1, 2).setContenuGeneral(new Roche());
        int [][] poids = plateau.initialiserMatricePoids();
        plateau.donnerPoidsCase(plateau.getCaseSortie(), poids);
        assertEquals(5, poids[1][3], "Le poids de cette case est bien égale à 5 (test parcours" +
                "en largeur");
    }

    @Test
    void testerCasesTerreQuiPousse(){
        plateau.getCase(1,1).setContenuPlante(new Terre());
        plateau.planteQuiPousse();
        plateau.planteQuiPousse();
        plateau.planteQuiPousse();
        assertFalse(plateau.getCase(1,1).getContenu() instanceof Terre, "Cette case n'est plus de type" +
                "Terre mais est devenue l'une des 3 plantes car on a passé plus de 2 tours");
    }

    @Test
    void testerMethodeSupprimerObstacle1(){
        plateau.getCase(3,3).setContenuGeneral(new Roche());
        plateau.supprimerObstacle(3,2,'E');
        assertFalse(plateau.getCase(3,3).getContenu() instanceof Roche, "La méthode a bien supprimé " +
                "La case roche à l'est");
    }

    @Test
    void testerMethodeSupprimerObstacle2(){
        plateau.getCase(3,3).setContenuGeneral(new Roche());
        plateau.supprimerObstacle(2,3,'S');
        assertFalse(plateau.getCase(3,3).getContenu() instanceof Roche, "La méthode a bien supprimé " +
                "La case roche au sud");
    }


}