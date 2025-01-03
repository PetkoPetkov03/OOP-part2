package com.sparks.of.fabrication.oop2.users;

import lombok.Getter;

import java.util.Set;

/**
 * Enum representing different user roles, each with a set of assigned privileges.
 */
@Getter
public enum Role {

    /**
     * Role for a cashier with the privilege to act as a cashier.
     */
    CASHIER(Set.of(Privileges.CASHIER)),

    /**
     * Role for a backstaff with the privilege to manage inventory.
     */
    BACKSTAFF(Set.of(Privileges.MANAGE_INVENTORY)),

    /**
     * Role for a manager with privileges to manage inventory and view reports.
     */
    MANAGER(Set.of(Privileges.MANAGE_INVENTORY, Privileges.VIEW_REPORTS)),

    /**
     * Role for an administrator with full administrative privileges and the ability to view reports.
     */
    ADMIN(Set.of(Privileges.ADMIN_PRIVILEGES, Privileges.VIEW_REPORTS));

    private final Set<Privileges> privileges;

    /**
     * Constructor for the Role enum to set the associated privileges.
     *
     * @param privileges The set of privileges associated with the role.
     */
    Role(Set<Privileges> privileges) {
        this.privileges = privileges;
    }
}
