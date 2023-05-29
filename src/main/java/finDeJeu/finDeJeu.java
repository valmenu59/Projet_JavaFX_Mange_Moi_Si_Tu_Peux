package finDeJeu;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class finDeJeu extends Scene {

    /*
    Pour la saé 202
     */
    public finDeJeu(boolean gagne){
        super(new BorderPane(), 1280,720);
        BorderPane panneau = (BorderPane) this.getRoot();
        Text texte;
        if (gagne){
            texte = new Text("Félicitation, le mouton est sorti de l'enclos !"+"\n"+
                    "Vous avez gagné !");
        } else {
            texte = new Text("Oh non ! Le loup a mangé le mouton !"+"\n"+
                    "Vous avez perdu !");
        }
        panneau.setCenter(texte);
    }
}
