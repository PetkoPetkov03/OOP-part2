package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

/**
 * Represents a transaction entity,
 */
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    /**
     * The unique identifier for the transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The client associated with the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /**
     * The employee who processed the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * The checkout associated with the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "checkout_id", nullable = false)
    private Checkout checkout;

    /**
     * The total amount of the transaction.
     */
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    /**
     * The date when the transaction occurred.
     */
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;
}


