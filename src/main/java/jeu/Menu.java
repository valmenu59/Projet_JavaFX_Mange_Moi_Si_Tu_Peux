package jeu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Menu {

    private Stage mainStage;

    public Menu(AnchorPane panneau, Scene mainScene) {
        panneau.getChildren().clear();

        Text texte = new Text("Menu ");
        texte.setX(100);
        texte.setY(100);
        texte.setFont(Font.font("calibri", FontWeight.BOLD,50));

        panneau.getChildren().add(texte);

        Button demarrer = bouton("Démarrer", 100, 200, Color.rgb(115,115,115));

        demarrer = demarrerJeu(demarrer,panneau, mainScene);

        panneau.getChildren().add(demarrer);

        Button parametre = bouton("Paramètres", 100, 350,Color.rgb(115,115,115));
        parametre = parametreJeu(parametre,panneau,mainScene);

        panneau.getChildren().add(parametre);

    }

    public Button bouton(String texte, double X, double Y, Color couleur){
        Button bouton = new Button(texte);
        bouton.setFont(Font.font("calibri", (FontWeight) null, 40));
        bouton.setTextFill(Color.BLACK);
        bouton.setLayoutX(X);
        bouton.setLayoutY(Y);
        BackgroundFill fondType = new BackgroundFill(couleur,null, new Insets(5));
        Background fond = new Background(fondType);
        bouton.setBackground(fond);
        return bouton;
    }

    public Button demarrerJeu(Button bouton, AnchorPane panneau, Scene mainScene){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //
            }
        });
        return bouton;
    }

    public Button parametreJeu(Button bouton, AnchorPane panneau, Scene mainScene){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parametre param = new Parametre(panneau, mainScene);
                param.getMainStage();
            }
        });
        return bouton;
    }

    public Stage getMainStage() {
        return mainStage;
    }

}
