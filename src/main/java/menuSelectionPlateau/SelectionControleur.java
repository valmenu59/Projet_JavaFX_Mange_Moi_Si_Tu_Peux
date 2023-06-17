/*
 * Copyright (c)  @link https://github.com/valmenu59/SAE_JavaFX @link https://github.com/valmenu59
 * CC BY-NC - Attribution - Partage dans les mêmes conditions - Pas d'utilisation commerciale
 */

package menuSelectionPlateau;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import jeu.AffichageJeu;

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