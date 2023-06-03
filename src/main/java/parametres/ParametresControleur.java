package parametres;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import menu.Menu;
import sauvegarde.DossierSauvegarde;

public class ParametresControleur {

    private final DossierSauvegarde sauvegarde = new DossierSauvegarde();


    public ParametresControleur(){}

    /**
     * @param bouton : le bouton à donner l'action
     * @param stage : la classe à afficher via une fenêtre
     * Méthode qui permet d'aller au menu
     */

    public void allerAuMenu(Button bouton, Stage stage){
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
     * Méthode qui permet d'effacer la sauvegarde à partir d'une source spécifique (s'il y en a une)
     */


    public void supprimerSauvegarde(Button b) {
        b.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                boolean resu = sauvegarde.removeSauvegarde(); //Supprime la sauvegarde
                Alert alerte = new Alert(Alert.AlertType.INFORMATION); //Donne une alerte
                alerte.setTitle("INFORMATION");
                alerte.setContentText("");
                if (resu) {
                    //En cas de réussite
                    alerte.setHeaderText("Sauvegarde supprimée avec succès");
                    b.setDisable(true); //Désactive le bouton
                } else {
                    //En cas d'échec
                    alerte.setHeaderText("Echec : sauvegarde non supprimée");
                }

                alerte.show(); //Affiche l'alerte
            }
        });
    }
}
