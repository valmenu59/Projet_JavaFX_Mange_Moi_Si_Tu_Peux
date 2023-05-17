package parametres;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import menu.Menu;

public class ParametresControleur {

    private Parametres parametres;


    public ParametresControleur(Parametres p){
        this.parametres = p;
    }

    public void demarrerJeu(Button bouton, Stage stage){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Menu menu = new Menu(stage);
                stage.setScene(menu);
                //Menu menu = new Menu();
                //menu.getMainStage();
            }
        });
    }
}
