package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Checkout;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.lang.reflect.Field;
import java.util.List;

public class CreateCashRegister {
    @FXML
    private TextField cashInput;
    @FXML
    private ComboBox<String> employeeComboBox;

    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    @FXML
    private void initialize(){
       List<Employee> employees = entityManagerWrapper.findAllEntities(Employee.class);
        for (Employee employee : employees) {
            employeeComboBox.getItems().add(employee.getName());
        }
    }

    @FXML
    private void checkout() throws NoSuchFieldException {
    Checkout checkout = new Checkout();
    Field employee = Employee.class.getDeclaredField("name");
    Employee Employee = entityManagerWrapper.findEntityByVal(Employee.class, employee, employeeComboBox.getValue()).y();
    checkout.setEmployee(Employee);
    checkout.setCash(Double.parseDouble(cashInput.getText()));
    entityManagerWrapper.genEntity(checkout);
    }
}
