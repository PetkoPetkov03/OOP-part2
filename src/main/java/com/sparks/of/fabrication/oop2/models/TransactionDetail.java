package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents the details of a transaction.
 */
@Entity
@Table(name = "transaction_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetail {

    /**
     * The unique identifier for the transaction detail.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The transaction associated with these details.
     */
    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    /**
     * The item involved in the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    /**
     * The quantity of the item in this transaction.
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * The price of the item in this transaction.
     */
    @Column(name = "price", nullable = false)
    private Double price;
}

