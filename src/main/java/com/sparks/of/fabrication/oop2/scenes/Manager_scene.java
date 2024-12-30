package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import com.sparks.of.fabrication.oop2.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;

public class Manager_scene {
    @FXML
    private ImageView notificationIcon;

    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private SceneLoader loader = Singleton.getInstance(SceneLoader.class);
    private static final Employee loggedEmployee = Singleton.getInstance(Employee.class);
    private LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);
    private Stage mainStage;
    private Stage otherStage;

    @FXML
    public void initialize() {

        notificationIcon.setOnMousePressed(event -> {
            try {
                handleNotificationIconPress(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        mainStage = new Stage();
        otherStage = new Stage();
        mainStage.initModality(Modality.NONE);
        otherStage.initModality(Modality.APPLICATION_MODAL);
    }

    private void handleNotificationIconPress(MouseEvent event) throws IOException {
        logEmployee.createLog("loaded notification window","");
        loader.loadScene("scenes/notification_window.fxml", 400, 400, "Notifications", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void showArrivalGoods() throws IOException {
        logEmployee.createLog("loaded take in goods window","");
        loader.loadScene("scenes/arrival_goods.fxml", 500, 500, "Nomenclature", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showInventory() throws IOException {
        logEmployee.createLog("loaded inventory window","");
        loader.loadScene("scenes/inventory_scene.fxml", 500, 500, "Inventory", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showInvoices() throws IOException {
        logEmployee.createLog("loaded invoice window","");
        loader.loadScene("scenes/invoiceStore_scene.fxml", 500, 500, "Invoice", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showCashRegister() throws IOException {
        logEmployee.createLog("loaded checkout window","");
        loader.loadScene("scenes/checkout_scene.fxml", 500, 500, "Checkout", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showEmployees() throws IOException {
        logEmployee.createLog("loaded employees window","");
        loader.loadScene("scenes/employees.fxml", 500, 500, "Employees", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }
    @FXML
    private void handleCreateEmployee() throws IOException {
        logEmployee.createLog("loaded CEmployee window","");
        loader.loadScene("scenes/createEmployee_scene.fxml", 500, 500, "Create Employee", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }
    @FXML
    private void handleCheckout() throws IOException {
        logEmployee.createLog("loaded CCheckout window","");
        loader.loadScene("scenes/createCashRegister.fxml", 500, 500, "Create Cash Register", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }
    @FXML
    private void showLogs() throws IOException {
        logEmployee.createLog("loaded Employee logs window","");
        loader.loadScene("scenes/employeeLogs_scene.fxml", 500, 500, "Logs", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }
    @FXML
    private void showAbout() throws IOException {
        logEmployee.createLog("loaded About window","");
        loader.loadScene("scenes/about.fxml", 500, 500, "About", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }
}
