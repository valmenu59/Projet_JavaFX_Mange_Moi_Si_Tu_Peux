package jeu;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Jeu {
    //Reprend les classes du jeu
    protected Plateau plateau;
    protected Plateau plateauTest;
    private NbTour tour = new NbTour();

    //Récupère la source des images
    private static final String IMG_CACTUS = "/ElementsJeu/cactusv2.png";
    private static final String IMG_HERBE = "/ElementsJeu/HERBEv2.jpg";
    private static final String IMG_LOUP = "/ElementsJeu/LOUPv2.png";
    private static final String IMG_MARGUERITE = "/ElementsJeu/MARGUERITEv2.png";
    private static final String IMG_MOUTON = "/ElementsJeu/MOUTONv2.jpg";
    private static final String IMG_ROCHER = "/ElementsJeu/ROCHERv2.jpg";
    private static final String IMG_TERRE = "/ElementsJeu/vide.png";
    private AnchorPane panneauPrincipal;
    private Stage mainStage;
    private Scene mainScene;
    private VBox panneau2;
    private double taille;
    private ChoiceBox<Integer> choixNblignes;
    private ChoiceBox<Integer> choixNbColonnes;
    private AnimationTimer tempsJeu;
    private Button boutonCreerPlateau;
    private Button boutonValiderEtape;
    private Text texteEtape;
    private Text texteExplicationEtape;
    private final ArrayList<Label> listeLabel = new ArrayList<>();
    private final ArrayList<ChoiceBox<Integer>> listeDeListeDeroulant = new ArrayList<>();
    private final ArrayList<Rectangle> listeRectangleRougeTransparant = new ArrayList<>();
    private final ArrayList<ImageView> listeImages = new ArrayList<>();
    private double xDepart;
    private double yDepart;
    private boolean sortieCree = false;
    private int numeroEtape;
    private boolean moutonCree = false;
    private boolean loupCree = false;

    private boolean joue = false;

    String musicFile = "/bruh.wav"; // Remplacez cette chaîne par le chemin vers votre fichier audio




    public Jeu(Scene mainScene, AnchorPane panneau) {
        this.tour = new NbTour();
        this.mainScene = mainScene;

        //Permet de créer la base pour les autres éléments visuels
        creerFenetreJeu(panneau);


        jouerSon();


        //On va vers l'étape n°1
        etape1();

    }

    public Jeu(int colonnes, int lignes){
        //sert uniquement pour les tests, il n'y a aucun composant JavaFx
        plateauTest = new Plateau(lignes,colonnes);
        plateauTest.creerPlateau();

    }


    public void etape1() {
        //Initialisation de l'attribut numeroEtape
        numeroEtape = 1;
        //On crée un texte
        Text texte = new Text("Options");
        //Cette méthode permet le texte du jeu
        texteEtape();
        //On initie un plateau vide
        plateau = null;
        //On rajoute le texte dans panneau2
        panneau2.getChildren().add(texte);
        //Cette méthode permet de créer un menu déroulant avec un chiffre mini, maxi et celui affiché par défaut
        choixNbColonnes = creerMenuDeroulant("Nombre de colonnes : ", 10, 18, 14);
        choixNblignes = creerMenuDeroulant("Nombre de lignes : ", 7, 14, 9);
        //Ensuite on crée différents boutons
        boutonCreerPlateau = new Button("Créer votre plateau");
        validerLignesColonnes(boutonCreerPlateau);
        panneau2.getChildren().add(boutonCreerPlateau);
        boutonValiderEtape = new Button("Valider cette étape");
        validerEtape(boutonValiderEtape);
        boutonValiderEtape.setDisable(true); //Permet de rendre le bouton inactif
        panneau2.getChildren().add(boutonValiderEtape);
    }

    public void jouerSon(){
        Media musique = new Media(getClass().getResource("/The Underdog Project - Summer Jam (Official Video HD).mp3").toExternalForm());
        MediaPlayer jouerMusique = new MediaPlayer(musique);

        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if (keyEvent.getCode() == KeyCode.O ){
                    System.out.println("ici");
                    AudioClip sound = new AudioClip(getClass().getResource(musicFile).toExternalForm());
                    sound.play();

                } else if (keyEvent.getCode() == KeyCode.C) {
                    if (!joue) {
                        jouerMusique.play();
                        joue = true;
                        System.out.println("Lecture de la musique...");
                    } else {
                        jouerMusique.pause();
                        joue = false;
                        System.out.println("Musique en pause.");
                    }
                } else if (keyEvent.getCode() == KeyCode.S){
                    jouerMusique.stop();
                    joue = false;
                    System.out.println("Musique arrêtée");
                }
            }
        });
    }


    public void etape2() {
        //Passage à l'étape 2
        this.numeroEtape = 2;
        boutonValiderEtape.setDisable(true);
        //Avant on supprime tout ce qu'on a plus besoin
        panneau2.getChildren().removeAll(listeLabel);
        panneau2.getChildren().removeAll(listeDeListeDeroulant);
        panneau2.getChildren().remove(boutonCreerPlateau);
        //On vide les 2 ArrayList
        listeLabel.clear();
        listeDeListeDeroulant.clear();
        //Permet de placer des rectangles rouges transparants
        placerRectanglesRougesTransparants();
        //Permet d'afficher le texte sur le panneau principal
        texteEtape();
    }

    public void etape3() {
        this.numeroEtape = 3;
        changerActionCaseSortie();
        placerRectanglesRougesTransparants();
        texteEtape();
        Button boutonCreerLabyrinthe = new Button("Générer un labyrinthe aléatoire");
        panneau2.getChildren().add(1,boutonCreerLabyrinthe);

    }

    public void etape4(){
        this.numeroEtape = 4;
        boutonValiderEtape.setDisable(true);
        boutonValiderEtape.setText("Commencer le jeu");
        panneau2.getChildren().remove(1);
        texteEtape();
    }


    public void creerFenetreJeu(AnchorPane panneau) {
        //On récupère l'AnchorPane de la scène
        mainScene = panneau.getScene();
        //On va à la racine
        Pane parentPane = (Pane) mainScene.getRoot();
        //On supprime l'AnchorPane de Menu
        parentPane.getChildren().remove(panneau);

        //Création du VBox à la partie gauche qui prend 20% de la scène
        this.panneau2 = new VBox();
        panneau2.setPrefWidth(mainScene.getWidth() * 0.20);

        // Création de l'AnchorPane pour la partie droite
        this.panneauPrincipal = new AnchorPane();

        // Création du SplitPane
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(panneau2, panneauPrincipal);
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
            for (int i = 0; i < this.plateau.getLignes(); i++) {
                for (int j = 0; j < this.plateau.getColonnes(); j++) {
                    if ((i == 0 && j == 0) || (i == 0 && j == this.plateau.getColonnes() - 1) ||
                            (i == this.plateau.getLignes() - 1 && j == 0) || (i == this.plateau.getLignes() - 1 &&
                            j == this.plateau.getColonnes() - 1) || (i != 0 && j != 0 && j != this.plateau.getColonnes() - 1 &&
                            i != plateau.getLignes() -1 )){
                        creerRectangleRougeTansparant(i,j);
                    }
                }
            }
        } else if (numeroEtape == 3){
            panneauPrincipal.getChildren().removeAll(listeRectangleRougeTransparant);
            listeRectangleRougeTransparant.clear();
            for (int i = 0; i < this.plateau.getLignes(); i++) {
                for (int j = 0; j < this.plateau.getColonnes(); j++) {

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
        for (int i = 0; i < this.plateau.getLignes(); i++) {
            for (int j = 0; j < this.plateau.getColonnes(); j++) {
                if (i == 0 || j == 0 || i == plateau.getLignes() - 1 || j == plateau.getColonnes() - 1){
                    if (this.plateau.cases[i][j].getContenu().getNom().equals("Herbe")){
                        ImageView img = listeImages.get(i * plateau.getColonnes() + j);
                        remplacerTypeTerrain(img, false,true);
                    }
                }
            }
        }
    }

    public void texteEtape() {
        //Cette méthode permet de créer un texte en fonction du numéro de l'étape
        if (numeroEtape == 1) {
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
        }
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
        listeLabel.add(lab);
        listeDeListeDeroulant.add(choix);
        //On ajoute les 2 dans le panneau2
        panneau2.getChildren().addAll(lab, choix);
        //Returne la ChoiceBox
        return choix;
    }

    /**
     *
     * @param img
     * @param remplacerRocheParHerbe
     * @param isSortie
     */


    public void remplacerTypeTerrain(ImageView img, boolean remplacerRocheParHerbe, boolean isSortie){
        img.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //On récupère la position x et y de l'image
                double x = img.getX();
                double y = img.getY();
                //Cette méthode permet de convertir les positions x et y en matrice[i][j]
                int[] liste = retrouverPosIetJ(x, y);
                int j = liste[0];
                int i = liste[1];

                //On initie toutes les images à partir de la source
                Image herbe = creerImage(IMG_HERBE);
                Image roche = creerImage(IMG_ROCHER);
                Image cactus = creerImage(IMG_CACTUS);
                Image marguerite = creerImage(IMG_MARGUERITE);
                Image mouton = creerImage(IMG_MOUTON);
                Image loup = creerImage(IMG_LOUP);


                //Ne marche qu'à l'étape n°2
                if (numeroEtape == 2 && remplacerRocheParHerbe) {
                    //si la sortie n'est pas crée
                    if (!(sortieCree)) {
                        sortieCree = true;
                        //On remplace l'image roche par une image herbe
                        img.setImage(herbe);
                        //La case sélectionnée devient de type herbe
                        Jeu.this.plateau.cases[i][j].setContenuGeneral(new Herbe());
                        //Permet d'activer le bouton valider
                        boutonValiderEtape.setDisable(false);
                        //Sinon dans l'autre cas si la sortie est créée est que le terrain sélectionné est de type herbe
                    } else if (sortieCree && Jeu.this.plateau.cases[i][j].getContenu().getNom().equals("Herbe")) {
                        //Même principe qu'au dessus
                        sortieCree = false;
                        img.setImage(roche);
                        Jeu.this.plateau.cases[i][j].setContenuGeneral(new Roche());
                        boutonValiderEtape.setDisable(true);
                    }
                }
                //Ne marche qu'à l'étape n°3
                else if (numeroEtape == 3 && !remplacerRocheParHerbe) {
                    if (Jeu.this.plateau.cases[i][j].getContenu().getNom().equals("Herbe")) {
                        img.setImage(cactus);
                        Jeu.this.plateau.cases[i][j].setContenuGeneral(new Cactus());
                    } else if (Jeu.this.plateau.cases[i][j].getContenu().getNom().equals("Cactus")) {
                        img.setImage(marguerite);
                        Jeu.this.plateau.cases[i][j].setContenuGeneral(new Marguerite());
                    } else if (Jeu.this.plateau.cases[i][j].getContenu().getNom().equals("Marguerite")) {
                        if (isSortie) {
                            img.setImage(herbe);
                            Jeu.this.plateau.cases[i][j].setContenuGeneral(new Herbe());
                        } else {
                            img.setImage(roche);
                            Jeu.this.plateau.cases[i][j].setContenuGeneral(new Roche());
                        }
                    } else {
                        img.setImage(herbe);
                        Jeu.this.plateau.cases[i][j].setContenuGeneral(new Herbe());
                    }
                }

                else if (numeroEtape == 4 && !remplacerRocheParHerbe) {
                    if (Jeu.this.plateau.cases[i][j].getContenu() instanceof Plante){
                        if (!moutonCree && !loupCree) {
                            img.setImage(mouton);
                            Jeu.this.plateau.cases[i][j].setAnimal(new Mouton());
                            moutonCree = true;
                        }
                        else if (!loupCree) {
                            img.setImage(loup);
                            Jeu.this.plateau.cases[i][j].setAnimal(new Loup());
                            loupCree = true;
                        } else if (Jeu.this.plateau.cases[i][j].animalPresent()) {
                            if (Jeu.this.plateau.cases[i][j].getContenu().getNom().equals("Herbe")){
                                img.setImage(herbe);
                            } else if (Jeu.this.plateau.cases[i][j].getContenu().getNom().equals("Cactus")){
                                img.setImage(cactus);
                            } else {
                                img.setImage(marguerite);
                            }
                            Jeu.this.plateau.cases[i][j].removeAnimal();
                        } else if (loupCree && !moutonCree){
                            img.setImage(mouton);
                            Jeu.this.plateau.cases[i][j].setAnimal(new Mouton());
                            moutonCree = true;
                        }
                        loupCree = verifPresenceAnimal("Loup");
                        moutonCree = verifPresenceAnimal("Mouton");
                        boutonValiderEtape.setDisable(!moutonCree || !loupCree);
                    }
                }
            }
        });
    }

    public boolean verifPresenceAnimal(String n){
        for (int i = 0; i < this.plateau.getLignes(); i++) {
            for (int j = 0; j < this.plateau.getColonnes(); j++) {
                if (this.plateau.cases[i][j].animalPresent()) {
                    if (this.plateau.cases[i][j].getAnimal().getNom().equals(n)) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    public void validerLignesColonnes(Button b) {
        //Cette méthode permet de donner l'action de créer le plateau de jeu visuellement et en mémoire
        b.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                //On supprime tout du panneau principal
                panneauPrincipal.getChildren().clear();
                //On initie un nouveau plateau en mémoire
                Jeu.this.plateau = new Plateau(getChoixNbLignes(), getChoixNbColonnes());
                //On crée le plateau
                Jeu.this.plateau.creerPlateau();
                //Permet de visualiser le plateau sur JavaFX
                afficherPlateauJeu();
            }
        });
    }

    public void validerEtape(Button b) {
        //Le but de cette étape est de rajouter une action Valider au bouton
        b.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                if (numeroEtape == 1) {
                    //Passage à l'étape n°2
                    etape2();
                } else if (numeroEtape == 2) {
                    //Passage à l'étape n°3
                    etape3();
                } else if (numeroEtape == 3){
                    etape4();
                }
            }
        });
    }

    public int getChoixNbLignes() {
        //Permet de récupérer la valeur choisie du menu déroulant
        return choixNblignes.getValue();
    }

    public int getChoixNbColonnes() {
        //Permet de récupérer la valeur choisie du menu déroulant
        return choixNbColonnes.getValue();
    }


    public Image creerImage(String imageSource) {
        //permet de récupérer une image dans le répertoire
        Image image = new Image(getClass().getResource(imageSource).toExternalForm(),
                taille, taille, false, false);
        return image;
    }

    public void afficherPlateauJeu() {
        //Le but de cette fonction est d'afficher le plateau du jeu dans le panneau principal
        //Permet d'afficher l'explication de l'étape n°1
        texteEtape();
        //Permet d'effacer l'Arraylist listrImages
        listeImages.clear();
        //Permet de rendre le bouton 'Valider étape' cliquable
        boutonValiderEtape.setDisable(false);
        /*
        Tout ce qui en dessous permet de centrer le plateau du jeu pour que peu importe le nombre de lignes
        et le nombre de colonnes le plateau soit centré et que la taille des cases s'adaptent pour que tout
        soit directement visible
         */
        double x = (mainScene.getWidth() * 0.8) * 0.05;
        double y = mainScene.getHeight() * 0.1;
        if ((mainScene.getWidth() * 0.8 - 2 * x) / this.plateau.getColonnes() <=
                (mainScene.getHeight() - 2 * y) / this.plateau.getLignes()) {
            taille = (mainScene.getWidth() * 0.8 - 2 * x) / this.plateau.getColonnes();
            y = (mainScene.getHeight() - taille * this.plateau.getLignes()) / 2;
        } else {
            taille = (mainScene.getHeight() - 2 * y) / this.plateau.getLignes();
            x = (mainScene.getWidth() * 0.8 - taille * this.plateau.getColonnes()) / 2;
        }
        xDepart = x;
        y += 50;
        yDepart = y;

        //Permet de récupérer les images dans le répertoire
        Image roche = creerImage(IMG_ROCHER);
        Image herbe = creerImage(IMG_HERBE);


        //Maintenant on fait une double boucle pour créer un "tableau" en fonction du nombre de colonnes et lignes choisies
        for (int i = 0; i < this.plateau.getLignes(); i++) {
            for (int j = 0; j < this.plateau.getColonnes(); j++) {
                //D'abord on ne créer que des carrés
                Rectangle carre = new Rectangle(x, y, taille, taille);
                carre.setFill(Color.WHITE);
                carre.setStroke(Color.BLACK);
                carre.setStrokeWidth(0.5);
                panneauPrincipal.getChildren().add(carre);
                //On regarde si la case est de type roche
                if (this.plateau.cases[i][j].getContenu().getNom().equals("Roche")) {
                    ImageView imageView = new ImageView(roche); //on créé une image roche
                    imageView.setX(x);
                    imageView.setY(y);
                    //Le but est d'enlever les extrémités
                    if (!((i == 0 && j == 0) || (i == 0 && j == this.plateau.getColonnes() - 1) ||
                            (i == this.plateau.getLignes() - 1 && j == 0) || (i == this.plateau.getLignes() - 1 &&
                            j == this.plateau.getColonnes() - 1))) {
                        //remplacerRocheHerbe(imageView); //permet de rajouter une action
                        remplacerTypeTerrain(imageView,true,false);
                    }
                    //rajoute imageView dans Arraylist
                    listeImages.add(imageView);
                    panneauPrincipal.getChildren().add(imageView); // on ajoute l'image dans panneauPrincipal
                } else {
                    ImageView imageView = new ImageView(herbe);
                    imageView.setX(x);
                    imageView.setY(y);
                    remplacerTypeTerrain(imageView, false, false);
                    //rajoute imageView dans Arraylist
                    listeImages.add(imageView);
                    panneauPrincipal.getChildren().add(imageView);
                }
                x += taille; //permet d'avoir une cohérance visuelle
            }
            x = xDepart;
            y += taille; //on fait pareil pour l'axe des ordonnées
        }
    }


    public Stage getMainStage() {
        return mainStage;
    }

    public Plateau getPlateau(){
        //sert uniquement pour les tests Junit
        return this.plateauTest;
    }



}