package demarrage;

import fx.ImageJeu;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.image.*;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import menu.Menu;


public class Intro extends Scene {

    private final BorderPane panneau;
    private static final String IMAGE_INTRO = "/Logos/UPHF_logo.svg.png";

    public Intro(Stage stage) {
        //Création de la scène
        super(new BorderPane(),1280,720);
        panneau = (BorderPane) getRoot(); //Récupération borderPane
        ImageView imageView = new ImageJeu(getClass().getResource(IMAGE_INTRO).toExternalForm(),
                this.getWidth() / 1.5, this.getHeight() / 1.5).afficherImage(); // création de l'image au format choisi

        panneau.setCenter(imageView); //Mettre l'image au centre
        Timer timer = new Timer(); //Mise en place d'un chronomètre

        //Permet d'aller au menu au bout de 2 secondes
        timer.schedule(new TimerTask() {
            @Override
            public void run () {
                Platform.runLater(() -> {
                    Menu menu = new Menu(stage);
                    stage.setScene(menu);
                });
            }
        },2000);
    }


}
