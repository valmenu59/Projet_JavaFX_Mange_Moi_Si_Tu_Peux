package menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jeu.AffichageJeu;
import menuSelectionPlateau.MenuSelectionAffichage;
import parametres.Parametres;

public class MenuControleur {


    public MenuControleur(){}

    /**
     * @param bouton : le bouton à donner l'action
     * @param stage : la classe à afficher via une fenêtre
     * Méthode qui permet d'aller dans les paramètres
     */

    public void parametreJeu(Button bouton, Stage stage){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parametres param = new Parametres(stage);
                stage.setScene(param);

            }
        });
    }

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
     * @param bouton : le bouton à donner l'action
     * @param stage : la classe à afficher via une fenêtre
     * Méthode qui permet dans le menu de sélection de plateaux préétablis ou d'ouvrir l'explorateur
     * de fichier afin de sélection un plateau déjà créé
     */

    public void demarrerMenuSelection(Button bouton, Stage stage){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                MenuSelectionAffichage menuSelection = new MenuSelectionAffichage(stage);
                stage.setScene(menuSelection);
            }
        });
    }




}
