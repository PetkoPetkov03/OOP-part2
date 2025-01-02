package com.sparks.of.fabrication.oop2.scenes.statistic;

import com.sparks.of.fabrication.oop2.models.InvoiceStore;
import com.sparks.of.fabrication.oop2.models.Transaction;
import com.sparks.of.fabrication.oop2.utils.Pair;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import com.sparks.of.fabrication.oop2.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class StatisticController {

    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, Long> transactionIdColumn;
    @FXML
    private TableColumn<Transaction, Double> transactionPriceColumn;
    @FXML
    private TableColumn<Transaction, String> transactionDateColumn;

    @FXML
    private TableView<InvoiceStore> invoiceTable;
    @FXML
    private TableColumn<InvoiceStore, Long> invoiceIdColumn;
    @FXML
    private TableColumn<InvoiceStore, Long> nomenclatureIdColumn;
    @FXML
    private TableColumn<InvoiceStore, Double> nomenclaturePriceColumn;

    @FXML
    private Label totalTransactionLabel;
    @FXML
    private Label winningsLabel;
    @FXML
    private Label spendMoneyLabel;

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button loadDataButton;

    private StatisticServices statisticServices;
    private static final Logger log = LogManager.getLogger(StatisticController.class);
    private final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    @FXML
    private void initialize() {
        statisticServices = new StatisticServices();
        TableViewStatistic.configureTableColumns(transactionTable, transactionIdColumn, transactionPriceColumn, transactionDateColumn,
                invoiceTable, invoiceIdColumn, nomenclatureIdColumn, nomenclaturePriceColumn);

        log.info("Statistic Controller Initialized.");
        logEmployee.createLog("Initialization", "Statistic controller initialized.");
    }

    @FXML
    private void handleLoadDataButton() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null && !startDate.isAfter(endDate)) {
            log.info("Loading statistics for date range: {} to {}", startDate, endDate);
            logEmployee.createLog("Load Statistics", "Loading statistics for date range: " + startDate + " to " + endDate);

            Pair<Double, Double> results = statisticServices.loadAndCalculateStatistics(startDate, endDate,
                    transactionTable, invoiceTable);
            totalTransactionLabel.setText("Total: " + results.x());
            spendMoneyLabel.setText("Spend Money: " + results.y());
            winningsLabel.setText("Winnings: " + (results.x() - results.y()));

            log.info("Statistics loaded: Total transactions = {}, Spend Money = {}, Winnings = {}",
                    results.x(), results.y(), (results.x() - results.y()));
            logEmployee.createLog("Load Statistics", "Statistics loaded: Total transactions = " + results.x() +
                    ", Spend Money = " + results.y() + ", Winnings = " + (results.x() - results.y()));
        } else {
            log.warn("Invalid date range selected: {} to {}", startDate, endDate);
            logEmployee.createLog("Invalid Date Range", "Invalid date range: " + startDate + " to " + endDate);
            System.out.println("Invalid date range.");
        }
    }
}
