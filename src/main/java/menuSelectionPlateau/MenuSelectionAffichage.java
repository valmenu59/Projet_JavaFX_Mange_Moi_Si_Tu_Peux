package menuSelectionPlateau;

import fx.BoutonJeu;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuSelectionAffichage extends Scene {

    public MenuSelectionAffichage(Stage stage){
        super(new BorderPane(), 1280.0, 720.0);
        BorderPane panneau = (BorderPane)this.getRoot();
        VBox vBox = new VBox();
        vBox.setSpacing(20.0);
        panneau.setTop(vBox);
        vBox.setAlignment(Pos.CENTER);


        SelectionControleur controleur = new SelectionControleur();


        Font police = Font.font("Segoe UI", FontWeight.BOLD, 50.0);
        Text texte1 = new Text("Sélectionnez un plateau préconfiguré");
        texte1.setFont(police);
        vBox.getChildren().add(texte1);

        //Code pour les différents imageView

        Text texte2 = new Text("Ou sélectionnez un de vos propres plateau");
        texte2.setFont(police);
        vBox.getChildren().add(texte2);

        BoutonJeu bouton = new BoutonJeu("Ouvrir l'explorateur de fichiers", Color.ORANGERED);
        controleur.ouvrirExplorateur(bouton, stage);
        vBox.getChildren().add(bouton);

        Text text = new Text();
        vBox.getChildren().add(text);

        BoutonJeu boutonMenu = new BoutonJeu("Retourner au menu", Color.LIGHTGRAY);
        controleur.retourMenu(boutonMenu, stage);
        vBox.getChildren().add(boutonMenu);



    }
}
