package jeu;

import sauvegarde.DossierSauvegarde;

import java.io.*;

public class Jeu {


    protected Plateau plateau;
    private boolean auTourDuMouton;
    private int deplacementMouton;
    private int deplacementLoup;
    private int planteDeplacementMouton;
    private int nbTour;
    private boolean moutonEnDanger = false; //pour la sae 202
    private boolean partiePerdue = false; //pour la sae 202
    private boolean partieGagne = false; //pour la sae 202
    private final DossierSauvegarde sauvegarde = new DossierSauvegarde();





    public Jeu(){
        plateau = null;
        auTourDuMouton = true;
        deplacementMouton = 2;
        deplacementLoup = 3;
        planteDeplacementMouton = 2;
        nbTour = -1;

    }




    public void boucleJeu() {
        if (auTourDuMouton) {
            if (deplacementMouton == planteDeplacementMouton) {
                this.nbTour++;
                this.plateau.planteQuiPousse();
            }
            deplacementMouton--;
            plateau.deplacerAnimal("Mouton");
            if (deplacementMouton == 0) {
                int[] caseM = plateau.getCaseMouton();
                int nbr = plateau.moutonMangePlante(caseM[0], caseM[1]);
                setDeplacementMouton(nbr);
                planteDeplacementMouton = nbr;
                auTourDuMouton = false;
            }
        } else {
            deplacementLoup--;
            plateau.deplacerAnimal("Loup");
            if (deplacementLoup == 0) {
                reinitialiserDeplacementLoup();
                auTourDuMouton = true;
            }
        }
    }

    public void setDeplacementMouton(int nbr) {
        this.deplacementMouton = nbr;
    }

    public void reinitialiserDeplacementLoup() {
        this.deplacementLoup = 3;
    }



    public boolean isAuTourDuMouton() {
        return auTourDuMouton;
    }

    public boolean isMoutonEnDanger() {
        return this.moutonEnDanger;
    }

    public boolean isPartiePerdue(){
        return this.partiePerdue;
    }

    public boolean isPartieGagne(){
        return this.partieGagne;
    }

    public int getNbTour() {
        return this.nbTour;
    }

    public int getDeplacementLoup(){
        return this.deplacementLoup;
    }

    public int getDeplacementMouton(){
        return this.deplacementMouton;
    }







    public void sauvegarderPlateau(){
        FileOutputStream fichier = null;
        try {
            fichier = new FileOutputStream(sauvegarde.getCheminDaccesSauvegarde());
            ObjectOutputStream objetFichier = new ObjectOutputStream(fichier);

            objetFichier.writeInt(this.plateau.getColonnes()); // Sauvegarde du nombre de colonnes
            objetFichier.writeInt(this.plateau.getLignes());

            //objetFichier.writeObject(this.plateau.cases); // Sauvegarde de l'objet plateau
            for (int i = 0; i < this.plateau.getLignes(); i++){
                for (int j = 0; j < this.plateau.getColonnes(); j++){
                    objetFichier.writeObject(this.plateau.cases[i][j]);
                }
            }

            objetFichier.close(); // Fermeture de ObjectOutputStream
            System.out.println("Fichier sauvegardé avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Assurez-vous de fermer le FileOutputStream après utilisation
            if (fichier != null) {
                try {
                    fichier.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    public void reprendreSauvegarde() {
        try (FileInputStream fileInputStream = new FileInputStream(sauvegarde.getCheminDaccesSauvegarde());
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            int colonnes = objectInputStream.readInt();
            int lignes = objectInputStream.readInt();
            System.out.println("Colonnes: " + colonnes);
            System.out.println("Lignes: " + lignes);
            plateau = new Plateau(lignes,colonnes);
            plateau.creerPlateau();

            for (int i = 0; i < lignes; i++){
                for (int j = 0; j < colonnes; j++){
                    plateau.cases[i][j] = (Case) objectInputStream.readObject();
                }
            }
            System.out.println("Fichier récupé avec succès !");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}
