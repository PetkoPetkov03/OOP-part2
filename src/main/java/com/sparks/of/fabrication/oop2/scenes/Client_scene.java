package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.users.Client;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.Item;
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

    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    public void setClient(Client client) {
        this.client = client;
        welcomeLabel.setText("Welcome, " + client.getName() + "!");
    }

    @FXML
    protected void filterItems() {
    }

    @FXML
    protected void addToCart(ActionEvent event) {
    }

    @FXML
    protected void showCart() {
    }


}
