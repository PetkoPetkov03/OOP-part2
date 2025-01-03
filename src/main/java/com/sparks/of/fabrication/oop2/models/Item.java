package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents Item entity
 */
@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    /**
     * The unique identifier for the item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item", nullable = false)
    private Long idItem;

    /**
     * The name of the item.
     */
    @Column(name = "name", length = 20)
    private String name;

    /**
     * The category to which the item belongs.
     */
    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private Category category;

    /**
     * The selling price of the item.
     */
    @Column(name = "price")
    private Double price;

    /**
     * The arrival price of the item.
     */
    @Column(name = "arrival_price")
    private Double arrivalPrice;

    /**
     * The quantity of the item available.
     */
    @Column(name = "quantity")
    private Integer quantity;
}
