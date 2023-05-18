package jeu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controleur {

    protected Plateau plateau;
    protected Jeu jeu;

    private boolean moutonCree = false;
    private boolean loupCree = false;

    private boolean joue = false;
    private boolean sortieCree = false;

    private int[] caseSortie = new int[2];


    public Controleur(Jeu jeu){
        this.plateau = null;
        this.jeu = jeu;
    }





    public void remplacerTypeTerrain(ImageView img, boolean remplacerRocheParHerbe, boolean isSortie){
        img.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //On récupère la position x et y de l'image
                double x = img.getX();
                double y = img.getY();
                //Cette méthode permet de convertir les positions x et y en matrice[i][j]
                int[] liste = jeu.retrouverPosIetJ(x, y);
                int j = liste[0];
                int i = liste[1];

                //On initie toutes les images à partir de la source

                Image herbe = jeu.creerImage(jeu.getSource("herbe"));
                Image roche = jeu.creerImage(jeu.getSource("roche"));
                Image cactus = jeu.creerImage(jeu.getSource("cactus"));
                Image marguerite = jeu.creerImage(jeu.getSource("marguerite"));
                Image mouton = jeu.creerImage(jeu.getSource("mouton"));
                Image loup = jeu.creerImage(jeu.getSource("loup"));



                //Ne marche qu'à l'étape n°2
                if (jeu.getNumeroEtape() == 2 && remplacerRocheParHerbe) {
                    //si la sortie n'est pas crée
                    if (!(sortieCree)) {
                        sortieCree = true;
                        //On remplace l'image roche par une image herbe
                        img.setImage(herbe);
                        //La case sélectionnée devient de type herbe
                        Controleur.this.plateau.cases[i][j].setContenuGeneral(new Herbe());
                        //Permet d'activer le bouton valider
                        jeu.getBoutonValiderEtape().setDisable(false);
                        //Sinon dans l'autre cas si la sortie est créée est que le terrain sélectionné est de type herbe
                    } else if (sortieCree && plateau.cases[i][j].getContenu().getNom().equals("Herbe")) {
                        //Même principe qu'au dessus
                        sortieCree = false;
                        img.setImage(roche);
                        plateau.cases[i][j].setContenuGeneral(new Roche());
                        jeu.getBoutonValiderEtape().setDisable(true);
                    }
                }
                //Ne marche qu'à l'étape n°3
                else if (jeu.getNumeroEtape() == 3 && !remplacerRocheParHerbe) {
                    if (Controleur.this.plateau.cases[i][j].getContenu().getNom().equals("Herbe")) {
                        img.setImage(cactus);
                        Controleur.this.plateau.cases[i][j].setContenuGeneral(new Cactus());
                    } else if (Controleur.this.plateau.cases[i][j].getContenu().getNom().equals("Cactus")) {
                        img.setImage(marguerite);
                        Controleur.this.plateau.cases[i][j].setContenuGeneral(new Marguerite());
                    } else if (Controleur.this.plateau.cases[i][j].getContenu().getNom().equals("Marguerite")) {
                        if (isSortie) {
                            img.setImage(herbe);
                            Controleur.this.plateau.cases[i][j].setContenuGeneral(new Herbe());
                        } else {
                            img.setImage(roche);
                            Controleur.this.plateau.cases[i][j].setContenuGeneral(new Roche());
                        }
                    } else {
                        img.setImage(herbe);
                        Controleur.this.plateau.cases[i][j].setContenuGeneral(new Herbe());
                    }
                }

                else if (jeu.getNumeroEtape() == 4 && !remplacerRocheParHerbe) {
                    if (Controleur.this.plateau.cases[i][j].getContenu() instanceof Plante){
                        if (!moutonCree && !loupCree) {
                            img.setImage(mouton);
                            Controleur.this.plateau.cases[i][j].setAnimal(new Mouton());
                            moutonCree = true;
                        }
                        else if (!loupCree) {
                            img.setImage(loup);
                            Controleur.this.plateau.cases[i][j].setAnimal(new Loup());
                            loupCree = true;
                        } else if (Controleur.this.plateau.cases[i][j].animalPresent()) {
                            if (Controleur.this.plateau.cases[i][j].getContenu().getNom().equals("Herbe")){
                                img.setImage(herbe);
                            } else if (Controleur.this.plateau.cases[i][j].getContenu().getNom().equals("Cactus")){
                                img.setImage(cactus);
                            } else {
                                img.setImage(marguerite);
                            }
                            Controleur.this.plateau.cases[i][j].removeAnimal();
                        } else if (loupCree && !moutonCree){
                            img.setImage(mouton);
                            Controleur.this.plateau.cases[i][j].setAnimal(new Mouton());
                            moutonCree = true;
                        }
                        loupCree = plateau.verifPresenceAnimal("Loup");
                        moutonCree = plateau.verifPresenceAnimal("Mouton");
                        jeu.getBoutonValiderEtape().setDisable(!moutonCree || !loupCree);
                    }
                }
            }
        });
    }


    public void validerLignesColonnes(Button b) {
        //Cette méthode permet de donner l'action de créer le plateau de jeu visuellement et en mémoire
        b.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                //On supprime tout du panneau principal
                //jeu.getPanneauPrincipale().getChildren().clear();
                //On initie un nouveau plateau en mémoire
                plateau = new Plateau(jeu.getChoixNbLignes(), jeu.getChoixNbColonnes());
                //On crée le plateau
                plateau.creerPlateau();
                //Permet de visualiser le plateau sur JavaFX
                jeu.afficherPlateauJeu();
            }
        });
    }


    public void validerEtape(Button b) {
        //Le but de cette étape est de rajouter une action Valider au bouton
        b.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                if (jeu.getNumeroEtape() == 1) {
                    //Passage à l'étape n°2
                    jeu.etape2(false);
                } else if (jeu.getNumeroEtape() == 2) {
                    //Passage à l'étape n°3
                    jeu.etape3();
                } else if (jeu.getNumeroEtape() == 3){
                    jeu.etape4();
                } else if (jeu.getNumeroEtape() == 4){
                    //
                }
            }
        });
    }


    public void retournerEtapePrecedente(Button b){
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (jeu.getNumeroEtape() == 2){
                    jeu.etape1(true);
                } else if (jeu.getNumeroEtape() == 3){
                    jeu.etape2(true);
                } else if (jeu.getNumeroEtape() == 4){
                    jeu.etape3();
                }
            }
        });
    }

    public void setSortie(int i, int j){
        caseSortie[0] = i;
        caseSortie[1] = j;
    }

    public boolean sortieBienCree(){
        return sortieCree;
    }








}
