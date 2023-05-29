package fx;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ImageJeu extends Image {
    private String url;

    public ImageJeu(String url) {
        super(url);
        this.url = url;
    }

    public ImageJeu(String url, double longueur, double hauteur) {
        super(url, longueur, hauteur, true, false);
        this.url = url;
    }

    public ImageView afficherImage() {
        return new ImageView(getClass().getResource(this.url).toExternalForm());
    }
}
