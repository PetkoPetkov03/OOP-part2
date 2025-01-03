package com.sparks.of.fabrication.oop2.scenes.invoices;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.InvoiceStore;
import com.sparks.of.fabrication.oop2.models.Nomenclature;
import com.sparks.of.fabrication.oop2.models.Suppliers;
import com.sparks.of.fabrication.oop2.scenes.inventory.InventoryController;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import com.sparks.of.fabrication.oop2.utils.SceneLoader;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
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
    private ComboBox<SearchOption> searchByComboBox;
    @FXML
    private ComboBox<String> searchCriteriaComboBox;
    @FXML
    private DatePicker datePicker;

    private final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private final SceneLoader loader = Singleton.getInstance(SceneLoader.class);
    private final InvoiceServices invoiceServices = new InvoiceServices();
    private static final Logger log = LogManager.getLogger(InventoryController.class);
    private final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    @Getter
    private Nomenclature nomenclature;

    @FXML
    public void initialize() {
        log.info("Initializing InvoiceStore_scene.");
        searchByComboBox.setItems(FXCollections.observableArrayList(SearchOption.values()));
        searchByComboBox.getSelectionModel().select(SearchOption.ALL);
        searchCriteriaComboBox.setVisible(false);
        datePicker.setVisible(false);
        Singleton.getInstance(InvoiceStore_scene.class, this);

        TableViewInvoice.configureTableColumns(invoiceTable, invoiceIdColumn, nomenclatureIdColumn, dateColumn, employeeNameColumn, this);
        loadInvoiceData();

        searchCriteriaComboBox.setOnAction(event -> {
            log.info("Search criteria changed in ComboBox.");
            onSearch();
        });
        datePicker.setOnAction(event -> {
            log.info("DatePicker value changed.");
            onSearch();
        });
    }

    private void loadInvoiceData() {
        log.info("Loading invoice data into the table.");
        invoiceTable.setItems(FXCollections.observableArrayList(invoiceServices.loadInvoiceData()));
    }

    protected String fetchEmployeeName(Long employeeId) {
        if (employeeId == null) {
            log.warn("Employee ID is null. Returning 'Unknown'.");
            return "Unknown";
        }

        String employeeName = entityManagerWrapper.findEntityById(Employee.class, employeeId.intValue()).y().getName();
        log.info("Fetched employee name: {}", employeeName);
        logEmployee.createLog("Fetch Employee", "Fetched details for employee ID: " + employeeId);
        return employeeName;
    }

    @FXML
    private void onSearchByChanged() {
        SearchOption selectedOption = searchByComboBox.getValue();
        log.info("Search option changed to: {}", selectedOption);
        searchCriteriaComboBox.getItems().clear();

        switch (selectedOption) {
            case SUPPLIER -> {
                log.info("Loading suppliers into search criteria ComboBox.");
                invoiceServices.loadValues(Suppliers.class, Suppliers::getName, searchCriteriaComboBox);
                searchCriteriaComboBox.setVisible(true);
                datePicker.setVisible(false);
            }
            case EMPLOYEE -> {
                log.info("Loading employees into search criteria ComboBox.");
                invoiceServices.loadValues(Employee.class, Employee::getName, searchCriteriaComboBox);
                searchCriteriaComboBox.setVisible(true);
                datePicker.setVisible(false);
            }
            case DATE -> {
                log.info("Setting DatePicker visible for date-based search.");
                searchCriteriaComboBox.setVisible(false);
                datePicker.setVisible(true);
            }
            case ALL -> {
                log.info("Search option set to ALL. Hiding filters.");
                searchCriteriaComboBox.setVisible(false);
                datePicker.setVisible(false);
            }
        }
    }

    @FXML
    private void onSearch() {
        SearchOption searchBy = searchByComboBox.getValue();
        List<InvoiceStore> filteredInvoices;

        if (searchBy == null || (searchCriteriaComboBox.getValue() == null && datePicker.getValue() == null)) {
            log.info("No specific search criteria selected. Loading all invoices.");
            loadInvoiceData();
            return;
        }

        Field field = null;
        if (datePicker.isVisible()) {
            try {
                field = InvoiceStore.class.getDeclaredField("date");
                log.info("Filtering invoices by date.");
            } catch (NoSuchFieldException e) {
                log.error("Field 'date' not found in InvoiceStore.", e);
                throw new RuntimeException(e);
            }
        }

        filteredInvoices = invoiceServices.ss(field, datePicker.getValue(), searchBy, searchCriteriaComboBox.getValue());

        log.info("Filtered {} invoices based on search criteria.", filteredInvoices.size());
        logEmployee.createLog("Search Invoices", "Performed search with criteria: " + searchBy); // Logging the search operation
        invoiceTable.setItems(FXCollections.observableArrayList(filteredInvoices));
        invoiceTable.refresh();
    }

    protected void onRowSelected(InvoiceStore selectedInvoice) {
        if (selectedInvoice != null) {
            log.info("Invoice selected: ID={}", selectedInvoice.getIdInvoice());
            logEmployee.createLog("Invoice Selection", "Selected invoice with ID: " + selectedInvoice.getIdInvoice()); // Logging the selection
            Nomenclature nomenclature = selectedInvoice.getNomenclatura();
            this.nomenclature = nomenclature;
            loadNomenclatureScene();
        }
    }

    private void loadNomenclatureScene() {
        try {
            log.info("Loading nomenclature scene.");
            loader.loadScene("scenes/nomenclature_scene.fxml", 500, 500, "Nomenclature", true, new Stage());
        } catch (IOException e) {
            log.error("Error loading nomenclature scene.", e);
            throw new RuntimeException(e);
        }
    }
}
