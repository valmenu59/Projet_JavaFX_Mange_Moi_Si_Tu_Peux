package jeu;

import java.io.*;

public class Jeu {


    protected Plateau plateau;
    protected NbTour tours;

    private boolean auTourDuMouton = true;

    private int deplacementMouton;
    private int deplacementLoup;





    public Jeu(){
        this.plateau = null;
        this.tours = null;
        deplacementMouton = 2;
        deplacementLoup = 3;
    }


    public void boucleJeu() {


        if (auTourDuMouton) {
            while (deplacementMouton > 0) {
                deplacementMouton--;
                this.plateau.deplacerAnimal("Mouton");
                if (deplacementMouton == 0) {
                    int[] caseM = plateau.getCaseMouton();
                    int nbr = plateau.moutonMangePlante(caseM[0],caseM[1]);
                    setDeplacementMouton(nbr);
                    auTourDuMouton = false;
                    break;
                }
            }
        } else {
            while (deplacementLoup > 0) {
                deplacementLoup--;
                this.plateau.deplacerAnimal("Loup");
                if (deplacementLoup == 0) {
                    reinitialiserDeplacementLoup();
                    auTourDuMouton = true;
                    break;
                }
            }
        }
    }

    public int getDeplacementMouton(){
        System.out.println("le déplacement du mouton est de :"+deplacementMouton);
        return this.deplacementMouton;
    }

    public int getDeplacementLoup(){
        return this.deplacementLoup;
    }

    public void setDeplacementMouton(int nbr){
        this.deplacementMouton = nbr;
    }

    public void reinitialiserDeplacementLoup(){
        this.deplacementLoup = 2;
    }

    public void estBienAuTourDuMouton(boolean b){
        this.auTourDuMouton = b;
    }




    public String getCheminDaccesSauvegarde(){
        String sourceFichierSauvegarde;

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            String userHome = System.getProperty("user.home");
            String appData = userHome + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "jeuSaeJavaFX";
            sourceFichierSauvegarde = appData + File.separator + "sauvegardePlateau";
        } else if (os.contains("mac")) {
            sourceFichierSauvegarde = System.getProperty("user.home") + "/Library/Application Support/jeuSaeJavaFX/sauvegardePlateau";
        } else {
            sourceFichierSauvegarde = System.getProperty("user.home") + File.separator + "jeuSaeJavaFX" + File.separator + ".sauvegardePlateau";
        }

        File directory = new File(sourceFichierSauvegarde).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return sourceFichierSauvegarde;
    }


    public boolean cheminDaccesExisteBien() {
        String chemin = getCheminDaccesSauvegarde();
        File fichier = new File(chemin);
        return fichier.exists();
    }



    public void sauvegarderPlateau(){
        FileOutputStream fichier = null;
        try {
            fichier = new FileOutputStream(getCheminDaccesSauvegarde());
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
        try (FileInputStream fileInputStream = new FileInputStream(getCheminDaccesSauvegarde());
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
