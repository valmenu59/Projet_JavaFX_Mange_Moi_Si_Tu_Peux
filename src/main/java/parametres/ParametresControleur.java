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

    public void demarrerJeu(Button bouton, Stage stage){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Menu menu = new Menu(stage);
                stage.setScene(menu);
            }
        });
    }

    public void supprimerSauvegarde(final Button b) {
        b.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                boolean resu = sauvegarde.removeSauvegarde();
                Alert alerte = new Alert(Alert.AlertType.INFORMATION);
                alerte.setTitle("INFORMATION");
                alerte.setContentText("");
                if (resu) {
                    alerte.setHeaderText("Sauvegarde supprimée avec succès");
                    b.setDisable(true);
                } else {
                    alerte.setHeaderText("Echec : sauvegarde non supprimée");
                }

                alerte.show();
            }
        });
    }
}
