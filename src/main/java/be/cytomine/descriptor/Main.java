package be.cytomine.descriptor;

import be.cytomine.descriptor.controllers.RootSceneController;

import be.cytomine.descriptor.util.FXMLBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Created by Romain on 28-06-17.
 * This is THE (main) class.
 */
public class Main extends Application {
    private static String ROOT_FXML = "/be/cytomine/descriptor/util/root_scene.fxml";
    private static String STYLE_CSS = "/be/cytomine/descriptor/util/style.css";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // register on close event
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        Pair<Parent, RootSceneController> rootScene = FXMLBuilder.build(ROOT_FXML);
        Scene scene = new Scene(rootScene.getKey());
        scene.getStylesheets().add(STYLE_CSS);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static String getCssPath() {
        return STYLE_CSS;
    }
}


