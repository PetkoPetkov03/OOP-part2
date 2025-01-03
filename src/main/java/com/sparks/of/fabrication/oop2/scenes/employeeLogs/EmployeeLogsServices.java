package com.sparks.of.fabrication.oop2.scenes.employeeLogs;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.EmployeeLog;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.Pair;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Service class responsible for handling operations related to employee logs.
 */
public class EmployeeLogsServices {

    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    /**
     * Loads a list of all employees from the database.
     *
     * @return A list of {@link Employee} objects representing all employees.
     */
    protected List<Employee> loadEmployees() {
        return entityManagerWrapper.findAllEntities(Employee.class);
    }

    /**
     * Loads a list of logs for the specified employee by querying the database.
     *
     * @param selectedEmployee The employee whose logs are to be retrieved.
     * @param field The field of the EmployeeLog entity that stores the reference to the employee.
     * @return A {@link Pair} containing a boolean indicating the success of the operation and a list of {@link EmployeeLog} objects.
     */
    protected Pair<Boolean, List<EmployeeLog>> loadLogs(Employee selectedEmployee, Field field) {
        return entityManagerWrapper.findEntityByValAll(EmployeeLog.class, field, selectedEmployee);
    }
}
