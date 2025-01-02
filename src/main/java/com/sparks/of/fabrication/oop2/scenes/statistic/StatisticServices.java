package com.sparks.of.fabrication.oop2.scenes.statistic;

import com.sparks.of.fabrication.oop2.Singleton;
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

public class StatisticServices {

    private final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    public Pair<Double, Double> loadAndCalculateStatistics(LocalDate startDate, LocalDate endDate,
                                                           TableView<Transaction> transactionTable,
                                                           TableView<InvoiceStore> invoiceTable) {
        double totalTransactions = loadTransactionData(startDate, endDate, transactionTable);
        double totalInvoices = loadInvoiceData(startDate, endDate, invoiceTable);
        return new Pair<>(totalTransactions, totalInvoices);
    }

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