package menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jeu.AffichageJeu;
import menuSelectionPlateau.MenuSelectionAffichage;
import parametres.Parametres;

import java.io.File;

public class MenuControleur {


    public MenuControleur(){}


    /**
     * @param bouton : le bouton à donner l'action
     * @param stage : la classe à afficher via une fenêtre
     * Méthode qui permet dans le jeu afin de créer un plateau
     */

    public void demarrerJeu(Button bouton, Stage stage){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AffichageJeu affichageJeu = new AffichageJeu(stage,false);
                stage.setScene(affichageJeu);
            }
        });
    }

    /**
     * @param bouton : le bouton à donner l'action
     * @param stage : la classe à afficher via une fenêtre
     * Méthode qui permet de démarrer la simulation via la dernière sauvegarde
     */

    public void demarrerJeuViaSauvegarde(Button bouton, Stage stage){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AffichageJeu affichageJeu = new AffichageJeu(stage,true);
                stage.setScene(affichageJeu);
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
                explorateur.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("BINAIRE SAE *.sae", "*.sae"),
                        new FileChooser.ExtensionFilter("TEXTE TXT *.txt", "*.txt"));
                //Ouvre l'explorateur de fichiers
                File fichier = explorateur.showOpenDialog(stage);
                //Si l'utilisateur a ouvert un fichier
                if (fichier != null){
                    //Affiche visuellement le plateau
                    AffichageJeu affichageJeu = new AffichageJeu(stage,fichier.getAbsolutePath(), false);
                    stage.setScene(affichageJeu);
                }


            }
        });
    }

    /**
     * @param b : le bouton à donner l'action
     * @param stage : la classe à afficher via une fenêtre
     * Méthode qui permet de fermer le jeu
     */

    public void quitterJeu(Button b, Stage stage){
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
    }




}
