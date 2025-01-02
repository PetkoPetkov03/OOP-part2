package com.sparks.of.fabrication.oop2.scenes.checkout;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.*;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.TableView;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

public class CheckoutServices {

    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private static final Logger log = LogManager.getLogger(CheckoutServices.class);
    private final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

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

    protected void clearScannedItems(List<ScannedItem> scannedItems) {
        try {
            scannedItems.clear();
        } catch (Exception e) {
            log.error("Error clearing scanned items: {}", e.getMessage());
            logEmployee.createLog("Clearing Scanned Items Error", "Error clearing scanned items: " + e.getMessage());
            throw e;
        }
    }

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

    protected Item fetchItemByCode(String code) {
        try {
            return entityManagerWrapper.findEntityById(Item.class, Integer.parseInt(code)).y();
        } catch (NumberFormatException e) {
            log.warn("Invalid code format: {}", code);
            logEmployee.createLog("Invalid Code Format", "Attempted to fetch item with invalid code: " + code);
            return null;
        }
    }

    protected ScannedItem findExistingItem(String name, List<ScannedItem> scannedItems) {
        return scannedItems.stream()
                .filter(item -> item.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    protected void addNewItemToScannedList(Item item, List<ScannedItem> scannedItems) {
        ScannedItem newItem = createScannedItem(item);
        scannedItems.add(newItem);
    }

    protected ScannedItem createScannedItem(Item item) {
        ScannedItem scannedItem = new ScannedItem();
        scannedItem.setId(item.getIdItem());
        scannedItem.setName(item.getName());
        scannedItem.setPrice(item.getPrice());
        scannedItem.setQuantity(1);
        return scannedItem;
    }

    protected void removeOrDecrementItem(ScannedItem item, List<ScannedItem> scannedItems, TableView<ScannedItem> scannedItemsList) {
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        } else {
            scannedItems.remove(item);
        }
        scannedItemsList.refresh();
    }

    protected Checkout findCheckout(Employee loggedEmployee) throws NoSuchFieldException {
        Field field = Checkout.class.getDeclaredField("employee");
        return entityManagerWrapper.findEntityByVal(Checkout.class, field, loggedEmployee).y();
    }

    protected void handleAddToCheckout(double amount, Checkout checkout) {
        checkout.setCash(checkout.getCash() + amount);
        entityManagerWrapper.genEntity(checkout);
    }

    protected void handleRemoveFromCheckout(double amount, Checkout checkout) {
        checkout.setCash(checkout.getCash() - amount);
        entityManagerWrapper.genEntity(checkout);
    }

    protected List<Client> loadClient() {
        return entityManagerWrapper.findAllEntities(Client.class);
    }
}
