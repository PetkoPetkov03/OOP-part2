package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Setter;

public class NotificationController {
    @FXML private TableView<?> notificationTable;
    @FXML private TableColumn<?, ?> idNotificationColumn;
    @FXML private TableColumn<?, Integer> idEmployeeColumn;
    @FXML private TableColumn<?, String> messageColumn;
    @FXML private TableColumn<?, String> statusColumn;
    @FXML private TableColumn<?,String> dateSentColumn;

    @Setter
    private EntityManagerWrapper entityManager = Singleton.getInstance(EntityManagerWrapper.class);

    @FXML
    public void initialize() {
        idNotificationColumn.setCellValueFactory(new PropertyValueFactory<>("idNotification"));
        idEmployeeColumn.setCellValueFactory(new PropertyValueFactory<>("idEmployee"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateSentColumn.setCellValueFactory(new PropertyValueFactory<>("dateSent"));

        idNotificationColumn.prefWidthProperty().bind(notificationTable.widthProperty().multiply(0.25));
        idEmployeeColumn.prefWidthProperty().bind(notificationTable.widthProperty().multiply(0.15));
        messageColumn.prefWidthProperty().bind(notificationTable.widthProperty().multiply(0.2));
        statusColumn.prefWidthProperty().bind(notificationTable.widthProperty().multiply(0.15));
        dateSentColumn.prefWidthProperty().bind(notificationTable.widthProperty().multiply(0.25));
    }

}
