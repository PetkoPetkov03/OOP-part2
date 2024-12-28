package com.sparks.of.fabrication.oop2.scenes.checkout;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Item;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Checkout {
    @FXML
    private TextField scanField;

    @FXML
    private ListView<ScannedItem> scannedItemsList;

    @FXML
    private Label totalLabel;

    @FXML
    private Button finishTransactionButton;

    private final StringBuilder builder = new StringBuilder();

    private ObservableList<ScannedItem> scannedItems;

    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private double totalAmount = 0.0;

    @FXML
    public void initialize() {
        scannedItems = FXCollections.observableArrayList();
        scannedItemsList.setItems(scannedItems);

        scanField.setOnAction(event -> {
            builder.append(scanField.getText().trim());
            if (builder.toString().startsWith("+") && builder.length() > 2) {
                handleAddItem(builder.substring(1));
            } else if (builder.toString().startsWith("-") && builder.length() > 2) {
                handleRemoveItem(builder.substring(1));
            }
            builder.setLength(0);
            scanField.clear();
        });
        finishTransactionButton.setOnAction(event -> finishTransaction());
    }
    private void handleAddItem(String code) {
        Item item = entityManagerWrapper.findEntityById(Item.class, Integer.parseInt(code)).y();
        if (item != null) {
            ScannedItem existingItem = findExistingItem(item.getName());
            if (existingItem != null)
                existingItem.incrementQuantity();
            else {
                ScannedItem newItem = new ScannedItem();
                newItem.setName(item.getName());
                newItem.setPrice(item.getPrice());
                newItem.setQuantity(1);
                scannedItems.add(newItem);
            }
            scannedItemsList.refresh();
            totalAmount += item.getPrice();
            totalLabel.setText(String.format("$%.2f", totalAmount));
        } else {
            System.out.println("Item not found in the database for code: " + code);
        }
    }
    private ScannedItem findExistingItem(String name) {
        for (ScannedItem item : scannedItems) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    private void handleRemoveItem(String code) {
        Item item = entityManagerWrapper.findEntityById(Item.class, Integer.parseInt(code)).y();
        if (item != null) {
            ScannedItem existingItem = new ScannedItem();
            existingItem.setName(item.getName());
            existingItem.setPrice(item.getPrice());
            if (scannedItems.contains(existingItem)) {
                if (existingItem.getQuantity() > 1) {
                    existingItem.setQuantity(existingItem.getQuantity() - 1);
                    totalAmount -= existingItem.getPrice();
                } else {
                    scannedItems.remove(existingItem);
                    totalAmount -= existingItem.getPrice();
                }
                totalLabel.setText(String.format("$%.2f", totalAmount));
            }
            else {
                System.out.println("Item not found in the scanned list for removal: " + code);
            }
        } else {
            System.out.println("Item not found in database for code: " + code);
        }
    }
    //NEED TO UPDATE TO ITEMS IN DATABASE
    private void finishTransaction() {
        System.out.println(totalAmount);
        scannedItems.clear();
        totalAmount = 0.0;
        totalLabel.setText("0.00");
    }
}
