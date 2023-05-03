module sae.saejavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens sae.saejavafx to javafx.fxml;
    exports sae.saejavafx;
}