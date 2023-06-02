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
    private Intro intro;
    private final MenuControleur menuControleur;
    private static final String IMG_LOGO = "/logoJeu2.png";
    private final VBox vBox;

    public Menu(Stage stage) {
        super(new BorderPane(), 1280.0, 720.0);
        panneau = (BorderPane)this.getRoot();
        menuControleur = new MenuControleur();
        vBox = new VBox();
        vBox.setSpacing(20.0);
        this.panneau.setTop(vBox);
        vBox.setAlignment(Pos.CENTER);
        DossierSauvegarde sauvegarde = new DossierSauvegarde();
        ImageView logo = (new ImageJeu(IMG_LOGO)).afficherImage();
        logo.setPreserveRatio(true);
        logo.setFitHeight(300.0);
        vBox.getChildren().add(logo);


        BoutonJeu demarrer = creerBoutonMenu("Créer votre propre plateau");
        this.menuControleur.demarrerJeu(demarrer, stage);

        BoutonJeu reprendreSauvegarde = creerBoutonMenu("Reprendre la dernière sauvegarde");
        this.menuControleur.demarrerJeuViaSauvegarde(reprendreSauvegarde, stage);
        if (!sauvegarde.isCheminExisteBien()) {
            reprendreSauvegarde.setDisable(true);
        }

        BoutonJeu recupererSauvegarde = creerBoutonMenu("Plateaux supplémentaires");
        this.menuControleur.demarrerMenuSelection(recupererSauvegarde, stage);

        BoutonJeu parametre = creerBoutonMenu("Paramètres");
        this.menuControleur.parametreJeu(parametre, stage);


        Text texteCredit = new Text("Créé généreusement par :\n" +
                "MENU Valentin, FONTAINE Valentin, FAES Hugo !");
        texteCredit.setTextAlignment(TextAlignment.CENTER);
        texteCredit.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20.0));
        vBox.getChildren().add(texteCredit);
    }

    public BoutonJeu creerBoutonMenu(String texte){
        BoutonJeu bouton = new BoutonJeu(texte, Color.rgb(200, 130, 100));
        vBox.getChildren().add(bouton);
        return bouton;
    }
}
