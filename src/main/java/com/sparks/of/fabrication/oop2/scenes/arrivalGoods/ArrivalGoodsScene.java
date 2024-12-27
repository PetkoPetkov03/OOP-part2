package com.sparks.of.fabrication.oop2.scenes.arrivalGoods;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.*;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.lang.reflect.Field;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArrivalGoodsScene {

    @FXML private TableView<Item> arrivalTable;
    @FXML private TableColumn<Item, Long> colCode;
    @FXML private TableColumn<Item, String> colName;
    @FXML private TableColumn<Item, String> colMeasure;
    @FXML private TableView<AmSData> AmS;
    @FXML private TableColumn<AmSData, Integer> colQuantity;
    @FXML private TableColumn<AmSData, Double> colArrivalPrice;
    @FXML private TableColumn<AmSData, Double> colSellingPrice;
    @FXML private TextField txtServiceNumber;
    @FXML private DatePicker dateDocument;
    @FXML private TextField txtDocumentNumber;
    @FXML private ComboBox<String> cmbDocumentType;
    @FXML private ComboBox<String> cmbPaymentType;
    @FXML private TextField txtSupplier;
    @FXML private Label lblEmployee;
    @FXML private DatePicker lblSystemDate;
    @FXML private ComboBox<ArrivalState> cmbStatus;
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
    ObservableList<AmSData> amsData = FXCollections.observableArrayList();
    private ArrivalGoodsService arrivalGoodsService;
    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private Employee loggedInEmployee = Singleton.getInstance(Employee.class);

    @FXML
    private void initialize() {
        arrivalGoodsService = new ArrivalGoodsService();
        nomenclatureList = new ArrayList<>();
        amsData = FXCollections.observableArrayList();

        TableViewSetup.configureTableColumns(arrivalTable, colCode, colName, colMeasure, AmS, colQuantity, colArrivalPrice, colSellingPrice);

        lblSystemDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Date selected: " + newValue);
                loadItemsFromDatabase(newValue);
            }
        });

        arrivalTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        AmS.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colCode.prefWidthProperty().bind(arrivalTable.widthProperty().multiply(0.2));
        colName.prefWidthProperty().bind(arrivalTable.widthProperty().multiply(0.5));
        colMeasure.prefWidthProperty().bind(arrivalTable.widthProperty().multiply(0.3));

        colQuantity.prefWidthProperty().bind(AmS.widthProperty().multiply(0.33));
        colArrivalPrice.prefWidthProperty().bind(AmS.widthProperty().multiply(0.33));
        colSellingPrice.prefWidthProperty().bind(AmS.widthProperty().multiply(0.34));

        lblSystemDate.setValue(LocalDate.now());
        cmbDocumentType.setItems(FXCollections.observableArrayList("Стокова разписка", "Фактура"));
        cmbPaymentType.setItems(FXCollections.observableArrayList("В брой", "Карта"));
        cmbStatus.setItems(FXCollections.observableArrayList(ArrivalState.values()));
        cmbStatus.setValue(ArrivalState.Open);
    }

    private void loadItemsFromDatabase(LocalDate selectedDate) {
        nomenclatureList = arrivalGoodsService.loadItems(selectedDate);
    }

    private void loadItemsForNomenclature(int currentIndex) {
        try {
            List<Item> items = arrivalGoodsService.loadItemsForNomenclature(nomenclatureList.get(currentIndex));

            itemList = FXCollections.observableArrayList(items);
            arrivalTable.setItems(itemList);

            for (Item item : arrivalTable.getItems()) {
                amsData.add(new AmSData(0, item.getArrivalPrice(), item.getPrice())); // Set quantity to 0
            }

            AmS.setItems(amsData);
            arrivalTable.refresh();
            AmS.refresh();
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
        Integer invoiceNumber = currentIndex;
        Nomenclature currentNomenclature = nomenclatureList.get(currentIndex);

        currentNomenclature.setEmployee(loggedInEmployee);
        currentNomenclature.setSuppliers(entityManagerWrapper.findEntityById(Suppliers.class, 1).y());

        entityManagerWrapper.genEntity(currentNomenclature);

        ArrivalState status = cmbStatus.getValue();

        if (status.equals(ArrivalState.Closed)) {
            InvoiceStore invoiceStore = new InvoiceStore();
            invoiceStore.setNomenclatura(currentNomenclature);
            invoiceStore.setEmployee(loggedInEmployee);
            invoiceStore.setNumber(invoiceNumber);
            invoiceStore.setDate(Date.valueOf(LocalDate.now()));
            invoiceStore.setStatus(true);

            entityManagerWrapper.genEntity(invoiceStore);

            for (Item item : arrivalTable.getItems()) {
                Item dbItem = entityManagerWrapper.findEntityById(Item.class, item.getIdItem().intValue()).y();

                int rowIndex = arrivalTable.getItems().indexOf(item);

                if (rowIndex >= 0 && rowIndex < AmS.getItems().size()) {
                    Double newArrivalPrice = colArrivalPrice.getCellData(rowIndex);
                    Double newSellingPrice = colSellingPrice.getCellData(rowIndex);
                    Integer newTableQuantity = colQuantity.getCellData(rowIndex);

                    boolean priceChanged = !dbItem.getArrivalPrice().equals(newArrivalPrice) || !dbItem.getPrice().equals(newSellingPrice);

                    if (priceChanged) {
                        dbItem.setArrivalPrice(newArrivalPrice);
                        dbItem.setPrice(newSellingPrice);
                    }
                    dbItem.setQuantity(dbItem.getQuantity() + (newTableQuantity != null ? newTableQuantity : 0));
                    entityManagerWrapper.genEntity(dbItem);

                    NomenclatureDetails nomenclatureDetails = new NomenclatureDetails();
                    nomenclatureDetails.setNomenclature(currentNomenclature);
                    nomenclatureDetails.setItem(dbItem);
                    nomenclatureDetails.setItemQuantity(newTableQuantity != null ? newTableQuantity : 0);
                    nomenclatureDetails.setItemPrice(newArrivalPrice);

                    entityManagerWrapper.genEntity(nomenclatureDetails);
                }
            }
        }
    }


    @FXML
    private void handleAddRow(ActionEvent event) {
        Item item = new Item();
        arrivalTable.getItems().add(item);
        AmSData newEntry = new AmSData(0,0.0,0.0);
        amsData.add(newEntry);
        AmS.setItems(amsData);
        AmS.refresh();
        arrivalTable.refresh();
    }

}
