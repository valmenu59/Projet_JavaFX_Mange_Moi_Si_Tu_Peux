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

import static org.junit.jupiter.api.Assertions.*;
class JeuTest {

    protected Plateau plateau;
    protected HelloApplication hello;
    protected Intro introductionJeu;
    protected Menu menuJeu;
    private Scene mainscene;
    private AnchorPane panneau;



    @BeforeEach
    void setUp() {
        plateau = new Plateau(12,9);
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





}