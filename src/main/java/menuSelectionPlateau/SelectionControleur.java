package menuSelectionPlateau;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jeu.AffichageJeu;
import menu.Menu;

import java.io.File;

public class SelectionControleur {

    public SelectionControleur() {
    }

    public void retourMenu(Button bouton, Stage stage) {
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Menu menu = new Menu(stage);
                stage.setScene(menu);
            }
        });
    }

    public void ouvrirExplorateur(Button b,Stage stage){
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser explorateur = new FileChooser();
                explorateur.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("SAE *.sae", "*.sae"));
                File fichier = explorateur.showOpenDialog(stage);
                if (fichier != null){
                    AffichageJeu affichageJeu = new AffichageJeu(stage,fichier.getAbsolutePath());
                    stage.setScene(affichageJeu);
                }


            }
        });
    }



}