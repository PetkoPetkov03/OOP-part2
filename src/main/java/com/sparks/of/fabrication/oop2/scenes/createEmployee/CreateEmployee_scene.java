package com.sparks.of.fabrication.oop2.scenes.createEmployee;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.users.Role;
import com.sparks.of.fabrication.oop2.utils.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller class responsible for the employee creation scene.
 */
public class CreateEmployee_scene {

    @FXML private Label messages;
    @FXML private PasswordField passwordEmployeeField;
    @FXML private TextField emailEmployeeField;
    @FXML private TextField nameField;
    @FXML private ComboBox<Role> roleComboBox = new ComboBox<>();

    private CreateEmployeeServices createEmployeeServices = new CreateEmployeeServices();
    private static final Logger log = LogManager.getLogger(CreateEmployee_scene.class);
    private static final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);
    private final Validation userValidation = new UserValidation();
    List<String> inputs;
    List<ValidationTypes> validationTypes;

    /**
     * Initializes the scene by setting up role options for the ComboBox
     * and preparing input validation types for email and password fields.
     */
    public void initialize() {
        inputs = new ArrayList<>();
        validationTypes = new ArrayList<>(List.of(new ValidationTypes[]{ValidationTypes.EMAIL, ValidationTypes.PASSWORD}));
        roleComboBox.setItems(FXCollections.observableArrayList(Role.values()));
        log.info("Roles loaded into the roleComboBox: {}", Arrays.toString(Role.values()));
    }

    /**
     * Creates a new employee entity
     */
    public void createEmployee() {
        StringBuilder builder = new StringBuilder();
        builder.setLength(0);
        inputs.add(emailEmployeeField.getText());
        inputs.add(passwordEmployeeField.getText());

        Pair<Boolean, List<String>> validationResponse = userValidation.validate(validationTypes, inputs);

        createEmployeeServices.appendValidationMessages(builder, validationResponse);
        if (validationResponse.x()) {
            try {
                createEmployeeServices.createAndPersistEmployee(passwordEmployeeField.getText(), emailEmployeeField.getText()
                        ,nameField.getText(), roleComboBox);
                builder.setLength(0);
                builder.append("Employee created!");
                log.info("Employee created successfully: {}", nameField.getText());
                logEmployee.createLog("Employee Created", "Employee: " + nameField.getText() + ", Role: " + roleComboBox.getValue());
            } catch (NoSuchFieldException e) {
                log.error("Error creating employee: ", e);
                logEmployee.createLog("Employee Creation Error", e.getMessage());
            }
        }

        messages.setText(builder.toString());
    }
}
