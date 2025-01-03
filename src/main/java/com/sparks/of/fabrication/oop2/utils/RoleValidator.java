package com.sparks.of.fabrication.oop2.utils;

import com.sparks.of.fabrication.oop2.users.Role;

import java.util.List;

/**
 * Utility class for validating user roles against provided roles.
 */
public class RoleValidator {

    /**
     * Checks if the user's role matches the provided role.
     *
     * @param userRole    The user's role to validate.
     * @param providedRole The role to check against.
     * @return true if the user's role matches the provided role, false otherwise.
     */
    public static boolean areRolesValid(Role userRole, Role providedRole) {
        if (userRole == null || providedRole == null) {
            return false;
        }
        return userRole.equals(providedRole);
    }

    /**
     * Checks if the user's role is included in the list of allowed roles.
     *
     * @param userRole     The user's role to validate.
     * @param allowedRoles The list of allowed roles.
     * @return true if the user's role is in the allowed roles list, false otherwise.
     */
    public static boolean areRolesValid(Role userRole, List<Role> allowedRoles) {
        return allowedRoles.contains(userRole);
    }
}
