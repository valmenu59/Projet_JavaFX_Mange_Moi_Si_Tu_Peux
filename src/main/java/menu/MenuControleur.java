package menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jeu.AffichageJeu;
import menuSelectionPlateau.MenuSelectionAffichage;
import parametres.Parametres;

public class MenuControleur {

    private Menu menu;
    private AffichageJeu affichageJeu;


    public MenuControleur(){
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
                AffichageJeu affichageJeu = new AffichageJeu(stage,false);
                stage.setScene(affichageJeu);
            }
        });
    }

    public void demarrerJeuViaSauvegarde(Button bouton, Stage stage){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AffichageJeu affichageJeu = new AffichageJeu(stage,true);
                stage.setScene(affichageJeu);
            }
        });
    }

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
