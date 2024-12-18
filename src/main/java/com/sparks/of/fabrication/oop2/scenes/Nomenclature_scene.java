package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Item;
import com.sparks.of.fabrication.oop2.models.NomenclatureDetails;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class Nomenclature_scene {

    @FXML
    private TableView<NomenclatureDetails> nomenclatureTable;
    @FXML
    private TableColumn<NomenclatureDetails, Long> idDetailsColumn;
    @FXML
    private TableColumn<NomenclatureDetails, String> itemColumn;
    @FXML
    private TableColumn<NomenclatureDetails, Integer> quantityColumn;
    @FXML
    private TableColumn<NomenclatureDetails, Double> priceColumn;

    @FXML
    private TextField searchField;

    private EntityManagerWrapper entityManager = Singleton.getInstance(EntityManagerWrapper.class);

    private ObservableList<NomenclatureDetails> nomenclatureDetailsList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        setupTableColumns();
        loadNomenclatureDetails();
        setupSearchListener();
    }

    private void setupTableColumns() {
        idDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("idNomDetails"));
        itemColumn.setCellValueFactory(data -> {
            Item item = data.getValue().getItem();
            String itemName = (item != null) ? item.getName() : "Unknown Item";
            return new javafx.beans.property.SimpleStringProperty(itemName);
        });

        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));

        idDetailsColumn.prefWidthProperty().bind(nomenclatureTable.widthProperty().multiply(0.1));
        itemColumn.prefWidthProperty().bind(nomenclatureTable.widthProperty().multiply(0.4));
        quantityColumn.prefWidthProperty().bind(nomenclatureTable.widthProperty().multiply(0.2));
        priceColumn.prefWidthProperty().bind(nomenclatureTable.widthProperty().multiply(0.3));
    }

    private void loadNomenclatureDetails() {
        List<NomenclatureDetails> detailsList = entityManager.findAllEntities(NomenclatureDetails.class);
        if (detailsList != null && !detailsList.isEmpty()) {
            nomenclatureDetailsList.setAll(detailsList);
            nomenclatureTable.setItems(nomenclatureDetailsList);
        } else {
            System.out.println("No nomenclature details found in the database.");
        }
    }

    private void setupSearchListener() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterTableByItemName(newValue));
    }

    private void filterTableByItemName(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            nomenclatureTable.setItems(nomenclatureDetailsList);
        } else {
            ObservableList<NomenclatureDetails> filteredList = FXCollections.observableArrayList();
            for (NomenclatureDetails detail : nomenclatureDetailsList) {
                if (detail.getItem().getName().toLowerCase().contains(filterText.toLowerCase())) {
                    filteredList.add(detail);
                }
            }
            nomenclatureTable.setItems(filteredList);
        }
    }

    @FXML
    private void Add() {
        NomenclatureDetails newDetail = new NomenclatureDetails();

        newDetail.setItem(new Item());
        newDetail.setItemQuantity(0);
        newDetail.setItemPrice(0.0);
        nomenclatureDetailsList.add(newDetail);
        nomenclatureTable.setItems(nomenclatureDetailsList);
    }


    @FXML
    private void Save() {
        try {
            for (NomenclatureDetails detail : nomenclatureDetailsList) {
                if (detail.getItem() != null && detail.getItemQuantity() != null && detail.getItemPrice() != null) {
                    boolean result = entityManager.genEntity(detail);
                    if (result) {
                        System.out.println("Successfully saved: " + detail);
                    } else {
                        System.out.println("Failed to save: " + detail);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void DeleteAll() {
        try {
            for (NomenclatureDetails detail : nomenclatureDetailsList) {
                boolean result = entityManager.deleteEntityById(NomenclatureDetails.class, detail.getIdNomDetails().intValue());
                if (result) {
                    nomenclatureDetailsList.remove(detail);
                    System.out.println("Successfully deleted: " + detail);
                } else {
                    System.out.println("Failed to delete: " + detail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Search() {
    }
}
