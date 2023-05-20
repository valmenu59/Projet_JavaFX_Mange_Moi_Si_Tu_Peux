package menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jeu.AffichageJeu;
import parametres.Parametres;

public class MenuControleur {

    private Menu menu;
    private AffichageJeu affichageJeu;


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
                AffichageJeu affichageJeu = new AffichageJeu(stage);
                stage.setScene(affichageJeu);
            }
        });
    }


}
