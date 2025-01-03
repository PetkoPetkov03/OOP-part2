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
 * Represents the details of a nomenclature
 */
@Entity
@Table(name = "nomenclature_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NomenclatureDetails {

    /**
     * The unique identifier for the nomenclature details.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nom_details", nullable = false)
    private Long idNomDetails;

    /**
     * The nomenclature associated with these details.
     */
    @ManyToOne
    @JoinColumn(name = "id_nomenclature", nullable = false)
    private Nomenclature nomenclature;

    /**
     * The item associated with these nomenclature details.
     */
    @ManyToOne
    @JoinColumn(name = "id_item", nullable = false)
    private Item item;

    /**
     * The quantity of the item in this nomenclature.
     */
    @Column(name = "item_quantity")
    private Integer itemQuantity;

    /**
     * The price of the item in this nomenclature.
     */
    @Column(name = "item_price")
    private Double itemPrice;
}
