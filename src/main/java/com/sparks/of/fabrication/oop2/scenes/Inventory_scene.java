package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.*;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.Pair;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TextField nameField, priceField, arrivalPriceField, quantityField, searchField, idField, catCliSupText;

    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private ComboBox<String> catCliSupComboBox;

    @FXML
    private Button saveButton;
    @FXML
    private Button catCliSupCreate,catCliSupDelete;

    private final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    private boolean isEditMode = false;
    private int catCliSup;
    private Item currentItem;

    @FXML
    public void initialize() {
        idItemColumn.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory().getCategory())
        );
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        arrivalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalPrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadCategories();

        idItemColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.10));
        nameColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.20));
        categoryColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.15));
        priceColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.15));
        arrivalPriceColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.15));
        quantityColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.15));

        inventoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                isEditMode = true;
                currentItem = newValue;
                populateFields(currentItem);
            } else {
                isEditMode = false;
                clearFields();
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadItems(newValue);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
        saveButton.setOnAction(event -> {
            try {
                saveOrUpdateItem();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
        searchField.setText(" ");
        searchField.setText("");
    }

    private void loadItems(String text) throws NoSuchFieldException {
        Field field = Item.class.getDeclaredField("name");
        List<Item> itemList = entityManagerWrapper.findEntityByValAllLikeR(Item.class,field,text).y();
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
        idField.setText(item.getIdItem().toString());
        nameField.setText(item.getName());
        priceField.setText(String.valueOf(item.getPrice()));
        arrivalPriceField.setText(String.valueOf(item.getArrivalPrice()));
        quantityField.setText(String.valueOf(item.getQuantity()));
        categoryComboBox.setValue(item.getCategory().getCategory());
    }

    private void clearFields() {
        idField.clear();
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
            searchField.setText("");
            clearFields();
        } else {
            System.out.println("Error creating new item.");
        }
    }
    @FXML
    private void BCat(){
        catCliSupComboBox.getItems().clear();
        catCliSup = 1;
        List<Category> categories = entityManagerWrapper.findAllEntities(Category.class);

        for (Category category : categories) {
            catCliSupComboBox.getItems().add(category.getCategory());
        }
    }
    @FXML
    private void BSup(){
        catCliSupComboBox.getItems().clear();
        catCliSup = 2;
        List<Suppliers> suppliers = entityManagerWrapper.findAllEntities(Suppliers.class);

        for (Suppliers supplier : suppliers ) {
            catCliSupComboBox.getItems().add(supplier.getName());
        }
    }
    @FXML
    private void BCli(){
        catCliSupComboBox.getItems().clear();
        catCliSup = 3;
        List<Client> clients = entityManagerWrapper.findAllEntities(Client.class);

        for (Client client : clients) {
            catCliSupComboBox.getItems().add(client.getName());
        }
    }

    @FXML
    private void catCliSupCreate(){
        switch(catCliSup){
            case 1:
                Category category = new Category();
                category.setCategory(catCliSupText.getText());
                entityManagerWrapper.genEntity(category);
                break;
            case 2:
                Suppliers supplier = new Suppliers();
                supplier.setName(catCliSupText.getText());
                entityManagerWrapper.genEntity(supplier);
                break;
            case 3:
                Client client = new Client();
                client.setName(catCliSupText.getText());
                entityManagerWrapper.genEntity(client);
                break;
        }
    }
    @FXML
    private void catCliSupDelete(){

    }
}
