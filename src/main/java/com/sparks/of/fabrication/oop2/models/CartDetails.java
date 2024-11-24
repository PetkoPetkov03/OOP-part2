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

@Entity
@Table(name = "cart_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cart_details", nullable = false)
    private Long idCartDetails;

    @ManyToOne
    @JoinColumn(name = "id_cart", nullable = false)
    private CartModel cart;

    @ManyToOne
    @JoinColumn(name = "id_item", nullable = false)
    private Item item;
}

