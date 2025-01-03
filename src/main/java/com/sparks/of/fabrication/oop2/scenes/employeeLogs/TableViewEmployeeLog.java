package com.sparks.of.fabrication.oop2.scenes.employeeLogs;

import com.sparks.of.fabrication.oop2.models.EmployeeLog;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Utility class responsible for configuring the columns of the Employee Logs table view.
 */
public class TableViewEmployeeLog {

    /**
     * Configures the table columns for displaying employee logs in the TableView.
     *
     * @param employeeLogsTable The TableView that will display the employee logs.
     * @param logId The column for displaying the log ID.
     * @param employee The column for displaying the employee's name.
     * @param timestamp The column for displaying the timestamp of the log entry.
     * @param message The column for displaying the message of the log entry.
     * @param details The column for displaying additional details of the log entry.
     */
    public static void configureTableColumns(TableView<EmployeeLog> employeeLogsTable,
                                             TableColumn<EmployeeLog, Long> logId,
                                             TableColumn<EmployeeLog, String> employee,
                                             TableColumn<EmployeeLog, String> timestamp,
                                             TableColumn<EmployeeLog, String> message,
                                             TableColumn<EmployeeLog, String> details){
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
    }
}
