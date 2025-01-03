package com.sparks.of.fabrication.oop2.scenes.transaction;

import com.sparks.of.fabrication.oop2.models.Transaction;
import com.sparks.of.fabrication.oop2.models.TransactionDetail;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import com.sparks.of.fabrication.oop2.utils.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * The TransactionController class handles the transaction-related operations and UI actions
 */
public class TransactionController {

    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, Long> idColumn;
    @FXML
    private TableColumn<Transaction, String> clientColumn;
    @FXML
    private TableColumn<Transaction, String> employeeColumn;
    @FXML
    private TableColumn<Transaction, String> checkoutColumn;
    @FXML
    private TableColumn<Transaction, Double> totalAmountColumn;

    @FXML
    private TableView<TransactionDetail> transactionDetailsTable;
    @FXML
    private TableColumn<TransactionDetail, String> itemColumn;
    @FXML
    private TableColumn<TransactionDetail, Integer> quantityColumn;
    @FXML
    private TableColumn<TransactionDetail, Double> priceColumn;

    @FXML
    private DatePicker datePicker;

    private final TransactionServices transactionServices = new TransactionServices();
    private static final Logger log = LogManager.getLogger(TransactionController.class);
    private final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    /**
     * Initializes the TransactionController by configuring table columns, adding event listeners, and loading transaction data.
     */
    @FXML
    private void initialize() {
        TableViewTransaction.configureTableColumns(transactionTable, idColumn, clientColumn, employeeColumn, checkoutColumn, totalAmountColumn, transactionDetailsTable, itemColumn, quantityColumn, priceColumn);

        log.info("Transaction Controller Initialized.");
        logEmployee.createLog("Initialization", "Transaction controller initialized.");

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                log.info("Filtering transactions by date: {}", newValue);
                logEmployee.createLog("Filter Transactions", "Filtering transactions by date: " + newValue);
                transactionServices.filterTransactionsByDate(datePicker.getValue(), transactionTable);
            }
        });

        transactionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                log.info("Transaction selected: ID = {}", newSelection.getId());
                logEmployee.createLog("Transaction Selection", "Transaction selected: ID = " + newSelection.getId());
                transactionServices.loadTransactionDetails(newSelection, transactionDetailsTable);
            } else {
                log.warn("No transaction selected.");
                logEmployee.createLog("Transaction Selection", "No transaction selected.");
                transactionDetailsTable.getItems().clear();
            }
        });

        log.info("Loading transactions into the table.");
        logEmployee.createLog("Load Transactions", "Loading transactions into the table.");
        transactionServices.loadTransactions(transactionTable);
    }
}
