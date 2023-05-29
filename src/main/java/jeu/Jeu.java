package jeu;

import sauvegarde.DossierSauvegarde;

import java.io.*;

public class Jeu {


    protected Plateau plateau = null;
    private boolean auTourDuMouton = true;
    private int deplacementMouton = 2;
    private int deplacementLoup = 3;
    private int planteDeplacementMouton = 2;
    private int nbTour = -1;
    private int planteMangee = 0;
    private boolean moutonEnDanger = false;
    private final DossierSauvegarde sauvegarde = new DossierSauvegarde();





    public Jeu(){
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

    public int getNbPlanteMangee() {
        return this.planteMangee;
    }

    public void estBienAuTourDuMouton(boolean b) {
        this.auTourDuMouton = b;
    }

    public boolean isMoutonEnDanger() {
        return this.moutonEnDanger;
    }

    public int getNbTour() {
        return this.nbTour;
    }

    public void moutonAMangePlante(){
        this.planteMangee++;
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
