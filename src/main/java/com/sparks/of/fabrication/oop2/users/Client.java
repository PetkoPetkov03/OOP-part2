package com.sparks.of.fabrication.oop2.users;

import com.sparks.of.fabrication.oop2.Item;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private String name;
    private List<Item> cart;

    public Client(String name) {
        this.name = name;
        cart = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Item> getCart() {
        return cart;
    }

    public void addItem(Item item) {
        cart.add(item);
    }

}
