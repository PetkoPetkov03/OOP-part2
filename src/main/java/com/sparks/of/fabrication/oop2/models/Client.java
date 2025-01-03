package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a client entity
 */
@Entity
@Table(name = "client_model")
@Getter
@Setter
public class Client {

    /**
     * The unique identifier for the client.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the client.
     */
    @Column(nullable = false, length = 100)
    @ToString.Include
    private String name;
}
