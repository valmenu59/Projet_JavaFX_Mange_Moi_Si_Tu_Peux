package finDeJeu;

import fx.BoutonJeu;
import fx.ImageJeu;
import fx.PoliceJeu;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class FinDeJeu extends Scene {

    private static final Font POLICE = PoliceJeu.creerPolice(38, true);
    private static final String IMG_LOUP = "/ElementsJeu/loupSansCouleur.png";
    private static final String IMG_MOUTON = "/ElementsJeu/moutonSansCouleur.png";


    public FinDeJeu(Stage stage, boolean gagne, int nbTour, int nbPlanteMangees){
        super(new AnchorPane(), 1280, 720);
        AnchorPane anchorPane = (AnchorPane) this.getRoot();


        VBox vBox = new VBox();
        vBox.setSpacing(100);
        vBox.setAlignment(Pos.CENTER);

        ActionFinDeJeu actions = new ActionFinDeJeu();
        Text texte;
        ImageView imageAnimal;


        double x;
        if (gagne) {
            texte = new Text("Félicitation, le mouton est sorti de l'enclos !\n" +
                    "Vous avez gagné !\n\n" +
                    "Nombre de tours : " + nbTour + "\nNombre de plantes mangées : " + nbPlanteMangees);
            imageAnimal = new ImageJeu(getClass().getResource(IMG_MOUTON).toExternalForm())
                    .afficherImage(300, 300, true);
            anchorPane.setStyle("-fx-background-color: #73f56c;");
            x = 250.;
        } else {
            texte = new Text("Oh non ! Le loup a mangé le mouton !\n" +
                    "Vous avez perdu !\n\n" +
                    "Nombre de tours : " + nbTour + "\nNombre de plantes mangées : " + nbPlanteMangees);
            imageAnimal = new ImageJeu(getClass().getResource(IMG_LOUP).toExternalForm())
                    .afficherImage(300, 300, true);
            anchorPane.setStyle("-fx-background-color: #1c1a1a;");
            texte.setFill(Color.WHITE);
            x = 300.;
        }

        imageAnimal.setRotate(45);

        // Positionnement de l'image en haut à droite
        imageAnimal.setLayoutX(anchorPane.getWidth() - 300);
        imageAnimal.setLayoutY(-15);
        anchorPane.getChildren().add(imageAnimal);

        texte.setFont(POLICE);
        texte.setTextAlignment(TextAlignment.CENTER);
        BoutonJeu boutonRetourMenu = new BoutonJeu("Retourner au menu", Color.rgb(200, 200, 200));
        actions.allerAuMenu(boutonRetourMenu, stage);
        vBox.getChildren().addAll(texte, boutonRetourMenu);


        AnchorPane.setTopAnchor(vBox, 200.);
        AnchorPane.setLeftAnchor(vBox, x);
        anchorPane.getChildren().add(vBox);

    }
}
