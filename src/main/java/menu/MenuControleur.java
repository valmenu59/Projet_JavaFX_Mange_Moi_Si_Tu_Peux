package menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jeu.Jeu;
import parametres.Parametres;

public class MenuControleur {

    private Menu menu;
    private Jeu jeu;


    public MenuControleur(Menu m){
        this.menu = m;
    }



    public void parametreJeu(Button bouton, Stage stage){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parametres param = new Parametres(stage);
                stage.setScene(param);

            }
        });
    }



    public void demarrerJeu(Button bouton, Stage stage){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Jeu jeu = new Jeu();
                //jeu.getMainStage();
                Jeu jeu = new Jeu(stage);
                stage.setScene(jeu);
            }
        });
    }


}
