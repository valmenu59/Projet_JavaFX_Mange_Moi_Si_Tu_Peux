package menuSelectionPlateau;

import fx.BoutonJeu;
import fx.ImageJeu;
import fx.PoliceJeu;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuSelectionAffichage extends Scene {

    private SelectionControleur controleur;
    private Stage mainStage;

    private static final Font POLICE = PoliceJeu.creerPolice(50, true);
    private static final String SAUVEGARDE_0 = "/plateau0.sae";
    private static final String SAUVEGARDE_1 = "/plateau1.sae";
    private static final String SAUVEGARDE_2 = "/plateau2.sae";
    private static final String SAUVEGARDE_3 = "/plateau3.sae";
    private static final String IMG_SAUVEGARDE_0 =  "/plateau0.png";
    private static final String IMG_SAUVEGARDE_1 = "/plateau1.png";
    private static final String IMG_SAUVEGARDE_2 = "/plateau2.png";
    private static final String IMG_SAUVEGARDE_3 = "/plateau3.png";

    private HBox hBox;


    public MenuSelectionAffichage(Stage stage){
        super(new BorderPane(), 1280.0, 720.0);
        BorderPane panneau = (BorderPane)this.getRoot();
        VBox vBox = new VBox();
        vBox.setSpacing(20.0);
        panneau.setTop(vBox);
        vBox.setAlignment(Pos.CENTER);
        mainStage = stage;

        controleur = new SelectionControleur();

        Text texte1 = new Text("Sélectionnez un plateau préconfiguré");
        texte1.setFont(POLICE);
        vBox.getChildren().add(texte1);

        hBox = new HBox();

        //Code pour les différents imageView
        creerLabel("Plateau 1", IMG_SAUVEGARDE_0, SAUVEGARDE_0);
        creerLabel("Plateau 2", IMG_SAUVEGARDE_1, SAUVEGARDE_1);
        creerLabel("Plateau 3", IMG_SAUVEGARDE_2, SAUVEGARDE_2);
        creerLabel("Plateau 4", IMG_SAUVEGARDE_3, SAUVEGARDE_3);


        vBox.getChildren().add(hBox);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);



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


    public void creerLabel(String nom, String url_img, String url_svg){
        Label plateau = new Label(nom);
        ImageJeu image = new ImageJeu(url_img);
        ImageView imagePlateau = image.afficherImage(240, 170, false);
        plateau.setGraphic(imagePlateau);
        // Appliquer un style CSS au Label
        plateau.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: lightgray; -fx-padding: 10px;");
        plateau.setContentDisplay(ContentDisplay.TOP); // Afficher le texte en dessous de l'image
        hBox.getChildren().add(plateau);

        controleur.ouvrirSauvegardeJeu(plateau, url_svg, mainStage);

    }


}
