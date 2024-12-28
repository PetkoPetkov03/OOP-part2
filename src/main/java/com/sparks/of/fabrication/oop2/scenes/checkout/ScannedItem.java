package com.sparks.of.fabrication.oop2.scenes.checkout;

import lombok.Getter;
import lombok.Setter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScannedItem {
    private String name;
    private double price;
    private int quantity;

    public void incrementQuantity() {
        this.quantity++;
    }

    @Override
    public String toString() {
        return name + " " + String.format("%.2f", price) + " (Qty: " + quantity + ")";
    }
}

