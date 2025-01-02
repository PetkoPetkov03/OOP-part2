package com.sparks.of.fabrication.oop2.scenes.notification;

import com.sparks.of.fabrication.oop2.models.Notification;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class NotificationController {

    @FXML
    private TableView<Notification> notificationTable;

    @FXML
    private TableColumn<Notification, Long> idNotificationColumn;

    @FXML
    private TableColumn<Notification, Long> idEmployeeColumn;

    @FXML
    private TableColumn<Notification, String> messageColumn;

    @FXML
    private TableColumn<Notification, String> statusColumn;

    @FXML
    private TableColumn<Notification, String> dateSentColumn;

    private final EntityManagerWrapper entityManager = Singleton.getInstance(EntityManagerWrapper.class);

    private static final Logger log = LogManager.getLogger(NotificationController.class);
    private final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    @FXML
    public void initialize() {
        log.info("Initializing Notification Controller.");

        TableViewNotification.configureTableColumns(notificationTable, idNotificationColumn, idEmployeeColumn, messageColumn, statusColumn, dateSentColumn);
        loadNotifications();

        logEmployee.createLog("Scene Initialization", "Notification controller initialized.");
    }

    private void loadNotifications() {
        log.info("Loading notifications.");

        ObservableList<Notification> notifications;
        List<Notification> notificationList = entityManager.findAllEntities(Notification.class);

        if (notificationList == null || notificationList.isEmpty()) {
            log.warn("No notifications found.");
            notifications = FXCollections.observableArrayList();
            logEmployee.createLog("Load Notifications", "No notifications found.");
        } else {
            log.info("Found {} notifications.", notificationList.size());
            notifications = FXCollections.observableArrayList(notificationList);
            logEmployee.createLog("Load Notifications", "Loaded " + notificationList.size() + " notifications.");
        }

        notificationTable.setItems(notifications);
        notificationTable.refresh();
    }
}
