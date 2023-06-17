/*
 * Copyright (c)  @link https://github.com/valmenu59/SAE_JavaFX @link https://github.com/valmenu59
 * CC BY-NC - Attribution - Partage dans les mêmes conditions - Pas d'utilisation commerciale
 */

package fx;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PoliceJeu {

    public static Font creerPolice(double taille, boolean enGras) {
        FontWeight weight = enGras ? FontWeight.BOLD : FontWeight.NORMAL; // condition ? vrai : faux
        return Font.font("Segoe UI", weight, taille);
    }
    
}
