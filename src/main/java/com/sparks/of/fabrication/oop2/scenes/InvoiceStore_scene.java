package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.InvoiceStore;
import com.sparks.of.fabrication.oop2.models.Nomenclature;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.Pair;
import com.sparks.of.fabrication.oop2.utils.SceneLoader;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class InvoiceStore_scene {

    @FXML
    private TableView<InvoiceStore> invoiceTable;

    @FXML
    private TableColumn<InvoiceStore, Long> invoiceIdColumn;

    @FXML
    private TableColumn<InvoiceStore, Long> nomenclatureIdColumn;

    @FXML
    private TableColumn<InvoiceStore, String> dateColumn;

    @FXML
    private TableColumn<InvoiceStore, String> employeeNameColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;

    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private SceneLoader loader = Singleton.getInstance(SceneLoader.class);
    private ObservableList<InvoiceStore> invoiceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        invoiceIdColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getIdInvoice())
        );

        nomenclatureIdColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getNomenclatura().getIdNomenclature()) // Nomenclature ID
        );

        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().toString()) // Date
        );

        employeeNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(fetchEmployeeName(cellData.getValue().getEmployee().getId())) // Employee Name
        );

        invoiceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Long nomenclatureId = newSelection.getNomenclatura().getIdNomenclature();
                Nomenclature nomenclature = entityManagerWrapper.findEntityById(Nomenclature.class, nomenclatureId.intValue()).y();
                if (nomenclature != null) {
                    try {
                        loadNomenclatureScene(nomenclature);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        invoiceIdColumn.prefWidthProperty().bind(invoiceTable.widthProperty().multiply(0.25));
        nomenclatureIdColumn.prefWidthProperty().bind(invoiceTable.widthProperty().multiply(0.25));
        dateColumn.prefWidthProperty().bind(invoiceTable.widthProperty().multiply(0.25));
        employeeNameColumn.prefWidthProperty().bind(invoiceTable.widthProperty().multiply(0.25));
        loadInvoiceData();
    }

    private void loadInvoiceData() {
        List<InvoiceStore> invoices = entityManagerWrapper.findAllEntities(InvoiceStore.class);
        invoiceTable.setItems(FXCollections.observableArrayList(invoices));
    }

    private String fetchEmployeeName(Long employeeId) {
        if (employeeId == null) {
            return "Unknown";
        }
        Pair<Boolean, Employee> result = entityManagerWrapper.findEntityById(Employee.class, employeeId.intValue());
        return result.x() ? result.y().getName() : "Unknown";
    }


    @FXML
    private void AddInvoice() {
        InvoiceStore newInvoice = new InvoiceStore();
        invoiceList.add(newInvoice);
        invoiceTable.refresh();
    }

    @FXML
    private void SaveInvoice() {
        InvoiceStore selectedInvoice = invoiceTable.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
            entityManagerWrapper.genEntity(selectedInvoice);
        }
    }

    @FXML
    private void DeleteInvoice() {
        InvoiceStore selectedInvoice = invoiceTable.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
            entityManagerWrapper.deleteEntityById(InvoiceStore.class, selectedInvoice.getIdInvoice().intValue());
            invoiceList.remove(selectedInvoice);
        }
    }

    @FXML
    private void onNomenclatureIdDoubleClick(MouseEvent event) throws IOException {
        InvoiceStore selectedInvoice = invoiceTable.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
            Long nomenclatureId = selectedInvoice.getNomenclatura().getIdNomenclature();

            int nomenclatureIdInt = nomenclatureId.intValue();
            Nomenclature nomenclature = entityManagerWrapper.findEntityById(Nomenclature.class, nomenclatureIdInt).y();
            if (nomenclature != null) {
                loadNomenclatureScene(nomenclature);
            }
        }
    }


    private void loadNomenclatureScene(Nomenclature nomenclature) throws IOException {
        loader.loadScene("scenes/nomenclature_scene.fxml", 500, 500, "Nomenclature", true, new Stage());
        System.out.println("Loading Nomenclature scene for: " + nomenclature.getIdNomenclature());
    }
}
