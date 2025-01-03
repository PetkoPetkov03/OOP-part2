package com.sparks.of.fabrication.oop2.scenes.transaction;

import com.sparks.of.fabrication.oop2.models.Transaction;
import com.sparks.of.fabrication.oop2.models.TransactionDetail;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * The TableViewTransaction class is responsible for configuring the columns of the transaction and transaction details tables.
 */
public class TableViewTransaction {

    /**
     * Configures the columns for the transaction and transaction details tables.
     *
     * @param transactionTable           The table displaying transactions.
     * @param idColumn                  The column for the transaction ID.
     * @param clientColumn              The column for the client's name.
     * @param employeeColumn            The column for the employee's name.
     * @param checkoutColumn            The column for the checkout ID.
     * @param totalAmountColumn         The column for the total amount of the transaction.
     * @param transactionDetailsTable   The table displaying transaction details.
     * @param itemColumn                The column for the item name in transaction details.
     * @param quantityColumn            The column for the quantity in transaction details.
     * @param priceColumn               The column for the price of each item in transaction details.
     */
    public static void configureTableColumns(TableView<Transaction> transactionTable,
                                             TableColumn<Transaction, Long> idColumn,
                                             TableColumn<Transaction, String> clientColumn,
                                             TableColumn<Transaction, String> employeeColumn,
                                             TableColumn<Transaction, String> checkoutColumn,
                                             TableColumn<Transaction, Double> totalAmountColumn,
                                             TableView<TransactionDetail> transactionDetailsTable,
                                             TableColumn<TransactionDetail, String> itemColumn,
                                             TableColumn<TransactionDetail, Integer> quantityColumn,
                                             TableColumn<TransactionDetail, Double> priceColumn) {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClient().getName()));
        employeeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployee().getName()));
        checkoutColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCheckout().getIdCheckout().toString()));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        itemColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem().getName()));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        idColumn.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.10));
        clientColumn.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.25));
        employeeColumn.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.25));
        checkoutColumn.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.20));
        totalAmountColumn.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.20));
        itemColumn.prefWidthProperty().bind(transactionDetailsTable.widthProperty().multiply(0.50));
        quantityColumn.prefWidthProperty().bind(transactionDetailsTable.widthProperty().multiply(0.20));
        priceColumn.prefWidthProperty().bind(transactionDetailsTable.widthProperty().multiply(0.30));
    }
}
