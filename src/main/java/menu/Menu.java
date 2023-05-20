package menu;

import demarrage.Intro;
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

public class Menu extends Scene {
    private AnchorPane panneau;
    private Intro intro;
    private MenuControleur menuControleur;

    //public Menu(AnchorPane panneau, Scene mainScene) {
    public Menu (Stage stage){
        super(new AnchorPane(),1280, 720);
        menuControleur = new MenuControleur(this);
        //mainScene = new Scene(panneau);
        panneau = (AnchorPane) getRoot();
        //panneau.getChildren().clear();
        Text texte = new Text("Menu ");
        texte.setX(100.0);
        texte.setY(100.0);
        texte.setFont(Font.font("calibri", FontWeight.BOLD, 50.0));
        panneau.getChildren().add(texte);
        Button demarrer = this.bouton("Démarrer", 100.0, 200.0, Color.rgb(115, 115, 115));
        //demarrer = this.demarrerJeu(demarrer, panneau, mainScene);
        menuControleur.demarrerJeu(demarrer,stage);
        panneau.getChildren().add(demarrer);
        Button parametre = this.bouton("Paramètres", 100.0, 350.0, Color.rgb(115, 115, 115));
        menuControleur.parametreJeu(parametre,stage);
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



}
