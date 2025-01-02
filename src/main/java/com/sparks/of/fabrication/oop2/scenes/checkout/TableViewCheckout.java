package com.sparks.of.fabrication.oop2.scenes.checkout;

import com.sparks.of.fabrication.oop2.models.Client;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

public class TableViewCheckout {
    public static void configureTableColumns(TableView<ScannedItem> scannedItemsTable,
                                             TableColumn<ScannedItem, Long> idColumn,
                                             TableColumn<ScannedItem, String> nameColumn,
                                             TableColumn<ScannedItem, Double> priceColumn,
                                             TableColumn<ScannedItem, Integer> quantityColumn,
                                             ComboBox<Client> clientBox){
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));
        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQuantity()));
        idColumn.prefWidthProperty().bind(scannedItemsTable.widthProperty().multiply(0.15));
        nameColumn.prefWidthProperty().bind(scannedItemsTable.widthProperty().multiply(0.35));
        priceColumn.prefWidthProperty().bind(scannedItemsTable.widthProperty().multiply(0.25));
        quantityColumn.prefWidthProperty().bind(scannedItemsTable.widthProperty().multiply(0.25));
        clientBox.setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                return client != null ? client.getName() : "";
            }

            @Override
            public Client fromString(String string) {
                return null;
            }
        });
    }
}
