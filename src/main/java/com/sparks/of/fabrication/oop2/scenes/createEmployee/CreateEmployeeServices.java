package com.sparks.of.fabrication.oop2.scenes.createEmployee;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.RoleModel;
import com.sparks.of.fabrication.oop2.users.Role;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import com.sparks.of.fabrication.oop2.utils.Pair;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCrypt;
import javafx.scene.control.ComboBox;

import java.util.List;

public class CreateEmployeeServices {
    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(CreateEmployeeServices.class);
    private static final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    protected void appendValidationMessages(StringBuilder builder, Pair<Boolean, List<String>> validationResponse) {
        for (String message : validationResponse.y()) {
            builder.append(message).append("\n");
        }
    }

    protected void createAndPersistEmployee(String password, String email, String name, ComboBox<Role> roleComboBox) throws NoSuchFieldException {
        String hashedPassword = hashPassword(password);
        RoleModel role = fetchRoleFromDatabase(roleComboBox);

        Employee employee = buildEmployee(hashedPassword, role,email,name);

        if (entityManagerWrapper.genEntity(employee)) {
            log.info("Employee created successfully: {}", name);
            logEmployee.createLog("Employee Created", "Employee: " + name + ", Role: " + role.getRole());
        } else {
            log.error("Failed to create employee: {}", name);
            logEmployee.createLog("Employee Creation Error", "Failed to create employee: " + name);
        }
    }

    private String hashPassword(String password) {
        String salt = BCrypt.gensalt(13);
        return BCrypt.hashpw(password, salt);
    }

    private RoleModel fetchRoleFromDatabase(ComboBox<Role> roleComboBox) throws NoSuchFieldException {
        return entityManagerWrapper.findEntityByVal(RoleModel.class, RoleModel.class.getDeclaredField("role"), roleComboBox.getValue()).y();
    }

    private Employee buildEmployee(String hashedPassword, RoleModel role, String email, String name) {
        Employee employee = new Employee();
        employee.setEmail(email);
        employee.setPassword(hashedPassword);
        employee.setName(name);
        employee.setRole(role);
        return employee;
    }
}
