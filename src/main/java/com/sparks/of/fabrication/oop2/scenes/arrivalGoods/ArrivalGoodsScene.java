package com.sparks.of.fabrication.oop2.scenes.arrivalGoods;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.InvoiceStore;
import com.sparks.of.fabrication.oop2.models.Item;
import com.sparks.of.fabrication.oop2.models.Nomenclature;
import com.sparks.of.fabrication.oop2.models.NomenclatureDetails;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.Pair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArrivalGoodsScene {

    @FXML private TableView<Item> arrivalTable;
    @FXML private TableColumn<Item, Long> colCode;
    @FXML private TableColumn<Item, String> colName;
    @FXML private TableColumn<Item, String> colMeasure;
    @FXML private TableColumn<Item, Integer> colQuantity;
    @FXML private TableColumn<Item, Double> colArrivalPrice;
    @FXML private TableColumn<Item, Double> colMarkup;
    @FXML private TableColumn<Item, Double> colSellingPrice;
    @FXML private TextField txtServiceNumber;
    @FXML private DatePicker dateDocument;
    @FXML private TextField txtDocumentNumber;
    @FXML private ComboBox<String> cmbDocumentType;
    @FXML private ComboBox<String> cmbPaymentType;
    @FXML private TextField txtSupplier;
    @FXML private Label lblEmployee;
    @FXML private DatePicker lblSystemDate;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private Button btnFirst;
    @FXML private Button btnPrevious;
    @FXML private Button btnNext;
    @FXML private Button btnLast;
    @FXML private Button btnNew;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;
    @FXML private Button btnAddRow;

    private int currentIndex = -1;
    private List<Nomenclature> nomenclatureList;
    private ObservableList<Item> itemList;
    private ArrivalGoodsService arrivalGoodsService;
    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    @FXML
    private void initialize() {
        arrivalGoodsService = new ArrivalGoodsService();
        nomenclatureList = new ArrayList<>();

        TableViewSetup.configureTableColumns(arrivalTable, colCode, colName, colMeasure, colQuantity, colArrivalPrice, colMarkup, colSellingPrice);

        lblSystemDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Date selected: " + newValue);
                loadItemsFromDatabase(newValue);
            }
        });

        colSellingPrice.prefWidthProperty().bind(arrivalTable.widthProperty().multiply(0.2));
        colMarkup.prefWidthProperty().bind(arrivalTable.widthProperty().multiply(0.1));

        // Set the width dynamically (you can adjust percentages as needed)
        colCode.prefWidthProperty().bind(arrivalTable.widthProperty().multiply(0.1));
        colName.prefWidthProperty().bind(arrivalTable.widthProperty().multiply(0.3));
        colMeasure.prefWidthProperty().bind(arrivalTable.widthProperty().multiply(0.1));
        colQuantity.prefWidthProperty().bind(arrivalTable.widthProperty().multiply(0.1));
        colArrivalPrice.prefWidthProperty().bind(arrivalTable.widthProperty().multiply(0.1));


        lblSystemDate.setValue(LocalDate.now());
        cmbDocumentType.setItems(FXCollections.observableArrayList("Стокова разписка", "Фактура"));
        cmbPaymentType.setItems(FXCollections.observableArrayList("В брой", "Карта"));
        cmbStatus.setItems(FXCollections.observableArrayList("Открита", "Закрита"));
    }

    private void loadItemsFromDatabase(LocalDate selectedDate) {
        nomenclatureList = arrivalGoodsService.loadItems(selectedDate);
    }

    private void loadItemsForNomenclature(int currentIndex) {
        try {
            List<Item> items = arrivalGoodsService.loadItemsForNomenclature(nomenclatureList.get(currentIndex));


            itemList = FXCollections.observableArrayList(items);
            arrivalTable.setItems(itemList);
        } catch (Exception e) {
            System.err.println("Error loading items for nomenclature: " + e.getMessage());
        }
    }

    @FXML
    private void handleFirstButtonAction(ActionEvent event) {
        if (!nomenclatureList.isEmpty()) {
            currentIndex = 0;
            loadItemsForNomenclature(currentIndex);
        }
    }

    @FXML
    private void handlePreviousButtonAction(ActionEvent event) {
        if (currentIndex > 0) {
            currentIndex--;
            loadItemsForNomenclature(currentIndex);
        }
    }

    @FXML
    private void handleNextButtonAction(ActionEvent event) {
        if (currentIndex < nomenclatureList.size() - 1) {
            currentIndex++;
            loadItemsForNomenclature(currentIndex);
        }
    }

    @FXML
    private void handleLastButtonAction(ActionEvent event) {
        if (!nomenclatureList.isEmpty()) {
            currentIndex = nomenclatureList.size() - 1;
            loadItemsForNomenclature(currentIndex);
        }
    }

    @FXML
    private void handleNewButtonAction(ActionEvent event) {
        System.out.println("New button clicked.");
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.out.println("Cancel button clicked.");
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        try {
            // Retrieve necessary values from the UI
            Integer invoiceNumber = Integer.parseInt(txtDocumentNumber.getText());
            Nomenclature currentNomenclature = nomenclatureList.get(currentIndex); // Assuming currentIndex is set properly

            // Get the selected status from cmbStatus combobox
            String status = cmbStatus.getValue();

            // If status is "Открито", set the status of InvoiceStore to false (open)
            boolean isInvoiceClosed = "Закрито".equals(status); // true if "Закрито", false if "Открито"

            // Create or update the InvoiceStore entity
            if ("Открито".equals(status)) {
                // Create a new InvoiceStore if it's not found
                InvoiceStore invoiceStore = new InvoiceStore();
                invoiceStore.setNomenclatura(currentNomenclature);
                //invoiceStore.setEmployee(getLoggedInEmployee()); // You need to implement this method to fetch logged-in employee
                invoiceStore.setNumber(invoiceNumber);
                invoiceStore.setDate(java.sql.Date.valueOf(dateDocument.getValue())); // Use DatePicker value
                invoiceStore.setStatus(false); // Invoice is open, status = false
                entityManagerWrapper.genEntity(invoiceStore); // Save the new InvoiceStore entity
            } else if ("Закрито".equals(status)) {
                // If status is "Закрито", try to find existing InvoiceStore by number
                Pair<Boolean, InvoiceStore> result = entityManagerWrapper.findEntityById(InvoiceStore.class, invoiceNumber);
                InvoiceStore existingInvoiceStore = result.x() ? result.y() : null;

                if (existingInvoiceStore != null) {
                    // Update the existing InvoiceStore if found
                    existingInvoiceStore.setNomenclatura(currentNomenclature);
                  //  existingInvoiceStore.setEmployee(getLoggedInEmployee());
                    existingInvoiceStore.setNumber(invoiceNumber);
                    existingInvoiceStore.setDate(java.sql.Date.valueOf(dateDocument.getValue()));
                    existingInvoiceStore.setStatus(true); // Invoice is closed, status = true

                    entityManagerWrapper.genEntity(existingInvoiceStore); // Update the existing InvoiceStore
                } else {
                    // If no InvoiceStore found, handle as an error or log it
                    System.err.println("InvoiceStore with number " + invoiceNumber + " not found.");
                }
            }

            // Handle NomenclatureDetails (the items associated with the invoice)
            for (Item item : itemList) {
                NomenclatureDetails details = new NomenclatureDetails();
                details.setNomenclature(currentNomenclature);
                details.setItem(item);
                details.setItemQuantity(item.getQuantity()); // Assuming Item has a getQuantity method
                details.setItemPrice(item.getPrice()); // Assuming Item has a getPrice method
                entityManagerWrapper.genEntity(details); // Save NomenclatureDetails
            }

            // Inform the user that the save was successful
            System.out.println("Data saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddRow(ActionEvent event) {
        Item newItem = new Item();
        itemList.add(newItem);
        arrivalTable.refresh();
    }
}