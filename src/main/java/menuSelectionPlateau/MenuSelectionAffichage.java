/*
 * Copyright (c)  @link https://github.com/valmenu59/SAE_JavaFX @link https://github.com/valmenu59
 * CC BY-NC - Attribution - Partage dans les mêmes conditions - Pas d'utilisation commerciale
 */

package menuSelectionPlateau;

import fx.ActionAllerNouvelleScene;
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
import menu.Menu;

public class MenuSelectionAffichage extends Scene {

    private SelectionControleur controleur;
    private Stage mainStage;

    private static final Font POLICE = PoliceJeu.creerPolice(50, true);
    private static final Font POLICE_LABEL = PoliceJeu.creerPolice(24, true);
    private static final String SAUVEGARDE_0 = "/Sauvegardes/plateau0.sae";
    private static final String SAUVEGARDE_1 = "/Sauvegardes/plateau1.sae";
    private static final String SAUVEGARDE_2 = "/Sauvegardes/plateau2.sae";
    private static final String SAUVEGARDE_3 = "/Sauvegardes/plateau3.sae";
    private static final String SAUVEGARDE_4 = "/Sauvegardes/plateau4.sae";
    private static final String SAUVEGARDE_5 = "/Sauvegardes/plateau5.sae";
    private static final String SAUVEGARDE_6 = "/Sauvegardes/plateau6.sae";
    private static final String SAUVEGARDE_7 = "/Sauvegardes/plateau7.sae";
    private static final String IMG_SAUVEGARDE_0 = "/Sauvegardes/ImagesPlateaux/plateau0.png";
    private static final String IMG_SAUVEGARDE_1 = "/Sauvegardes/ImagesPlateaux/plateau1.png";
    private static final String IMG_SAUVEGARDE_2 = "/Sauvegardes/ImagesPlateaux/plateau2.png";
    private static final String IMG_SAUVEGARDE_3 = "/Sauvegardes/ImagesPlateaux/plateau3.png";
    private static final String IMG_SAUVEGARDE_4 = "/Sauvegardes/ImagesPlateaux/plateau4.png";
    private static final String IMG_SAUVEGARDE_5 = "/Sauvegardes/ImagesPlateaux/plateau5.png";
    private static final String IMG_SAUVEGARDE_6 = "/Sauvegardes/ImagesPlateaux/plateau6.png";
    private static final String IMG_SAUVEGARDE_7 = "/Sauvegardes/ImagesPlateaux/plateau7.png";


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

        HBox hBox = new HBox();
        HBox hBox2 = new HBox();
        VBox vBox2 = new VBox();

        //Code pour les différents labels
        creerLabel("Plateau 1", IMG_SAUVEGARDE_0, SAUVEGARDE_0, hBox);
        creerLabel("Plateau 2", IMG_SAUVEGARDE_1, SAUVEGARDE_1, hBox);
        creerLabel("Plateau 3", IMG_SAUVEGARDE_2, SAUVEGARDE_2, hBox);
        creerLabel("Plateau 4", IMG_SAUVEGARDE_3, SAUVEGARDE_3, hBox);
        creerLabel("Plateau 5", IMG_SAUVEGARDE_4, SAUVEGARDE_4, hBox2);
        creerLabel("Plateau 6", IMG_SAUVEGARDE_5, SAUVEGARDE_5, hBox2);
        creerLabel("Plateau 7", IMG_SAUVEGARDE_6, SAUVEGARDE_6, hBox2);
        creerLabel("Plateau 8", IMG_SAUVEGARDE_7, SAUVEGARDE_7, hBox2);


        vBox2.getChildren().add(hBox);
        vBox2.getChildren().add(hBox2);
        vBox2.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        hBox2.setAlignment(Pos.CENTER);
        hBox2.setSpacing(20);

        vBox.getChildren().add(vBox2);


        Text text = new Text();
        vBox.getChildren().add(text);



        BoutonJeu boutonMenu = new BoutonJeu("Retourner au menu", Color.rgb(150,150,150));
        boutonMenu.setOnAction(event -> new ActionAllerNouvelleScene<>(stage, Menu.class).handle(event));
        vBox.getChildren().add(boutonMenu);

    }


    public void creerLabel(String nom, String url_img, String url_svg, HBox hBox){
        Label plateau = new Label(nom);
        plateau.setFont(POLICE_LABEL);
        ImageJeu image = new ImageJeu(getClass().getResource(url_img).toExternalForm());
        ImageView imagePlateau = image.afficherImage(240, 170, false);
        plateau.setGraphic(imagePlateau);
        // Appliquer un style CSS au Label
        plateau.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: lightgray; -fx-padding: 10px;");
        plateau.setContentDisplay(ContentDisplay.TOP); // Afficher le texte en dessous de l'image
        hBox.getChildren().add(plateau);

        controleur.ouvrirSauvegardeJeu(plateau, url_svg, mainStage);

    }


}
