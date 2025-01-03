package com.sparks.of.fabrication.oop2.scenes.checkout;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an item that has been scanned during the checkout process.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScannedItem {

    /**
     * The unique identifier for the scanned item.
     */
    private Long id;

    /**
     * The name of the scanned item.
     */
    private String name;

    /**
     * The price of the scanned item.
     */
    private double price;

    /**
     * The quantity of the scanned item in the current transaction.
     */
    private int quantity;

    /**
     * Increments the quantity of the scanned item by 1.
     */
    public void incrementQuantity() {
        this.quantity++;
    }

    /**
     * toString() method to present the scanned item
     *
     * @return A string representation of the scanned item in the format:
     *         "name price (Qty: quantity)".
     */
    @Override
    public String toString() {
        return name + " " + String.format("%.2f", price) + " (Qty: " + quantity + ")";
    }
}
