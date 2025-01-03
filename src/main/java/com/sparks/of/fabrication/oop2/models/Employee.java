package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * Represents an employee entity.
 */
@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    /**
     * The unique identifier for the employee.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the employee.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * The email address of the employee.
     */
    @Column(nullable = false)
    @ToString.Include
    private String email;

    /**
     * The password of the employee.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The role assigned to the employee.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private RoleModel role;
}
