package com.sparks.of.fabrication.oop2.utils;

import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.EmployeeLog;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 * A utility class for creating logs related to employee actions.
 */
public class LogEmployee {
    private static final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    /**
     * Initializes the LogEmployee instance by registering it in the singleton.
     */
    public static void initializeLogEmployee() {
        Singleton.getInstance(LogEmployee.class, new LogEmployee());
    }

    /**
     * Creates and persists a log entry for the logged-in employee.
     *
     * @param message The log message.
     * @param details Additional details for the log.
     */
    public void createLog(String message, String details) {
        Employee loggedInEmployee = Singleton.getInstance(Employee.class);
        EmployeeLog log = new EmployeeLog();
        log.setEmployee(loggedInEmployee);
        log.setTimestamp(Date.valueOf(LocalDateTime.now().toLocalDate()));
        log.setMessage(message);
        log.setDetails(details);

        entityManagerWrapper.genEntity(log);
    }
}
