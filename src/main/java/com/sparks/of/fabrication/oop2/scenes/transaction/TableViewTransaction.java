package com.sparks.of.fabrication.oop2.scenes.transaction;

import com.sparks.of.fabrication.oop2.models.Transaction;
import com.sparks.of.fabrication.oop2.models.TransactionDetail;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewTransaction {

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
