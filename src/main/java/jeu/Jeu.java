package jeu;

import exception.PlateauException;
import sauvegarde.DossierSauvegarde;

import java.io.*;
import java.util.Arrays;

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
        int multiplicateurPheromones = plateau.getColonnes() * plateau.getLignes() / 20;
        System.out.println("Loup menaçant "+ plateau.manhattan());
        moutonEnDanger = plateau.manhattan();
        if (auTourDuMouton) {
            if (deplacementMouton == planteDeplacementMouton) {
                this.nbTour++;
                this.plateau.planteQuiPousse();
                plateau.getFileNbDeplacementMouton().add(planteDeplacementMouton);
                if (nbTour > multiplicateurPheromones){
                    plateau.supprimerPheromones(true);
                }
            }
            deplacementMouton--;
            System.out.println("Je suis dans jeu "+deplacementMouton);
            plateau.deplacerAnimal("Mouton", moutonEnDanger);
            if (deplacementMouton == 0) {
                int[] caseM = plateau.getCaseMouton();
                if (plateau.isAnimalPresent("Mouton")) {
                    int nbr = plateau.moutonMangePlante(caseM[0], caseM[1]);
                    setDeplacementMouton(nbr);
                    planteDeplacementMouton = nbr;
                    auTourDuMouton = false;
                }
            }
        } else {
            if (deplacementLoup == 3 && nbTour > multiplicateurPheromones){
                plateau.supprimerPheromones(false);
            }
            deplacementLoup--;
            plateau.deplacerAnimal("Loup", moutonEnDanger);
            if (deplacementLoup == 0) {
                reinitialiserDeplacementLoup();
                auTourDuMouton = true;
            }
        }

        //Permet de donner les conditions de victoire ou de défaite
        if (Arrays.equals(plateau.getCaseSortie(), plateau.getCaseMouton())) {
            partieGagne = true;
        } else if (!plateau.isAnimalPresent("Mouton")){
            partiePerdue = true;
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
     * @param adresse : donne l'adresse du fichier binaire
     * Méthode permettant de reprendre l'état du plateau venant d'un fichier binaire à partir
     * d'un emplacement choisi
     */

    public void reprendreSauvegarde(String adresse, boolean vientDuFichierRessource) throws Exception {
        InputStream fileInputStream;
        if (!vientDuFichierRessource){
            fileInputStream = new FileInputStream(adresse);
        } else {
            fileInputStream = getClass().getResourceAsStream(adresse);
        }
        try (fileInputStream){
            if (adresse.endsWith(".sae")){
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                int colonnes = objectInputStream.readInt();
                int lignes = objectInputStream.readInt();
                plateau = new Plateau(lignes, colonnes);
                plateau.creerPlateau();

                for (int i = 0; i < lignes; i++) {
                    for (int j = 0; j < colonnes; j++) {
                        plateau.cases[i][j] = (Case) objectInputStream.readObject(); //récupération de l'objet case[i][j]
                    }
                }
                System.out.println("Fichier récupéré avec succès !");
            } else if (adresse.endsWith(".txt")){
                String message = creerPlateauViaFichierTexte(adresse);
                if (!message.equals("succès")){
                    throw new PlateauException(message);
                }
            }
        } catch (IOException e) {
            throw new IOException("Impossible de lire le fichier de sauvegarde.", e);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Impossible de charger les données de sauvegarde.", e);
        } catch (Exception e){
            throw new Exception("Autre erreur : "+e.getMessage());
        }
    }



    public String creerPlateauViaFichierTexte(String adresse) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(adresse));
        String texteLigne;
        int ligneIndex = 0;
        int nbColonnes;
        int nbLignes = 0;

        texteLigne = br.readLine().replaceAll("[\n\t\r ]", "");
        nbColonnes = texteLigne.length();
        nbLignes++;

        while((texteLigne = br.readLine()) != null)
        {
            nbLignes++;
        }

        br.close();

        System.out.println("Nombre de lignes : " + nbLignes);
        System.out.println("Nombre de lettres dans la première ligne : " + nbColonnes);

        if (nbLignes < 5 || nbColonnes < 6){
            //Trop petit
            System.out.println("Plateau trop petit");
            plateau = null;
            return "Plateau trop petit";
        }

        plateau = new Plateau(nbLignes, nbColonnes);
        plateau.creerPlateau();

        BufferedReader br2 = new BufferedReader(new FileReader(adresse));


        while ((texteLigne = br2.readLine()) != null) {
            // Ignorer les caractères de saut de ligne, tabulation et retour chariot
            texteLigne = texteLigne.replaceAll("[\n\t\r ]", "");

            for (int colonneIndex = 0; colonneIndex < nbColonnes; colonneIndex++) {

                char caractere = texteLigne.charAt(colonneIndex);


                switch (caractere) {
                    case 'x', 'X' -> plateau.getCase(ligneIndex, colonneIndex).setContenuGeneral(new Roche());
                    case 'h', 'H', 's', 'S' -> plateau.getCase(ligneIndex, colonneIndex).setContenuGeneral(new Herbe());
                    case 'f', 'F' -> plateau.getCase(ligneIndex, colonneIndex).setContenuGeneral(new Marguerite());
                    case 'c', 'C' -> plateau.getCase(ligneIndex, colonneIndex).setContenuGeneral(new Cactus());
                    case 'm', 'M' -> plateau.getCase(ligneIndex, colonneIndex).setAnimal(new Mouton());
                    case 'l', 'L' -> plateau.getCase(ligneIndex, colonneIndex).setAnimal(new Loup());
                }
            }
            //passage à la ligne suivante
            ligneIndex++;
        }

        br2.close();


        //Maintenant, on vérifie l'état du plateau
        if (!plateau.verifPlateauCorrect()){
            System.out.println("Labyrinthe imparfait");
            plateau = null;
            return "Labyrinthe imparfait";
        }
        if (!plateau.isAnimalPresent("Loup")){
            System.out.println("Loup manquant ou trop de loups");
            plateau = null;
            return "Loup manquant ou trop de loups";
        }
        if (!plateau.isAnimalPresent("Mouton")){
            System.out.println("Mouton manquant ou trop de moutons");
            plateau = null;
            return "Mouton manquant ou trop de moutons";
        }

        System.out.println("Fichier récupéré avec succès !");
        return "succès";
    }



}
