package com.sparks.of.fabrication.oop2.scenes.notification;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.Notification;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewNotification {
    public static void configureTableColumns(TableView<Notification> notificationTable,
                                             TableColumn<Notification, Long> idNotificationColumn,
                                             TableColumn<Notification, Long> idEmployeeColumn,
                                             TableColumn<Notification, String> messageColumn,
                                             TableColumn<Notification, String> statusColumn,
                                             TableColumn<Notification, String> dateSentColumn){
        EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
        idNotificationColumn.setCellValueFactory(new PropertyValueFactory<>("idNotification"));
        notificationTable.setEditable(true);
        idEmployeeColumn.setCellValueFactory(data -> {
            Employee employee = data.getValue().getEmployee();
            return new javafx.beans.property.SimpleObjectProperty<>(
                    (employee != null) ? employee.getId() : null
            );
        });
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(col -> {
            return new ComboBoxTableCell<>(FXCollections.observableArrayList("read", "unread"));
        });
        statusColumn.setOnEditCommit(event -> {
            Notification notification = event.getRowValue();
            String newStatus = event.getNewValue();
            notification.setStatus(newStatus);

            entityManagerWrapper.genEntity(notification);
        });
        dateSentColumn.setCellValueFactory(new PropertyValueFactory<>("dateSent"));

        idNotificationColumn.prefWidthProperty().bind(notificationTable.widthProperty().multiply(0.1));
        idEmployeeColumn.prefWidthProperty().bind(notificationTable.widthProperty().multiply(0.1));
        messageColumn.prefWidthProperty().bind(notificationTable.widthProperty().multiply(0.4));
        statusColumn.prefWidthProperty().bind(notificationTable.widthProperty().multiply(0.2));
        dateSentColumn.prefWidthProperty().bind(notificationTable.widthProperty().multiply(0.2));

    }
}
