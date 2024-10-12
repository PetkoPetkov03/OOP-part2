package com.sparks.of.fabrication.oop2.users;

import java.util.Set;

public enum Role {
    CASHIER(Set.of(Privileges.CASHIER)),  // No need for WORK_AT_CHECKOUT here
    MANAGER(Set.of(Privileges.MANAGE_INVENTORY, Privileges.VIEW_REPORTS)),
    ADMIN(Set.of(Privileges.ADMIN_PRIVILEGES, Privileges.VIEW_REPORTS));

    private final Set<Privileges> privileges;

    Role(Set<Privileges> privileges) {
        this.privileges = privileges;
    }

    public Set<Privileges> getPrivileges() {
        return privileges;
    }
}
