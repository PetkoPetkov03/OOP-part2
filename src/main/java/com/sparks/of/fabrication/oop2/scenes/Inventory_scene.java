package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Category;
import com.sparks.of.fabrication.oop2.models.InvoiceStore;
import com.sparks.of.fabrication.oop2.models.Item;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.Pair;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import lombok.Setter;

import java.lang.reflect.Field;
import java.util.List;

public class Inventory_scene {

    @FXML
    private TableView<Item> inventoryTable;
    @FXML
    private TableColumn<Item, Long> idItemColumn;
    @FXML
    private TableColumn<Item, String> nameColumn;
    @FXML
    private TableColumn<Item, String> categoryColumn;
    @FXML
    private TableColumn<Item, Double> priceColumn;
    @FXML
    private TableColumn<Item, Double> arrivalPriceColumn;
    @FXML
    private TableColumn<Item, Integer> quantityColumn;

    @FXML
    private TextField nameField, priceField, arrivalPriceField, quantityField;

    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private Button saveButton;

    @Setter
    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    private boolean isEditMode = false;
    private Item currentItem;

    @FXML
    public void initialize() {
        // Set up table columns
        idItemColumn.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory().getCategory())
        );
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        arrivalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalPrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Load data into the table
        loadItems();
        loadCategories();

        // Add listener for table selection
        inventoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                isEditMode = true;
                currentItem = newValue;
                populateFields(currentItem);
            } else {
                isEditMode = false; // Deselecting the item
                clearFields();
            }
        });

        // Button action
        saveButton.setOnAction(event -> {
            try {
                saveOrUpdateItem();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadItems() {
        List<Item> itemList = entityManagerWrapper.findAllEntities(Item.class);
        ObservableList<Item> items = FXCollections.observableArrayList(itemList);
        inventoryTable.setItems(items);
    }

    private void loadCategories() {
        List<Category> categories = entityManagerWrapper.findAllEntities(Category.class);
        List<String> categoryNames = categories.stream()
                .map(Category::getCategory)
                .toList();
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryNames));
    }

    private void populateFields(Item item) {
        nameField.setText(item.getName());
        priceField.setText(String.valueOf(item.getPrice()));
        arrivalPriceField.setText(String.valueOf(item.getArrivalPrice()));
        quantityField.setText(String.valueOf(item.getQuantity()));
        categoryComboBox.setValue(item.getCategory().getCategory());
    }

    private void clearFields() {
        nameField.clear();
        priceField.clear();
        arrivalPriceField.clear();
        quantityField.clear();
        categoryComboBox.setValue(null);
    }

    @FXML
    private void saveOrUpdateItem() throws NoSuchFieldException {
        if (isEditMode) {
            updateItem();
        } else {
            createNewItem();
        }
    }

    private void updateItem() throws NoSuchFieldException {
        Field dateField = Category.class.getDeclaredField("category");
        Pair<Boolean, Category> result = entityManagerWrapper.findEntityByVal(Category.class, dateField, categoryComboBox.getValue());
        currentItem.setName(nameField.getText());
        currentItem.setPrice(Double.parseDouble(priceField.getText()));
        currentItem.setArrivalPrice(Double.parseDouble(arrivalPriceField.getText()));
        currentItem.setQuantity(Integer.parseInt(quantityField.getText()));
        currentItem.setCategory(result.y());

        boolean success = entityManagerWrapper.genEntity(currentItem);
        if (success) {
            inventoryTable.refresh();
            clearFields();
            isEditMode = false;
        } else {
            System.out.println("Error updating item.");
        }
    }

    private void createNewItem() throws NoSuchFieldException {
        Field dateField = Category.class.getDeclaredField("category");
        Pair<Boolean, Category> result = entityManagerWrapper.findEntityByVal(Category.class, dateField, categoryComboBox.getValue());
        Item newItem = new Item();
        newItem.setName(nameField.getText());
        newItem.setPrice(Double.parseDouble(priceField.getText()));
        newItem.setArrivalPrice(Double.parseDouble(arrivalPriceField.getText()));
        newItem.setQuantity(Integer.parseInt(quantityField.getText()));
        newItem.setCategory(result.y());

        boolean success = entityManagerWrapper.genEntity(newItem);
        if (success) {
            loadItems();
            clearFields();
        } else {
            System.out.println("Error creating new item.");
        }
    }
}
