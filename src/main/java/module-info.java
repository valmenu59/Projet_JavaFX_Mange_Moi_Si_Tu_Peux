module sae.saejavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens sae.saejavafx to javafx.fxml;
    exports sae.saejavafx;
}