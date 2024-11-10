package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart")
public class CartModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
}
