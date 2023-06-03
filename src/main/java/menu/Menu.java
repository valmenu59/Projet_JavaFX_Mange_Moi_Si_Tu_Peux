package menu;

import demarrage.Intro;
import fx.BoutonJeu;
import fx.ImageJeu;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import sauvegarde.DossierSauvegarde;

public class Menu extends Scene {
    private BorderPane panneau;
    private final MenuControleur menuControleur;
    private static final String IMG_LOGO = "/logoJeu2.png";
    private final VBox vBox;

    public Menu(Stage stage) {
        //Création de la scène
        super(new BorderPane(), 1280.0, 720.0);
        panneau = (BorderPane)this.getRoot();
        //Initialisation du contrôleur
        menuControleur = new MenuControleur();
        //Création d'une VBox
        vBox = new VBox();
        vBox.setSpacing(20.0); //Espacement de 20px entre chaque élément de la VBox
        this.panneau.setTop(vBox); //Mettre la VBox en haut
        vBox.setAlignment(Pos.CENTER); //Centrer la VBox
        //Initialisation de la sauvegarde
        DossierSauvegarde sauvegarde = new DossierSauvegarde();

        ImageView logo = (new ImageJeu(IMG_LOGO)).afficherImage(); //Affichage du logo
        logo.setPreserveRatio(true); //Préservation du ratio de l'image
        logo.setFitHeight(300.0); //300px de large
        vBox.getChildren().add(logo);

        //Ajout du bouton démarrer
        BoutonJeu demarrer = creerBoutonMenu("Créer votre propre plateau");
        this.menuControleur.demarrerJeu(demarrer, stage);

        //Ajout du bouton reprendre sauvegarde
        BoutonJeu reprendreSauvegarde = creerBoutonMenu("Reprendre la dernière sauvegarde");
        this.menuControleur.demarrerJeuViaSauvegarde(reprendreSauvegarde, stage);
        // Est désactivé si aucune sauvegarde présente
        if (!sauvegarde.isCheminExisteBien()) {
            reprendreSauvegarde.setDisable(true);
        }

        //Ajout d'un bouton
        BoutonJeu recupererSauvegarde = creerBoutonMenu("Plateaux supplémentaires");
        this.menuControleur.demarrerMenuSelection(recupererSauvegarde, stage);

        //Ajout d'un bouton
        BoutonJeu parametre = creerBoutonMenu("Paramètres");
        this.menuControleur.parametreJeu(parametre, stage);

        //Ajout d'un texte
        Text texteCredit = new Text("Créé généreusement par :\n" +
                "MENU Valentin, FONTAINE Valentin, FAES Hugo !");
        texteCredit.setTextAlignment(TextAlignment.CENTER);
        texteCredit.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20.0));
        vBox.getChildren().add(texteCredit);
    }

    /**
     * @param texte : Permet d'afficher un texte au sein du bouton
     * @return : Retourne une classe BoutonJeu qui est une extension de la classe Button
     * Permet de créer des boutons à couleur uniforme au sein du menu
     */

    public BoutonJeu creerBoutonMenu(String texte){
        BoutonJeu bouton = new BoutonJeu(texte, Color.rgb(200, 130, 100));
        vBox.getChildren().add(bouton);
        return bouton;
    }
}
