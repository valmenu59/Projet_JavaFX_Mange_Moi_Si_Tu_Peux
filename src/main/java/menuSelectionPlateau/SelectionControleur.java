package menuSelectionPlateau;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import jeu.AffichageJeu;
import menu.Menu;

import java.io.File;

public class SelectionControleur {

    public SelectionControleur() {
    }


    public void ouvrirSauvegardeJeu(Label l, String url, Stage stage){
        l.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                AffichageJeu affichageJeu = new AffichageJeu(stage,url, true);
                stage.setScene(affichageJeu);
            }
        });
    }



}