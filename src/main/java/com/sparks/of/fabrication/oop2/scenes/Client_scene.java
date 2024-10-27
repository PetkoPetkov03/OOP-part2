package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Item;
import com.sparks.of.fabrication.oop2.users.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;

public class Client_scene {

    @FXML
    private TextField searchField;

    @FXML
    private GridPane itemsGrid;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button cartButton;

    @FXML
    private Button searchButton;

    private Client client;

    public Client_scene() {

    }

    public void setClient(Client client) {
        this.client = client;
        welcomeLabel.setText("Welcome, " + client.getName() + "!");
    }

    @FXML
    protected void filterItems() {
        String query = searchField.getText().toLowerCase();

        for (int i = 0; i < itemsGrid.getChildren().size(); i += 2) {
            Label itemLabel = (Label) itemsGrid.getChildren().get(i);
            String itemText = itemLabel.getText().toLowerCase();
            boolean visible = itemText.contains(query);
            itemLabel.setVisible(visible);
            itemLabel.setManaged(visible);

            Button addButton = (Button) itemsGrid.getChildren().get(i + 1);
            addButton.setVisible(visible);
            addButton.setManaged(visible);
        }
    }

    @FXML
    protected void addToCart(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        int rowIndex = GridPane.getRowIndex(clickedButton);
        String itemName = getItemNameByRowIndex(rowIndex);
        Item item = getItemByID(itemName);

        if (item != null) {
            client.addItem(item);
            showAlert(item.getName() + " has been added to your cart.");
        } else {
            showAlert("Item could not be found.");
        }
    }

    private String getItemNameByRowIndex(int rowIndex) {
        Label itemLabel = (Label) itemsGrid.getChildren()
                .stream()
                .filter(node -> GridPane.getRowIndex(node) == rowIndex
                        && GridPane.getColumnIndex(node) == 0)
                .findFirst()
                .orElse(null);

        if (itemLabel != null) {
            return itemLabel.getText().split(" - ")[0];
        }
        return null;
    }

    private Item getItemByID(String name) {
        // Placeholder for item retrieval logic
        return null;
    }

    @FXML
    protected void showCart() {
        StringBuilder cartContents = new StringBuilder("Cart Contents:\n");
        client.getCart().forEach(item -> cartContents.append(item.getName()).append("\n"));
        showAlert(cartContents.toString());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
