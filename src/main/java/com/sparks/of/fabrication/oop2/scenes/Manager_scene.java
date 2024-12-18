package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Manager_scene {
    @FXML
    private ImageView notificationIcon;

    @FXML
    private BorderPane mainPane;

    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private SceneLoader loader = Singleton.getInstance(SceneLoader.class);

    @FXML
    public void initialize() {
        notificationIcon.setOnMousePressed(this::handleNotificationIconPress);
    }

    private void handleNotificationIconPress(MouseEvent event) {
        try {
            loader.loadScene("scenes/notification_window.fxml", 400, 400, "Notifications", true,new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void showWelcomePage() {
        mainPane.setCenter(new javafx.scene.control.Label("Welcome Page"));
    }

    @FXML
    private void showArrivalGoods() throws IOException {
        loader.loadScene("scenes/arrival_goods.fxml",500,500,"Nomenclature",true,new Stage());
    }

    @FXML
    private void showCashRegister() {
        mainPane.setCenter(new javafx.scene.control.Label("Cash Register Section"));
    }

    @FXML
    private void showEmployees() {
        mainPane.setCenter(new javafx.scene.control.Label("Employees Section"));
    }

    @FXML
    private void showAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("About Manager Interface");
        alert.setContentText("This application is designed to manage warehouse operations.");
        alert.showAndWait();
    }
}
