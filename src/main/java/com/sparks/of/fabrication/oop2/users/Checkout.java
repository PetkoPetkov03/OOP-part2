package com.sparks.of.fabrication.oop2.users;

import com.sparks.of.fabrication.oop2.utils.Item;

public class Checkout {
    private Employee employee;
    private double cash;

    public Checkout(double cash) {
        this.cash = cash;
    }

    public double getCash() {
        return cash;
    }

    public void assignEmployee(Employee employee) {
        if (employee.hasPrivilege(Privileges.CASHIER)) {
            this.employee = employee;
            System.out.println(this.employee.getName() + " is now assigned to this checkout.");
        } else {
            System.out.println("Employee does not have the permission to be on the checkout.");
        }
    }

    public void addCash(double cash) {
        this.cash += cash;
    }

    public void finishTransaction(Client client) {
        double finalPrice = 0;
        for(Item item : client.getCart()){
            finalPrice += item.getPrice();
        }
        //Finish
    }
}
