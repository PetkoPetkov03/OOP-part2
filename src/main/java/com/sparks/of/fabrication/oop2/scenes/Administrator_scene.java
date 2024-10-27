package com.sparks.of.fabrication.oop2.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Administrator_scene {
    @FXML
    private Button btnInventory;

    @FXML
    private Button btnCreateEmployee;

    @FXML
    private Button btnCheckout;


    @FXML
    private void handleInventory() {
        System.out.println("Managing Inventory...");
        //load scene
    }

    @FXML
    private void handleCreateEmployee() {
        System.out.println("Creating Employee...");
        //load scene
    }

    @FXML
    private void handleCheckout() {
        System.out.println("Processing Checkout...");
        //load scene
    }
}
