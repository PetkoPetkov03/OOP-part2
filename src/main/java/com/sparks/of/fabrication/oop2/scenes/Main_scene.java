package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import com.sparks.of.fabrication.oop2.utils.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Arrays;
import java.util.List;

public class Main_scene {

    private final Logger log = LogManager.getLogger(Main_scene.class);
    private final SceneLoader loader = Singleton.getInstance(SceneLoader.class);
    private final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private final UserValidation userValidation = new UserValidation();

    @FXML
    private TextField username;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void checkCredentials() {
        String user = username.getText();
        String password = passwordField.getText();

        if (user.isEmpty() || password.isEmpty()) {
            showAlert("Enter both user and password");
        }

        try {
            Pair<Boolean, Employee> employeeResult = entityManagerWrapper.findEntityByVal(Employee.class,
                    Employee.class.getDeclaredField("email"), user);

            if (employeeResult.x()) {
                Employee employee = employeeResult.y();

                if (BCrypt.checkpw(password, employee.getPassword())) {
                    String roleName = employee.getRole().getRole().toString();

                    if ("admin".equalsIgnoreCase(roleName)) {
                        loader.loadScene("scenes/administrator_scene.fxml", 500, 500, "Admin", true, new Stage());
                    } else {
                        loader.loadScene("scenes/manager_scene.fxml", 500, 500, "Manager", true, new Stage());
                    }

                    username.getScene().getWindow().hide();
                } else {
                    showAlert("Invalid password");
                }
            } else {
                showAlert("User not found");
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            showAlert("An error occurred during login");
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

