package com.sparks.of.fabrication.oop2.scenes.ccheckout;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Checkout;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling checkout operations, such as creating,
 * updating, and retrieving checkout records.
 */
public class CheckoutServices {

    private final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    /**
     * Retrieves the names of all employees from the database.
     *
     * @return A list of employee names.
     */
    public List<String> getAllEmployeeNames() {
        return entityManagerWrapper.findAllEntities(Employee.class).stream()
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves descriptions of all checkout records
     *
     * @return A list of checkout descriptions in the format:
     *         "EmployeeName - CashAmount - CheckoutID".
     */
    public List<String> getAllCheckoutsDescriptions() {
        return entityManagerWrapper.findAllEntities(Checkout.class).stream()
                .map(checkout -> checkout.getEmployee().getName() + " - " + checkout.getCash() + " - " + checkout.getIdCheckout())
                .collect(Collectors.toList());
    }

    /**
     * Finds an employee by their name.
     *
     * @param employeeName The name of the employee.
     * @return The `Employee` object corresponding to the given name.
     */
    public Employee findEmployeeByName(String employeeName) throws NoSuchFieldException {
        Field employeeField = Employee.class.getDeclaredField("name");
        return entityManagerWrapper.findEntityByVal(Employee.class, employeeField, employeeName).y();
    }

    /**
     * Finds the checkout by specific employee.
     *
     * @param employee The employee whose checkout is to be found.
     * @return The `Checkout` object associated with the given employee.
     */
    public Checkout findCheckoutByEmployee(Employee employee) throws NoSuchFieldException {
        Field employeeField = Checkout.class.getDeclaredField("employee");
        return entityManagerWrapper.findEntityByVal(Checkout.class, employeeField, employee).y();
    }

    /**
     * Creates a new checkout record for an employee with a specified cash amount.
     *
     * @param employee The employee for whom the checkout is to be created.
     * @param cashAmount The cash amount for the checkout.
     */
    public void createCheckout(Employee employee, double cashAmount) {
        Checkout newCheckout = new Checkout();
        newCheckout.setEmployee(employee);
        newCheckout.setCash(cashAmount);
        entityManagerWrapper.genEntity(newCheckout);
        logEmployee.createLog("Created Checkout", employee.getName());
    }

    /**
     * Updates the cash amount for an existing checkout.
     *
     * @param checkout The checkout to be updated.
     * @param newCashAmount The new cash amount to be set for the checkout.
     */
    public void updateCheckoutCash(Checkout checkout, double newCashAmount) {
        checkout.setCash(newCashAmount);
        entityManagerWrapper.genEntity(checkout);
        logEmployee.createLog("Updated Checkout", "ID: " + checkout.getIdCheckout() + ", New Cash: $" + newCashAmount);
    }

    /**
     * Validates the format of the cash input.
     *
     * @param input The input string representing the cash amount.
     * @return True if the input is a valid cash amount
     */
    public boolean isValidCashInput(String input) {
        return input != null && input.matches("^[0-9]+(\\.[0-9]{1,2})?$");
    }

    /**
     * Updates the employee associated with an existing checkout.
     *
     * @param checkout The checkout to be updated.
     * @param newEmployee The new employee to be assigned to the checkout.
     */
    public void updateCheckoutEmployee(Checkout checkout, Employee newEmployee) {
        checkout.setEmployee(newEmployee);
        entityManagerWrapper.genEntity(checkout);
        logEmployee.createLog("Updated Checkout Employee", "Checkout ID: " + checkout.getIdCheckout() + ", New Employee: " + newEmployee.getName());
    }

}
