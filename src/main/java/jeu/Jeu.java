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


    /**
     * Constructeur de la classe jeu
     */


    public Jeu(){
        plateau = null;
        auTourDuMouton = true;
        deplacementMouton = 2;
        deplacementLoup = 3;
        planteDeplacementMouton = 2;
        nbTour = -1;

    }

    /////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////GET, SET, IS//////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

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



    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////FIN GET, SET, IS////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    /**
     * Permet de faire une boucle en déplaçant les animaux, et de vérifier si la partie est terminée ou pas
     */


    public void boucleJeu() {
        System.out.println("Loup menaçant "+ plateau.manhattan());
        moutonEnDanger = plateau.manhattan();
        if (auTourDuMouton) {
            if (deplacementMouton == planteDeplacementMouton) {
                this.nbTour++;
                this.plateau.planteQuiPousse();
            }
            deplacementMouton--;
            plateau.deplacerAnimal("Mouton", moutonEnDanger);
            if (deplacementMouton == 0) {
                int[] caseM = plateau.getCaseMouton();
                int nbr = plateau.moutonMangePlante(caseM[0], caseM[1]);
                setDeplacementMouton(nbr);
                planteDeplacementMouton = nbr;
                auTourDuMouton = false;
            }
        } else {
            deplacementLoup--;
            plateau.deplacerAnimal("Loup", moutonEnDanger);
            if (deplacementLoup == 0) {
                reinitialiserDeplacementLoup();
                auTourDuMouton = true;
            }
        }
    }

    /**
     * Méthode permettant de sauvegarder l'état du plateau créé
     * Créer un fichier binaire
     */


    public void sauvegarderPlateau(){
        FileOutputStream fichier = null;
        try {
            fichier = new FileOutputStream(sauvegarde.getCheminDaccesSauvegarde());
            ObjectOutputStream objetFichier = new ObjectOutputStream(fichier);

            objetFichier.writeInt(this.plateau.getColonnes()); // Sauvegarde du nombre de colonnes
            objetFichier.writeInt(this.plateau.getLignes()); // Sauvegarde du nombre de lignes

            for (int i = 0; i < this.plateau.getLignes(); i++){
                for (int j = 0; j < this.plateau.getColonnes(); j++){
                    objetFichier.writeObject(this.plateau.cases[i][j]); // Sauvegarde de l'objet case[i][j]
                }
            }

            objetFichier.close(); // Fermeture de ObjectOutputStream
            System.out.println("Fichier sauvegardé avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // permet de fermer le FileOutputStream après utilisation
            if (fichier != null) {
                try {
                    fichier.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Méthode permettant de reprendre l'état du plateau venant d'un fichier binaire à partir
     * d'un emplacement précis
     */


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
                    plateau.cases[i][j] = (Case) objectInputStream.readObject(); //récupération de l'objet case[i][j]
                }
            }
            System.out.println("Fichier récupé avec succès !");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param adresse : donne l'adresse du fichier binaire
     * Méthode permettant de reprendre l'état du plateau venant d'un fichier binaire à partir
     * d'un emplacement choisi
     */

    public void reprendreSauvegarde(String adresse) throws Exception {
        try (FileInputStream fileInputStream = new FileInputStream(adresse);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            int colonnes = objectInputStream.readInt();
            int lignes = objectInputStream.readInt();
            plateau = new Plateau(lignes,colonnes);
            plateau.creerPlateau();

            for (int i = 0; i < lignes; i++){
                for (int j = 0; j < colonnes; j++){
                    plateau.cases[i][j] = (Case) objectInputStream.readObject(); //récupération de l'objet case[i][j]
                }
            }
            System.out.println("Fichier récupé avec succès !");
        } catch (IOException e) {
            throw new IOException("Impossible de lire le fichier de sauvegarde.", e);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Impossible de charger les données de sauvegarde.", e);
        } catch (Exception e){
            throw new Exception("Autre erreur !");
        }
    }

    public void reprendreSauvegardeViaFichierResources(String adresse) throws Exception {
        try (InputStream inputStream = getClass().getResourceAsStream(adresse);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            int colonnes = objectInputStream.readInt();
            int lignes = objectInputStream.readInt();
            plateau = new Plateau(lignes,colonnes);
            plateau.creerPlateau();

            for (int i = 0; i < lignes; i++){
                for (int j = 0; j < colonnes; j++){
                    plateau.cases[i][j] = (Case) objectInputStream.readObject(); //récupération de l'objet case[i][j]
                }
            }
            System.out.println("Fichier récupé avec succès !");
        } catch (IOException e) {
            throw new IOException("Impossible de lire le fichier de sauvegarde.", e);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Impossible de charger les données de sauvegarde.", e);
        } catch (Exception e){
            throw new Exception("Autre erreur !");
        }
    }


}
