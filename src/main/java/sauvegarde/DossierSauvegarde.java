package sauvegarde;

import java.io.File;

public class DossierSauvegarde {


    public DossierSauvegarde() {
    }

    public String getCheminDaccesSauvegarde() {
        String os = System.getProperty("os.name").toLowerCase();
        String sourceFichierSauvegarde;
        if (os.contains("win")) {
            String userHome = System.getProperty("user.home");
            String appData = userHome + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "jeuSaeJavaFX";
            sourceFichierSauvegarde = appData + File.separator + "sauvegardePlateau.sae";
        } else if (os.contains("mac")) {
            sourceFichierSauvegarde = System.getProperty("user.home") + "/Library/Application Support/jeuSaeJavaFX/sauvegardePlateau.sae";
        } else {
            String var10000 = System.getProperty("user.home");
            sourceFichierSauvegarde = var10000 + File.separator + "jeuSaeJavaFX" + File.separator + ".sauvegardePlateau.sae";
        }

        File directory = (new File(sourceFichierSauvegarde)).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return sourceFichierSauvegarde;
    }


    public String getCheminFichier() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            String userHome = System.getProperty("user.home");
            return userHome + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "jeuSaeJavaFX";
        } else if (os.contains("mac")) {
            return System.getProperty("user.home") + "/Library/Application Support/jeuSaeJavaFX/";
        } else {
            String var10000 = System.getProperty("user.home");
            return var10000 + File.separator + "jeuSaeJavaFX";
        }
    }

    public boolean cheminDaccesExisteBien() {
        File fichier = new File(this.getCheminDaccesSauvegarde());
        return fichier.exists();
    }

    public boolean effacerSauvegarde() {
        File fichier = new File(this.getCheminDaccesSauvegarde());
        fichier.delete();
        return !fichier.exists();
    }
}
