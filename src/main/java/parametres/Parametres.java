package parametres;

import fx.BoutonJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sauvegarde.DossierSauvegarde;

public class Parametres extends Scene {
    private ParametresControleur parametresControleur;

    public Parametres(Stage stage) {
        super(new BorderPane(), 1280.0, 720.0);
        BorderPane panneau = (BorderPane)this.getRoot();
        VBox vBox = new VBox();
        vBox.setSpacing(30.0);
        panneau.setTop(vBox);
        vBox.setAlignment(Pos.CENTER);
        this.parametresControleur = new ParametresControleur();
        Text texte = new Text("Paramètres ");
        texte.setX(100.0);
        texte.setY(100.0);
        texte.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50.0));
        vBox.getChildren().add(texte);
        DossierSauvegarde sauvegarde = new DossierSauvegarde();
        BoutonJeu boutonSupprimerSauvegarde = new BoutonJeu("Supprimer la sauvegarde", Color.rgb(200, 20, 20));
        boutonSupprimerSauvegarde.setTextFill(Color.WHITE);
        boutonSupprimerSauvegarde.setDisable(!sauvegarde.cheminDaccesExisteBien());
        this.parametresControleur.supprimerSauvegarde(boutonSupprimerSauvegarde);
        vBox.getChildren().add(boutonSupprimerSauvegarde);
        BoutonJeu menuPrincipal = new BoutonJeu("Menu Principal", Color.rgb(115, 115, 115));
        vBox.getChildren().add(menuPrincipal);
        this.parametresControleur.demarrerJeu(menuPrincipal, stage);
    }
}
