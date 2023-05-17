package demarrage;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.*;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import menu.Menu;


public class Intro extends Scene {

    //private Scene mainScene;
    private AnchorPane panneau;

    private final static String IMAGE_INTRO = "/UPHF_logo.svg.png";

    public Intro (Stage stage) {
        super(new AnchorPane(),1280, 720 );
        panneau = (AnchorPane) getRoot();
        //panneau = new AnchorPane();
        //mainStage = new Stage();
        //mainStage.setResizable(true);
        //mainStage.setScene(mainScene);

        Image imageIntro = new  Image(getClass().getResource(IMAGE_INTRO).toExternalForm(),
                getWidth()/1.5, getHeight()/1.5, true,false);
        ImageView imageView = new ImageView(imageIntro);
        panneau.getChildren().add(imageView);


        imageView.setLayoutX((panneau.getWidth() - imageView.getBoundsInParent().getWidth()) / 2);
        imageView.setLayoutY((panneau.getHeight() - imageView.getBoundsInParent().getHeight()) / 2);

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Menu menu = new Menu(stage);
                    stage.setScene(menu);
                });
            }
        }, 2000);

        //mainStage.show();
    }

/*
    public Stage getMainStage() {
        return mainStage;
    }

 */

    /*
    public Scene getMainScene(){
        return mainScene;
    }

     */

    public AnchorPane getPanneau(){
        return this.panneau;
    }

}
