/*
 * Copyright (c)  @link https://github.com/valmenu59/SAE_JavaFX @link https://github.com/valmenu59
 * CC BY-NC - Attribution - Partage dans les mêmes conditions - Pas d'utilisation commerciale
 */

package sauvegarde;

import java.io.File;

public class DossierSauvegarde {


    public DossierSauvegarde() {}

    /**
     * @return : Retourne le chemin absolu
     * Méthode qui donne le chemin absolu du fichier de sauvegarde en fonction de l'OS
     */

    public String getCheminDaccesSauvegarde() {
        //Donne le nom de l'OS en minuscule
        String os = System.getProperty("os.name").toLowerCase();
        //Initialisation d'un String
        String sourceFichierSauvegarde;
        //Windows
        if (os.contains("win")) {
            String userHome = System.getProperty("user.home");
            String appData = userHome + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "jeuSaeJavaFX";
            sourceFichierSauvegarde = appData + File.separator + "sauvegardePlateau.sae";
        //Mac
        } else if (os.contains("mac")) {
            sourceFichierSauvegarde = System.getProperty("user.home") + "/Library/Application Support/jeuSaeJavaFX/sauvegardePlateau.sae";
        //Unix / Linux
        } else {
            String var10000 = System.getProperty("user.home");
            sourceFichierSauvegarde = var10000 + File.separator + "jeuSaeJavaFX" + File.separator + ".sauvegardePlateau.sae";
        }

        //Si le chemin n'existe pas la créer
        File directory = (new File(sourceFichierSauvegarde)).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //Retourne le chemin
        return sourceFichierSauvegarde;
    }

    /**
     * @return : retourne un String du chemin contenant la sauvegarde
     * Méthode qui permet de donner le chemin contenant le fichier binaire de sauvegarde en fonction de l'OS
     */

    public String getCheminFichier() {
        String os = System.getProperty("os.name").toLowerCase();
        //Windows
        if (os.contains("win")) {
            String userHome = System.getProperty("user.home");
            return userHome + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "jeuSaeJavaFX";
        //Mac
        } else if (os.contains("mac")) {
            return System.getProperty("user.home") + "/Library/Application Support/jeuSaeJavaFX/";
        //Unix / Linux
        } else {
            String var10000 = System.getProperty("user.home");
            return var10000 + File.separator + "jeuSaeJavaFX";
        }
    }

    /**
     * @return : retourne un boolean si le dossier binaire de sauvegarde existe bien
     */

    public boolean isCheminExisteBien() {
        File fichier = new File(this.getCheminDaccesSauvegarde());
        return fichier.exists();
    }

    /**
     * Méthode qui permet de supprimer le dossier de sauvegarde
     * @return : retourne vrai si le fichier a bien été supprimé, sinon renvoie faux
     */

    public boolean removeSauvegarde() {
        File fichier = new File(this.getCheminDaccesSauvegarde());
        fichier.delete();
        return !fichier.exists();
    }
}
