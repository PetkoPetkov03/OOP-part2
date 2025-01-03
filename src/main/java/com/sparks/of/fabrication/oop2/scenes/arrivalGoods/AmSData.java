package com.sparks.of.fabrication.oop2.scenes.arrivalGoods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Helper class to store specific information about an item.
 */
@AllArgsConstructor
@Getter
@Setter
public class AmSData {

    /**
     * The quantity of the item.
     */
    private Integer quantity;

    /**
     * The arrival price of the item.
     */
    private Double arrivalPrice;

    /**
     * The selling price of the item.
     */
    private Double sellingPrice;
}
