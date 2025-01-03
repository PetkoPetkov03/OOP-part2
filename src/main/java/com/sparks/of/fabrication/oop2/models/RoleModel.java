package com.sparks.of.fabrication.oop2.models;

import com.sparks.of.fabrication.oop2.users.Privileges;
import com.sparks.of.fabrication.oop2.users.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * Represents a role entity for defining roles within the system.
 */
@Entity
@Table(name = "role_model")
@NoArgsConstructor
@Data
@Getter
@Setter
public class RoleModel {

    /**
     * The unique identifier for the role model.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rolemodel", nullable = false)
    private Long id;

    /**
     * The role with is associated with Role enum .
     */
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
}
