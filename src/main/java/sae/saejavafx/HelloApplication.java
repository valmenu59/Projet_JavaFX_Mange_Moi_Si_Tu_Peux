package sae.saejavafx;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import jeu.Intro;
import jeu.Jeu;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Intro intro = new Intro();
        Stage fenetre = intro.getMainStage();
        fenetre.setTitle("LOUP ET MOUTON");
        fenetre.show();
    }

    public static void main(String[] args) {
        launch();
    }
}