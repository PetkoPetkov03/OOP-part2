package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client_model")
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @ToString.Include
    private String name;

}