package com.sparks.of.fabrication.oop2.users;

public class Administrator {

    public Employee createEmployee(String name, int employeeId) {
        Employee employee = new Employee(name, employeeId);
        // Add to database
        System.out.println("Employee " + name + " created with ID: " + employeeId);
        return employee;
    }

    public void assignRoleToEmployee(Employee employee, Role role) {
        employee.assignRole(role);
        System.out.println("Role " + role.name() + " assigned to " + employee.getName());
    }

    public Checkout createCheckout(double initialCash) {
        Checkout checkout = new Checkout(initialCash);
        System.out.println("Checkout created with initial cash: " + initialCash);
        return checkout;
    }
}
