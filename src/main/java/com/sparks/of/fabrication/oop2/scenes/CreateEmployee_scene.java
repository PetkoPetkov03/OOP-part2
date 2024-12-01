package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.Role;
import com.sparks.of.fabrication.oop2.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class CreateEmployee_scene {

    BCrypt bCrypt = new BCrypt();

    @FXML private Label messages;
    @FXML private PasswordField passwordEmployeeField;
    @FXML private TextField emailEmployeeField;
    @FXML private TextField nameField;
    @FXML private ComboBox<?> roleComboBox;

    private final EntityManagerWrapper entityManager = Singleton.getInstance(EntityManagerWrapper.class);

    private final Validation userValidation = new UserValidation();

    public void createEmployee() throws NoSuchFieldException {
        List<String> inputs = new ArrayList<>();

        List<ValidationTypes> validationTypes = new ArrayList<>(List.of(new ValidationTypes[]{ValidationTypes.EMAIL, ValidationTypes.PASSWORD}));

        inputs.add(emailEmployeeField.getText());
        inputs.add(passwordEmployeeField.getText());

        Pair<Boolean, List<String>> response =  userValidation.validate(validationTypes, inputs);

        StringBuilder builder = new StringBuilder();

        for(String mes: response.y()) {
            builder.append(mes).append("\n");
        }

        if(response.x()) {
            String salt = BCrypt.gensalt(13);
            String hashedPassword = BCrypt.hashpw(passwordEmployeeField.getText(), salt);

            assert entityManager != null;
            Role role = entityManager.findEntityByVal(Role.class, Role.class.getDeclaredField("roleName"),"test2").y();

            Employee employee = new Employee();
            employee.setEmail(emailEmployeeField.getText());
            employee.setPassword(hashedPassword);
            employee.setName(nameField.getText());
            employee.setRole(role);

            entityManager.genEntity(employee);
        }
        messages.setText(builder.toString());
    }
}
