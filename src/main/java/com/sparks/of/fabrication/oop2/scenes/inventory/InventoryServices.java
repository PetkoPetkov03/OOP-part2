package com.sparks.of.fabrication.oop2.scenes.inventory;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Category;
import com.sparks.of.fabrication.oop2.models.Client;
import com.sparks.of.fabrication.oop2.models.Item;
import com.sparks.of.fabrication.oop2.models.Suppliers;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.lang.reflect.Field;
import java.util.List;

public class InventoryServices {
    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    protected ObservableList<Item> loadItems(String text) throws NoSuchFieldException {
        Field field = Item.class.getDeclaredField("name");
        List<Item> itemList = entityManagerWrapper.findEntityByValAllLikeR(Item.class,field,text).y();
        ObservableList<Item> items = FXCollections.observableArrayList(itemList);
        return items;
    }
    protected Boolean updateItem(Item currentItem,String nameField,double priceField,double arrivalPriceField,
                              int quantityField,Category category){
        currentItem.setName(nameField);
        currentItem.setPrice(priceField);
        currentItem.setArrivalPrice(arrivalPriceField);
        currentItem.setQuantity(quantityField);
        currentItem.setCategory(category);

        return entityManagerWrapper.genEntity(currentItem);
    }
    protected Boolean createNewItem(String nameField,double priceField,double arrivalPriceField,int quantityField,Category category) {
        Item newItem = new Item();
        newItem.setName(nameField);
        newItem.setPrice(priceField);
        newItem.setArrivalPrice(arrivalPriceField);
        newItem.setQuantity(quantityField);
        newItem.setCategory(category);

        return entityManagerWrapper.genEntity(newItem);
    }
    protected void BCatSupCli(ComboBox<String> catCliSupComboBox,int catCliSup){
        catCliSupComboBox.getItems().clear();
        switch (catCliSup){
            case 1:
                List<Category> categories = entityManagerWrapper.findAllEntities(Category.class);
                for (Category category : categories) {catCliSupComboBox.getItems().add(category.getCategory());}
                break;
            case 2:
                List<Suppliers> suppliers = entityManagerWrapper.findAllEntities(Suppliers.class);
                for (Suppliers supplier : suppliers) {catCliSupComboBox.getItems().add(supplier.getName());}
                break;
            case 3:
                List<Client> clients = entityManagerWrapper.findAllEntities(Client.class);
                for (Client client : clients) {catCliSupComboBox.getItems().add(client.getName());}
                break;
        }
    }
    protected void loadCategories(ComboBox<String> categoryComboBox) {
        List<Category> categories = entityManagerWrapper.findAllEntities(Category.class);
        List<String> categoryNames = categories.stream()
                .map(Category::getCategory)
                .toList();
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryNames));
    }
    @FXML
    protected void catCliSupCreate(int catCliSup, TextField catCliSupText){
        switch (catCliSup) {
            case 1 -> {
                Category category = new Category();
                category.setCategory(catCliSupText.getText());
                entityManagerWrapper.genEntity(category);
            }
            case 2 -> {
                Suppliers supplier = new Suppliers();
                supplier.setName(catCliSupText.getText());
                entityManagerWrapper.genEntity(supplier);
            }
            case 3 -> {
                Client client = new Client();
                client.setName(catCliSupText.getText());
                entityManagerWrapper.genEntity(client);
            }
        }
    }
    @FXML
    protected void catCliSupDelete(ComboBox<String> catCliSupComboBox,int catCliSup,Field field){
        switch (catCliSup) {
            case 1 -> {
                Category category = entityManagerWrapper.findEntityByVal(Category.class,field,catCliSupComboBox.getValue()).y();
                entityManagerWrapper.deleteEntityById(Category.class,category.getIdCategory().intValue());
            }
            case 2 -> {
                Suppliers supplier = entityManagerWrapper.findEntityByVal(Suppliers.class,field,catCliSupComboBox.getValue()).y();
                entityManagerWrapper.deleteEntityById(Suppliers.class,supplier.getIdSupplier().intValue());
            }
            case 3 -> {
                Client client = entityManagerWrapper.findEntityByVal(Client.class,field,catCliSupComboBox.getValue()).y();
                entityManagerWrapper.deleteEntityById(Client.class,client.getId().intValue());
            }
        }
    }
}
