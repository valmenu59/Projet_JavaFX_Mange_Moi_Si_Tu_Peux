package jeu;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.*;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;


public class Intro {

    private Scene mainScene;
    private Stage mainStage;
    private AnchorPane panneau;



    private final static String IMAGE_INTRO = "/UPHF_logo.svg.png";

    public Intro() throws FileNotFoundException {
        panneau = new AnchorPane();
        mainScene = new Scene(panneau, 1280, 720 );
        mainStage = new Stage();
        mainStage.setResizable(true);
        mainStage.setScene(mainScene);

        Image imageIntro = new  Image(getClass().getResource(IMAGE_INTRO).toExternalForm(),
                mainScene.getWidth()/1.5,mainScene.getHeight()/1.5,
                true,false);
        ImageView imageView = new ImageView(imageIntro);
        panneau.getChildren().add(imageView);


        imageView.setLayoutX((panneau.getWidth() - imageView.getBoundsInParent().getWidth()) / 2);
        imageView.setLayoutY((panneau.getHeight() - imageView.getBoundsInParent().getHeight()) / 2);

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Menu menu = new Menu(panneau, mainScene);
                    menu.getMainStage();
                });
            }
        }, 2000);

        mainStage.show();
    }


    public Stage getMainStage() {
        return mainStage;
    }

}
