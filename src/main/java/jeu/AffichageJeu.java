package jeu;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import finDeJeu.FinDeJeu;
import fx.PoliceJeu;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.util.Duration;
import sauvegarde.DossierSauvegarde;

public class AffichageJeu extends Scene {
    //Reprend la classe controleur
    protected Controleur controleur;

    //Création de 3 polices différentes
    private static final Font POLICE_PRINCIPALE = PoliceJeu.creerPolice(25, true);
    private static final Font POLICE_SECONDAIRE = PoliceJeu.creerPolice(15, false);
    private static final Font POLICE_ALERTE = PoliceJeu.creerPolice(12, true);


    //Récupère la source des images
    private static final String IMG_CACTUS = "/ElementsJeu/CactusColors.png";
    private static final String IMG_HERBE = "/ElementsJeu/HerbeColors.png";
    private static final String IMG_LOUP = "/ElementsJeu/loup.png";
    private static final String IMG_MARGUERITE = "/ElementsJeu/MargueColors.png";
    private static final String IMG_MOUTON = "/ElementsJeu/MoutonColors.png";
    private static final String IMG_ROCHER = "/ElementsJeu/RocherColors.png";
    private static final String IMG_TERRE = "/ElementsJeu/terre.png";
    private static final String IMG_LOUP_MENACANT = "/ElementsJeu/loup2.png";
    private static final String IMG_MOUTON_MENACE = "/ElementsJeu/mouton2.png";





    //Rassemblement des styles d'affichage
    private AnchorPane panneauPrincipal;
    private final SplitPane separateur;
    private final Stage mainStage;
    private Scene mainScene;
    private VBox panneau2;

    private VBox vBox1;

    //Panneaux déroulants
    private ChoiceBox<Integer> choixNblignes;
    private ChoiceBox<Integer> choixNbColonnes;
    private ChoiceBox<Double> choixTempsEntreChaqueDeplacement;

    //Boutons
    private Button boutonCreerPlateau;
    private Button boutonValiderEtape;
    private Button boutonRetour;
    private Button boutonCreerLabyrinthe;
    private Button boutonRetourMenu;
    private Button boutonPause;

    //Textes
    private Text texteEtape;
    private Text texteExplicationEtape;
    private Text texteAlerteLabyrintheImparfait;
    private Text texteJeu;

    //Listes d'éléments
    private final ArrayList<Node> listeMenuDeroulantetLabelEtape1 = new ArrayList<>();
    private final ArrayList<Rectangle> listeCarreNoir = new ArrayList<>();
    private final ArrayList<ImageView> listeImages = new ArrayList<>();

    private final ArrayList<Rectangle> listeCasePassees = new ArrayList<>();
    private final ArrayList<Circle> listeCercles = new ArrayList<>();

    //Autres
    private AnimationTimer boucleJeu;
    private double taille;
    private double xDepart;
    private double yDepart;
    private boolean estEnPause;
    private int numeroEtape;
    private final boolean vientSauvegarde;


    public AffichageJeu(Stage stage, boolean vientDeLaSauvegarde){
        super(new SplitPane(), 1280,720);
        //panneauTemporaire = (AnchorPane) getRoot();
        separateur = (SplitPane) getRoot();
        mainStage = stage;

        vientSauvegarde = vientDeLaSauvegarde;


        //Permet de créer la base pour les autres éléments visuels
        creerFenetreJeu();

        //On initie les actions utilisateurs
        controleur = new Controleur(this);


        if (vientDeLaSauvegarde){
            DossierSauvegarde sauvegarde = new DossierSauvegarde();
            try {
                this.controleur.jeu.reprendreSauvegarde(sauvegarde.getCheminDaccesSauvegarde(), false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            boutonRetourMenu = new Button("Retourner au menu");
            controleur.retournerAuMenu(boutonRetourMenu, mainStage);
            panneau2.getChildren().add(boutonRetourMenu);

            afficherPlateauJeu(false);
            getJeu().plateau.printMatrices();

            etapeJeu();
        } else {
            //On va vers l'étape n°1
            etape1(false);
        }

    }

    public AffichageJeu(Stage stage, String adressePlateau, boolean sauvegardeVientDuFichierResources){
        super(new SplitPane(), 1280,720);
        separateur = (SplitPane) getRoot();
        mainStage = stage;
        vientSauvegarde = true;
        //Permet de créer la base pour les autres éléments visuels
        creerFenetreJeu();
        //On initie les actions utilisateurs
        controleur = new Controleur(this);
        try {
            if (sauvegardeVientDuFichierResources){
                this.controleur.jeu.reprendreSauvegarde(adressePlateau, true);
            } else {
                this.controleur.jeu.reprendreSauvegarde(adressePlateau, false);
            }
        } catch (IOException e) {
            String texte = "Erreur de lecture : "+"\n"+"" +
                    "Impossible de lire le fichier de sauvegarde.";
            messageAlerte(e, texte, stage);
        } catch (ClassNotFoundException e) {
            String texte = "Erreur de chargement : "+"\n"+
                    "Impossible de charger les données de sauvegarde.";
            messageAlerte(e, texte, stage);
        }  catch (Exception e){
            messageAlerte(e,"Erreur !", stage);
        }
        boutonRetourMenu = new Button("Retourner au menu");
        controleur.retournerAuMenu(boutonRetourMenu, mainStage);
        panneau2.getChildren().add(boutonRetourMenu);
        afficherPlateauJeu(false);
        getJeu().plateau.printMatrices();
        etapeJeu();
    }

    /////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////GET, SET, IS//////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    public int getChoixNbLignes() {
        //Permet de récupérer la valeur choisie du menu déroulant
        return choixNblignes.getValue();
    }

    public int getChoixNbColonnes() {
        //Permet de récupérer la valeur choisie du menu déroulant
        return choixNbColonnes.getValue();
    }

    public double getChoixTempsEntreChaqueDeplacement() {
        //Permet de récupérer la valeur choisie du menu déroulant
        return choixTempsEntreChaqueDeplacement.getValue();
    }

    public Button getBoutonValiderEtape(){
        return this.boutonValiderEtape;
    }

    public int getNumeroEtape(){
        return this.numeroEtape;
    }



    public VBox getPanneauSecondaire(){
        return this.panneau2;
    }


    public Plateau getPlateau(){
        return this.controleur.jeu.plateau;
    }

    public Case getCase(int i, int j){
        return this.controleur.jeu.plateau.cases[i][j];
    }

    /**
     * @param element : Un String qui correspond à un objet du jeu
     * @return : retourne la source de l'image d'un élément
     */

    public String getSource (String element){
        switch (element){
            case "roche" -> {
                return IMG_ROCHER;
            }
            case "herbe" -> {
                return IMG_HERBE;
            }
            case "marguerite" -> {
                return IMG_MARGUERITE;
            }
            case "cactus" -> {
                return IMG_CACTUS;
            }
            case "mouton" -> {
                return IMG_MOUTON;
            }
            case "loup" -> {
                return IMG_LOUP;
            }
            case "terre" -> {
                return IMG_TERRE;
            }
            case "mouton2" -> {
                return IMG_MOUTON_MENACE;
            }
            case "loup2" -> {
                return IMG_LOUP_MENACANT;
            }
            default -> {
                return null;
            }
        }
    }

    public ImageView getImageView(int i, int j){
        return listeImages.get(i * getPlateau().getColonnes() + j );
    }

    public Text getTexteAlerteLabyrintheImparfait(){
        return this.texteAlerteLabyrintheImparfait;
    }

    public AnimationTimer getBoucleJeu(){
        return this.boucleJeu;
    }

    public boolean isEstEnPause(){
        return estEnPause;
    }

    public void setMettreEnPause(boolean b){
        this.estEnPause = b;
    }

    public Jeu getJeu(){
        return this.controleur.jeu;
    }

    public Button getBoutonPause(){
        return boutonPause;
    }


    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////FIN GET, SET, IS////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////


    /**
     * Permet d'afficher une Alerte de type ERROR et qui retourne au menu ensuite
     * @param e : Une exception
     * @param texte : Le texte spécifique correspondant à l'exception
     * @param stage : Stage actuel
     */

    public void messageAlerte(Exception e, String texte, Stage stage){
        System.out.println("iciiiiiiiiiiiiiiii");
        e.printStackTrace();
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setHeaderText(texte);
        alerte.setContentText(e.toString());
        alerte.showAndWait();
        menu.Menu menu = new menu.Menu(stage);
        stage.setScene(menu);
    }

    /**
     * Permet de créer la fenêtre du jeu avec un Splitpane possédant un VBox à gauche et un AnchorPane à droite
     */


    public void creerFenetreJeu() {

        //récupération de la scène
        mainScene = separateur.getScene();

        //Création du VBox à la partie gauche qui prend 20% de la scène
        this.panneau2 = new VBox();
        panneau2.setSpacing(20);
        panneau2.setPadding(new Insets(10, 0, 0, 10));
        panneau2.setPrefWidth(mainScene.getWidth() * 0.20);

        // Création de l'AnchorPane pour la partie droite
        this.panneauPrincipal = new AnchorPane();

        // Ajout des 2 items
        separateur.getItems().addAll(panneau2,panneauPrincipal);
        //Permet de diviser les deux parties à 20% de la taille de la scène
        separateur.setDividerPositions(0.20);
        //Permet de ne plus redimensionner les panneaux
        panneau2.maxWidthProperty().bind(separateur.widthProperty().multiply(0.20));
        panneau2.minWidthProperty().bind(separateur.widthProperty().multiply(0.20));
        //Permet de définir le style de panneau2
        panneau2.setStyle("-fx-background-color: rgb(161,236,158)");

    }


    /**
     * Etape 1 du jeu : implémente les différents boutons et actions nécessaires
     * @param retour : si revient de l'étape 2
     */


    public void etape1(boolean retour) {
        //Initialisation de l'attribut numeroEtape
        numeroEtape = 1;
        texteEtape();
        if (!retour){
            //On crée un texte
            Text texte = new Text("Options");
            //Cette méthode permet le texte du jeu

            //Ajout d'un vBox
            vBox1 = new VBox();

            //On rajoute le texte dans panneau2
            panneau2.getChildren().add(texte);
            //Cette méthode permet de créer un menu déroulant avec un chiffre mini, maxi et celui affiché par défaut
            choixNbColonnes = creerMenuDeroulant("Nombre de colonnes : ", 10, 27, 14);
            choixNblignes = creerMenuDeroulant("Nombre de lignes : ", 7, 21, 9);
            //Ensuite on crée différents boutons
            boutonCreerPlateau = new Button("Créer votre plateau");
            controleur.validerLignesColonnes(boutonCreerPlateau);
            panneau2.getChildren().add(boutonCreerPlateau);

            vBox1.getChildren().addAll(listeMenuDeroulantetLabelEtape1);
            vBox1.getChildren().add(boutonCreerPlateau);
            panneau2.getChildren().add(vBox1);

            boutonValiderEtape = new Button("Valider cette étape");
            controleur.validerEtape(boutonValiderEtape);
            boutonValiderEtape.setDisable(true); //Permet de rendre le bouton inactif
            panneau2.getChildren().add(boutonValiderEtape);

            //On crée un bouton qui permet de retourner à l'étape précédente
            boutonRetour = new Button("Retourner à l'étape précédente");
            controleur.retournerEtapePrecedente(boutonRetour);
            boutonRetour.setDisable(true);
            panneau2.getChildren().add(boutonRetour);

            //On crée un bouton qui permet de retourner au menu
            boutonRetourMenu = new Button("Retourner au menu");
            controleur.retournerAuMenu(boutonRetourMenu, mainStage);
            panneau2.getChildren().add(boutonRetourMenu);

        } else {
            panneau2.getChildren().add(1,vBox1);
            boutonValiderEtape.setDisable(false);
            boutonRetour.setDisable(true);
        }

    }



    /**
     * Etape 2 du jeu : implémente les différents boutons et actions nécessaires
     * @param retour : si revient de l'étape 3
     */


    public void etape2(boolean retour) {
        //Passage à l'étape 2
        this.numeroEtape = 2;
        boutonRetour.setDisable(false);
        //Permet d'afficher le texte sur le panneau principal
        texteEtape();
        if (controleur.isSortieCree()) {
            boutonValiderEtape.setDisable(false);
        } else {
            boutonValiderEtape.setDisable(true);
        }
        //boutonRetour.setDisable(false);
        if (!retour) {
            //Avant on supprime tout ce qu'on a plus besoin
            panneau2.getChildren().remove(vBox1);
        } else {
            panneau2.getChildren().remove(boutonCreerLabyrinthe);
        }
    }

    /**
     * Etape 3 du jeu : implémente les différents boutons et actions nécessaires
     */

    public void etape3() {
        this.numeroEtape = 3;
        if (boutonValiderEtape.getText().equals("Commencer le jeu")){
            boutonValiderEtape.setText("Valider cette étape");
        }
        texteAlerteLabyrintheImparfait();
        changerActionCaseSortie();
        texteEtape();
        boutonCreerLabyrinthe = new Button("Générer un labyrinthe aléatoire");
        controleur.boutonCreerLabyrinthe(boutonCreerLabyrinthe);
        panneau2.getChildren().add(1,boutonCreerLabyrinthe);

    }

    /**
     * Etape 4 du jeu : implémente les différents boutons et actions nécessaires
     */

    public void etape4(){
        this.numeroEtape = 4;
        boutonValiderEtape.setDisable(true);
        boutonValiderEtape.setText("Commencer le jeu");
        panneau2.getChildren().remove(1);
        texteEtape();
    }

    /**
     * Etape principale du jeu
     */

    public void etapeJeu(){
        numeroEtape = 5;
        texteEtape();
        panneau2.getChildren().remove(boutonRetour);
        panneau2.getChildren().remove(boutonValiderEtape);
        estEnPause = false;



        boutonPause = new Button("Pause");
        controleur.mettreEnPause(boutonPause);
        panneau2.getChildren().add(boutonPause);

        texteJeu(true);

        mettreAJourAffichagePlateau();
        creerMenuDeroulantTemps();
        boucleAffichageJeu();
    }

    /**
     * Permet de donner une action différente à la case de sortie
     */

    public void changerActionCaseSortie(){
        for (int i = 0; i < getPlateau().getLignes(); i++) {
            for (int j = 0; j < getPlateau().getColonnes(); j++) {
                if (i == 0 || j == 0 || i == getPlateau().getLignes() - 1 || j == getPlateau().getColonnes() - 1){
                    if (getPlateau().cases[i][j].getContenu().getNom().equals("Herbe")){
                        ImageView img = listeImages.get(i * getPlateau().getColonnes() + j);
                        controleur.remplacerTypeTerrain(img, true,true);
                    }
                }
            }
        }
    }

    /**
     * Donne le texte de chaque étape
     */


    public void texteEtape() {
        //Cette méthode permet de créer un texte en fonction du numéro de l'étape
        if (numeroEtape == 1) {
            if (texteEtape == null) {
                texteEtape = new Text("Etape 1 : Choisissez la taille de votre plateau");
                texteEtape.setX(50);
                texteEtape.setY(50);
                texteEtape.setFont(POLICE_PRINCIPALE);
                texteExplicationEtape = new Text("Pour cette étape, choisissez votre nombre de lignes et de colonnes que vous souhaitez à la partie gauche de l'écran." + "\n" +
                        "Cliquez sur 'Créer votre votre plateau' pour créer un plateau sur l'écran puis cliquez sur 'Valider cette étape' pour passer à l'étape n°2");
                texteExplicationEtape.setX(50);
                texteExplicationEtape.setY(75);
                texteExplicationEtape.setFont(POLICE_SECONDAIRE);
                panneauPrincipal.getChildren().addAll(texteEtape, texteExplicationEtape);
            } else {
                texteEtape.setText("Etape 1 : Choisissez la taille de votre plateau");
                texteExplicationEtape.setText("Pour cette étape, choisissez votre nombre de lignes et de colonnes que vous souhaitez à la partie gauche de l'écran." + "\n" +
                        "Cliquez sur 'Créer votre votre plateau' pour créer un plateau sur l'écran puis cliquez sur 'Valider cette étape' pour passer à l'étape n°2");
            }
        } else if (numeroEtape == 2) {
            //.setText permet de modifier un texte déjà initialisé
            texteEtape.setText("Etape 2 : Choisissez l'emplacement de sortie");
            texteExplicationEtape.setText("Pour cette étape, choisissez une unique sortie c'est à dire que vous remplacez une case rocher par une case herbe." + "\n" +
                    "Pour cela, sur le plateau, cliquez sur un rocher sauf les extrémités puis cliquez sur 'Valider cette étape' pour passer à l'étape n°3");
        } else if (numeroEtape == 3) {
            texteEtape.setText("Etape 3 : Personnalisez le plateau du jeu");
            texteExplicationEtape.setText("Pour cette étape, cliquez sur terrain puis sélectionnez votre type de terrain souhaité." + "\n" +
                    "Attention il n'est pas possible de boucher la sortie ni de séparer le terrain en 2, il faut qu'à partir de tout plante on puisse sortir !");
        } else if (numeroEtape == 4){
            texteEtape.setText("Etape 4 : Placez le loup et le mouton dans le plateau du jeu");
            texteExplicationEtape.setText("Pour cette étape, cliquez sur le plateau du jeu où vous voulez placer votre loup et mouton." +"\n" +
                    "Il faut que ces 2 animaux soient au moins distancés de 4 cases. Une fois cette finie le jeu commencera si vous cliquez sur valider !");
        } else if (numeroEtape == 5){
            if (!vientSauvegarde) {
                texteEtape.setText("Jeu en cours...");
                texteExplicationEtape.setText("Maintenant laissons tourner la simulation !");
            } else {
                texteEtape = new Text("Jeu en cours...");
                texteEtape.setX(50);
                texteEtape.setY(50);
                texteEtape.setFont(POLICE_PRINCIPALE);
                texteExplicationEtape = new Text("Maintenant laissons tourner la simulation !");
                texteExplicationEtape.setX(50);
                texteExplicationEtape.setY(75);
                texteExplicationEtape.setFont(POLICE_SECONDAIRE);
                panneauPrincipal.getChildren().addAll(texteEtape, texteExplicationEtape);
            }
        }
    }

    /**
     * Texte lors du jeu avec les différents attributs appelés
     * @param nouveau : si ce n'est déjà pas implementé
     */

    public void texteJeu(boolean nouveau){
        String nomAnimal;
        if (getJeu().isAuTourDuMouton()){
            nomAnimal = "mouton";
        } else {
            nomAnimal = "loup";
        }
        if (nouveau){
            texteJeu = new Text("Tour n°"+getJeu().getNbTour()+"\n"+
                    "Nombre de plantes mangées : "+getPlateau().getNbrPlanteMangee()+"\n"+
                    "Au tour du "+nomAnimal+"\n"+
                    "Nombre tour(s) restant pour le mouton : "+getJeu().getDeplacementMouton()+"\n"+
                    "Nombre tour(s) restant pour le loup : "+getJeu().getDeplacementLoup());
            texteJeu.setFont(POLICE_SECONDAIRE);
            texteJeu.setX(500);
            texteJeu.setY(30);
            panneauPrincipal.getChildren().add(texteJeu);
        } else {
            texteJeu.setText("Tour n°"+getJeu().getNbTour()+"\n"+
                    "Nombre de plantes mangées : "+getPlateau().getNbrPlanteMangee()+"\n"+
                    "Au tour du "+nomAnimal+"\n"+
                    "Nombre tour(s) restant pour le mouton : "+getJeu().getDeplacementMouton()+"\n"+
                    "Nombre tour(s) restant pour le loup : "+getJeu().getDeplacementLoup());
        }
    }


    /**
     * Permet de modifier le jeu tout en mettant l'affichage du jeu
     * Si le jeu est fini, va dans la classe ActionFinDeJeu
     */

    public void boucleAffichageJeu() {
        boucleJeu = new AnimationTimer() {
            private long dernierTemps = 0;
            private long tempsAccumule = 0;
            private long tempsEntreDeplacements = (long) (getChoixTempsEntreChaqueDeplacement() * 1_000_000_000); // Conversion en nanosecondes

            @Override
            public void handle(long tempsActuel) {
                if (!estEnPause) {
                    if (dernierTemps == 0) {
                        dernierTemps = tempsActuel;
                    } else {
                        long tempsEcoule = tempsActuel - dernierTemps;
                        dernierTemps = tempsActuel;

                        tempsAccumule += tempsEcoule;

                        // Vérifier si suffisamment de temps s'est écoulé pour effectuer un déplacement
                        if (tempsAccumule >= tempsEntreDeplacements) {
                            AffichageJeu.this.controleur.jeu.boucleJeu();
                            mettreAJourAffichagePlateau();
                            afficherListeCasesPassesEtPheromones();
                            texteJeu(false);
                            if (getJeu().isPartieGagne() || getJeu().isPartiePerdue()) {
                                boutonRetourMenu.setDisable(true);
                                boucleJeu.stop();
                                System.out.println("Le jeu est fini");
                                //Permet de mettre le jeu en pause pendant 3 secondes, mais en mettant bien à jour l'affichage
                                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                                pause.setOnFinished(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        FinDeJeu fin = new FinDeJeu(mainStage, getJeu().isPartieGagne());
                                        mainStage.setScene(fin);
                                    }
                                });
                                pause.play();
                            }

                            tempsAccumule -= tempsEntreDeplacements;
                        }
                    }
                    tempsEntreDeplacements = (long) (getChoixTempsEntreChaqueDeplacement() * 1_000_000_000);
                }
            }
        };

        // Démarrer la boucle
        boucleJeu.start();
    }



    /**
     * Permet de créer un menu déroulant avec un label
     * @param texte : texte du label
     * @param nbMin : nombre minimum du menu déroulant
     * @param nbMax : nombre maximum du menu déroulant
     * @param nbDefaut : nombre affiché par défaut
     * @return : retourne le nombre affiché
     */


    public ChoiceBox<Integer> creerMenuDeroulant(String texte, int nbMin, int nbMax, int nbDefaut) {
        //Cette méthode permet de créer un menu déroulant à partir de différents paramètres
        //D'abord on crée un label avec texte comme paramètre
        Label lab = new Label(texte);
        //On crée un menu déroulant composé de chiffres entiers
        ChoiceBox<Integer> choix = new ChoiceBox<>();
        //On rajoute les chiffres souhaités en faisant une boucle
        for (int i = nbMin; i <= nbMax; i++) {
            choix.getItems().add(i);
        }
        //Permet d'afficher la valeur par défaut
        choix.setValue(nbDefaut);
        //On rajoute le label et la choiceBox dans une arraylist (pour les supprimer ensuite)
        listeMenuDeroulantetLabelEtape1.add(lab);
        listeMenuDeroulantetLabelEtape1.add(choix);
        //On ajoute les 2 dans le panneau2
        panneau2.getChildren().addAll(lab, choix);
        //Returne la ChoiceBox
        return choix;
    }

    /**
     * Permet de retrouver la position i et j de la matrice à partir de la position de la souris
     * @param x : position x de la souris
     * @param y : position y de la souris
     * @return : retourne un tableau de position i, j
     */


    public int[] retrouverPosIetJ(double x, double y) {
        int[] l = new int[2];
        //Permet de convertir la position x et y de la souris en matrice [i][j]
        l[0] = (int) Math.round((x - xDepart) / taille);
        l[1] = (int) Math.round((y - yDepart) / taille);
        return l;
    }

    /**
     * Méthode permettant de créer une image
     * @param imageSource : Un string de l'URL
     * @return : retourne une Image
     */

    public Image creerImage(String imageSource) {
        //permet de récupérer une image dans le répertoire
        return new Image(getClass().getResource(imageSource).toExternalForm(),
                taille, taille, false, false);
    }

    /**
     * Méthode permettant d'afficher un nouveau plateau
     * @param avecActionUtilisateur : permet de donner une action utilisateur ou pas
     */

    public void afficherPlateauJeu(boolean avecActionUtilisateur) {
        //Le but de cette fonction est d'afficher le plateau du jeu dans le panneau principal
        //Permet d'afficher l'explication de l'étape n°1
        //texteEtape();
        panneauPrincipal.getChildren().removeAll(listeCarreNoir);
        panneauPrincipal.getChildren().removeAll(listeImages);
        listeCarreNoir.clear();
        listeImages.clear();
        //Permet d'effacer l'Arraylist listrImages
        //Permet de rendre le bouton 'Valider étape' cliquable
        if (avecActionUtilisateur) {
            boutonValiderEtape.setDisable(false);
        }

        double[] position = taillePlateauEcran();
        double x = position[0];
        double y = position[1];

        xDepart = x;
        y += 50;
        yDepart = y;

        //Permet de récupérer les images dans le répertoire
        Image roche = creerImage(getSource("roche"));
        Image herbe = creerImage(getSource("herbe"));

        //Maintenant on fait une double boucle pour créer un "tableau" en fonction du nombre de colonnes et lignes choisies
        for (int i = 0; i < getPlateau().getLignes(); i++) {
            for (int j = 0; j < getPlateau().getColonnes(); j++) {
                //D'abord on ne créer que des carrés
                Rectangle carre = new Rectangle(x, y, taille, taille);
                carre.setFill(Color.WHITE);
                carre.setStroke(Color.BLACK);
                carre.setStrokeWidth(0.5);
                panneauPrincipal.getChildren().add(carre);
                listeCarreNoir.add(carre);

                //On regarde si la case est de type roche
                if (getPlateau().cases[i][j].getContenu() instanceof Roche) {
                    ImageView imageView = new ImageView(roche); //on créé une image roche
                    imageView.setX(x);
                    imageView.setY(y);
                    //Le but est d'enlever les extrémités
                    if (avecActionUtilisateur) {
                        if (!((i == 0 && j == 0) || (i == 0 && j == getPlateau().getColonnes() - 1) ||
                                (i == getPlateau().getLignes() - 1 && j == 0) || (i == getPlateau().getLignes() - 1 &&
                                j == getPlateau().getColonnes() - 1))) {
                            //remplacerRocheHerbe(imageView); //permet de rajouter une action
                            controleur.remplacerTypeTerrain(imageView, true, false);
                        }
                    }
                    //rajoute imageView dans Arraylist
                    listeImages.add(imageView);
                    panneauPrincipal.getChildren().add(imageView); // on ajoute l'image dans panneauPrincipal
                } else  {
                    mettreImageViewDansPlateau(x,y,avecActionUtilisateur,herbe);
                }
                x += taille; //permet d'avoir une cohérance visuelle
            }
            x = xDepart;
            y += taille; //on fait pareil pour l'axe des ordonnées
        }
    }

    /**
     * Permet d'insérer des ImageView dans le plateau avec la position x et y du panneau, s'il donne une action
     * à l'utilisateur à partir d'une image
     * @param x : position x du panneau
     * @param y : position y du panneau
     * @param avecActionUtilisateur : peut donner une action utilisateur
     * @param objet : l'image à afficher
     */

    public void mettreImageViewDansPlateau(double x, double y, boolean avecActionUtilisateur, Image objet){
        ImageView imageView = new ImageView(objet);
        imageView.setX(x);
        imageView.setY(y);
        if (avecActionUtilisateur) {
            controleur.remplacerTypeTerrain(imageView, false, false);
        }
        listeImages.add(imageView);
        panneauPrincipal.getChildren().add(imageView);
    }

    /**
     * Permet de mettre à jour l'affichage du jeu en fonction de la classe Plateau
     */


    public void mettreAJourAffichagePlateau(){
        Image herbe = creerImage(getSource("herbe"));
        Image roche = creerImage(getSource("roche"));
        Image cactus = creerImage(getSource("cactus"));
        Image marguerite = creerImage(getSource("marguerite"));
        Image mouton = creerImage(getSource("mouton"));
        Image loup = creerImage(getSource("loup"));
        Image terre = creerImage(getSource("terre"));
        Image moutonDanger = creerImage(getSource("mouton2"));
        Image loupMenacant = creerImage(getSource("loup2"));
        for (int i=0; i < getPlateau().getLignes(); i++){
            for (int j=0; j < getPlateau().getColonnes(); j++){
                ImageView img = getImageView(i,j);
                //Si un animal est présent
                if (getCase(i,j).isAnimalPresent()) {
                    //Si le loup n'est pas menaçant
                    if (!this.getJeu().isMoutonEnDanger()) {
                        if (getCase(i,j).getAnimal() instanceof Loup) {
                            img.setImage(loup);
                        } else {
                            img.setImage(mouton);
                        }
                    //Si le loup est menaçant
                    } else if (getCase(i,j).getAnimal() instanceof Loup) {
                        img.setImage(loupMenacant);
                    } else {
                        img.setImage(moutonDanger);
                    }
                //Si c'est pas un animal
                } else if (getCase(i,j).getContenu() instanceof Roche) {
                    img.setImage(roche);
                } else if (getCase(i,j).getContenu() instanceof Herbe) {
                    img.setImage(herbe);
                } else if (getCase(i,j).getContenu() instanceof Cactus) {
                    img.setImage(cactus);
                } else if (getCase(i,j).getContenu() instanceof Marguerite) {
                    img.setImage(marguerite);
                } else {
                    img.setImage(terre);
                }
            }
        }
    }

    /**
     * Tout ce qui en dessous permet de centrer le plateau du jeu pour que peu importe le nombre de lignes
     *  et le nombre de colonnes le plateau soit centré et que la taille des cases s'adaptent pour que tout
     *  soit directement visible
     * @return : retourne un double de position x et y
     */

    public double[] taillePlateauEcran(){
        double[] tableau = new double[2];
        double x = (mainScene.getWidth() * 0.8) * 0.05;
        double y = mainScene.getHeight() * 0.1;
        if ((mainScene.getWidth() * 0.8 - 2 * x) / getPlateau().getColonnes() <=
                (mainScene.getHeight() - 2 * y) / getPlateau().getLignes()) {
            taille = (mainScene.getWidth() * 0.8 - 2 * x) / getPlateau().getColonnes();
            y = (mainScene.getHeight() - taille * getPlateau().getLignes()) / 2;
        } else {
            taille = (mainScene.getHeight() - 2 * y) / getPlateau().getLignes();
            x = (mainScene.getWidth() * 0.8 - taille * getPlateau().getColonnes()) / 2;
        }
        tableau[0] = x;
        tableau[1] = y;
        return tableau;
    }

    /**
     * Permet d'afficher un texte d'alerte si le labyrinthe créé est imparfait
     */


    public void texteAlerteLabyrintheImparfait(){
        texteAlerteLabyrintheImparfait = new Text("Attention labyrinthe imparfait !"+"\n"+
                "Vous ne pouvez pas valider cette étape "+"\n"+
                "tant que : "+"\n"+
                "   *Vous bloquez la case à côté de la sortie"+"\n"+
                "   *Vous avez une case de type herbe isolée"+"\n"+
                "\n"+
                "Veuillez modifier la/les case(s) "+"\n"+
                "correspondantes en type herbe");
        texteAlerteLabyrintheImparfait.setFill(Color.RED);
        texteAlerteLabyrintheImparfait.setFont(POLICE_ALERTE);
        texteAlerteLabyrintheImparfait.setX(10);
        texteAlerteLabyrintheImparfait.setY(20);
    }

    public void afficherListeCasesPassesEtPheromones(){
        panneauPrincipal.getChildren().removeAll(listeCercles);
        listeCercles.clear();
        double tailleObjet = taille / 15;
        //Circle cercle = new Circle(tailleObjet);
        //Rectangle rectangle = new Rectangle(tailleObjet, tailleObjet);
        for (int[] pheromonesMouton : getPlateau().getFilePheromonesMouton()){
            double x = xDepart + taille * pheromonesMouton[1] + tailleObjet * 2;
            double y = yDepart + taille * pheromonesMouton[0] + tailleObjet * 2;
            Circle cercle = new Circle(tailleObjet);
            cercle.setLayoutX(x);
            cercle.setLayoutY(y);
            cercle.setFill(Color.BLACK);
            listeCercles.add(cercle);
        }
        for (int[] pheromonesLoup : getPlateau().getFilePheromonesLoup()){
            double x = xDepart + taille * (pheromonesLoup[1] + 1) - tailleObjet * 2;
            double y = yDepart + taille * pheromonesLoup[0] + tailleObjet * 2;
            Circle cercle = new Circle(tailleObjet);
            cercle.setLayoutX(x);
            cercle.setLayoutY(y);
            cercle.setFill(Color.BLACK);
            listeCercles.add(cercle);
        }
        for (int[] listePassageMouton : getPlateau().getCasesMoutonPassees()){
            double x = xDepart + taille * listePassageMouton[1] + tailleObjet * 2;
            double y = yDepart + taille * (listePassageMouton[0] + 1) - tailleObjet * 2;
            Circle cercle = new Circle(tailleObjet);
            cercle.setLayoutX(x);
            cercle.setLayoutY(y);
            cercle.setFill(Color.BLACK);
            listeCercles.add(cercle);
        }

        for (int[] listePassageLoup : getPlateau().getCasesLoupPassees()){
            double x = xDepart + taille * (listePassageLoup[1] + 1) - tailleObjet * 2;
            double y = yDepart + taille * (listePassageLoup[0] + 1) - tailleObjet * 2;
            Circle cercle = new Circle(tailleObjet);
            cercle.setLayoutX(x);
            cercle.setLayoutY(y);
            cercle.setFill(Color.BLACK);
            listeCercles.add(cercle);
        }

        panneauPrincipal.getChildren().addAll(listeCercles);
    }

    /**
     * Permet de créer un menu déroulant de Double pour le temps entre chaque déplacement
     */


    public void creerMenuDeroulantTemps() {
        //Cette méthode permet de créer un menu déroulant à partir de différents paramètres
        //D'abord on crée un label avec texte comme paramètre
        Label lab = new Label("Choisissez le temps entre chaque \ndéplacement : \n");
        //On crée un menu déroulant composé de chiffres entiers
        choixTempsEntreChaqueDeplacement = new ChoiceBox<>();
        //On rajoute le nombre de secondes entre chaque tour
        choixTempsEntreChaqueDeplacement.getItems().addAll(0.05, 0.1, 0.25, 0.4, 0.5, 0.6, 0.75, 1., 1.25, 1.5, 2., 3., 5., 8., 10.);
        //Permet d'afficher la valeur par défaut
        choixTempsEntreChaqueDeplacement.setValue(0.5);
        VBox vBox = new VBox();

        vBox.getChildren().add(lab);
        vBox.getChildren().add(choixTempsEntreChaqueDeplacement);
        panneau2.getChildren().add(vBox);
    }




}