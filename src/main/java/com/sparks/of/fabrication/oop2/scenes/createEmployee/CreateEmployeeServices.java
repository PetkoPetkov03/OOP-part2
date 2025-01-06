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

/**
 * Service class responsible for creating and persisting new employees in the system.
 */
public class CreateEmployeeServices {

    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);
    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(CreateEmployeeServices.class);
    private static final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    /**
     * Appends validation messages to the provided StringBuilder.
     *
     * @param builder The StringBuilder to which the validation messages will be appended.
     * @param validationResponse The validation result containing the list of validation messages.
     */
    protected void appendValidationMessages(StringBuilder builder, Pair<Boolean, List<String>> validationResponse) {
        for (String message : validationResponse.y()) {
            builder.append(message).append("\n");
        }
    }

    /**
     * Creates a new employee by hashing the password, fetching the role from the ComboBox, and
     * persisting the employee entity in the database.
     *
     * @param password The raw password to be hashed.
     * @param email The email of the new employee.
     * @param name The name of the new employee.
     * @param roleComboBox The ComboBox containing the role for the new employee.
     * @throws NoSuchFieldException If the field required for fetching the role is not found.
     */
    public void createAndPersistEmployee(String password, String email, String name, ComboBox<Role> roleComboBox) throws NoSuchFieldException {
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

    /**
     * Hashes the provided password using BCrypt with a salt to enhance security.
     *
     * @param password The raw password to be hashed.
     * @return The hashed password.
     */
    private String hashPassword(String password) {
        String salt = BCrypt.gensalt(13);
        return BCrypt.hashpw(password, salt);
    }

    /**
     * Fetches the role from the database based on the selected value in the ComboBox.
     *
     * @param roleComboBox The ComboBox containing the selected role.
     * @return The RoleModel representing the selected role.
     * @throws NoSuchFieldException If the field required for fetching the role is not found.
     */
    private RoleModel fetchRoleFromDatabase(ComboBox<Role> roleComboBox) throws NoSuchFieldException {
        return entityManagerWrapper.findEntityByVal(RoleModel.class, RoleModel.class.getDeclaredField("role"), roleComboBox.getValue()).y();
    }

    /**
     * Builds a new Employee entity with the provided details.
     *
     * @param hashedPassword The hashed password of the new employee.
     * @param role The role assigned to the new employee.
     * @param email The email of the new employee.
     * @param name The name of the new employee.
     * @return The created Employee entity.
     */
    private Employee buildEmployee(String hashedPassword, RoleModel role, String email, String name) {
        Employee employee = new Employee();
        employee.setEmail(email);
        employee.setPassword(hashedPassword);
        employee.setName(name);
        employee.setRole(role);
        return employee;
    }
}
