package com.sparks.of.fabrication.oop2.utils;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.EmployeeLog;

import java.sql.Date;
import java.time.LocalDateTime;

public class LogEmployee {
    private static final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    public static void initializeLogEmployee() {
        Singleton.getInstance(LogEmployee.class, new LogEmployee());
    }

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
