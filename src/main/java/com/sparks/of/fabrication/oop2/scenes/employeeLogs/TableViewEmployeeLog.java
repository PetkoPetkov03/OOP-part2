package com.sparks.of.fabrication.oop2.scenes.employeeLogs;

import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.EmployeeLog;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class TableViewEmployeeLog {
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
