package com.sparks.of.fabrication.oop2.scenes.checkout;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.*;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CheckoutController {
    @FXML
    private TextField scanField;

    @FXML
    private ListView<ScannedItem> scannedItemsList;

    @FXML
    private ComboBox<Client> clientBox;

    @FXML
    private Label totalLabel;

    @FXML
    private Button finishTransactionButton;

    private Checkout checkout;

    private Employee loggedemployee = Singleton.getInstance(Employee.class);

    private final StringBuilder builder = new StringBuilder();

    private ObservableList<ScannedItem> scannedItems;

    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private double totalAmount = 0.0;

    @FXML
    public void initialize() throws NoSuchFieldException {
        List<Client> clients = entityManagerWrapper.findAllEntities(Client.class);
        clientBox.getItems().setAll(clients);

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

        Field field = Checkout.class.getDeclaredField("employee");
        checkout = entityManagerWrapper.findEntityByVal(Checkout.class,field,loggedemployee).y();

        scannedItems = FXCollections.observableArrayList();
        scannedItemsList.setItems(scannedItems);

        scanField.setOnAction(event -> {
            builder.append(scanField.getText().trim());
            if (builder.toString().startsWith("+") && builder.length() > 1) {
                handleAddItem(builder.substring(1));
            } else if (builder.toString().startsWith("-") && builder.length() > 1) {
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
                newItem.setId(item.getIdItem());
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
            ScannedItem existingItem = findExistingItem(item.getName());
            if (scannedItems.contains(existingItem)) {
                if (existingItem.getQuantity() > 1) {
                    existingItem.setQuantity(existingItem.getQuantity() - 1);
                    totalAmount -= existingItem.getPrice();
                } else {
                    scannedItems.remove(existingItem);
                    scannedItemsList.refresh();
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
    private void finishTransaction() {
        Employee employee = Singleton.getInstance(Employee.class);
        Client client = clientBox.getValue();

        Transaction transaction = new Transaction();
        transaction.setClient(client);
        transaction.setEmployee(employee);
        transaction.setCheckout(checkout);

        List<TransactionDetail> transactionDetails = new ArrayList<>();
        double totalAmount = 0.0;

        for (ScannedItem scannedItem : scannedItems) {
            Item dbItem = entityManagerWrapper.findEntityById(Item.class, scannedItem.getId().intValue()).y();
            dbItem.setQuantity(dbItem.getQuantity() - scannedItem.getQuantity());
            entityManagerWrapper.genEntity(dbItem);

            TransactionDetail transactionDetail = new TransactionDetail();
            transactionDetail.setTransaction(transaction);
            transactionDetail.setItem(dbItem);
            transactionDetail.setQuantity(scannedItem.getQuantity());

            transactionDetails.add(transactionDetail);

            totalAmount += dbItem.getPrice() * scannedItem.getQuantity();
        }

        transaction.setTransactionDetails(transactionDetails);
        transaction.setTotalAmount(totalAmount);

        entityManagerWrapper.genEntity(transaction);

        scannedItems.clear();

        checkout.setCash(checkout.getCash() + totalAmount);
        totalLabel.setText("0.00");
    }



}
