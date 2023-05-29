package fx;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BoutonJeu extends Button {
    private String texte;

    public BoutonJeu(String texte, double X, double Y, Color couleur) {
        super(texte);
        this.setFont(Font.font("Segoe UI", (FontWeight)null, 40.0));
        this.setTextFill(Color.BLACK);
        this.setLayoutX(X);
        this.setLayoutY(Y);
        BackgroundFill fondType = new BackgroundFill(couleur, (CornerRadii)null, new Insets(5.0));
        Background fond = new Background(new BackgroundFill[]{fondType});
        this.setBackground(fond);
    }

    public BoutonJeu(String texte, Color couleur) {
        super(texte);
        this.setFont(Font.font("Segoe UI", (FontWeight)null, 40.0));
        this.setTextFill(Color.BLACK);
        BackgroundFill fondType = new BackgroundFill(couleur, (CornerRadii)null, new Insets(5.0));
        Background fond = new Background(new BackgroundFill[]{fondType});
        this.setBackground(fond);
    }
}