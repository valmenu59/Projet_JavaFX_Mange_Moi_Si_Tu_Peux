package jeu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import menu.Menu;

public class Controleur {

    //protected Plateau plateau;
    protected Jeu jeu;
    protected AffichageJeu affichageJeu;

    private boolean moutonCree = false;
    private boolean loupCree = false;

    private boolean joue = false;
    private boolean sortieCree = false;




    public Controleur(AffichageJeu affichageJeu){
        //this.plateau = null;
        this.jeu = new Jeu();
        this.affichageJeu = affichageJeu;
    }





    public void remplacerTypeTerrain(ImageView img, boolean remplacerRocheParHerbe, boolean isSortie){
        img.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //On récupère la position x et y de l'image
                double x = img.getX();
                double y = img.getY();
                //Cette méthode permet de convertir les positions x et y en matrice[i][j]
                int[] liste = affichageJeu.retrouverPosIetJ(x, y);
                int j = liste[0];
                int i = liste[1];

                //On initie toutes les images à partir de la source

                Image herbe = affichageJeu.creerImage(affichageJeu.getSource("herbe"));
                Image roche = affichageJeu.creerImage(affichageJeu.getSource("roche"));
                Image cactus = affichageJeu.creerImage(affichageJeu.getSource("cactus"));
                Image marguerite = affichageJeu.creerImage(affichageJeu.getSource("marguerite"));
                Image mouton = affichageJeu.creerImage(affichageJeu.getSource("mouton"));
                Image loup = affichageJeu.creerImage(affichageJeu.getSource("loup"));



                //Ne marche qu'à l'étape n°2
                if (affichageJeu.getNumeroEtape() == 2 && remplacerRocheParHerbe) {
                    //si la sortie n'est pas crée
                    if (!(sortieCree)) {
                        getPlateau().setCaseSortie(i,j);
                        sortieCree = true;
                        remplacerTypeTerrain(img, true, true);
                        //On remplace l'image roche par une image herbe
                        img.setImage(herbe);
                        //La case sélectionnée devient de type herbe
                        getCase(i,j).setContenuGeneral(new Herbe());
                        //Permet d'activer le bouton valider
                        affichageJeu.getBoutonValiderEtape().setDisable(false);
                        //Sinon dans l'autre cas si la sortie est créée est que le terrain sélectionné est de type herbe
                    } else if (sortieCree && getCase(i,j).getContenu().getNom().equals("Herbe")) {
                        //Même principe qu'au dessus
                        sortieCree = false;
                        remplacerTypeTerrain(img,true,false);
                        img.setImage(roche);
                        getCase(i,j).setContenuGeneral(new Roche());
                        affichageJeu.getBoutonValiderEtape().setDisable(true);
                    }
                }
                //Ne marche qu'à l'étape n°3
                else if (affichageJeu.getNumeroEtape() == 3 && !remplacerRocheParHerbe) {
                    if (getCase(i,j).getContenu().getNom().equals("Herbe")) {
                        img.setImage(cactus);
                        getCase(i,j).setContenuGeneral(new Cactus());
                    } else if (getCase(i,j).getContenu().getNom().equals("Cactus")) {
                        img.setImage(marguerite);
                        getCase(i,j).setContenuGeneral(new Marguerite());
                    } else if (getCase(i,j).getContenu().getNom().equals("Marguerite")) {
                        if (isSortie) {
                            img.setImage(herbe);
                            getCase(i,j).setContenuGeneral(new Herbe());
                        } else {
                            img.setImage(roche);
                            getCase(i,j).setContenuGeneral(new Roche());
                        }
                    } else {
                        img.setImage(herbe);
                        getCase(i,j).setContenuGeneral(new Herbe());
                    }
                    affichageJeu.texteAlerteLabyrintheImparfait();

                    if (getPlateau().verifPlateauCorrect()){
                        affichageJeu.getBoutonValiderEtape().setDisable(false);
                        if (affichageJeu.getPanneauSecondaire().getChildren().size() == 6){
                            affichageJeu.getPanneauSecondaire().getChildren().remove(5);
                        }
                    } else {
                        affichageJeu.getBoutonValiderEtape().setDisable(true);
                        affichageJeu.getPanneauSecondaire().getChildren().add(affichageJeu.getTexteAlerteLabyrintheImparfait());
                    }
                }

                else if (affichageJeu.getNumeroEtape() == 4 && !remplacerRocheParHerbe) {
                    if (getCase(i,j).getContenu() instanceof Plante){
                        if (!moutonCree && !loupCree) {
                            img.setImage(mouton);
                            getCase(i,j).setAnimal(new Mouton());
                            moutonCree = true;
                        }
                        else if (!loupCree) {
                            img.setImage(loup);
                            getCase(i,j).setAnimal(new Loup());
                            loupCree = true;
                        } else if (getCase(i,j).animalPresent()) {
                            if (getCase(i,j).getContenu().getNom().equals("Herbe")){
                                img.setImage(herbe);
                            } else if (getCase(i,j).getContenu().getNom().equals("Cactus")){
                                img.setImage(cactus);
                            } else {
                                img.setImage(marguerite);
                            }
                            getCase(i,j).removeAnimal();
                        } else if (loupCree && !moutonCree){
                            img.setImage(mouton);
                            getCase(i,j).setAnimal(new Mouton());
                            moutonCree = true;
                        }
                        loupCree = getPlateau().verifPresenceAnimal("Loup");
                        moutonCree = getPlateau().verifPresenceAnimal("Mouton");
                        affichageJeu.getBoutonValiderEtape().setDisable(!moutonCree || !loupCree);
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
                jeu.plateau = new Plateau(affichageJeu.getChoixNbLignes(), affichageJeu.getChoixNbColonnes());
                //On crée le plateau
                getPlateau().creerPlateau();
                //Permet de visualiser le plateau sur JavaFX
                affichageJeu.afficherPlateauJeu(true);
                sortieCree = false;
            }
        });
    }


    public void validerEtape(Button b) {
        //Le but de cette étape est de rajouter une action Valider au bouton
        b.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                if (affichageJeu.getNumeroEtape() == 1) {
                    //Passage à l'étape n°2
                    affichageJeu.etape2(false);
                } else if (affichageJeu.getNumeroEtape() == 2) {
                    //Passage à l'étape n°3
                    affichageJeu.etape3();
                } else if (affichageJeu.getNumeroEtape() == 3){
                    affichageJeu.etape4();
                } else if (affichageJeu.getNumeroEtape() == 4){
                    affichageJeu.etapeJeu();
                }
            }
        });
    }

    public void boutonCreerLabyrinthe(Button b){
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getPlateau().creerLabyrinthe();
                getPlateau().printMatrices();
                affichageJeu.mettreAJourAffichagePlateau();
            }
        });
    }


    public void retournerEtapePrecedente(Button b){
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (affichageJeu.getNumeroEtape() == 2){
                    affichageJeu.etape1(true);
                } else if (affichageJeu.getNumeroEtape() == 3){
                    affichageJeu.etape2(true);
                } else if (affichageJeu.getNumeroEtape() == 4){
                    affichageJeu.etape3();
                }
            }
        });
    }

    public void retournerAuMenu(Button b, Stage stage){
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                affichageJeu.getBoucleJeu().stop();
                Menu menu = new Menu(stage);
                stage.setScene(menu);
            }
        });
    }

    public void mettreEnPause(Button b){
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (affichageJeu.estBienEnPause()){
                    b.setText("Marche");
                    affichageJeu.mettreEnPauseOuPas(false);
                    affichageJeu.getBoucleJeu().stop();
                } else {
                    b.setText("Pause");
                    affichageJeu.mettreEnPauseOuPas(true);
                    affichageJeu.getBoucleJeu().start();
                }
            }
        });
    }



    public boolean sortieBienCree(){
        return sortieCree;
    }

    public Plateau getPlateau(){
        return this.jeu.plateau;
    }

    public Case getCase(int i, int j){
        return this.jeu.plateau.cases[i][j];
    }




}
