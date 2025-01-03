package com.sparks.of.fabrication.oop2.scenes.statistic;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.InvoiceStore;
import com.sparks.of.fabrication.oop2.models.Transaction;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.Pair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

/**
 * The StatisticServices class handles loading and calculating statistics for transactions and invoices within a date range.
 */
public class StatisticServices {

    private final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    /**
     * Loads and calculates statistics for transactions and invoices in the specified date range and updates the tables.
     *
     * @param startDate       the start date of the date range
     * @param endDate         the end date of the date range
     * @param transactionTable the table to display the transaction data
     * @param invoiceTable     the table to display the invoice data
     * @return a pair of total transactions and total invoices amounts
     */
    public Pair<Double, Double> loadAndCalculateStatistics(LocalDate startDate, LocalDate endDate,
                                                           TableView<Transaction> transactionTable,
                                                           TableView<InvoiceStore> invoiceTable) {
        double totalTransactions = loadTransactionData(startDate, endDate, transactionTable);
        double totalInvoices = loadInvoiceData(startDate, endDate, invoiceTable);
        return new Pair<>(totalTransactions, totalInvoices);
    }

    /**
     * Loads transaction data between the specified dates.
     *
     * @param startDate        the start date of the date range
     * @param endDate          the end date of the date range
     * @param transactionTable the table to display the transaction data
     * @return the sum of the transaction amounts
     */
    private double loadTransactionData(LocalDate startDate, LocalDate endDate, TableView<Transaction> transactionTable) {
        try {
            Field transactionDateField = Transaction.class.getDeclaredField("transactionDate");
            Pair<Boolean, List<Transaction>> result = entityManagerWrapper.findEntitiesBetweenDates(
                    Transaction.class, transactionDateField, startDate, endDate);

            if (result.x()) {
                ObservableList<Transaction> transactionList = FXCollections.observableArrayList(result.y());
                transactionTable.setItems(transactionList);
                return transactionList.stream().mapToDouble(Transaction::getTotalAmount).sum();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("Error loading transaction data.");
        }
        return 0;
    }

    /**
     * Loads invoice data between the specified dates.
     *
     * @param startDate       the start date of the date range
     * @param endDate         the end date of the date range
     * @param invoiceTable    the table to display the invoice data
     * @return the sum of the invoice final prices
     */
    private double loadInvoiceData(LocalDate startDate, LocalDate endDate, TableView<InvoiceStore> invoiceTable) {
        try {
            Field dateField = InvoiceStore.class.getDeclaredField("date");
            Pair<Boolean, List<InvoiceStore>> result = entityManagerWrapper.findEntitiesBetweenDates(
                    InvoiceStore.class, dateField, startDate, endDate);

            if (result.x()) {
                ObservableList<InvoiceStore> invoiceList = FXCollections.observableArrayList(result.y());
                invoiceTable.setItems(invoiceList);
                return invoiceList.stream().mapToDouble(InvoiceStore::getFinalPrice).sum();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("Error loading invoice data.");
        }
        return 0;
    }
}
