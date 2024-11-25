package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.id.UUIDGenerator;

import java.util.UUID;

@Entity
@Table(name = "cart_model")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class CartModel {
    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
