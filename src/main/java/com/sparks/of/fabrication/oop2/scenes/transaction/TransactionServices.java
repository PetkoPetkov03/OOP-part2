package com.sparks.of.fabrication.oop2.scenes.transaction;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Transaction;
import com.sparks.of.fabrication.oop2.models.TransactionDetail;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.scene.control.TableView;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

/**
 * The TransactionServices class provides methods for loading, filtering, and displaying transactions and their details.
 */
public class TransactionServices {

    private final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    /**
     * Loads all transactions into the provided table.
     *
     * @param transactionTable The table where the transactions will be displayed.
     */
    public void loadTransactions(TableView<Transaction> transactionTable) {
        List<Transaction> transactions = entityManagerWrapper.findAllEntities(Transaction.class);
        transactionTable.getItems().setAll(transactions);
    }

    /**
     * Loads the details of the selected transaction into the provided table.
     *
     * @param selectedTransaction The transaction whose details need to be loaded.
     * @param transactionDetailsTable The table where the transaction details will be displayed.
     */
    public void loadTransactionDetails(Transaction selectedTransaction, TableView<TransactionDetail> transactionDetailsTable) {
        try {
            Field field = TransactionDetail.class.getDeclaredField("transaction");
            List<TransactionDetail> details = entityManagerWrapper.findEntityByValAll(TransactionDetail.class, field, selectedTransaction).y();
            transactionDetailsTable.getItems().setAll(details);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Error loading transaction details: " + e.getMessage(), e);
        }
    }

    /**
     * Filters transactions by the specified date and loads them into the provided table.
     * If no date is provided, all transactions are loaded.
     *
     * @param date The date to filter transactions by.
     * @param transactionTable The table where the filtered transactions will be displayed.
     */
    public void filterTransactionsByDate(LocalDate date, TableView<Transaction> transactionTable) {
        try {
            if (date != null) {
                Field field = Transaction.class.getDeclaredField("transactionDate");
                List<Transaction> filteredTransactions = entityManagerWrapper.findEntityByValAll(Transaction.class, field, date).y();
                transactionTable.getItems().setAll(filteredTransactions);
            } else {
                loadTransactions(transactionTable);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Error filtering transactions by date: " + e.getMessage(), e);
        }
    }
}
