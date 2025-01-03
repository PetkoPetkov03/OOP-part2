package com.sparks.of.fabrication.oop2.scenes.inventory;

import com.sparks.of.fabrication.oop2.models.Item;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewSetup {

    /**
     * Configures the columns of the inventory table to display item details.
     *
     * @param inventoryTable the TableView of Item objects
     * @param idItemColumn the TableColumn displaying the item ID
     * @param nameColumn the TableColumn displaying the item name
     * @param categoryColumn the TableColumn displaying the item category
     * @param priceColumn the TableColumn displaying the item price
     * @param arrivalPriceColumn the TableColumn displaying the arrival price
     * @param quantityColumn the TableColumn displaying the item quantity
     * @param inventoryController the controller that manages the inventory table and item interactions
     */
    public static void configureTableColumns(TableView<Item> inventoryTable,
                                             TableColumn<Item, Long> idItemColumn,
                                             TableColumn<Item, String> nameColumn,
                                             TableColumn<Item, String> categoryColumn,
                                             TableColumn<Item, Double> priceColumn,
                                             TableColumn<Item, Double> arrivalPriceColumn,
                                             TableColumn<Item, Integer> quantityColumn,
                                             InventoryController inventoryController) {

        idItemColumn.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory().getCategory())
        );
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        arrivalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalPrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        idItemColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.10));
        nameColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.20));
        categoryColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.15));
        priceColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.15));
        arrivalPriceColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.20));
        quantityColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.20));

        inventoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                inventoryController.setEditMode(true);
                inventoryController.setCurrentItem(newValue);
                inventoryController.populateFields(inventoryController.getCurrentItem());
            } else {
                inventoryController.setEditMode(false);
                inventoryController.clearFields();
            }
        });

        inventoryController.getSearchField().textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                inventoryController.loadItems(newValue);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
        inventoryController.getSaveButton().setOnAction(event -> {
            try {
                inventoryController.saveOrUpdateItem();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
        inventoryController.getSearchField().setText(" ");
        inventoryController.getSearchField().setText("");
    }
}
