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

@Entity
@Table(name = "invoice_store")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invoice", nullable = false)
    private Long idInvoice;

    @ManyToOne
    @JoinColumn(name = "id_nomenclature", nullable = false)
    private Nomenclature nomenclatura;

    @ManyToOne
    @JoinColumn(name = "id_employee", nullable = false)
    private Employee employee;

    @Column(name = "number")
    private Integer number;

    @Column(name = "finalPrice")
    private Double finalPrice;

    @Column(name = "date")
    private Date date;

    @Column(name = "status")
    private Boolean status;
}

