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
        return new ImageView(getClass().getResource(this.url).toExternalForm());
    }

    public ImageView afficherImage(double longueur, double largeur, boolean preserverRation){
        ImageView img = new ImageView(getClass().getResource(this.url).toExternalForm());
        img.setFitWidth(longueur);
        img.setFitHeight(largeur);
        img.setPreserveRatio(preserverRation);
        return img;
    }
}
