package sae.saejavafx;

import demarrage.Intro;
import fx.ImageJeu;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {

        Intro intro = new Intro(stage);
        stage.setTitle("MANGE-MOI SI TU PEUX !");
        stage.setResizable(false);
        //Permet de rajouter des icônes
        stage.getIcons().setAll(new Image(getClass().getResource("/Logos/icones/16.png").toExternalForm()),
                new Image(getClass().getResource("/Logos/icones/24.png").toExternalForm()),
                new Image(getClass().getResource("/Logos/icones/32.png").toExternalForm()),
                new Image(getClass().getResource("/Logos/icones/48.png").toExternalForm()),
                new Image(getClass().getResource("/Logos/icones/64.png").toExternalForm()),
                new Image(getClass().getResource("/Logos/icones/128.png").toExternalForm()),
                new Image(getClass().getResource("/Logos/icones/256.png").toExternalForm()));
        stage.show();
        stage.setScene(intro);



    }

    public static void main(String[] args) {
        launch();
    }
}