package com.sparks.of.fabrication.oop2.scenes.checkout;

import com.sparks.of.fabrication.oop2.models.Client;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

/**
 * A utility class that helps configure the columns of a TableView for displaying
 * scanned items during the checkout process
 */
public class TableViewCheckout {

    /**
     * Configures the columns of the given TableView and ComboBox.
     * Sets the cell value factories for the columns in the TableView to display
     *
     * @param scannedItemsTable The TableView displaying the scanned items.
     * @param idColumn The TableColumn for displaying the ID of the scanned item.
     * @param nameColumn The TableColumn for displaying the name of the scanned item.
     * @param priceColumn The TableColumn for displaying the price of the scanned item.
     * @param quantityColumn The TableColumn for displaying the quantity of the scanned item.
     * @param clientBox The ComboBox for selecting a client.
     */
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
