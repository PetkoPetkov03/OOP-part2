package com.sparks.of.fabrication.oop2.scenes.ccheckout;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Checkout;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutServices {

    private final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    public List<String> getAllEmployeeNames() {
        return entityManagerWrapper.findAllEntities(Employee.class).stream()
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    public List<String> getAllCheckoutsDescriptions() {
        return entityManagerWrapper.findAllEntities(Checkout.class).stream()
                .map(checkout -> checkout.getEmployee().getName() + " - " + checkout.getCash() + " - " + checkout.getIdCheckout())  // Adding checkout id as identifier
                .collect(Collectors.toList());
    }


    public Employee findEmployeeByName(String employeeName) throws NoSuchFieldException {
        Field employeeField = Employee.class.getDeclaredField("name");
        return entityManagerWrapper.findEntityByVal(Employee.class, employeeField, employeeName).y();
    }

    public Checkout findCheckoutByEmployee(Employee employee) throws NoSuchFieldException {
        Field employeeField = Checkout.class.getDeclaredField("employee");
        return entityManagerWrapper.findEntityByVal(Checkout.class, employeeField, employee).y();
    }

    public void createCheckout(Employee employee, double cashAmount) {
        Checkout newCheckout = new Checkout();
        newCheckout.setEmployee(employee);
        newCheckout.setCash(cashAmount);
        entityManagerWrapper.genEntity(newCheckout);
        logEmployee.createLog("Created Checkout", employee.getName());
    }

    public void updateCheckoutCash(Checkout checkout, double newCashAmount) {
        checkout.setCash(newCashAmount);
        entityManagerWrapper.genEntity(checkout);
        logEmployee.createLog("Updated Checkout", "ID: " + checkout.getIdCheckout() + ", New Cash: $" + newCashAmount);
    }

    public boolean isValidCashInput(String input) {
        return input != null && input.matches("^[0-9]+(\\.[0-9]{1,2})?$");
    }
    public void updateCheckoutEmployee(Checkout checkout, Employee newEmployee) {
        checkout.setEmployee(newEmployee);
        entityManagerWrapper.genEntity(checkout);
        logEmployee.createLog("Updated Checkout Employee", "Checkout ID: " + checkout.getIdCheckout() + ", New Employee: " + newEmployee.getName());
    }

}
