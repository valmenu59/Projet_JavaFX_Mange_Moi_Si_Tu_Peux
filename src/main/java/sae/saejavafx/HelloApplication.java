package sae.saejavafx;

import demarrage.Intro;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Intro intro = new Intro(stage);
        stage.setTitle("MANGE-MOI SI TU PEUX !");
        stage.setResizable(false);
        stage.show();
        stage.setScene(intro);



    }

    public static void main(String[] args) {
        launch();
    }
}