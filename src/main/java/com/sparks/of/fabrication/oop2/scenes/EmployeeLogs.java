package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.EmployeeLog;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.Pair;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

import java.lang.reflect.Field;
import java.util.List;

public class EmployeeLogs {

    @FXML
    private TableView<EmployeeLog> employeeLogsTable;

    @FXML
    private TableColumn<EmployeeLog, Long> logId;

    @FXML
    private TableColumn<EmployeeLog, String> employee;

    @FXML
    private TableColumn<EmployeeLog, String> timestamp;

    @FXML
    private TableColumn<EmployeeLog, String> message;

    @FXML
    private TableColumn<EmployeeLog, String> details;

    @FXML
    private ComboBox<Employee> employeeComboBox;

    private final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    @FXML
    private void initialize() {
        logId.setCellValueFactory(new PropertyValueFactory<>("logId"));
        employee.setCellValueFactory(cellData -> {
            if (cellData.getValue().getEmployee() != null) {
                return new SimpleStringProperty(cellData.getValue().getEmployee().getName());
            }
            return new SimpleStringProperty("Unknown");
        });
        timestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        message.setCellValueFactory(new PropertyValueFactory<>("message"));
        details.setCellValueFactory(new PropertyValueFactory<>("details"));

        logId.prefWidthProperty().bind(employeeLogsTable.widthProperty().multiply(0.10));
        employee.prefWidthProperty().bind(employeeLogsTable.widthProperty().multiply(0.20));
        timestamp.prefWidthProperty().bind(employeeLogsTable.widthProperty().multiply(0.20));
        message.prefWidthProperty().bind(employeeLogsTable.widthProperty().multiply(0.25));
        details.prefWidthProperty().bind(employeeLogsTable.widthProperty().multiply(0.25));

        employeeComboBox.setConverter(new StringConverter<Employee>() {
            @Override
            public String toString(Employee employee) {
                return employee != null ? employee.getName() : "";
            }

            @Override
            public Employee fromString(String string) {
                return null;
            }
        });
        loadEmployees();
        employeeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> loadLogs(newValue));
    }

    private void loadEmployees() {
        List<Employee> employees = entityManagerWrapper.findAllEntities(Employee.class);
        employeeComboBox.getItems().setAll(employees);
    }

    private void loadLogs(Employee selectedEmployee) {
        if (selectedEmployee != null) {
            try {
                Field employeeField = EmployeeLog.class.getDeclaredField("employee");
                Pair<Boolean, List<EmployeeLog>> result = entityManagerWrapper.findEntityByValAll(EmployeeLog.class, employeeField, selectedEmployee);

                if (result.x()) {
                    employeeLogsTable.getItems().setAll(result.y());
                } else {
                    employeeLogsTable.getItems().clear();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        } else {
            employeeLogsTable.getItems().clear();
        }
    }
}
