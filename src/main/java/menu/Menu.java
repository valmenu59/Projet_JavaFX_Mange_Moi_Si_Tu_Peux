/*
 * Copyright (c)  @link https://github.com/valmenu59/SAE_JavaFX @link https://github.com/valmenu59
 * CC BY-NC - Attribution - Partage dans les mêmes conditions - Pas d'utilisation commerciale
 */

package menu;

import fx.ActionAllerNouvelleScene;
import fx.BoutonJeu;
import fx.ImageJeu;
import fx.PoliceJeu;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import menuSelectionPlateau.MenuSelectionAffichage;
import parametres.Parametres;
import sauvegarde.DossierSauvegarde;

public class Menu extends Scene {
    private BorderPane panneau;
    private final MenuControleur menuControleur;
    private final VBox vBox;
    private static final String IMG_ARRIERE_PLAN = "/Logos/arriereplan.png";
    private static final Font POLICE = PoliceJeu.creerPolice(20, true);


    public Menu(Stage stage) {
        //Création de la scène
        super(new BorderPane(), 1280.0, 720.0);
        panneau = (BorderPane)this.getRoot();
        //Initialisation du contrôleur
        menuControleur = new MenuControleur();
        //Création d'une VBox
        vBox = new VBox();
        vBox.setSpacing(30.0); //Espacement de 20px entre chaque élément de la VBox
        this.panneau.setLeft(vBox); //Mettre la VBox à gauche
        vBox.setAlignment(Pos.CENTER_LEFT); //Centrer la VBox à gauche
        //Initialisation de la sauvegarde
        DossierSauvegarde sauvegarde = new DossierSauvegarde();


        BackgroundImage imageArrierePlan = new BackgroundImage(
                new ImageJeu(getClass().getResource(IMG_ARRIERE_PLAN).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );
        Background arrierePlan = new Background(imageArrierePlan);
        panneau.setBackground(arrierePlan);

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
        recupererSauvegarde.setOnAction(event -> new ActionAllerNouvelleScene<>(stage, MenuSelectionAffichage.class).handle(event));

        //Ajout d'un bouton
        BoutonJeu bouton = creerBoutonMenu("Ouvrez vos propres plateaux");
        menuControleur.ouvrirExplorateur(bouton, stage);

        //Ajout d'un bouton
        BoutonJeu parametre = creerBoutonMenu("Paramètres");
        parametre.setOnAction(event -> new ActionAllerNouvelleScene<>(stage, Parametres.class).handle(event));

        //Ajout d'un bouton
        BoutonJeu boutonQuitter = creerBoutonMenu("Quitter le jeu");
        this.menuControleur.quitterJeu(boutonQuitter, stage);

        //Ajout d'un lien internet cliquable
        Hyperlink lien = new Hyperlink("Ouvrir la page Github du projet");
        menuControleur.ouvrirLien(lien);
        lien.setTextFill(Color.BLACK);
        lien.setTextAlignment(TextAlignment.CENTER);
        lien.setFont(POLICE);
        panneau.setBottom(lien);
        BorderPane.setAlignment(lien ,Pos.BOTTOM_CENTER);




    }

    /**
     * @param texte : Permet d'afficher un texte au sein du bouton
     * @return : Retourne une classe BoutonJeu qui est une extension de la classe Button
     * Permet de créer des boutons à couleur uniforme au sein du menu
     */

    public BoutonJeu creerBoutonMenu(String texte){
        BoutonJeu bouton = new BoutonJeu(texte, Color.rgb(200, 200, 200,0.80));
        vBox.getChildren().add(bouton);
        return bouton;
    }
}
