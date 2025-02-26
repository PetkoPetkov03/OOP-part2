package com.sparks.of.fabrication.oop2.scenes.arrivalGoods;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Item;
import com.sparks.of.fabrication.oop2.models.Suppliers;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Utility class to configure table columns and cell behaviors for arrival goods table views.
 */
public class TableViewSetup {

    /**
     * @param tableView       The table view for items.
     * @param colCode         The column for item code (ID).
     * @param colName         The column for item name.
     * @param colMeasure      The column for item measure.
     * @param AmsView         The table view for AmS data.
     * @param colQuantity     The column for item quantity.
     * @param colArrivalPrice The column for arrival price.
     * @param colSellingPrice The column for selling price.
     * @param SupplierBox     The combo box for selecting suppliers.
     */
    public static void configureTableColumns(
            TableView<Item> tableView,
            TableColumn<Item, Long> colCode,
            TableColumn<Item, String> colName,
            TableColumn<Item, String> colMeasure,
            TableView<AmSData> AmsView,
            TableColumn<AmSData, Integer> colQuantity,
            TableColumn<AmSData, Double> colArrivalPrice,
            TableColumn<AmSData, Double> colSellingPrice,
            ComboBox<String> SupplierBox
    ) {
        final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

        colCode.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMeasure.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue().getCategory() != null ? data.getValue().getCategory().getCategory() : "");
        });
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colArrivalPrice.setCellValueFactory(new PropertyValueFactory<>("arrivalPrice"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

        tableView.setEditable(true);

        colCode.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colArrivalPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colSellingPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        colCode.prefWidthProperty().bind(tableView.widthProperty().multiply(0.20));
        colName.prefWidthProperty().bind(tableView.widthProperty().multiply(0.60));
        colMeasure.prefWidthProperty().bind(tableView.widthProperty().multiply(0.20));

        colQuantity.prefWidthProperty().bind(tableView.widthProperty().multiply(0.20));
        colArrivalPrice.prefWidthProperty().bind(tableView.widthProperty().multiply(0.40));
        colSellingPrice.prefWidthProperty().bind(tableView.widthProperty().multiply(0.40));

        AmsView.setEditable(true);
        colQuantity.setOnEditCommit(event -> {
            AmSData row = event.getRowValue();
            row.setQuantity(event.getNewValue());
            AmsView.refresh();
        });

        colArrivalPrice.setOnEditCommit(event -> {
            AmSData row = event.getRowValue();
            row.setArrivalPrice(event.getNewValue());
            AmsView.refresh();
        });

        colSellingPrice.setOnEditCommit(event -> {
            AmSData row = event.getRowValue();
            row.setSellingPrice(event.getNewValue());
            AmsView.refresh();
        });

        colCode.setOnEditCommit(event -> {
            try {
                Field name = Item.class.getDeclaredField("idItem");
                Item item = entityManagerWrapper.findEntityByVal(Item.class, name, event.getNewValue()).y();
                int rowIndex = event.getTablePosition().getRow();
                event.getTableView().getItems().set(rowIndex, item);
                AmsView.getItems().get(rowIndex).setArrivalPrice(item.getArrivalPrice());
                AmsView.getItems().get(rowIndex).setSellingPrice(item.getPrice());
                AmsView.refresh();
                event.getTableView().refresh();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });

        colName.setOnEditCommit(event -> {
            try {
                Field name = Item.class.getDeclaredField("name");
                Item item = entityManagerWrapper.findEntityByVal(Item.class, name, event.getNewValue()).y();
                int rowIndex = event.getTablePosition().getRow();
                event.getTableView().getItems().set(rowIndex, item);
                AmsView.getItems().get(rowIndex).setArrivalPrice(item.getArrivalPrice());
                AmsView.getItems().get(rowIndex).setSellingPrice(item.getPrice());
                AmsView.refresh();
                event.getTableView().refresh();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });

        List<Suppliers> suppliers = entityManagerWrapper.findAllEntities(Suppliers.class);
        List<String> supplierNames = suppliers.stream()
                .map(Suppliers::getName)
                .toList();
        SupplierBox.setItems(FXCollections.observableArrayList(supplierNames));
    }
}
