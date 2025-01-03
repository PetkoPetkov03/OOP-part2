package com.sparks.of.fabrication.oop2.scenes.statistic;

import com.sparks.of.fabrication.oop2.models.InvoiceStore;
import com.sparks.of.fabrication.oop2.models.Transaction;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * The TableViewStatistic class configures the columns for displaying transaction and invoice data in a table view.
 */
public class TableViewStatistic {

    /**
     * Configures the columns for displaying transaction and invoice data in the respective tables.
     *
     * @param transactionTable         the table to display transaction data
     * @param transactionIdColumn      the column to display transaction IDs
     * @param transactionPriceColumn   the column to display transaction total amounts
     * @param transactionDateColumn    the column to display transaction dates
     * @param invoiceTable             the table to display invoice data
     * @param invoiceIdColumn          the column to display invoice IDs
     * @param nomenclatureIdColumn     the column to display nomenclature IDs
     * @param nomenclaturePriceColumn  the column to display nomenclature final prices
     */
    public static void configureTableColumns(TableView<Transaction> transactionTable,
                                             TableColumn<Transaction, Long> transactionIdColumn,
                                             TableColumn<Transaction, Double> transactionPriceColumn,
                                             TableColumn<Transaction, String> transactionDateColumn,
                                             TableView<InvoiceStore> invoiceTable,
                                             TableColumn<InvoiceStore, Long> invoiceIdColumn,
                                             TableColumn<InvoiceStore, Long> nomenclatureIdColumn,
                                             TableColumn<InvoiceStore, Double> nomenclaturePriceColumn) {

        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        transactionPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        transactionDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTransactionDate().toString()));

        invoiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("idInvoice"));
        nomenclatureIdColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getNomenclatura().getIdNomenclature()).asObject());
        nomenclaturePriceColumn.setCellValueFactory(new PropertyValueFactory<>("finalPrice"));

        transactionIdColumn.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.20));
        transactionPriceColumn.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.40));
        transactionDateColumn.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.40));
        invoiceIdColumn.prefWidthProperty().bind(invoiceTable.widthProperty().multiply(0.20));
        nomenclatureIdColumn.prefWidthProperty().bind(invoiceTable.widthProperty().multiply(0.40));
        nomenclaturePriceColumn.prefWidthProperty().bind(invoiceTable.widthProperty().multiply(0.40));
    }
}
