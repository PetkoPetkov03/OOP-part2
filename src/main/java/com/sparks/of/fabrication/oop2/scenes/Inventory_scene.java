package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Setter;

public class Inventory_scene {

    @FXML private TableView<?> inventoryTable;
    @FXML private TableColumn<?, Integer> idItemColumn;
    @FXML private TableColumn<?, String> nameColumn;
    @FXML private TableColumn<?, Integer> categoryColumn;
    @FXML private TableColumn<?, Double> priceColumn;
    @FXML private TableColumn<?, Double> arrivalPriceColumn;
    @FXML private TableColumn<?, Integer> quantityColumn;

    @Setter
    private EntityManagerWrapper entityManager = Singleton.getInstance(EntityManagerWrapper.class);

    @FXML
    public void initialize() {
        idItemColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        arrivalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("ArrivalPrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        idItemColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.1));
        nameColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.2));
        categoryColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.2));
        priceColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.2));
        arrivalPriceColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.2));
        quantityColumn.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.1));
    }
}
