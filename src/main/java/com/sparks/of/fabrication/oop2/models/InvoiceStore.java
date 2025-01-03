package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents invoice entity
 */
@Entity
@Table(name = "invoice_store")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceStore {

    /**
     * The unique identifier for the invoice.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invoice", nullable = false)
    private Long idInvoice;

    /**
     * The nomenclature associated with this invoice.
     */
    @ManyToOne
    @JoinColumn(name = "id_nomenclature", nullable = false)
    private Nomenclature nomenclatura;

    /**
     * The employee who creates this invoice.
     */
    @ManyToOne
    @JoinColumn(name = "id_employee", nullable = false)
    private Employee employee;

    /**
     * The invoice number for day of creation.
     */
    @Column(name = "number")
    private Integer number;

    /**
     * The price of the invoice.
     */
    @Column(name = "finalPrice")
    private Double finalPrice;

    /**
     * The date of the invoice.
     */
    @Column(name = "date")
    private Date date;

    /**
     * The status of the invoice (true for active, false for inactive).
     */
    @Column(name = "status")
    private Boolean status;
}
