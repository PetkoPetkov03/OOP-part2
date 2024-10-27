package com.sparks.of.fabrication.oop2.scenes;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;


import java.io.IOException;

public class Manager_scene {
    @FXML
    private ImageView notificationIcon;


    @FXML
    public void initialize() {
        notificationIcon.setOnMousePressed(this::handleNotificationIconPress);
    }

    private void handleNotificationIconPress(MouseEvent event) {
        try {
            SceneLoader sceneLoader = new SceneLoader();
            sceneLoader.loadScene("notification_window.fxml", 400, 400, "Notifications", true, new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
