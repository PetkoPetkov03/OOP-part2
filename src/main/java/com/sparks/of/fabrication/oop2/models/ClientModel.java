package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "client")
public class ClientModel {
    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private CartModel cart;
}
