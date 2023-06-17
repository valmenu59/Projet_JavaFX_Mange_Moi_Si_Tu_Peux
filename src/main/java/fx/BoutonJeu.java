/*
 * Copyright (c)  @link https://github.com/valmenu59/SAE_JavaFX @link https://github.com/valmenu59
 * CC BY-NC - Attribution - Partage dans les mêmes conditions - Pas d'utilisation commerciale
 */

package fx;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class BoutonJeu extends Button {

    private static final Font POLICE = PoliceJeu.creerPolice(40., false);


    public BoutonJeu(String texte, double X, double Y, Color couleur) {
        super(texte);
        this.setFont(POLICE);
        this.setTextFill(Color.BLACK);
        this.setLayoutX(X);
        this.setLayoutY(Y);
        BackgroundFill fondType = new BackgroundFill(couleur, (CornerRadii)null, new Insets(5.0));
        Background fond = new Background(new BackgroundFill[]{fondType});
        this.setBackground(fond);
    }

    public BoutonJeu(String texte, Color couleur) {
        super(texte);
        this.setFont(POLICE);
        this.setTextFill(Color.BLACK);
        BackgroundFill fondType = new BackgroundFill(couleur, (CornerRadii)null, new Insets(5.0));
        Background fond = new Background(new BackgroundFill[]{fondType});
        this.setBackground(fond);
    }
}