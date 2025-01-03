package com.sparks.of.fabrication.oop2.utils;

import com.sparks.of.fabrication.oop2.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Utility class to load scenes for the JavaFX application.
 */
public class SceneLoader {

    /**
     * Loads a new scene into the specified stage with the given properties.
     *
     * @param filename     The path to the FXML file.
     * @param width        The width of the scene.
     * @param height       The height of the scene.
     * @param title        The title of the stage.
     * @param isResizable  Whether the stage is resizable.
     * @param stage        The stage where the scene will be loaded.
     * @throws IOException If there is an error loading the FXML file.
     */
    public void loadScene(String filename, double width, double height, String title, boolean isResizable, Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(filename));
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            scene.getStylesheets().add(Objects.requireNonNull(Application.class.getResource("global.css")).toExternalForm());
            stage.setTitle(title);
            stage.setResizable(isResizable);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
