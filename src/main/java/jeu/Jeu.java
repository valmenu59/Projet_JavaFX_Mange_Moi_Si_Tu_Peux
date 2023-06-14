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
        nbTour = 0;

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

    public int getPlanteDeplacementMouton(){
        return this.planteDeplacementMouton;
    }



    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////FIN GET, SET, IS////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    /**
     * Permet de faire une boucle en déplaçant les animaux, et de vérifier si la partie est terminée ou pas
     * Si c'est au tour du mouton et que c'est un nouveau tour, on augmente le nombre de tours de 1, on fait pousser
     * les cases de type terre et on déplace le mouton. On supprime les phéromones mouton à partir d'un certain nombre
     * de tours (chaque déplacement laisse une phéromone)
     * Si le nombre de déplacements du mouton est égal à 0, il mange la plante présente sur la case et prend le "pouvoir"
     * de celle-ci
     * C'est exactement pareil pour le loup sauf qu'il ne mange pas de plante et se déplace toujours de 3
     * Vérifie à la fin si la partie est terminée ou pas (mouton mangé ou le mouton est à la case sortie
     */


    public void boucleJeu(String[] choixAlgoAnimaux) {
        int multiplicateurPheromones = plateau.getColonnes() * plateau.getLignes() / 12;
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
            plateau.deplacerAnimal("Mouton", moutonEnDanger, choixAlgoAnimaux[0]);
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
            plateau.deplacerAnimal("Loup", moutonEnDanger, choixAlgoAnimaux[1]);
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
            //Si vient de l'explorateur de fichiers
            fileInputStream = new FileInputStream(adresse);
        } else {
            //Si vient de ressources
            fileInputStream = getClass().getResourceAsStream(adresse);
        }
        try (fileInputStream){
            if (adresse.endsWith(".sae")){
                //Si c'est un fichier binaire
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                int colonnes = objectInputStream.readInt(); //récupère nombre de colonnes
                int lignes = objectInputStream.readInt(); //récupère nombre de lignes
                plateau = new Plateau(lignes, colonnes);
                plateau.creerPlateau(); //création du plateau

                for (int i = 0; i < lignes; i++) {
                    for (int j = 0; j < colonnes; j++) {
                        plateau.cases[i][j] = (Case) objectInputStream.readObject(); //récupération de l'objet case[i][j]
                    }
                }
                System.out.println("Fichier récupéré avec succès !");
            } else if (adresse.endsWith(".txt")){
                //si c'est un fichier texte
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

        //Première étape : récupération du nombre de lignes et de colonnes

        texteLigne = br.readLine().replaceAll("[\n\t\r ]", ""); //enlève les caractères entre crochet
        nbColonnes = texteLigne.length(); //récupère le nombre de caractères de la ligne 0
        nbLignes++;

        while((texteLigne = br.readLine()) != null)
        {
            nbLignes++; //Permet de récupérer le nombre de lignes
        }

        br.close(); //Ferme le fichier texte

        System.out.println("Nombre de lignes : " + nbLignes);
        System.out.println("Nombre de lettres dans la première ligne : " + nbColonnes);

        if (nbLignes < 5 || nbColonnes < 6){
            //Trop petit
            plateau = null;
            return "Plateau trop petit";
        }

        // Etape 2 : création du tableau et récupération de chaque caractère individuellement

        plateau = new Plateau(nbLignes, nbColonnes);
        plateau.creerPlateau();

        BufferedReader br2 = new BufferedReader(new FileReader(adresse));


        while ((texteLigne = br2.readLine()) != null) {
            // Ignorer les caractères de saut de ligne, tabulation et retour chariot
            texteLigne = texteLigne.replaceAll("[\n\t\r ]", "");

            for (int colonneIndex = 0; colonneIndex < nbColonnes; colonneIndex++) {

                char caractere = texteLigne.charAt(colonneIndex);

                //Permet de transformer des caractères spécifiques en un objet
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
        if (Arrays.equals(plateau.getCaseSortie(), new int[2])){
            plateau = null;
            return "Sortie manquante";
        }
        if (!plateau.verifPlateauCorrect()){
            plateau = null;
            return "Labyrinthe imparfait";
        }
        if (!plateau.isAnimalPresent("Loup")){
            plateau = null;
            return "Loup manquant ou trop de loups";
        }
        if (!plateau.isAnimalPresent("Mouton")){
            plateau = null;
            return "Mouton manquant ou trop de moutons";
        }

        System.out.println("Fichier récupéré avec succès !");
        return "succès";
    }



}
