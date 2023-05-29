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

    private BorderPane panneau;
    private static final String IMAGE_INTRO = "/UPHF_logo.svg.png";

    public Intro(Stage stage) {
        super(new BorderPane(),1280,720);
        panneau = (BorderPane) getRoot();
        ImageView imageView = (new ImageJeu("/UPHF_logo.svg.png", this.getWidth() / 1.5,
                this.getHeight() / 1.5)).afficherImage();
        panneau.setCenter(imageView);
        Timer timer = new Timer();

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
