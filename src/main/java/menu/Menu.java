package menu;

import demarrage.Intro;
import fx.BoutonJeu;
import fx.ImageJeu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import jeu.Jeu;
import sauvegarde.DossierSauvegarde;

public class Menu extends Scene {
    private BorderPane panneau = (BorderPane)this.getRoot();
    private Intro intro;
    private MenuControleur menuControleur;
    private static final String IMG_LOGO = "/logoJeu2.png";

    public Menu(Stage stage) {
        super(new BorderPane(), 1280.0, 720.0);
        menuControleur = new MenuControleur();
        VBox vBox = new VBox();
        vBox.setSpacing(30.0);
        this.panneau.setTop(vBox);
        vBox.setAlignment(Pos.CENTER);
        DossierSauvegarde sauvegarde = new DossierSauvegarde();
        ImageView logo = (new ImageJeu("/logoJeu2.png")).afficherImage();
        logo.setPreserveRatio(true);
        logo.setFitHeight(300.0);
        vBox.getChildren().add(logo);
        BoutonJeu demarrer = new BoutonJeu("Démarrer", Color.rgb(200, 130, 100));
        this.menuControleur.demarrerJeu(demarrer, stage);
        vBox.getChildren().add(demarrer);
        BoutonJeu reprendreSauvegarde = new BoutonJeu("Reprendre la sauvegarde", Color.rgb(200, 130, 100));
        this.menuControleur.demarrerJeuViaSauvegarde(reprendreSauvegarde, stage);
        vBox.getChildren().add(reprendreSauvegarde);
        if (!sauvegarde.cheminDaccesExisteBien()) {
            reprendreSauvegarde.setDisable(true);
        }

        BoutonJeu parametre = new BoutonJeu("Paramètres", Color.rgb(200, 130, 100));
        this.menuControleur.parametreJeu(parametre, stage);
        vBox.getChildren().add(parametre);
        Text texteCredit = new Text("Créé généreusement par :\nMENU Valentin, FONTAINE Valentin, FAES Hugo !");
        texteCredit.setTextAlignment(TextAlignment.CENTER);
        texteCredit.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20.0));
        vBox.getChildren().add(texteCredit);
    }
}
