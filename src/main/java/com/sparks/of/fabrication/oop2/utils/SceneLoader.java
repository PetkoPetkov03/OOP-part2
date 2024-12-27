package com.sparks.of.fabrication.oop2.utils;

import com.sparks.of.fabrication.oop2.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneLoader {

    public void loadScene(String filename, int width, int height,String title,boolean isResizable, Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(filename));
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            scene.getStylesheets().add(Objects.requireNonNull(Application.class.getResource("global.css")).toExternalForm());
            stage.setTitle(title);
            stage.setResizable(isResizable);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
