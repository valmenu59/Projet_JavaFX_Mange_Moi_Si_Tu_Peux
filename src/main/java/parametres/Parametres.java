package parametres;

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

public class Parametres extends Scene {

    private ParametresControleur parametresControleur;

    public Parametres(Stage stage) {
        super(new AnchorPane(), 1280,720);

        AnchorPane panneau = (AnchorPane) getRoot();

        parametresControleur = new ParametresControleur(this);


        Text texte = new Text("Paramètres ");
        texte.setX(100);
        texte.setY(100);
        texte.setFont(Font.font("calibri", FontWeight.BOLD,50));

        panneau.getChildren().add(texte);

        Button menuPrincipal = bouton("Menu Principal", 100, 200, Color.rgb(115,115,115));

        //menuPrincipal = demarrerJeu(stage);
        parametresControleur.demarrerJeu(menuPrincipal,stage);

        panneau.getChildren().add(menuPrincipal);


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


    /*

    public Stage getMainStage() {
        return mainStage;
    }

     */

}
