package test;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import jeu.*;
import sae.saejavafx.HelloApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class JeuTest {

    protected Jeu jeu;
    protected HelloApplication hello;
    protected Intro introductionJeu;
    protected Menu menuJeu;
    private Scene mainscene;
    private AnchorPane panneau;
    protected Plateau plateau;



    @BeforeEach
    void setUp() {
        jeu = new Jeu(12,9);
    }

    @AfterEach
    void tearDown() {
        jeu = null;
    }

    /*
    Le but des méthodes Test testPlateauJeu est de vérifier si sur les extrémités il contient bien de la roche et qu'à l'intérieur de l'herbe
     */

    @Test
    void testPlateauJeu (){
        assertEquals(new Roche().getNom(), jeu.getPlateau().getCase(0,0).getContenu().getNom(), "Cette case contient bien de la roche");

    }

    @Test
    void testPlateauJeu2(){
        assertEquals(new Herbe().getNom(), jeu.getPlateau().getCase(1,1).getContenu().getNom(), "Cette case contient bien de l'herbe");
    }

    @Test
    void testPlateauJeu3(){
        assertEquals(new Roche().getNom(), jeu.getPlateau().getCase(jeu.getPlateau().getLignes()-1,jeu.getPlateau().getColonnes()-1).getContenu().getNom(), "Cette case contient bien de la roche");
    }





}