package com.sparks.of.fabrication.oop2.scenes.arrivalGoods;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.Item;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import org.hibernate.loader.ast.spi.SingleEntityLoader;

import java.lang.reflect.Field;

public class TableViewSetup {

    public static void configureTableColumns(
            TableView<Item> tableView,
            TableColumn<Item, Long> colCode,
            TableColumn<Item, String> colName,
            TableColumn<Item, String> colMeasure,
            TableColumn<Item, Integer> colQuantity,
            TableColumn<Item, Double> colArrivalPrice,
            TableColumn<Item, Double> colMarkup,
            TableColumn<Item, Double> colSellingPrice
    ) {
        //create TABLE
        colCode.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMeasure.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue().getCategory() != null ? data.getValue().getCategory().getCategory() : "");
        });
        colQuantity.setCellValueFactory(cellData -> {
            Integer quantity = cellData.getValue().getQuantity();
            return new SimpleObjectProperty<>(quantity != null ? quantity : null);
        });
        colArrivalPrice.setCellValueFactory(new PropertyValueFactory<>("arrivalPrice"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableView.setEditable(true);
        //make specific columns editable
        colCode.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colArrivalPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colSellingPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        //calculate markup column
        colMarkup.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            if (item.getArrivalPrice() != null && item.getPrice() != null && item.getArrivalPrice() != 0) {
                double markup = (item.getPrice() - item.getArrivalPrice()) / item.getArrivalPrice() * 100;
                return new SimpleDoubleProperty(markup).asObject();
            }
            return new SimpleDoubleProperty(0).asObject();
        });

        colName.setOnEditCommit(event -> {
            EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
            try {
                Field name = Item.class.getDeclaredField("name");
                Item item = entityManagerWrapper.findEntityByVal(Item.class,name,event.getNewValue()).y();
                int rowIndex = event.getTablePosition().getRow();
                event.getTableView().getItems().set(rowIndex, item);
                event.getTableView().refresh();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });

        colCode.setOnEditCommit(event -> {
            EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
            try {
                Field name = Item.class.getDeclaredField("idItem");
                Item item = entityManagerWrapper.findEntityByVal(Item.class,name,event.getNewValue()).y();
                int rowIndex = event.getTablePosition().getRow();
                event.getTableView().getItems().set(rowIndex, item);
                event.getTableView().refresh();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }


        });
        colQuantity.setOnEditCommit(event -> {
            Item item = event.getRowValue();
            Integer newQuantity = event.getNewValue();

            if (newQuantity != null) {
                item.setQuantity(newQuantity);
            } else {
                item.setQuantity(0);
            }

            event.getTableView().refresh();
        });

    }
}

