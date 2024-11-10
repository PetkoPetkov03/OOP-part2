package com.sparks.of.fabrication.oop2.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NotificationController {

    @FXML
    private ListView<String> notificationListView;

    private ObservableList<String> notifications;

    @FXML
    public void initialize() {

        notifications = FXCollections.observableArrayList(
                "Notification 1: New message",
                "Notification 2: Task completed",
                "Notification 3: Reminder"
        );
        notificationListView.setItems(notifications);

        notificationListView.setCellFactory(lv -> new ListCell<>() {
            private Button markAsReadButton = new Button("Mark as Read");

            {
                markAsReadButton.setOnAction(e -> {
                    String notification = getItem();
                    if (notification != null) {
                        markAsRead(notification);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setGraphic(markAsReadButton);
                }
            }
        });
    }

    private void markAsRead(String notification) {
        notifications.remove(notification);
    }

    @FXML
    private void closeWindow() {
        notificationListView.getScene().getWindow().hide();
    }
}

