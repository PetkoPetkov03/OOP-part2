package com.sparks.of.fabrication.oop2.scenes.arrivalGoods;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.*;
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
    private Employee loggedInEmployee = Singleton.getInstance(Employee.class);
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
        Nomenclature nomenclature = new Nomenclature();
        nomenclatureList.add(nomenclature);
        currentIndex = nomenclatureList.size() - 1;
        loadItemsForNomenclature(currentIndex);
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.out.println("Cancel button clicked.");
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        try {
            Integer invoiceNumber = Integer.parseInt(txtDocumentNumber.getText());
            Nomenclature currentNomenclature = nomenclatureList.get(currentIndex);

            String status = cmbStatus.getValue();

            if ("Открито".equals(status)) {
                InvoiceStore invoiceStore = new InvoiceStore();
                invoiceStore.setNomenclatura(currentNomenclature);
                invoiceStore.setEmployee(loggedInEmployee);
                invoiceStore.setNumber(invoiceNumber);
                invoiceStore.setDate(java.sql.Date.valueOf(dateDocument.getValue()));
                invoiceStore.setStatus(false);
                entityManagerWrapper.genEntity(invoiceStore);
            } else if ("Закрито".equals(status)) {
                Pair<Boolean, InvoiceStore> result = entityManagerWrapper.findEntityById(InvoiceStore.class, invoiceNumber);
                InvoiceStore existingInvoiceStore = result.x() ? result.y() : null;

                if (existingInvoiceStore != null) {
                    existingInvoiceStore.setNomenclatura(currentNomenclature);
                    existingInvoiceStore.setEmployee(loggedInEmployee);
                    existingInvoiceStore.setNumber(invoiceNumber);
                    existingInvoiceStore.setDate(java.sql.Date.valueOf(dateDocument.getValue()));
                    existingInvoiceStore.setStatus(true);

                    entityManagerWrapper.genEntity(existingInvoiceStore);
                } else {
                    System.err.println("InvoiceStore with number " + invoiceNumber + " not found.");
                }
            }

            for (Item item : itemList) {
                NomenclatureDetails details = new NomenclatureDetails();
                details.setNomenclature(currentNomenclature);
                details.setItem(item);
                details.setItemQuantity(item.getQuantity());
                details.setItemPrice(item.getPrice());
                entityManagerWrapper.genEntity(details);
            }

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