package com.sparks.of.fabrication.oop2.scenes.employeeLogs;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.EmployeeLog;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

/**
 * Controller for the EmployeeLogs scene. Controller for the EmployeeLogs scene initializing the scene,
 * configuring the table to display employee logs, and loading employee data and logs.
 */
public class EmployeeLogs {

    @FXML private TableView<EmployeeLog> employeeLogsTable;
    @FXML private TableColumn<EmployeeLog, Long> logId;
    @FXML private TableColumn<EmployeeLog, String> employee;
    @FXML private TableColumn<EmployeeLog, String> timestamp;
    @FXML private TableColumn<EmployeeLog, String> message;
    @FXML private TableColumn<EmployeeLog, String> details;
    @FXML private ComboBox<Employee> employeeComboBox;

    private final EmployeeLogsServices employeeLogsServices = new EmployeeLogsServices();
    private static final Logger log = LogManager.getLogger(EmployeeLogs.class);
    private final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    /**
     * Initializes the EmployeeLogs scene, sets up the table columns, configures the comboBox
     * for selecting employees, and loads the employee data.
     */
    @FXML
    private void initialize() {
        try {
            log.info("Initializing EmployeeLogs scene.");
            TableViewEmployeeLog.configureTableColumns(employeeLogsTable, logId, employee, timestamp, message, details);

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
            employeeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    loadLogs(newValue);
                } catch (NoSuchFieldException e) {
                    log.error("Error loading logs for employee: {}", newValue, e);
                    logEmployee.createLog("Error Loading Logs", "Error loading logs for employee: " + newValue);
                    throw new RuntimeException(e);
                }
            });

            log.info("EmployeeLogs scene initialized successfully.");
        } catch (Exception e) {
            log.error("Error initializing EmployeeLogs scene: ", e);
            logEmployee.createLog("Initialization Error", "Failed to initialize EmployeeLogs scene: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the list of employees into the employee comboBox.
     */
    private void loadEmployees() {
        try {
            log.info("Loading employees into the ComboBox.");
            employeeComboBox.getItems().setAll(employeeLogsServices.loadEmployees());
            log.info("Employees loaded successfully.");
        } catch (Exception e) {
            log.error("Error loading employees: ", e);
            logEmployee.createLog("Error Loading Employees", "Failed to load employees: " + e.getMessage());
        }
    }

    /**
     * Loads the logs for the selected employee and populates the logs table.
     *
     * @param selectedEmployee The selected employee for whom the logs are being loaded.
     * @throws NoSuchFieldException If the employee field is not found in the EmployeeLog class.
     */
    private void loadLogs(Employee selectedEmployee) throws NoSuchFieldException {
        try {
            if (selectedEmployee == null) {
                log.warn("No employee selected for loading logs.");
                return;
            }
            log.info("Loading logs for employee: {}", selectedEmployee.getName());
            Field field = EmployeeLog.class.getDeclaredField("employee");
            employeeLogsTable.getItems().setAll(employeeLogsServices.loadLogs(selectedEmployee, field).y());
            log.info("Logs loaded successfully for employee: {}", selectedEmployee.getName());
        } catch (Exception e) {
            log.error("Error loading logs for employee: {}", selectedEmployee, e);
            logEmployee.createLog("Error Loading Logs", "Failed to load logs for employee: " + selectedEmployee + " - " + e.getMessage());
        }
    }
}
