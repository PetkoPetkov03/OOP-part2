package com.sparks.of.fabrication.oop2.scenes.employeeLogs;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.EmployeeLog;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.Pair;

import java.lang.reflect.Field;
import java.util.List;

public class EmployeeLogsServices {
    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    protected List<Employee> loadEmployees() {
        return entityManagerWrapper.findAllEntities(Employee.class);
    }
    protected Pair<Boolean, List<EmployeeLog>> loadLogs(Employee selectedEmployee,Field field) {
         return entityManagerWrapper.findEntityByValAll(EmployeeLog.class, field, selectedEmployee);
    }
}
