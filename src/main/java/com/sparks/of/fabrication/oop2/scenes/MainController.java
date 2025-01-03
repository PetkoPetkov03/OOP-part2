package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.users.Role;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import com.sparks.of.fabrication.oop2.utils.RoleValidator;
import com.sparks.of.fabrication.oop2.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;

public class MainController {
    private SceneLoader loader = Singleton.getInstance(SceneLoader.class);
    private LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);
    private static final Logger log = LogManager.getLogger(MainController.class);
    private Employee loggedEmployee = Singleton.getInstance(Employee.class);
    private RoleValidator roleValidator = new RoleValidator();
    private Screen screen = Screen.getPrimary();
    private Stage mainStage;
    private Stage otherStage;

    @FXML
    public void initialize() {
        mainStage = new Stage();
        otherStage = new Stage();
        mainStage.initModality(Modality.NONE);
        otherStage.initModality(Modality.APPLICATION_MODAL);
    }

    @FXML
    private void handleNotification() throws IOException {
        if(!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Arrays.asList(Role.ADMIN, Role.MANAGER)))
            return;
        logAction("Loading notification window");
        loader.loadScene("scenes/notification_window.fxml", 400, 400, "Notifications", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void handleExit() {
        logAction("Application exited");
        System.exit(0);
    }

    @FXML
    private void showArrivalGoods() throws IOException {
        if(!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Arrays.asList(Role.ADMIN, Role.MANAGER,Role.BACKSTAFF)))
            return;
        logAction("Loading take-in goods window");
        loader.loadScene("scenes/arrival_goods.fxml", screen.getBounds().getWidth()-2, screen.getBounds().getHeight() -2, "Nomenclature", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showInventory() throws IOException {
        logAction("Loading inventory window");
        loader.loadScene("scenes/inventory_scene.fxml", screen.getBounds().getWidth(), screen.getBounds().getHeight(), "Inventory", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showInvoices() throws IOException {
        if(!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Arrays.asList(Role.ADMIN, Role.MANAGER)))
            return;
        logAction("Loading invoice window");
        loader.loadScene("scenes/invoiceStore_scene.fxml", screen.getBounds().getWidth(), screen.getBounds().getHeight(), "Invoice", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showCashRegister() throws IOException {
        if(!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Role.CASHIER))
            return;
        logAction("Loading checkout window");
        loader.loadScene("scenes/checkout_scene.fxml", screen.getBounds().getWidth(), screen.getBounds().getHeight(), "Checkout", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showTransactions() throws IOException {
        if(!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Arrays.asList(Role.ADMIN, Role.MANAGER)))
            return;
        logAction("Loading transactions window");
        loader.loadScene("scenes/transaction_scene.fxml", screen.getBounds().getWidth(), screen.getBounds().getHeight(), "Transactions", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void handleCreateEmployee() throws IOException {
        if(!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Role.ADMIN))
            return;
        logAction("Loading Create Employee window");
        loader.loadScene("scenes/createEmployee_scene.fxml", 500, 500, "Create Employee", false, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void handleCheckout() throws IOException {
        if(!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(),Role.ADMIN))
            return;
        logAction("Loading Create Cash Register window");
        loader.loadScene("scenes/createCheckout.fxml", 400, 300, "Create Checkout", false, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showLogs() throws IOException {
        if(!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Arrays.asList(Role.ADMIN, Role.MANAGER)))
            return;
        logAction("Loading Employee logs window");
        loader.loadScene("scenes/employeeLogs_scene.fxml", screen.getBounds().getWidth(), screen.getBounds().getHeight(), "Logs", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showStatistic() throws IOException {
        if(!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Arrays.asList(Role.ADMIN, Role.MANAGER)))
            return;
        logAction("Loading Statistic window");
        loader.loadScene("scenes/statistic_scene.fxml", screen.getBounds().getWidth()-2, screen.getBounds().getHeight(), "Statistic", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    @FXML
    private void showAbout() throws IOException {
        logAction("Loading About window");
        loader.loadScene("scenes/about.fxml", 500, 200, "About", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    private void logAction(String message) {
        log.info(message);
        logEmployee.createLog(message, "");
    }
}
