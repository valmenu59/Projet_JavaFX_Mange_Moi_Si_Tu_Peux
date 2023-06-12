package finDeJeu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import menu.Menu;

public class ActionFinDeJeu {


    public ActionFinDeJeu(){}

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
}
