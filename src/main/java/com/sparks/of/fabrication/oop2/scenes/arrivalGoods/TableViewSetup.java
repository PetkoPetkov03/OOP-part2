package com.sparks.of.fabrication.oop2.scenes.arrivalGoods;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Item;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

import java.lang.reflect.Field;

public class TableViewSetup {

    public static void configureTableColumns(
            TableView<Item> tableView,
            TableColumn<Item, Long> colCode,
            TableColumn<Item, String> colName,
            TableColumn<Item, String> colMeasure,
            TableView<AmSData> AmsView,
            TableColumn<AmSData, Integer> colQuantity,
            TableColumn<AmSData, Double> colArrivalPrice,
            TableColumn<AmSData, Double> colSellingPrice
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
                event.getTableView().refresh();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

