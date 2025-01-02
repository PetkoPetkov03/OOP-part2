package com.sparks.of.fabrication.oop2.scenes.checkout;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.*;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.time.LocalDate;

public class CheckoutController {
    @FXML private TableView<ScannedItem> scannedItemsTable;
    @FXML private TableColumn<ScannedItem, Long> idColumn;
    @FXML private TableColumn<ScannedItem, String> nameColumn;
    @FXML private TableColumn<ScannedItem, Double> priceColumn;
    @FXML private TableColumn<ScannedItem, Integer> quantityColumn;
    @FXML private TextField scanField;
    @FXML private ComboBox<Client> clientBox;
    @FXML private Label totalLabel;
    @FXML private Button finishTransactionButton;

    private Checkout checkout;
    private final Employee loggedEmployee = Singleton.getInstance(Employee.class);
    private final StringBuilder builder = new StringBuilder();
    private ObservableList<ScannedItem> scannedItems;
    private final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private final CheckoutServices checkoutServices = new CheckoutServices();

    private static final Logger log = LogManager.getLogger(CheckoutController.class);
    private final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    private double totalAmount = 0.0;

    @FXML
    public void initialize() throws NoSuchFieldException {
        try {
            scannedItems = FXCollections.observableArrayList();
            scannedItemsTable.setItems(scannedItems);

            TableViewCheckout.configureTableColumns(scannedItemsTable, idColumn, nameColumn, priceColumn, quantityColumn, clientBox);

            checkout = checkoutServices.findCheckout(loggedEmployee);
            clientBox.getItems().setAll(checkoutServices.loadClient());
            clientBox.setValue(checkoutServices.loadClient().getFirst());

            scanField.setOnAction(event -> {
                builder.append(scanField.getText().trim());
                if (builder.length() > 1) {
                    String input = builder.toString();
                    if (input.startsWith("+++")) {
                        checkoutServices.handleAddToCheckout(Double.parseDouble(input.substring(3)), checkout);
                    } else if (input.startsWith("---")) {
                        checkoutServices.handleRemoveFromCheckout(Double.parseDouble(input.substring(3)), checkout);
                        checkAndNotifyLowCash();
                    } else if (input.startsWith("+")) {
                        handleAddItem(input.substring(1));
                    } else if (input.startsWith("-")) {
                        handleRemoveItem(input.substring(1));
                    } else {
                        log.warn("Invalid input detected: {}", input);
                        logEmployee.createLog("Invalid Input", "Invalid input detected during scan: " + input);
                    }
                }
                builder.setLength(0);
                scanField.clear();
            });

            finishTransactionButton.setOnAction(event -> finishTransaction());
        } catch (Exception e) {
            log.error("Error initializing CheckoutController: {}", e.getMessage());
            logEmployee.createLog("Initialization Error", "Error initializing CheckoutController: " + e.getMessage());
        }
    }

    private void handleAddItem(String code) {
        try {
            Item item = checkoutServices.fetchItemByCode(code);
            if (item == null) {
                log.warn("Item with code {} not found.", code);
                logEmployee.createLog("Item Not Found", "Attempted to add non-existent item with code: " + code);
                return;
            }

            ScannedItem existingItem = checkoutServices.findExistingItem(item.getName(), scannedItems);
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + 1);
            } else {
                checkoutServices.addNewItemToScannedList(item, scannedItems);
            }
            scannedItemsTable.refresh();
            totalAmount += item.getPrice();
            totalLabel.setText(String.format("$%.2f", totalAmount));
        } catch (Exception e) {
            log.error("Error adding item with code {}: {}", code, e.getMessage());
            logEmployee.createLog("Add Item Error", "Error adding item with code " + code + ": " + e.getMessage());
        }
    }

    private void handleRemoveItem(String code) {
        try {
            Item item = checkoutServices.fetchItemByCode(code);
            if (item == null) {
                log.warn("Item with code {} not found for removal.", code);
                logEmployee.createLog("Item Not Found for Removal", "Attempted to remove non-existent item with code: " + code);
                return;
            }

            ScannedItem existingItem = checkoutServices.findExistingItem(item.getName(), scannedItems);
            if (existingItem != null && scannedItems.contains(existingItem)) {
                checkoutServices.removeOrDecrementItem(existingItem, scannedItems, scannedItemsTable);
                totalAmount -= item.getPrice();
            }
        } catch (Exception e) {
            log.error("Error removing item with code {}: {}", code, e.getMessage());
            logEmployee.createLog("Remove Item Error", "Error removing item with code " + code + ": " + e.getMessage());
        }
    }

    private void finishTransaction() {
        try {
            Employee employee = Singleton.getInstance(Employee.class);
            Client client = clientBox.getValue();
            Checkout managedCheckout = entityManagerWrapper.findEntityById(Checkout.class, checkout.getIdCheckout().intValue()).y();

            Transaction transaction = checkoutServices.createTransaction(employee, client, checkout);
            double totalAmount = checkoutServices.processScannedItems(transaction, scannedItems);

            checkoutServices.finalizeTransaction(transaction, totalAmount);
            checkoutServices.clearScannedItems(scannedItems);
            checkoutServices.updateCheckoutCash(totalAmount, managedCheckout);

            checkAndNotifyLowCash();
            totalLabel.setText("0.00");

            log.info("Transaction successfully finished.");
            logEmployee.createLog("Transaction Finished", "Transaction successfully completed with total amount: " + totalAmount);
        } catch (Exception e) {
            log.error("Error finishing transaction: {}", e.getMessage());
            logEmployee.createLog("Transaction Error", "Error finishing transaction: " + e.getMessage());
        }
    }

    private void checkAndNotifyLowCash() {
        try {
            if (checkout.getCash() < 250) {
                Notification notification = new Notification();
                notification.setEmployee(loggedEmployee);
                notification.setMessage("Checkout cash is below 250. Current balance: " + checkout.getCash());
                notification.setStatus("Unread");
                notification.setDateSent(Date.valueOf(LocalDate.now()));

                entityManagerWrapper.genEntity(notification);
                log.warn("Checkout cash is below 250. Current balance: {}", checkout.getCash());
                logEmployee.createLog("Low Cash Warning", "Checkout cash is below 250. Current balance: " + checkout.getCash());
            }
        } catch (Exception e) {
            log.error("Error checking low cash status: {}", e.getMessage());
            logEmployee.createLog("Low Cash Check Error", "Error during low cash check: " + e.getMessage());
        }
    }
}
