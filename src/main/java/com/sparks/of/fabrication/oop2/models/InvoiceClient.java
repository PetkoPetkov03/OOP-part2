package com.sparks.of.fabrication.oop2.models;


import com.sparks.of.fabrication.oop2.models.ClientModel;
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

@Entity
@Table(name = "invoice_client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invoice_client", nullable = false)
    private Long idInvoiceClient;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private ClientModel client;

    @ManyToOne
    @JoinColumn(name = "id_checkout", nullable = false)
    private Checkout checkout;
}

