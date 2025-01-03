package com.sparks.of.fabrication.oop2.scenes.inventory;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.*;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.lang.reflect.Field;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Service class responsible for interacting with the inventory and performing various operations such as loading, creating,
 * updating, and deleting items, categories, suppliers, and clients.
 */
public class InventoryServices {
    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    /**
     * Loads items from the database based on a search text and filters those with low quantities.
     *
     * @param text The search text to filter items.
     * @return An observable list of items matching the search criteria.
     * @throws NoSuchFieldException If there is an error accessing the specified field.
     */
    protected ObservableList<Item> loadItems(String text) throws NoSuchFieldException {
        Field field = Item.class.getDeclaredField("name");
        List<Item> itemList = entityManagerWrapper.findEntityByValAllLikeR(Item.class, field, text).y();
        lowQuantity(itemList);
        ObservableList<Item> items = FXCollections.observableArrayList(itemList);
        return items;
    }

    /**
     * Updates the specified item with new data.
     *
     * @param currentItem The item to update.
     * @param nameField The new name of the item.
     * @param priceField The new price of the item.
     * @param arrivalPriceField The new arrival price of the item.
     * @param quantityField The new quantity of the item.
     * @param category The new category of the item.
     * @return True if the item was successfully updated, false otherwise.
     */
    protected Boolean updateItem(Item currentItem, String nameField, double priceField, double arrivalPriceField,
                                 int quantityField, Category category) {
        currentItem.setName(nameField);
        currentItem.setPrice(priceField);
        currentItem.setArrivalPrice(arrivalPriceField);
        currentItem.setQuantity(quantityField);
        currentItem.setCategory(category);

        return entityManagerWrapper.genEntity(currentItem);
    }

    /**
     * Creates a new item with the specified data.
     *
     * @param nameField The name of the new item.
     * @param priceField The price of the new item.
     * @param arrivalPriceField The arrival price of the new item.
     * @param quantityField The quantity of the new item.
     * @param category The category of the new item.
     * @return True if the new item was successfully created, false otherwise.
     */
    protected Boolean createNewItem(String nameField, double priceField, double arrivalPriceField, int quantityField, Category category) {
        Item newItem = new Item();
        newItem.setName(nameField);
        newItem.setPrice(priceField);
        newItem.setArrivalPrice(arrivalPriceField);
        newItem.setQuantity(quantityField);
        newItem.setCategory(category);

        return entityManagerWrapper.genEntity(newItem);
    }

    /**
     * Populates the specified ComboBox with categories, suppliers, or clients based on the provided type.
     *
     * @param catCliSupComboBox The ComboBox to populate.
     * @param catCliSup The type of entities to load (1 for categories, 2 for suppliers, 3 for clients).
     */
    protected void BCatSupCli(ComboBox<String> catCliSupComboBox, int catCliSup) {
        catCliSupComboBox.getItems().clear();
        switch (catCliSup) {
            case 1:
                List<Category> categories = entityManagerWrapper.findAllEntities(Category.class);
                for (Category category : categories) {
                    catCliSupComboBox.getItems().add(category.getCategory());
                }
                break;
            case 2:
                List<Suppliers> suppliers = entityManagerWrapper.findAllEntities(Suppliers.class);
                for (Suppliers supplier : suppliers) {
                    catCliSupComboBox.getItems().add(supplier.getName());
                }
                break;
            case 3:
                List<Client> clients = entityManagerWrapper.findAllEntities(Client.class);
                for (Client client : clients) {
                    catCliSupComboBox.getItems().add(client.getName());
                }
                break;
        }
    }

    /**
     * Loads categories into the specified ComboBox.
     *
     * @param categoryComboBox The ComboBox to populate with category names.
     */
    protected void loadCategories(ComboBox<String> categoryComboBox) {
        List<Category> categories = entityManagerWrapper.findAllEntities(Category.class);
        List<String> categoryNames = categories.stream()
                .map(Category::getCategory)
                .toList();
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryNames));
    }

    /**
     * Creates a new category, supplier, or client based on the provided type and input text.
     *
     * @param catCliSup The type of entity to create (1 for category, 2 for supplier, 3 for client).
     * @param catCliSupText The name or value to assign to the new entity.
     */
    @FXML
    protected void catCliSupCreate(int catCliSup, TextField catCliSupText) {
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

    /**
     * Deletes a category, supplier, or client based on the selected entity and field.
     *
     * @param catCliSupComboBox The ComboBox containing the selected entity name.
     * @param catCliSup The type of entity to delete (1 for category, 2 for supplier, 3 for client).
     * @param field The field used to identify the entity to delete.
     */
    @FXML
    protected void catCliSupDelete(ComboBox<String> catCliSupComboBox, int catCliSup, Field field) {
        switch (catCliSup) {
            case 1 -> {
                Category category = entityManagerWrapper.findEntityByVal(Category.class, field, catCliSupComboBox.getValue()).y();
                entityManagerWrapper.deleteEntityById(Category.class, category.getIdCategory().intValue());
            }
            case 2 -> {
                Suppliers supplier = entityManagerWrapper.findEntityByVal(Suppliers.class, field, catCliSupComboBox.getValue()).y();
                entityManagerWrapper.deleteEntityById(Suppliers.class, supplier.getIdSupplier().intValue());
            }
            case 3 -> {
                Client client = entityManagerWrapper.findEntityByVal(Client.class, field, catCliSupComboBox.getValue()).y();
                entityManagerWrapper.deleteEntityById(Client.class, client.getId().intValue());
            }
        }
    }

    /**
     * Deletes connections to items in NomenclatureDetails and TransactionDetail tables.
     *
     * @param item The item to delete connections for.
     * @param field1 The field used to identify the item in NomenclatureDetails.
     * @param field2 The field used to identify the item in TransactionDetail.
     * @throws NoSuchFieldException If there is an error accessing the specified fields.
     */
    protected void CascadeConnections(Item item, Field field1, Field field2) throws NoSuchFieldException {
        List<NomenclatureDetails> s = entityManagerWrapper.findEntityByValAll(NomenclatureDetails.class, field1, item).y();
        List<TransactionDetail> x = entityManagerWrapper.findEntityByValAll(TransactionDetail.class, field2, item).y();
        for (NomenclatureDetails detail : s) {
            entityManagerWrapper.deleteEntityById(NomenclatureDetails.class, detail.getIdNomDetails().intValue());
        }
        for (TransactionDetail detail : x) {
            entityManagerWrapper.deleteEntityById(TransactionDetail.class, detail.getId().intValue());
        }
    }

    /**
     * Checks the quantity of items and sends a notification if an item is low on stock.
     *
     * @param items The list of items to check for low stock.
     */
    private void lowQuantity(List<Item> items) {
        for (Item item : items) {
            if (item.getQuantity() < 5) {
                Notification notification = new Notification();
                notification.setDateSent(Date.valueOf(LocalDate.now()));
                notification.setEmployee(Singleton.getInstance(Employee.class));
                notification.setMessage("Item " + item.getName() + " is low on stock!");
                notification.setStatus("unread");
                entityManagerWrapper.genEntity(notification);
            }
        }
    }
}
