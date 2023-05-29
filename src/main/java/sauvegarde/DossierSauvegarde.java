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
            sourceFichierSauvegarde = appData + File.separator + "sauvegardePlateau";
        } else if (os.contains("mac")) {
            sourceFichierSauvegarde = System.getProperty("user.home") + "/Library/Application Support/jeuSaeJavaFX/sauvegardePlateau";
        } else {
            String var10000 = System.getProperty("user.home");
            sourceFichierSauvegarde = var10000 + File.separator + "jeuSaeJavaFX" + File.separator + ".sauvegardePlateau";
        }

        File directory = (new File(sourceFichierSauvegarde)).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return sourceFichierSauvegarde;
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
