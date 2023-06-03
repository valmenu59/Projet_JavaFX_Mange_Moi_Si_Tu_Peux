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

    /**
     * @param bouton : le bouton à donner l'action
     * @param stage : la classe à afficher via une fenêtre
     * Méthode qui permet d'aller au menu
     */

    public void retourMenu(Button bouton, Stage stage) {
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Menu menu = new Menu(stage);
                stage.setScene(menu);
            }
        });
    }

    /**
     * @param b : le bouton à donner l'action
     * @param stage : la classe à afficher via une fenêtre
     * Méthode qui permet d'ouvrir l'explorateur de fichiers afin de sélectionner un fichier
     * binaire de type .sae pour créer un plateau à partir de la sauvegarde
     */

    public void ouvrirExplorateur(Button b,Stage stage){
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser explorateur = new FileChooser();
                //Permet de filtrer la sélection de fichier
                explorateur.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("SAE *.sae", "*.sae"));
                //Ouvre l'explorateur de fichiers
                File fichier = explorateur.showOpenDialog(stage);
                //Si l'utilisateur a ouvert un fichier
                if (fichier != null){
                    //Affiche visuellement le plateau
                    AffichageJeu affichageJeu = new AffichageJeu(stage,fichier.getAbsolutePath());
                    stage.setScene(affichageJeu);
                }


            }
        });
    }



}