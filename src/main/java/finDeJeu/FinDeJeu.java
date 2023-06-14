package finDeJeu;

import fx.BoutonJeu;
import fx.PoliceJeu;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class FinDeJeu extends Scene {

    private static final Font POLICE = PoliceJeu.creerPolice(38, true);


    public FinDeJeu(Stage stage, boolean gagne, int nbTour, int nbPlanteMangees){
        super(new BorderPane(), 1280,720);
        BorderPane panneau = (BorderPane) this.getRoot();
        VBox vBox = new VBox();
        vBox.setSpacing(100);
        vBox.setAlignment(Pos.CENTER);
        panneau.setCenter(vBox);
        ActionFinDeJeu actions = new ActionFinDeJeu();
        Text texte;
        if (gagne){
            texte = new Text("Félicitation, le mouton est sorti de l'enclos !\n"+
                    "Vous avez gagné !\n\n" +
                    "Nombre de tours : "+nbTour+"\t Nombre de plantes mangées : "+nbPlanteMangees);
        } else {
            texte = new Text("Oh non ! Le loup a mangé le mouton !\n"+
                    "Vous avez perdu !\n\n" +
                    "Nombre de tours : "+nbTour+"\t Nombre de plantes mangées : "+nbPlanteMangees);
        }
        texte.setFont(POLICE);
        texte.setTextAlignment(TextAlignment.CENTER);
        vBox.getChildren().add(texte);

        BoutonJeu boutonRetourMenu = new BoutonJeu("Retourner au menu", Color.rgb(200, 200, 200));
        actions.allerAuMenu(boutonRetourMenu, stage);
        vBox.getChildren().add(boutonRetourMenu);

    }
}
