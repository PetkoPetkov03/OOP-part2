package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.InvoiceStore;
import com.sparks.of.fabrication.oop2.models.Nomenclature;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableColumn<InvoiceStore, java.util.Date> invoiceDateColumn;
    @FXML
    private TableColumn<InvoiceStore, String> nomenclatureIdColumn;
    @FXML
    private TableColumn<InvoiceStore, Long> employeeIdColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;

    private EntityManagerWrapper entityManager = Singleton.getInstance(EntityManagerWrapper.class);
    private SceneLoader loader = Singleton.getInstance(SceneLoader.class);
    private ObservableList<InvoiceStore> invoiceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        invoiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("id_invoice"));
        invoiceDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        nomenclatureIdColumn.setCellValueFactory(data -> {
            return new javafx.beans.property.SimpleStringProperty(data.getValue().getNomenclatura().getIdNomenclature().toString());
        });
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id_employee"));

        // Add mouse click event listener on the entire TableView
        invoiceTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Check for double-click
                TablePosition<InvoiceStore, ?> pos = invoiceTable.getSelectionModel().getSelectedCells().get(0); // Get selected cell

                int colIndex = pos.getColumn(); // Get the column index of the clicked cell
                if (colIndex == 2) { // Check if the clicked column is "nomenclatureIdColumn" (index 2)
                    InvoiceStore selectedInvoice = invoiceTable.getSelectionModel().getSelectedItem();
                    if (selectedInvoice != null) {
                        Long nomenclatureId = selectedInvoice.getNomenclatura().getIdNomenclature();
                        int nomenclatureIdInt = nomenclatureId.intValue();
                        Nomenclature nomenclature = entityManager.findEntityById(Nomenclature.class, nomenclatureIdInt).y();
                        if (nomenclature != null) {
                            try {
                                loadNomenclatureScene(nomenclature);  // Load the Nomenclature scene
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        // Load data into the table
        List<InvoiceStore> invoices = entityManager.findAllEntities(InvoiceStore.class);
        invoiceList.setAll(invoices);
        invoiceTable.setItems(invoiceList);
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
            entityManager.genEntity(selectedInvoice);
        }
    }

    @FXML
    private void DeleteInvoice() {
        InvoiceStore selectedInvoice = invoiceTable.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
            entityManager.deleteEntityById(InvoiceStore.class, selectedInvoice.getIdInvoice().intValue());
            invoiceList.remove(selectedInvoice);
        }
    }

    @FXML
    private void onNomenclatureIdDoubleClick(MouseEvent event) throws IOException {
        // need to add an event on a double click
        InvoiceStore selectedInvoice = invoiceTable.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
            Long nomenclatureId = selectedInvoice.getNomenclatura().getIdNomenclature();

            int nomenclatureIdInt = nomenclatureId.intValue();
            Nomenclature nomenclature = entityManager.findEntityById(Nomenclature.class, nomenclatureIdInt).y();
            if (nomenclature != null) {
                loadNomenclatureScene(nomenclature);
            }
        }
    }


    private void loadNomenclatureScene(Nomenclature nomenclature) throws IOException {
        //Need to add access to id of nomenclature
        loader.loadScene("scenes/nomenclature_scene.fxml", 500, 500, "Nomenclature", true, new Stage());
        System.out.println("Loading Nomenclature scene for: " + nomenclature.getIdNomenclature());
    }
}
