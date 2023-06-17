module sae.saejavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;


    opens sae.saejavafx to javafx.fxml;
    exports sae.saejavafx;
}