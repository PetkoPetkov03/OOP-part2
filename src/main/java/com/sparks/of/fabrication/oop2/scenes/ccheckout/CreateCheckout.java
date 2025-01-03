package com.sparks.of.fabrication.oop2.scenes.ccheckout;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Checkout;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.utils.LogEmployee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * Controller class for creating and managing checkouts in the application.
 * This class handles the creation and update of checkout records, populating
 * combo boxes with employees and checkout details, and handling user input
 * for checkout operations.
 */
public class CreateCheckout {

    @FXML private TextField cashInput;
    @FXML private ComboBox<String> employeeComboBox;
    @FXML private ComboBox<String> allCheckoutsComboBox;
    @FXML private TextField checkoutMoneyField;
    @FXML private ComboBox<String> changeEmployeeComboBox;

    private final CheckoutServices checkoutService = new CheckoutServices();
    private static final Logger log = LogManager.getLogger(CreateCheckout.class);
    private static final LogEmployee logEmployee = Singleton.getInstance(LogEmployee.class);

    /**
     * Initializes the CreateCheckout view, populating the combo boxes for
     * employees and checkouts, and handling any initialization errors.
     */
    @FXML
    private void initialize() {
        try {
            populateEmployeeComboBox();
            populateCheckoutComboBox();
            populateChangeEmployeeComboBox();
        } catch (Exception e) {
            log.error("Error initializing CreateCheckout: ", e);
            logEmployee.createLog("Initialization Error", e.getMessage());
        }
    }

    /**
     * Populates the employee combo box with a list of all employee names
     * retrieved from the checkout service.
     */
    private void populateEmployeeComboBox() {
        try {
            List<String> employeeNames = checkoutService.getAllEmployeeNames();
            employeeComboBox.getItems().setAll(employeeNames);
            log.info("Employee ComboBox populated successfully.");
        } catch (Exception e) {
            log.error("Error populating employeeComboBox: ", e);
            logEmployee.createLog("Error Populating Employee ComboBox", e.getMessage());
        }
    }

    /**
     * Populates the change employee combo box with a list of all employee names.
     */
    private void populateChangeEmployeeComboBox() {
        try {
            List<String> employeeNames = checkoutService.getAllEmployeeNames();
            changeEmployeeComboBox.getItems().setAll(employeeNames);
            log.info("Change Employee ComboBox populated successfully.");
        } catch (Exception e) {
            log.error("Error populating changeEmployeeComboBox: ", e);
            logEmployee.createLog("Error Populating Change Employee ComboBox", e.getMessage());
        }
    }

    /**
     * Populates the checkout combo box with a list of all checkout descriptions
     * retrieved from the checkout service.
     */
    private void populateCheckoutComboBox() {
        try {
            List<String> checkoutDescriptions = checkoutService.getAllCheckoutsDescriptions();
            allCheckoutsComboBox.getItems().setAll(checkoutDescriptions);
            log.info("Checkout ComboBox populated successfully.");
        } catch (Exception e) {
            log.error("Error populating allCheckoutsComboBox: ", e);
            logEmployee.createLog("Error Populating Checkout ComboBox", e.getMessage());
        }
    }

    /**
     * Creates a new checkout record with the selected employee and cash input.
     * It also logs the operation and updates the checkout combo box.
     *
     * @throws IllegalArgumentException if no employee is selected or if the
     *         cash input is invalid.
     */
    @FXML
    private void checkout() {
        try {
            String selectedEmployeeName = employeeComboBox.getValue();
            if (selectedEmployeeName == null || selectedEmployeeName.isEmpty()) {
                throw new IllegalArgumentException("No employee selected.");
            }

            Employee employee = checkoutService.findEmployeeByName(selectedEmployeeName);
            double cashAmount = parseCashInput(cashInput.getText());

            checkoutService.createCheckout(employee, cashAmount);
            populateCheckoutComboBox();

            log.info("Checkout created successfully for employee: {}", selectedEmployeeName);
            logEmployee.createLog("Checkout Created", "Employee: " + selectedEmployeeName + ", Cash: " + cashAmount);
        } catch (Exception e) {
            log.error("Error creating checkout: ", e);
            logEmployee.createLog("Checkout Error", e.getMessage());
        }
    }

    /**
     * Loads the details of a selected checkout and updates the checkout information.
     * This method allows changing the employee and cash amount for an existing checkout.
     */
    @FXML
    private void loadCheckoutDetails() {
        try {
            String selectedCheckout = allCheckoutsComboBox.getValue();
            String newEmployeeName = changeEmployeeComboBox.getValue();
            String newCashInput = checkoutMoneyField.getText();

            if (selectedCheckout == null || selectedCheckout.isEmpty()) {
                throw new IllegalArgumentException("No checkout selected.");
            }
            Employee employee = checkoutService.findEmployeeByName(allCheckoutsComboBox.getValue().split(" - ")[0]);
            Checkout checkout = checkoutService.findCheckoutByEmployee(employee);

            if (checkoutService.isValidCashInput(newCashInput)) {
                double newCashAmount = Double.parseDouble(newCashInput);
                checkoutService.updateCheckoutCash(checkout, newCashAmount);
                log.info("Updated checkout cash for ID: {}", checkout.getIdCheckout());
                logEmployee.createLog("Checkout Cash Updated", "ID: " + checkout.getIdCheckout() + ", New Cash: $" + newCashAmount);
            }

            if (newEmployeeName != null && !newEmployeeName.isEmpty()) {
                Employee newEmployee = checkoutService.findEmployeeByName(newEmployeeName);
                checkoutService.updateCheckoutEmployee(checkout, newEmployee);
                log.info("Updated checkout employee for ID: {}", checkout.getIdCheckout());
                logEmployee.createLog("Checkout Employee Updated", "ID: " + checkout.getIdCheckout() + ", New Employee: " + newEmployeeName);
            }
            populateCheckoutComboBox();

        } catch (Exception e) {
            log.error("Error updating checkout: ", e);
            logEmployee.createLog("Checkout Update Error", e.getMessage());
        }
    }

    /**
     * Parses the cash input value from the text field and returns it as a double.
     *
     * @param cashInput The cash input value as a string.
     * @return The parsed cash amount as a double.
     * @throws IllegalArgumentException if the cash input format is invalid.
     */
    private double parseCashInput(String cashInput) {
        if (!checkoutService.isValidCashInput(cashInput)) {
            throw new IllegalArgumentException("Invalid cash input format.");
        }
        return Double.parseDouble(cashInput);
    }
}
