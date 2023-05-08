package jeu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        texte.setX(100.0);
        texte.setY(100.0);
        texte.setFont(Font.font("calibri", FontWeight.BOLD, 50.0));
        panneau.getChildren().add(texte);
        Button demarrer = this.bouton("Démarrer", 100.0, 200.0, Color.rgb(115, 115, 115));
        demarrer = this.demarrerJeu(demarrer, panneau, mainScene);
        panneau.getChildren().add(demarrer);
        Button parametre = this.bouton("Paramètres", 100.0, 350.0, Color.rgb(115, 115, 115));
        parametre = this.parametreJeu(parametre, panneau, mainScene);
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


    public Label champTexte(){
        Label label = new Label("Nombre de lignes : ");
        return label;
    }
    public Button demarrerJeu(Button bouton, AnchorPane panneau, Scene mainScene){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Jeu jeu = new Jeu(12,8, mainScene, panneau);
                jeu.getMainStage();
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
