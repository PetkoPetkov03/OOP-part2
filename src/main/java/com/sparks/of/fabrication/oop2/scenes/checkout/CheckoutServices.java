package com.sparks.of.fabrication.oop2.scenes.checkout;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.*;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.TableView;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

/**
 * Provides business logic and services related to managing a checkout process. This includes creating
 * and finalizing transactions, handling scanned items, updating item stock, and managing checkout cash.
 */
public class CheckoutServices {

    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private static final Logger log = LogManager.getLogger(CheckoutServices.class);
    private final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    /**
     * Creates a new transaction.
     *
     * @param employee The employee handling the transaction.
     * @param client The client making the purchase.
     * @param checkout The current checkout instance.
     * @return The created `Transaction` entity.
     * @throws Exception If an error occurs during transaction creation.
     */
    protected Transaction createTransaction(Employee employee, Client client, Checkout checkout) {
        try {
            Transaction transaction = new Transaction();
            transaction.setClient(client);
            transaction.setEmployee(employee);
            transaction.setCheckout(checkout);
            transaction.setTransactionDate(LocalDate.now());
            transaction.setTotalAmount(0.0);
            entityManagerWrapper.genEntity(transaction);

            return transaction;
        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage());
            logEmployee.createLog("Transaction Creation Error", "Error creating transaction: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Processes the scanned items.
     *
     * @param transaction The current transaction being processed.
     * @param scannedItems The list of scanned items.
     * @return The total amount of the transaction.
     * @throws Exception If an error occurs while processing scanned items.
     */
    protected double processScannedItems(Transaction transaction, List<ScannedItem> scannedItems) {
        double totalAmount = 0.0;
        try {
            for (ScannedItem scannedItem : scannedItems) {
                Item dbItem = updateItemStock(scannedItem);
                totalAmount += createTransactionDetail(transaction, scannedItem, dbItem);
            }
            return totalAmount;
        } catch (Exception e) {
            log.error("Error processing scanned items: {}", e.getMessage());
            logEmployee.createLog("Processing Scanned Items Error", "Error processing scanned items: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Updates the stock for the item associated with the scanned item.
     *
     * @param scannedItem The scanned item whose stock needs to be updated.
     * @return The updated `Item` entity.
     * @throws Exception If an error occurs while updating the item stock.
     */
    protected Item updateItemStock(ScannedItem scannedItem) {
        try {
            Item dbItem = entityManagerWrapper.findEntityById(Item.class, scannedItem.getId().intValue()).y();
            dbItem.setQuantity(dbItem.getQuantity() - scannedItem.getQuantity());
            entityManagerWrapper.genEntity(dbItem);

            return dbItem;
        } catch (Exception e) {
            log.error("Error updating item stock for item ID {}: {}", scannedItem.getId(), e.getMessage());
            logEmployee.createLog("Item Stock Update Error", "Error updating item stock for item ID " + scannedItem.getId() + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Creates a transaction detail for a scanned item and updates the transaction.
     *
     * @param transaction The current transaction.
     * @param scannedItem The scanned item.
     * @param dbItem The corresponding item from the database.
     * @return The price of the scanned item for the transaction.
     * @throws Exception If an error occurs while creating the transaction detail.
     */
    protected double createTransactionDetail(Transaction transaction, ScannedItem scannedItem, Item dbItem) {
        try {
            TransactionDetail transactionDetail = new TransactionDetail();
            transactionDetail.setTransaction(transaction);
            transactionDetail.setItem(dbItem);
            transactionDetail.setQuantity(scannedItem.getQuantity());
            transactionDetail.setPrice(scannedItem.getPrice() * scannedItem.getQuantity());
            entityManagerWrapper.genEntity(transactionDetail);

            return dbItem.getPrice() * scannedItem.getQuantity();
        } catch (Exception e) {
            log.error("Error creating transaction detail for item {}: {}", dbItem.getName(), e.getMessage());
            logEmployee.createLog("Transaction Detail Error", "Error creating transaction detail for item: " + dbItem.getName() + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Finalizes a transaction.
     *
     * @param transaction The transaction to finalize.
     * @param totalAmount The total amount for the transaction.
     * @throws Exception If an error occurs while finalizing the transaction.
     */
    protected void finalizeTransaction(Transaction transaction, double totalAmount) {
        try {
            transaction.setTotalAmount(totalAmount);
            entityManagerWrapper.genEntity(transaction);
        } catch (Exception e) {
            log.error("Error finalizing transaction: {}", e.getMessage());
            logEmployee.createLog("Transaction Finalization Error", "Error finalizing transaction: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Clears the list of scanned items.
     *
     * @param scannedItems The list of scanned items to clear.
     * @throws Exception If an error occurs while clearing the scanned items.
     */
    protected void clearScannedItems(List<ScannedItem> scannedItems) {
        try {
            scannedItems.clear();
        } catch (Exception e) {
            log.error("Error clearing scanned items: {}", e.getMessage());
            logEmployee.createLog("Clearing Scanned Items Error", "Error clearing scanned items: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Updates the cash in the checkout based on the total amount of the transaction.
     *
     * @param totalAmount The total amount for the transaction.
     * @param checkout The checkout to update.
     * @throws Exception If an error occurs while updating the checkout cash.
     */
    protected void updateCheckoutCash(double totalAmount, Checkout checkout) {
        try {
            checkout.setCash(checkout.getCash() + totalAmount);
            entityManagerWrapper.genEntity(checkout);
        } catch (Exception e) {
            log.error("Error updating checkout cash: {}", e.getMessage());
            logEmployee.createLog("Checkout Cash Update Error", "Error updating checkout cash: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Fetches an item by its id.
     *
     * @param code The id of the item to fetch.
     * @return The item corresponding to the id, or null if not found or the id is invalid.
     */
    protected Item fetchItemByCode(String code) {
        try {
            return entityManagerWrapper.findEntityById(Item.class, Integer.parseInt(code)).y();
        } catch (NumberFormatException e) {
            log.warn("Invalid code format: {}", code);
            logEmployee.createLog("Invalid Code Format", "Attempted to fetch item with invalid code: " + code);
            return null;
        }
    }

    /**
     * Finds an existing scanned item in the list of scanned items based on the item name.
     *
     * @param name The name of the item to find.
     * @param scannedItems The list of scanned items.
     * @return The found `ScannedItem`, or null if not found.
     */
    protected ScannedItem findExistingItem(String name, List<ScannedItem> scannedItems) {
        return scannedItems.stream()
                .filter(item -> item.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a new item to the scanned items list.
     *
     * @param item The item to add.
     * @param scannedItems The list of scanned items.
     */
    protected void addNewItemToScannedList(Item item, List<ScannedItem> scannedItems) {
        ScannedItem newItem = createScannedItem(item);
        scannedItems.add(newItem);
    }

    /**
     * Creates a new scanned item based on the provided item.
     *
     * @param item The item to create a scanned item for.
     * @return The created `ScannedItem`.
     */
    protected ScannedItem createScannedItem(Item item) {
        ScannedItem scannedItem = new ScannedItem();
        scannedItem.setId(item.getIdItem());
        scannedItem.setName(item.getName());
        scannedItem.setPrice(item.getPrice());
        scannedItem.setQuantity(1);
        return scannedItem;
    }

    /**
     * Removes or decrements the quantity of a scanned item from the list.
     *
     * @param item The scanned item to remove or decrement.
     * @param scannedItems The list of scanned items.
     * @param scannedItemsList The TableView that displays the scanned items.
     */
    protected void removeOrDecrementItem(ScannedItem item, List<ScannedItem> scannedItems, TableView<ScannedItem> scannedItemsList) {
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        } else {
            scannedItems.remove(item);
        }
        scannedItemsList.refresh();
    }

    /**
     * Finds the checkout associated with the logged employee.
     *
     * @param loggedEmployee The logged employee.
     * @return The `Checkout` entity associated with the employee.
     */
    protected Checkout findCheckout(Employee loggedEmployee) throws NoSuchFieldException {
        Field field = Checkout.class.getDeclaredField("employee");
        return entityManagerWrapper.findEntityByVal(Checkout.class, field, loggedEmployee).y();
    }

    /**
     * Adds an amount to the checkout cash.
     *
     * @param amount The amount to add.
     * @param checkout The checkout to update.
     */
    protected void handleAddToCheckout(double amount, Checkout checkout) {
        checkout.setCash(checkout.getCash() + amount);
        entityManagerWrapper.genEntity(checkout);
    }

    /**
     * Removes an amount from the checkout cash.
     *
     * @param amount The amount to remove.
     * @param checkout The checkout to update.
     */
    protected void handleRemoveFromCheckout(double amount, Checkout checkout) {
        checkout.setCash(checkout.getCash() - amount);
        entityManagerWrapper.genEntity(checkout);
    }

    /**
     * Loads all clients from the database.
     *
     * @return A list of all `Client` objects.
     */
    protected List<Client> loadClient() {
        return entityManagerWrapper.findAllEntities(Client.class);
    }
}
