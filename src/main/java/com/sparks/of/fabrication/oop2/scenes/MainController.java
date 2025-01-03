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

/**
 * The MainController class handles the main functionality of the application, including opening different scenes
 * based on user roles and logging actions performed within the application.
 */
public class MainController {

    private SceneLoader loader = Singleton.getInstance(SceneLoader.class);
    private LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);
    private static final Logger log = LogManager.getLogger(MainController.class);
    private Employee loggedEmployee = Singleton.getInstance(Employee.class);
    private Screen screen = Screen.getPrimary();
    private Stage mainStage;
    private Stage otherStage;

    /**
     * Initializes the main stage and the other stage with respective modality settings.
     */
    @FXML
    public void initialize() {
        mainStage = new Stage();
        otherStage = new Stage();
        mainStage.initModality(Modality.NONE);
        otherStage.initModality(Modality.APPLICATION_MODAL);
    }

    /**
     * Handles the notification window display and logs the action if the user has the proper role.
     *
     * @throws IOException if an I/O error occurs while loading the notification window.
     */
    @FXML
    private void handleNotification() throws IOException {
        if (!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Arrays.asList(Role.ADMIN, Role.MANAGER)))
            return;
        logAction("Loading notification window");
        loader.loadScene("scenes/notification_window.fxml", 400, 400, "Notifications", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    /**
     * Exits the application and logs the action.
     */
    @FXML
    private void handleExit() {
        logAction("Application exited");
        System.exit(0);
    }

    /**
     * Shows the arrival goods window and logs the action if the user has the proper role.
     *
     * @throws IOException if an I/O error occurs while loading the arrival goods window.
     */
    @FXML
    private void showArrivalGoods() throws IOException {
        if (!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Arrays.asList(Role.ADMIN, Role.MANAGER, Role.BACKSTAFF)))
            return;
        logAction("Loading take-in goods window");
        loader.loadScene("scenes/arrival_goods.fxml", screen.getBounds().getWidth() - 2, screen.getBounds().getHeight() - 2, "Nomenclature", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    /**
     * Shows the inventory window and logs the action.
     *
     * @throws IOException if an I/O error occurs while loading the inventory window.
     */
    @FXML
    private void showInventory() throws IOException {
        logAction("Loading inventory window");
        loader.loadScene("scenes/inventory_scene.fxml", screen.getBounds().getWidth(), screen.getBounds().getHeight(), "Inventory", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    /**
     * Shows the invoices window and logs the action if the user has the proper role.
     *
     * @throws IOException if an I/O error occurs while loading the invoices window.
     */
    @FXML
    private void showInvoices() throws IOException {
        if (!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Arrays.asList(Role.ADMIN, Role.MANAGER)))
            return;
        logAction("Loading invoice window");
        loader.loadScene("scenes/invoiceStore_scene.fxml", screen.getBounds().getWidth(), screen.getBounds().getHeight(), "Invoice", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    /**
     * Shows the checkout window and logs the action if the user has the proper role.
     *
     * @throws IOException if an I/O error occurs while loading the checkout window.
     */
    @FXML
    private void showCashRegister() throws IOException {
        if (!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Role.CASHIER))
            return;
        logAction("Loading checkout window");
        loader.loadScene("scenes/checkout_scene.fxml", screen.getBounds().getWidth(), screen.getBounds().getHeight(), "Checkout", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    /**
     * Shows the transactions window and logs the action if the user has the proper role.
     *
     * @throws IOException if an I/O error occurs while loading the transactions window.
     */
    @FXML
    private void showTransactions() throws IOException {
        if (!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Arrays.asList(Role.ADMIN, Role.MANAGER)))
            return;
        logAction("Loading transactions window");
        loader.loadScene("scenes/transaction_scene.fxml", screen.getBounds().getWidth(), screen.getBounds().getHeight(), "Transactions", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    /**
     * Shows the create employee window and logs the action if the user has the proper role.
     *
     * @throws IOException if an I/O error occurs while loading the create employee window.
     */
    @FXML
    private void handleCreateEmployee() throws IOException {
        if (!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Role.ADMIN))
            return;
        logAction("Loading Create Employee window");
        loader.loadScene("scenes/createEmployee_scene.fxml", 500, 500, "Create Employee", false, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    /**
     * Shows the create checkout window and logs the action if the user has the proper role.
     *
     * @throws IOException if an I/O error occurs while loading the create checkout window.
     */
    @FXML
    private void handleCheckout() throws IOException {
        if (!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Role.ADMIN))
            return;
        logAction("Loading Create Cash Register window");
        loader.loadScene("scenes/createCheckout.fxml", 400, 300, "Create Checkout", false, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    /**
     * Shows the employee logs window and logs the action if the user has the proper role.
     *
     * @throws IOException if an I/O error occurs while loading the employee logs window.
     */
    @FXML
    private void showLogs() throws IOException {
        if (!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Arrays.asList(Role.ADMIN, Role.MANAGER)))
            return;
        logAction("Loading Employee logs window");
        loader.loadScene("scenes/employeeLogs_scene.fxml", screen.getBounds().getWidth(), screen.getBounds().getHeight(), "Logs", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    /**
     * Shows the statistics window and logs the action if the user has the proper role.
     *
     * @throws IOException if an I/O error occurs while loading the statistics window.
     */
    @FXML
    private void showStatistic() throws IOException {
        if (!RoleValidator.areRolesValid(loggedEmployee.getRole().getRole(), Arrays.asList(Role.ADMIN, Role.MANAGER)))
            return;
        logAction("Loading Statistic window");
        loader.loadScene("scenes/statistic_scene.fxml", screen.getBounds().getWidth() - 2, screen.getBounds().getHeight(), "Statistic", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    /**
     * Shows the about window and logs the action.
     *
     * @throws IOException if an I/O error occurs while loading the about window.
     */
    @FXML
    private void showAbout() throws IOException {
        logAction("Loading About window");
        loader.loadScene("scenes/about.fxml", 500, 200, "About", true, otherStage);
        otherStage.hide();
        otherStage.showAndWait();
    }

    /**
     * Logs the specified action message both to the application log and the employee log.
     *
     * @param message The action message to log.
     */
    private void logAction(String message) {
        log.info(message);
        logEmployee.createLog(message, "");
    }
}
