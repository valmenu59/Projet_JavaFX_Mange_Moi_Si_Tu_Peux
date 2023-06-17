/*
 * Copyright (c)  @link https://github.com/valmenu59/SAE_JavaFX @link https://github.com/valmenu59
 * CC BY-NC - Attribution - Partage dans les mêmes conditions - Pas d'utilisation commerciale
 */

package fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ActionAllerNouvelleScene<UNE_SCENE extends Scene> implements EventHandler<ActionEvent> {
    private final Stage stage;
    private final Class<UNE_SCENE> classeScene;

    /**
     * Le but de cette classe c'est qu'à partir d'une action utilisateur on peut aller dans n'importe quelle scène
     * @param stage : le stage à aller
     * @param sceneClass : la classe où aller
     */

    public ActionAllerNouvelleScene(Stage stage, Class<UNE_SCENE> sceneClass) {
        this.stage = stage;
        this.classeScene = sceneClass;
    }

    @Override
    public void handle(ActionEvent event) {
        UNE_SCENE scene = null;
        try {
            //Permet de créer une scène à partir d'un constructeur
            scene = classeScene.getConstructor(Stage.class).newInstance(stage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Ouvre la scène
            stage.setScene(scene);
        }
    }
}
