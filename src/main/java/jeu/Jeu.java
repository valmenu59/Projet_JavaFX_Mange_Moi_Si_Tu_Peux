package jeu;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Jeu {
    //Reprend les méthodes du jeu :
    protected Plateau plateau;
    private NbTour tour;

    //Pour ajouter les images au jeu :
    private static final String IMG_CACTUS = "/ElementsJeu/cactus.png";
    private static final String IMG_HERBE = "/ElementsJeu/herbe.png";
    private static final String IMG_LOUP = "/ElementsJeu/loup.png";
    private static final String IMG_MARGUERITE = "/ElementsJeu/marguerite.png";
    private static final String IMG_MOUTON = "/ElementsJeu/mouton.png";
    private static final String IMG_ROCHER = "/ElementsJeu/rocher.png";
    private static final String IMG_TERRE = "/ElementsJeu/vide.png";

    //Appel des éléments de JavaFX :
    private AnchorPane panneauPrincipal;
    private Stage mainStage;
    private Scene mainScene;
    private VBox panneau2;

    private double taille;



    public Jeu(int l, int h, Scene mainScene, AnchorPane panneau){
        this.plateau = new Plateau(l,h);
        this.plateau.creerPlateau();
        this.tour = new NbTour();
        this.mainScene = mainScene;

        mainScene = panneau.getScene();
        Pane parentPane = (Pane) mainScene.getRoot();
        parentPane.getChildren().remove(panneau);


        jeuFX();



    }

    public void jeuFX(){
        this.panneau2 = new VBox();
        panneau2.setPrefWidth(mainScene.getWidth() * 0.20);

        // Création de l'AnchorPane pour la partie droite
        this.panneauPrincipal = new AnchorPane();

        // Création du SplitPane
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(panneau2, panneauPrincipal);
        splitPane.setDividerPositions(0.20);
        //Permet de ne plus redimensionner les panneaux
        panneau2.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.20));
        panneau2.minWidthProperty().bind(splitPane.widthProperty().multiply(0.20));

        mainScene.setRoot(splitPane);

        panneau2.setStyle("-fx-background-color: rgb(200, 200, 200)");

        Text texte = new Text("Options");
        Text texte2 = new Text("Plateau du jeu : ");
        texte2.setX((mainScene.getWidth()*0.8/2) - texte2.getLayoutBounds().getWidth());
        texte2.setY(50);

        panneau2.getChildren().add(texte);
        panneauPrincipal.getChildren().add(texte2);
        //afficherImage();
        afficherPlateauJeu();

    }

    public void afficherImage(){
        int x = 0;
        Image image = new Image(getClass().getResource(IMG_ROCHER).toExternalForm(),
                100,100,true,false);
        for (int i=0; i<2; i++) {
            ImageView imageView = new ImageView(image);
            imageView.setX(x);
            panneauPrincipal.getChildren().add(imageView);
            x+= 150;
        }
    }

    public ImageView enleverRoche(ImageView i ){
        i.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double x = i.getX();
                double y = i.getY();
                panneauPrincipal.getChildren().remove(i);
                Image herbe = new Image(getClass().getResource(IMG_HERBE).toExternalForm(),
                        taille,taille,true,false);
                ImageView imageView = new ImageView(herbe);
                imageView.setX(x);
                imageView.setY(y);
                panneauPrincipal.getChildren().add(imageView);
            }
        });
        return i;
    }

    public void afficherPlateauJeu(){
        double x = (mainScene.getWidth()*0.8)*0.05 ;
        double y = mainScene.getHeight() * 0.1;
        if ((mainScene.getWidth()*0.8 - 2*x) / this.plateau.getColonnes() <=
                (mainScene.getHeight() - 2*y) / this.plateau.getLignes()){
            taille = (mainScene.getWidth()*0.8 - 2*x) / this.plateau.getColonnes();
            y = (mainScene.getHeight() - taille * this.plateau.getLignes()) / 2;
        } else {
            taille = (mainScene.getHeight() - 2*y ) / this.plateau.getLignes();
            x = (mainScene.getWidth() * 0.8 - taille * this.plateau.getColonnes()) / 2;
        }
        double xDepart = x;

        Image roche  = new Image(getClass().getResource(IMG_ROCHER).toExternalForm(),
                taille,taille,true,false);
        Image herbe = new Image(getClass().getResource(IMG_HERBE).toExternalForm(),
                taille,taille,true,false);
        for (int i=0; i < this.plateau.getLignes(); i++){
            for (int j=0; j < this.plateau.getColonnes(); j++){
                Rectangle carre = new Rectangle(x,y,taille,taille);
                carre.setFill(Color.WHITE);
                carre.setStroke(Color.BLACK);
                carre.setStrokeWidth(0.5);
                panneauPrincipal.getChildren().add(carre);
                if (this.plateau.cases[i][j].getContenu().getNom().equals("Roche")){
                    ImageView imageView = new ImageView(roche);
                    imageView.setX(x);
                    imageView.setY(y);
                    if (!((i == 0 && j == 0) || (i == 0 && j == this.plateau.getColonnes() - 1) ||
                            (i == this.plateau.getLignes() - 1 && j == 0) || (i == this.plateau.getLignes() - 1 &&
                            j == this.plateau.getColonnes() - 1))){
                        imageView = enleverRoche(imageView);
                    }
                    panneauPrincipal.getChildren().add(imageView);
                } else {
                    ImageView imageView = new ImageView(herbe);
                    imageView.setX(x);
                    imageView.setY(y);
                    panneauPrincipal.getChildren().add(imageView);
                }
                x+=taille;
            }
            x = xDepart;
            y+= taille;
        }

    }




    public Stage getMainStage() {
        return mainStage;
    }

    public void jeu(){
        this.plateau.printMatrices();
    }


}
