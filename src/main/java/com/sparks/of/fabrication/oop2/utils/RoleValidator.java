package com.sparks.of.fabrication.oop2.utils;

import com.sparks.of.fabrication.oop2.users.Role;

import java.util.List;

public class RoleValidator {

    public static boolean areRolesValid(Role userRole, Role providedRole) {
        if (userRole == null || providedRole == null) {
            return false;
        }
        return userRole.equals(providedRole);
    }
    public static boolean areRolesValid(Role userRole, List<Role> allowedRoles) {
        return allowedRoles.contains(userRole);
    }
}
