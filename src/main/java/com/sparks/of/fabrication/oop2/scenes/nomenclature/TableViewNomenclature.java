package com.sparks.of.fabrication.oop2.scenes.nomenclature;

import com.sparks.of.fabrication.oop2.models.Item;
import com.sparks.of.fabrication.oop2.models.NomenclatureDetails;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * The TableViewNomenclature class is responsible for configuring the columns in the Nomenclature Details table.
 */
public class TableViewNomenclature {

    /**
     * Configures the columns for the Nomenclature Details table.
     *
     * @param nomenclatureTable The table to configure columns for.
     * @param idDetailsColumn The column for displaying the ID of the nomenclature details.
     * @param itemColumn The column for displaying the item name.
     * @param quantityColumn The column for displaying the quantity of the item.
     * @param priceColumn The column for displaying the price of the item.
     */
    public static void configureTableColumns(TableView<NomenclatureDetails> nomenclatureTable,
                                             TableColumn<NomenclatureDetails, Long> idDetailsColumn,
                                             TableColumn<NomenclatureDetails, String> itemColumn,
                                             TableColumn<NomenclatureDetails, Integer> quantityColumn,
                                             TableColumn<NomenclatureDetails, Double> priceColumn){
        idDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("idNomDetails"));
        itemColumn.setCellValueFactory(data -> {
            Item item = data.getValue().getItem();
            String itemName = (item != null) ? item.getName() : "Unknown Item";
            return new javafx.beans.property.SimpleStringProperty(itemName);
        });

        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));

        idDetailsColumn.prefWidthProperty().bind(nomenclatureTable.widthProperty().multiply(0.1));
        itemColumn.prefWidthProperty().bind(nomenclatureTable.widthProperty().multiply(0.4));
        quantityColumn.prefWidthProperty().bind(nomenclatureTable.widthProperty().multiply(0.2));
        priceColumn.prefWidthProperty().bind(nomenclatureTable.widthProperty().multiply(0.3));
    }
}
