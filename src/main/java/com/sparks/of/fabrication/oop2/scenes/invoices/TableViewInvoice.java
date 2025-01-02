package com.sparks.of.fabrication.oop2.scenes.invoices;

import com.sparks.of.fabrication.oop2.models.InvoiceStore;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableViewInvoice {
    public static void configureTableColumns(TableView<InvoiceStore> invoiceTable,
                                             TableColumn<InvoiceStore, Long> invoiceIdColumn,
                                             TableColumn<InvoiceStore, Long> nomenclatureIdColumn,
                                             TableColumn<InvoiceStore, String> dateColumn,
                                             TableColumn<InvoiceStore, String> employeeNameColumn,
                                             InvoiceStore_scene invoiceStoreController){
        invoiceIdColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getIdInvoice())
        );
        nomenclatureIdColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getNomenclatura().getIdNomenclature())
        );
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().toString())
        );
        employeeNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(invoiceStoreController.fetchEmployeeName(cellData.getValue().getEmployee().getId()))
        );
        invoiceTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                invoiceStoreController.onRowSelected(newValue);
            }
        });
        invoiceIdColumn.prefWidthProperty().bind(invoiceTable.widthProperty().multiply(0.25));
        nomenclatureIdColumn.prefWidthProperty().bind(invoiceTable.widthProperty().multiply(0.25));
        dateColumn.prefWidthProperty().bind(invoiceTable.widthProperty().multiply(0.25));
        employeeNameColumn.prefWidthProperty().bind(invoiceTable.widthProperty().multiply(0.25));

    }
}
