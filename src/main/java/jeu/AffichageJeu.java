package jeu;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import menu.Menu;

public class AffichageJeu extends Scene {
    //Reprend les classes du jeu
    //protected Plateau plateau;
    protected Controleur controleur;
    //protected Plateau plateauTest;
    private NbTour tour = new NbTour();

    //Récupère la source des images
    private static final String IMG_CACTUS = "/ElementsJeu/CactusColors.png";
    private static final String IMG_HERBE = "/ElementsJeu/HerbeColors.png";
    private static final String IMG_LOUP = "/ElementsJeu/LoupColors.png";
    private static final String IMG_MARGUERITE = "/ElementsJeu/MargueColors.png";
    private static final String IMG_MOUTON = "/ElementsJeu/MoutonColors.png";
    private static final String IMG_ROCHER = "/ElementsJeu/RocherColors.png";
    private static final String IMG_TERRE = "/ElementsJeu/vide.png";
    private AnchorPane panneauTemporaire;
    private AnchorPane panneauPrincipal;
    //private SplitPane separateur;
    private Stage mainStage;
    private Scene mainScene;
    private VBox panneau2;
    private double taille;
    private ChoiceBox<Integer> choixNblignes;
    private ChoiceBox<Integer> choixNbColonnes;
    private final ArrayList<Node> listeMenuDeroulantetLabelEtape1 = new ArrayList<>();
    private AnimationTimer tempsJeu;
    private Button boutonCreerPlateau;
    private Button boutonValiderEtape;
    private Button boutonRetour;
    private Button boutonCreerLabyrinthe;
    private Button boutonRetourMenu;
    private Button boutonPause;
    private Text texteEtape = null;
    private Text texteExplicationEtape = null;
    private Text texteAlerteLabyrintheImparfait = null;
    //private final ArrayList<Label> listeLabel = new ArrayList<>();
    //private final ArrayList<ChoiceBox<Integer>> listeDeListeDeroulant = new ArrayList<>();
    private final ArrayList<Rectangle> listeRectangleRougeTransparant = new ArrayList<>();
    private final ArrayList<Rectangle> listeCarreNoir = new ArrayList<>();
    private final ArrayList<ImageView> listeImages = new ArrayList<>();
    private double xDepart;
    private double yDepart;
    private Menu menu;
    private AnimationTimer boucleJeu;

    private boolean estEnPause;

    private int numeroEtape;








    //public Jeu(Scene mainScene, AnchorPane panneau) {
    public AffichageJeu(Stage stage, boolean vientDeLaSauvegarde){
        super(new AnchorPane(), 1280,720);
        panneauTemporaire = (AnchorPane) getRoot();
        mainStage = stage;


        this.tour = new NbTour();
        //menu = new Menu();
        //this.mainScene = menu.getMainScene();

        //Permet de créer la base pour les autres éléments visuels
        creerFenetreJeu(panneauTemporaire);

        //On initie un plateau vide
        controleur = new Controleur(this);


        if (vientDeLaSauvegarde){
            this.controleur.jeu.reprendreSauvegarde();
            afficherPlateauJeu(false);
        } else {
            //On va vers l'étape n°1
            etape1(false);
        }

    }




    public void etape1(boolean retour) {
        //Initialisation de l'attribut numeroEtape
        numeroEtape = 1;
        texteEtape();
        if (!retour){
            //On crée un texte
            Text texte = new Text("Options");
            //Cette méthode permet le texte du jeu


            //On rajoute le texte dans panneau2
            panneau2.getChildren().add(texte);
            //Cette méthode permet de créer un menu déroulant avec un chiffre mini, maxi et celui affiché par défaut
            choixNbColonnes = creerMenuDeroulant("Nombre de colonnes : ", 10, 18, 14);
            choixNblignes = creerMenuDeroulant("Nombre de lignes : ", 7, 14, 9);
            //Ensuite on crée différents boutons
            boutonCreerPlateau = new Button("Créer votre plateau");
            controleur.validerLignesColonnes(boutonCreerPlateau);
            panneau2.getChildren().add(boutonCreerPlateau);
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
            panneauPrincipal.getChildren().removeAll(listeRectangleRougeTransparant);
            panneau2.getChildren().addAll(1,listeMenuDeroulantetLabelEtape1);
            panneau2.getChildren().add(5,boutonCreerPlateau);
            boutonValiderEtape.setDisable(false);
            boutonRetour.setDisable(true);
        }

    }




    public void etape2(boolean retour) {
        //Passage à l'étape 2
        this.numeroEtape = 2;
        //Permet de placer des rectangles rouges transparants
        placerRectanglesRougesTransparants();
        boutonRetour.setDisable(false);
        //Permet d'afficher le texte sur le panneau principal
        texteEtape();
        if (controleur.sortieBienCree()) {
            boutonValiderEtape.setDisable(false);
        } else {
            boutonValiderEtape.setDisable(true);
        }
        //boutonRetour.setDisable(false);
        if (!retour) {
            //Avant on supprime tout ce qu'on a plus besoin
            panneau2.getChildren().removeAll(listeMenuDeroulantetLabelEtape1);
            panneau2.getChildren().remove(boutonCreerPlateau);
        } else {
            panneau2.getChildren().remove(boutonCreerLabyrinthe);
        }
        //On vide les 2 ArrayList
        //listeLabel.clear();
        //listeDeListeDeroulant.clear();


    }

    public void etape3() {
        this.numeroEtape = 3;
        if (boutonValiderEtape.getText().equals("Commencer le jeu")){
            boutonValiderEtape.setText("Valider cette étape");
        }
        texteAlerteLabyrintheImparfait();
        changerActionCaseSortie();
        placerRectanglesRougesTransparants();
        texteEtape();
        boutonCreerLabyrinthe = new Button("Générer un labyrinthe aléatoire");
        controleur.boutonCreerLabyrinthe(boutonCreerLabyrinthe);
        panneau2.getChildren().add(1,boutonCreerLabyrinthe);
        /*
        if (getPlateau().verifPlateauCorrect()){
            boutonValiderEtape.setDisable(false);
        } else {
            boutonValiderEtape.setDisable(true);
        }

         */
    }

    public void etape4(){
        this.numeroEtape = 4;
        boutonValiderEtape.setDisable(true);
        boutonValiderEtape.setText("Commencer le jeu");
        panneau2.getChildren().remove(1);
        texteEtape();
    }

    public void etapeJeu(){
        this.controleur.jeu.sauvegarderPlateau();
        numeroEtape = 5;
        texteEtape();
        panneau2.getChildren().remove(boutonRetour);
        panneau2.getChildren().remove(boutonValiderEtape);
        estEnPause = false;

        boutonPause = new Button("Pause");
        controleur.mettreEnPause(boutonPause);
        panneau2.getChildren().add(boutonPause);

        boucleAffichageJeu();
    }


    public void creerFenetreJeu(AnchorPane panneau) {
        //On récupère l'AnchorPane de la scène
        mainScene = panneau.getScene();
        //On va à la racine
        Pane parentPane = (Pane) mainScene.getRoot();
        //On supprime l'AnchorPane de Menu
        parentPane.getChildren().remove(panneau);
        //separateur = (SplitPane) getRoot();

        //Création du VBox à la partie gauche qui prend 20% de la scène
        this.panneau2 = new VBox();
        panneau2.setPrefWidth(mainScene.getWidth() * 0.20);

        // Création de l'AnchorPane pour la partie droite
        this.panneauPrincipal = new AnchorPane();

        // Création du SplitPane
        SplitPane splitPane = new SplitPane(panneau2,panneauPrincipal);
        //splitPane.addAll(panneau2, panneauPrincipal);
        //Permet de diviser les deux parties à 20% de la taille de la scène
        splitPane.setDividerPositions(0.20);
        //Permet de ne plus redimensionner les panneaux
        panneau2.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.20));
        panneau2.minWidthProperty().bind(splitPane.widthProperty().multiply(0.20));
        //on rajoute à la racine le splitPane
        mainScene.setRoot(splitPane);
        //Permet de définir le style de panneau2
        panneau2.setStyle("-fx-background-color: rgb(220,241,196)");

    }

    public void placerRectanglesRougesTransparants(){
        if (numeroEtape == 2){
            for (int i = 0; i < getPlateau().getLignes(); i++) {
                for (int j = 0; j < getPlateau().getColonnes(); j++) {
                    if ((i == 0 && j == 0) || (i == 0 && j == getPlateau().getColonnes() - 1) ||
                            (i == getPlateau().getLignes() - 1 && j == 0) || (i == getPlateau().getLignes() - 1 &&
                            j == getPlateau().getColonnes() - 1) || (i != 0 && j != 0 && j != getPlateau().getColonnes() - 1 &&
                            i != getPlateau().getLignes() -1 )){
                        creerRectangleRougeTansparant(i,j);
                    }
                }
            }
        } else if (numeroEtape == 3){
            panneauPrincipal.getChildren().removeAll(listeRectangleRougeTransparant);
            listeRectangleRougeTransparant.clear();
            for (int i = 0; i < getPlateau().getLignes(); i++) {
                for (int j = 0; j < getPlateau().getColonnes(); j++) {
                    //Non fini
                }
            }
        }
    }


    public void creerRectangleRougeTansparant(double i, double j){
        double x = xDepart + j * taille;
        double y = yDepart + i * taille;
        Rectangle rectangle = new Rectangle(x,y,taille,taille);
        rectangle.setFill(Color.rgb(200,0,0,0.33));
        listeRectangleRougeTransparant.add(rectangle);
        panneauPrincipal.getChildren().add(rectangle);
    }

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




    public void texteEtape() {
        //Cette méthode permet de créer un texte en fonction du numéro de l'étape
        if (numeroEtape == 1) {
            if (texteEtape == null) {
                Font font = Font.font("Segoe UI", FontWeight.BOLD, 25); //Permet de modifier la police
                Font font1 = Font.font("Segoe UI", 15);
                texteEtape = new Text("Etape 1 : Choisissez la taille de votre plateau");
                texteEtape.setX(50);
                texteEtape.setY(50);
                texteEtape.setFont(font);
                texteExplicationEtape = new Text("Pour cette étape, choisissez votre nombre de lignes et de colonnes que vous souhaitez à la partie gauche de l'écran." + "\n" +
                        "Cliquez sur 'Créer votre votre plateau' pour créer un plateau sur l'écran puis cliquez sur 'Valider cette étape' pour passer à l'étape n°2");
                texteExplicationEtape.setX(50);
                texteExplicationEtape.setY(75);
                texteExplicationEtape.setFont(font1);
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
            texteEtape.setText("Jeu en cours...");
            texteExplicationEtape.setText("Maintenant laissons tourner la simulation !");
        }
    }


/*
    public void boucleAffichageJeu(){
        boucleJeu = new AnimationTimer() {
            @Override
            public void handle(long l) {
                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    System.out.println("je suis dans la boucle");
                    AffichageJeu.this.controleur.jeu.boucleJeu();
                    mettreAJourAffichagePlateau();
                }
            }
        };
        boucleJeu.start();
    }

 */

    public void boucleAffichageJeu(){
        boucleJeu = new AnimationTimer() {
            @Override
            public void handle(long l) {
                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    System.out.println("Je suis dans la boucle");
                    AffichageJeu.this.controleur.jeu.boucleJeu();
                    mettreAJourAffichagePlateau();
                }
            }
        };
        boucleJeu.start();
    }


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
        //listeLabel.add(lab);
        //listeDeListeDeroulant.add(choix);
        listeMenuDeroulantetLabelEtape1.add(lab);
        listeMenuDeroulantetLabelEtape1.add(choix);
        //On ajoute les 2 dans le panneau2
        panneau2.getChildren().addAll(lab, choix);
        //Returne la ChoiceBox
        return choix;
    }





    public int[] retrouverPosIetJ(double x, double y) {
        int[] l = new int[2];
        //Permet de convertir la position x et y de la souris en matrice [i][j]
        l[0] = (int) Math.round((x - xDepart) / taille);
        l[1] = (int) Math.round((y - yDepart) / taille);
        return l;
    }

    public double[] retrouverPosXetY(int i, int j){
        double[] l = new double[2];
        l[0]  = xDepart + j * taille;
        l[1] = yDepart + i * taille;
        return l;
    }

    public int getChoixNbLignes() {
        //Permet de récupérer la valeur choisie du menu déroulant
        return choixNblignes.getValue();
    }

    public int getChoixNbColonnes() {
        //Permet de récupérer la valeur choisie du menu déroulant
        return choixNbColonnes.getValue();
    }




    public Button getBoutonValiderEtape(){
        return this.boutonValiderEtape;
    }




    public Image creerImage(String imageSource) {
        //permet de récupérer une image dans le répertoire
        Image image = new Image(getClass().getResource(imageSource).toExternalForm(),
                taille, taille, false, false);
        return image;
    }

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
        Image cactus = creerImage(getSource("cactus"));
        Image marguerite = creerImage(getSource("marguerite"));
        Image mouton = creerImage(getSource("mouton"));
        Image loup = creerImage(getSource("loup"));
        Image terre = creerImage(getSource("terre"));

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
                } else if (getCase(i,j).getContenu() instanceof Herbe) {
                    mettreImageViewDansPlateau(x,y,avecActionUtilisateur,herbe);
                    /*
                    ImageView imageView = new ImageView(herbe);
                    imageView.setX(x);
                    imageView.setY(y);
                    if (avecActionUtilisateur) {
                        controleur.remplacerTypeTerrain(imageView, false, false);
                    }
                    //rajoute imageView dans Arraylist
                    listeImages.add(imageView);
                    panneauPrincipal.getChildren().add(imageView);

                     */
                } else if (getCase(i,j).getContenu() instanceof Cactus){
                    mettreImageViewDansPlateau(x,y,avecActionUtilisateur,cactus);
                } else if (getCase(i,j).getContenu() instanceof Marguerite){
                    mettreImageViewDansPlateau(x,y,avecActionUtilisateur,marguerite);
                }
                if (getCase(i,j).animalPresent()){
                    if (getCase(i,j).getAnimal() instanceof Mouton){
                        mettreImageViewDansPlateau(x,y,avecActionUtilisateur,mouton);
                    } else {
                        mettreImageViewDansPlateau(x,y,avecActionUtilisateur,loup);
                    }
                }
                x += taille; //permet d'avoir une cohérance visuelle
            }
            x = xDepart;
            y += taille; //on fait pareil pour l'axe des ordonnées
        }
    }

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




    public void mettreAJourAffichagePlateau(){
        Image herbe = creerImage(getSource("herbe"));
        Image roche = creerImage(getSource("roche"));
        Image cactus = creerImage(getSource("cactus"));
        Image marguerite = creerImage(getSource("marguerite"));
        Image mouton = creerImage(getSource("mouton"));
        Image loup = creerImage(getSource("loup"));
        Image terre = creerImage(getSource("terre"));
        for (int i=0; i < getPlateau().getLignes(); i++){
            for (int j=0; j < getPlateau().getColonnes(); j++){
                ImageView img = getImageView(i,j);
                if (getPlateau().cases[i][j].getContenu() instanceof Roche){
                    img.setImage(roche);
                } else if (getPlateau().cases[i][j].getContenu() instanceof Herbe){
                    img.setImage(herbe);
                } else if (getPlateau().cases[i][j].getContenu() instanceof Cactus){
                    img.setImage(cactus);
                } else if (getPlateau().cases[i][j].getContenu() instanceof Marguerite){
                    img.setImage(marguerite);
                } else {
                    img.setImage(terre);
                }

                if (getPlateau().cases[i][j].animalPresent()){
                    if (getPlateau().cases[i][j].getAnimal() instanceof Loup){
                        img.setImage(loup);
                    } else {
                        img.setImage(mouton);
                    }
                }
            }
        }
    }

    public double[] taillePlateauEcran(){
        /*
        Tout ce qui en dessous permet de centrer le plateau du jeu pour que peu importe le nombre de lignes
        et le nombre de colonnes le plateau soit centré et que la taille des cases s'adaptent pour que tout
        soit directement visible
         */
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


    public void texteAlerteLabyrintheImparfait(){
        Font font = Font.font("Segoe UI", FontWeight.BOLD, 12);
        texteAlerteLabyrintheImparfait = new Text("Attention labyrinthe imparfait !"+"\n"+
                "Vous ne pouvez pas valider cette étape "+"\n"+
                "tant que : "+"\n"+
                "   *Vous bloquez la case à côté de la sortie"+"\n"+
                "   *Vous avez une case de type herbe isolée"+"\n"+
                "\n"+
                "Veuillez modifier la/les case(s) "+"\n"+
                "correspondantes en type herbe");
        texteAlerteLabyrintheImparfait.setFill(Color.RED);
        texteAlerteLabyrintheImparfait.setFont(font);
        texteAlerteLabyrintheImparfait.setX(10);
        texteAlerteLabyrintheImparfait.setY(20);
    }


    public int getNumeroEtape(){
        return this.numeroEtape;
    }


    public Stage getMainStage() {
        return mainStage;
    }



    public AnchorPane getPanneauPrincipale(){
        return this.panneauPrincipal;
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
            default -> {
                return null;
            }
        }
    }

    public ImageView getImageView(int i, int j){
        return listeImages.get(i * getPlateau().getColonnes() + j );
    }

    public Scene getMainScene(){
        return this.mainScene;
    }

    public Text getTexteAlerteLabyrintheImparfait(){
        return this.texteAlerteLabyrintheImparfait;
    }

    public AnimationTimer getBoucleJeu(){
        return this.boucleJeu;
    }

    public boolean estBienEnPause(){
        return estEnPause;
    }

    public void mettreEnPauseOuPas(boolean b){
        this.estEnPause = b;
    }

    public Jeu getJeu(){
        return this.controleur.jeu;
    }









}