package jeu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Parametre {

    private Stage mainStage;

    public Parametre(AnchorPane panneau, Scene mainScene) {
        panneau.getChildren().clear();

        Text texte = new Text("Paramètres ");
        texte.setX(100);
        texte.setY(100);
        texte.setFont(Font.font("calibri", FontWeight.BOLD,50));

        panneau.getChildren().add(texte);

        Button menuPrincipal = bouton("Menu Principal", 100, 200, Color.rgb(115,115,115));

        menuPrincipal = demarrerJeu(menuPrincipal,panneau, mainScene);

        panneau.getChildren().add(menuPrincipal);


    }

    public Button bouton(String texte, double X, double Y, Color couleur){
        Button bouton = new Button(texte);
        bouton.setFont(Font.font("calibri", (FontWeight) null, 40));
        bouton.setTextFill(Color.BLACK);
        bouton.setLayoutX(X);
        bouton.setLayoutY(Y);
        BackgroundFill fondType = new BackgroundFill(couleur,null, new Insets(5));
        Background fond = new Background(fondType);
        bouton.setBackground(fond);
        return bouton;
    }

    public Button demarrerJeu(Button bouton, AnchorPane panneau, Scene mainScene){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Menu menu = new Menu(panneau, mainScene);
                menu.getMainStage();
            }
        });
        return bouton;
    }

    public Stage getMainStage() {
        return mainStage;
    }

}
