package com.sparks.of.fabrication.oop2.scenes;

import javafx.fxml.FXML;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main_scene {
    @FXML
    private TextField username;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void checkCredentials() {
            String user = username.getText();
            String password = passwordField.getText();

            if(user.isEmpty() || password.isEmpty()) {
                showAlert("Enter both user and password");
            }


            // Check credentials from database
        try {
            SceneLoader loader = new SceneLoader();
            if(user.equals("admin") && password.equals("admin")) {
                loader.loadScene("administrator_scene.fxml",500,500,"Admin",true,new Stage());
            }
            if(user.equals("manager") && password.equals("manager")) {
                loader.loadScene("manager_scene.fxml",500,500,"Manager",true,new Stage());
            }
            loader.loadScene("client_scene.fxml", 500, 500, "Client", true, new Stage());
            username.getScene().getWindow().hide();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
