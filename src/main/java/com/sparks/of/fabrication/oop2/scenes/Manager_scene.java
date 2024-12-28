package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Manager_scene {
    @FXML
    private ImageView notificationIcon;

    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private SceneLoader loader = Singleton.getInstance(SceneLoader.class);

    private Stage mainStage;
    private Stage otherStage;

    @FXML
    public void initialize() {
        notificationIcon.setOnMousePressed(this::handleNotificationIconPress);
        mainStage = new Stage();
        otherStage = new Stage();
        mainStage.initModality(Modality.NONE);
        otherStage.initModality(Modality.APPLICATION_MODAL);
    }

    private void handleNotificationIconPress(MouseEvent event) {
        try {
            loader.loadScene("scenes/notification_window.fxml", 400, 400, "Notifications", true, otherStage);
            otherStage.hide();
            otherStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void showArrivalGoods() throws IOException {
        loader.loadScene("scenes/arrival_goods.fxml", 500, 500, "Nomenclature", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showInventory() throws IOException {
        loader.loadScene("scenes/inventory_scene.fxml", 500, 500, "Inventory", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showInvoices() throws IOException {
        loader.loadScene("scenes/invoiceStore_scene.fxml", 500, 500, "Invoice", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showCashRegister() throws IOException {
        loader.loadScene("scenes/checkout_scene.fxml", 500, 500, "Checkout", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showEmployees() throws IOException {
        loader.loadScene("scenes/employees.fxml", 500, 500, "Employees", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
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
