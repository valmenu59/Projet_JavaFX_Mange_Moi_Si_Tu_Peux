package menuSelectionPlateau;

import fx.BoutonJeu;
import fx.PoliceJeu;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuSelectionAffichage extends Scene {

    private static final Font POLICE = PoliceJeu.creerPolice(50, true);
    private static final String SAUVEGARDE_0 =  "/Sauvegardes/plateau0.sae";
    private static final String SAUVEGARDE_1 =  "/Sauvegardes/plateau1.sae";
    private static final String SAUVEGARDE_2 =  "/Sauvegardes/plateau2.sae";
    private static final String SAUVEGARDE_3 =  "/Sauvegardes/plateau3.sae";


    public MenuSelectionAffichage(Stage stage){
        super(new BorderPane(), 1280.0, 720.0);
        BorderPane panneau = (BorderPane)this.getRoot();
        VBox vBox = new VBox();
        vBox.setSpacing(20.0);
        panneau.setTop(vBox);
        vBox.setAlignment(Pos.CENTER);




        SelectionControleur controleur = new SelectionControleur();


        Text texte1 = new Text("Sélectionnez un plateau préconfiguré");
        texte1.setFont(POLICE);
        vBox.getChildren().add(texte1);

        //Code pour les différents imageView
        Label plateau0 = new Label("Plateau 0");


        Text texte2 = new Text("Ou sélectionnez un de vos propres plateau");
        texte2.setFont(POLICE);
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
