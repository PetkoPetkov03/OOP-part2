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
 * Represents a nomenclature entity
 */
@Entity
@Table(name = "nomenclatura")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nomenclature {

    /**
     * The unique identifier for the nomenclature.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nomenclature", nullable = false)
    private Long idNomenclature;

    /**
     * The supplier associated with this nomenclature.
     */
    @ManyToOne
    @JoinColumn(name = "id_supp", nullable = false)
    private Suppliers suppliers;

    /**
     * The employee associated with this nomenclature.
     */
    @ManyToOne
    @JoinColumn(name = "id_employee", nullable = false)
    private Employee employee;
}
