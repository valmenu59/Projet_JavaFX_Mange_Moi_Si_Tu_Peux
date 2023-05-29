package test;

import demarrage.Intro;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import jeu.*;
import menu.Menu;
import sae.saejavafx.HelloApplication;
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
        plateau.setCaseSortie(0,1); //initilisation de la case de sortie
        plateau.getCase(0,1).setContenuPlante(new Herbe()); //permet de remplacer la case roche par herbe
        assertTrue(plateau.verifPlateauCorrect(),"Le labyrinthe créé est bien parfait");
    }

    //Pour tester si l'algo verifPlateauCorrect affiche faux si c'est un labyrinthe imparfait
    @Test
    void testMethodeLabyrintheImparfait(){
        plateau.setCaseSortie(0,1); //initilisation de la case de sortie
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
        assertTrue(plateau.presentDansLaListe(arr,new int[]{-4,12}),"Cette méthode est fonctionnelle");
    }

    @Test
    void testMethodePresentDansLaListe2(){
        ArrayList<int[]> arr = new ArrayList<>();
        arr.add(new int[]{1,34});
        arr.add(new int[]{-4,12});
        arr.add(new int[]{3,3});
        assertFalse(plateau.presentDansLaListe(arr,new int[]{-10,12}),"Cette méthode est fonctionnelle");
    }









}