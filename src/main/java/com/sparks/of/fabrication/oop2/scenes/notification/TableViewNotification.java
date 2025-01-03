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

/**
 * The TableViewNotification class configures the columns and behavior of the notification table in the user interface.
 */
public class TableViewNotification {

    /**
     * Configures the columns of the notification table, including editable status and column width.
     *
     * @param notificationTable The table where notifications will be displayed.
     * @param idNotificationColumn The column for the notification ID.
     * @param idEmployeeColumn The column for the employee ID.
     * @param messageColumn The column for the notification message.
     * @param statusColumn The column for the notification status.
     * @param dateSentColumn The column for the date when the notification was sent.
     */
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
            return new javafx.beans.property.SimpleObjectProperty<>(employee != null ? employee.getId() : null);
        });
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(col -> new ComboBoxTableCell<>(FXCollections.observableArrayList("read", "unread")));
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
