package jeu;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Jeu {
    protected Plateau plateau;
    private NbTour tour = new NbTour();
    private static final String IMG_CACTUS = "/ElementsJeu/cactus.png";
    private static final String IMG_HERBE = "/ElementsJeu/herbe.png";
    private static final String IMG_LOUP = "/ElementsJeu/loup.png";
    private static final String IMG_MARGUERITE = "/ElementsJeu/marguerite.png";
    private static final String IMG_MOUTON = "/ElementsJeu/mouton.png";
    private static final String IMG_ROCHER = "/ElementsJeu/rocher.png";
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
    private Rectangle rectangleInvisible;
    private Text texteEtape;
    private Text texteExplicationEtape;
    private ArrayList<Label> listeLabel = new ArrayList();
    private ArrayList<ChoiceBox<Integer>> listeDeListeDeroulant = new ArrayList();
    private double xDepart;
    private double yDepart;
    private boolean sortieCree = false;
    private int numeroEtape;

    public Jeu(int l, int h, Scene mainScene, AnchorPane panneau) {
        //On récupère la scène
        this.mainScene = mainScene;
        //Permet de créer la fenêtre du jeu
        creerFenetreJeu(panneau);
        //Va à la méthode étape 1
        etape1();
    }

    public void etape1() {
        //Permet d'initialiser le numéro étape par 1
        this.numeroEtape = 1;
        //On rajoute un texte à la VBox
        Text texte = new Text("Options");
        panneau2.getChildren().add(texte);
        //Permet d'afficher le texte de l'étape au panneau principal
        texteEtape();
        //On initie la valeur du plateau par null
        this.plateau = null;
        //Permet de créer un menu déroulant
        choixNbColonnes = creerMenuDeroulant("Nombre de colonnes : ", 10, 18, 14);
        choixNblignes = creerMenuDeroulant("Nombre de lignes : ", 7, 14, 9);
        //Ce bouton permet de créer le plateau
        boutonCreerPlateau = new Button("Créer votre plateau");
        //On rajoute un event au bouton
        boutonCreerPlateau = validerLignesColonnes(boutonCreerPlateau);
        //On rajoute ce bouton au panneau 2
        panneau2.getChildren().add(boutonCreerPlateau);
        //Ce bouton permet de valider les étapes
        boutonValiderEtape = new Button("Valider cette étape");
        boutonValiderEtape = validerEtape(boutonValiderEtape);
        boutonValiderEtape.setDisable(true);
        panneau2.getChildren().add(boutonValiderEtape);
    }

    public void etape2() {
        this.numeroEtape = 2;
        this.boutonValiderEtape.setDisable(true);
        this.panneauPrincipal.getChildren().remove(this.rectangleInvisible);
        this.panneau2.getChildren().removeAll(this.listeLabel);
        this.panneau2.getChildren().removeAll(this.listeDeListeDeroulant);
        this.panneau2.getChildren().remove(this.boutonCreerPlateau);
        this.listeLabel.clear();
        this.listeDeListeDeroulant.clear();
        this.texteEtape();
    }

    public void etape3() {
        this.numeroEtape = 3;
        this.texteEtape();
    }

    public void creerFenetreJeu(AnchorPane panneau) {
        this.mainScene = panneau.getScene();
        Pane parentPane = (Pane)this.mainScene.getRoot();
        parentPane.getChildren().remove(panneau);
        this.panneau2 = new VBox();
        this.panneau2.setPrefWidth(this.mainScene.getWidth() * 0.2);
        this.panneauPrincipal = new AnchorPane();
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(this.panneau2, this.panneauPrincipal);
        splitPane.setDividerPositions(0.2);
        this.panneau2.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.2));
        this.panneau2.minWidthProperty().bind(splitPane.widthProperty().multiply(0.2));
        this.mainScene.setRoot(splitPane);
        this.panneau2.setStyle("-fx-background-color: rgb(179,204,161)");
    }

    public void texteEtape() {
        if (this.numeroEtape == 1) {
            Font font = Font.font("Segoe UI", FontWeight.BOLD, 25.0);
            Font font1 = Font.font("Segoe UI", 15.0);
            this.texteEtape = new Text("Etape 1 : Choisissez la taille de votre plateau");
            this.texteEtape.setX(50.0);
            this.texteEtape.setY(50.0);
            this.texteEtape.setFont(font);
            this.texteExplicationEtape = new Text("Pour cette étape, choisissez votre nombre de lignes et de colonnes que vous souhaitez à la partie gauche de l'écran.\nCliquez sur 'Créer votre votre plateau' pour créer un plateau sur l'écran puis cliquez sur 'Valider cette étape' pour passer à l'étape n°2");
            this.texteExplicationEtape.setX(50.0);
            this.texteExplicationEtape.setY(75.0);
            this.texteExplicationEtape.setFont(font1);
            this.panneauPrincipal.getChildren().addAll(new Node[]{this.texteEtape, this.texteExplicationEtape});
        } else if (this.numeroEtape == 2) {
            this.texteEtape.setText("Etape 2 : Choisissez l'emplacement de sortie");
            this.texteExplicationEtape.setText("Pour cette étape, choisissez une unique sortie c'est à dire que vous remplacez une case rocher par une case herbe.\nPour cela, sur le plateau, cliquez sur un rocher sauf les extrémités puis cliquez sur 'Valider cette étape' pour passer à l'étape n°3");
        } else if (this.numeroEtape == 3) {
            this.texteEtape.setText("Etape 3 : Personnalisez le plateau du jeu");
            this.texteExplicationEtape.setText("Pour cette étape, cliquez sur terrain puis sélectionnez votre type de terrain souhaité.\nAttention il n'est pas possible de boucher la sortie ni de séparer le terrain en 2, il faut qu'à partir de tout plante on puisse sortir !");
        }

    }

    public ChoiceBox<Integer> creerMenuDeroulant(String texte, int nbMin, int nbMax, int nbDefaut) {
        Label lab = new Label(texte);
        ChoiceBox<Integer> choix = new ChoiceBox();

        for(int i = nbMin; i <= nbMax; ++i) {
            choix.getItems().add(i);
        }

        choix.setValue(nbDefaut);
        listeLabel.add(lab);
        listeDeListeDeroulant.add(choix);
        panneau2.getChildren().addAll(lab, choix);
        return choix;
    }

    public ImageView remplacerRocheHerbe(final ImageView img) {
        img.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                double x = img.getX();
                double y = img.getY();
                int[] liste = Jeu.this.retrouverPosIetJ(x, y);
                int j = liste[0];
                int i = liste[1];
                Image roche;
                if (!Jeu.this.sortieCree) {
                    Jeu.this.sortieCree = true;
                    roche = Jeu.this.creerImage("/ElementsJeu/herbe.png");
                    img.setImage(roche);
                    Jeu.this.plateau.cases[i][j] = new Case(new Herbe());
                    Jeu.this.boutonValiderEtape.setDisable(false);
                } else if (Jeu.this.sortieCree && Jeu.this.plateau.cases[i][j].getContenu() instanceof Herbe) {
                    Jeu.this.sortieCree = false;
                    roche = Jeu.this.creerImage("/ElementsJeu/rocher.png");
                    img.setImage(roche);
                    Jeu.this.plateau.cases[i][j] = new Case(new Roche());
                    Jeu.this.boutonValiderEtape.setDisable(true);
                }

            }
        });
        return img;
    }

    public int[] retrouverPosIetJ(double x, double y) {
        int[] l = new int[]{(int)Math.round((x - this.xDepart) / this.taille), (int)Math.round((y - this.yDepart) / this.taille)};
        return l;
    }

    public Button validerLignesColonnes(Button b) {
        b.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                Jeu.this.panneauPrincipal.getChildren().clear();
                Jeu.this.plateau = new Plateau(Jeu.this.getChoixNbLignes(), Jeu.this.getChoixNbColonnes());
                Jeu.this.plateau.creerPlateau();
                Jeu.this.afficherPlateauJeu();
                Jeu.this.rectangleInvisible = new Rectangle(0.0, 0.0, Jeu.this.panneauPrincipal.getWidth(), Jeu.this.panneauPrincipal.getHeight());
                Jeu.this.rectangleInvisible.setFill(Color.TRANSPARENT);
                Jeu.this.panneauPrincipal.getChildren().add(Jeu.this.rectangleInvisible);
            }
        });
        return b;
    }

    public Button validerEtape(Button b) {
        b.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                if (Jeu.this.numeroEtape == 1) {
                    Jeu.this.etape2();
                } else if (Jeu.this.numeroEtape == 2) {
                    Jeu.this.etape3();
                }

            }
        });
        return b;
    }

    public int getChoixNbLignes() {
        return (Integer)this.choixNblignes.getValue();
    }

    public int getChoixNbColonnes() {
        return (Integer)this.choixNbColonnes.getValue();
    }

    public Image creerImage(String imageSource) {
        Image image = new Image(this.getClass().getResource(imageSource).toExternalForm(), this.taille, this.taille, true, false);
        return image;
    }

    public void afficherPlateauJeu() {
        this.texteEtape();
        this.boutonValiderEtape.setDisable(false);
        double x = this.mainScene.getWidth() * 0.8 * 0.05;
        double y = this.mainScene.getHeight() * 0.1;
        if ((this.mainScene.getWidth() * 0.8 - 2.0 * x) / (double)this.plateau.getColonnes() <= (this.mainScene.getHeight() - 2.0 * y) / (double)this.plateau.getLignes()) {
            this.taille = (this.mainScene.getWidth() * 0.8 - 2.0 * x) / (double)this.plateau.getColonnes();
            y = (this.mainScene.getHeight() - this.taille * (double)this.plateau.getLignes()) / 2.0;
        } else {
            this.taille = (this.mainScene.getHeight() - 2.0 * y) / (double)this.plateau.getLignes();
            x = (this.mainScene.getWidth() * 0.8 - this.taille * (double)this.plateau.getColonnes()) / 2.0;
        }

        this.xDepart = x;
        y += 50.0;
        this.yDepart = y;
        Image roche = this.creerImage("/ElementsJeu/rocher.png");
        Image herbe = this.creerImage("/ElementsJeu/herbe.png");

        for(int i = 0; i < this.plateau.getLignes(); ++i) {
            for(int j = 0; j < this.plateau.getColonnes(); ++j) {
                Rectangle carre = new Rectangle(x, y, this.taille, this.taille);
                carre.setFill(Color.WHITE);
                carre.setStroke(Color.BLACK);
                carre.setStrokeWidth(0.5);
                this.panneauPrincipal.getChildren().add(carre);
                ImageView imageView;
                if (!this.plateau.cases[i][j].getContenu().getNom().equals("Roche")) {
                    imageView = new ImageView(herbe);
                    imageView.setX(x);
                    imageView.setY(y);
                    this.panneauPrincipal.getChildren().add(imageView);
                } else {
                    imageView = new ImageView(roche);
                    imageView.setX(x);
                    imageView.setY(y);
                    if ((i != 0 || j != 0) && (i != 0 || j != this.plateau.getColonnes() - 1) && (i != this.plateau.getLignes() - 1 || j != 0) && (i != this.plateau.getLignes() - 1 || j != this.plateau.getColonnes() - 1)) {
                        imageView = this.remplacerRocheHerbe(imageView);
                    }

                    this.panneauPrincipal.getChildren().add(imageView);
                }

                x += this.taille;
            }

            x = this.xDepart;
            y += this.taille;
        }

    }

    public Stage getMainStage() {
        return this.mainStage;
    }

    public void jeu() {
        this.plateau.printMatrices();
    }
}
