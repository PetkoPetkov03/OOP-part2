package com.sparks.of.fabrication.oop2.scenes.nomenclature;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Item;
import com.sparks.of.fabrication.oop2.models.Nomenclature;
import com.sparks.of.fabrication.oop2.models.NomenclatureDetails;
import com.sparks.of.fabrication.oop2.scenes.invoices.InvoiceStore_scene;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;
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

    private NomenclatureServices nomenclatureServices = new NomenclatureServices();

    private static final Logger log = LogManager.getLogger(Nomenclature_scene.class);
    private final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    @FXML
    public void initialize() {
        log.info("Initializing Nomenclature Scene.");

        TableViewNomenclature.configureTableColumns(nomenclatureTable, idDetailsColumn, itemColumn, quantityColumn, priceColumn);
        loadNomenclatureDetails();

        logEmployee.createLog("Scene Initialization", "Nomenclature scene initialized.");
    }

    private void loadNomenclatureDetails() {
        try {
            log.info("Loading nomenclature details.");
            Field field = NomenclatureDetails.class.getDeclaredField("nomenclature");

            Nomenclature nomenclature = Singleton.getInstance(InvoiceStore_scene.class).getNomenclature();
            if (nomenclature != null) {
                log.info("Loaded nomenclature: {}", nomenclature.getIdNomenclature());
                logEmployee.createLog("Load Nomenclature", "Loaded nomenclature with ID: " + nomenclature.getIdNomenclature());
            } else {
                log.warn("Nomenclature is null.");
            }

            nomenclatureDetailsList.setAll(nomenclatureServices.loadNomenclatureDetails(field, nomenclature));
            nomenclatureTable.setItems(nomenclatureDetailsList);
            nomenclatureTable.refresh();
        } catch (NoSuchFieldException e) {
            log.error("Error while loading nomenclature details.", e);
            throw new RuntimeException(e);
        }
    }
}
