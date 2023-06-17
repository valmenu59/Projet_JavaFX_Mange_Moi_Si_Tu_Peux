/*
 * Copyright (c)  @link https://github.com/valmenu59/SAE_JavaFX @link https://github.com/valmenu59
 * CC BY-NC - Attribution - Partage dans les mêmes conditions - Pas d'utilisation commerciale
 */

package fx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageJeu extends Image {
    private final String url;

    public ImageJeu(String url) {
        super(url);
        this.url = url;
    }


    public ImageJeu(String url, double longueur, double hauteur) {
        super(url, longueur, hauteur, true, false);
        this.url = url;
    }

    public ImageJeu(String url, double longueur, double hauteur, boolean garderProportion) {
        super(url, longueur, hauteur, garderProportion, false);
        this.url = url;
    }

    public ImageView afficherImage() {
        return new ImageView(url);
    }

    public ImageView afficherImage(double longueur, double largeur, boolean preserverRatio){
        ImageView img = new ImageView(url);
        img.setFitWidth(longueur);
        img.setFitHeight(largeur);
        img.setPreserveRatio(preserverRatio);
        return img;
    }
}
