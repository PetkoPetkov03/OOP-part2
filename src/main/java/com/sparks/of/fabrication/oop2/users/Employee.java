package com.sparks.of.fabrication.oop2.users;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Employee {
    private String name;
    private int employeeId;
    private Role role; // Role determines privileges

    protected Employee(String name, int employeeId) {
        this.name = name;
        this.employeeId = employeeId;
    }

    // Method to assign a role
    public void assignRole(Role role) {
        this.role = role;
        System.out.println(name + " assigned role: " + role.name());
        System.out.println("Privileges granted: " + role.getPrivileges());
    }

    // Check if the employee has a specific privilege based on their role
    public boolean hasPrivilege(Privileges privilege) {
        return role != null && role.getPrivileges().contains(privilege);
    }

    public void accessCheckout(Checkout checkout, Client client) {
        if (hasPrivilege(Privileges.CASHIER)) {
            checkout.finishTransaction(client);
        } else {
            System.out.println(name + " does not have the WORK_AT_CHECKOUT privilege.");
        }
    }

    public void showPrivileges() {
        if (role == null) {
            System.out.println(name + " has no role and therefore no privileges.");
        } else {
            System.out.println(name + "'s privileges: " + role.getPrivileges());
        }
    }

}
