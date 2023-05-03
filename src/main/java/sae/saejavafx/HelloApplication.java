package sae.saejavafx;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import jeu.Intro;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Intro intro = new Intro();
        Stage fenetre = intro.getMainStage();
        fenetre.setTitle("Jeu 2048");
        fenetre.show();
    }

    public static void main(String[] args) {
        launch();
    }
}