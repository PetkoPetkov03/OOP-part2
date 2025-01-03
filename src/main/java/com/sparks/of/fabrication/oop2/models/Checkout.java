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
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a checkout entity
 */
@Entity
@Table(name = "checkout")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Checkout {

    /**
     * The unique identifier for the checkout.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_checkout", nullable = false)
    private Long idCheckout;

    /**
     * The employee associated with this checkout.
     */
    @ManyToOne
    @JoinColumn(name = "id_employee", nullable = false)
    private Employee employee;

    /**
     * The cash amount associated with the checkout.
     */
    @Column(name = "cash")
    private Double cash;
}

