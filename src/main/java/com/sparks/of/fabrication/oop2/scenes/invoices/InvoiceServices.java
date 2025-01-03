package com.sparks.of.fabrication.oop2.scenes.invoices;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Employee;
import com.sparks.of.fabrication.oop2.models.InvoiceStore;
import com.sparks.of.fabrication.oop2.models.Nomenclature;
import com.sparks.of.fabrication.oop2.models.Suppliers;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Provides methods for managing invoices, including filtering and loading invoice data.
 */
public class InvoiceServices {
    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    /**
     * Loads values from an entity class into a ComboBox for selection.
     *
     * @param <T> the type of entity
     * @param entityClass the class of the entity
     * @param nameExtractor a function to extract names from entities
     * @param searchCriteriaComboBox the ComboBox to populate
     */
    protected <T> void loadValues(Class<T> entityClass, Function<T,String> nameExtractor, ComboBox<String> searchCriteriaComboBox ) {
        List<T> entities = entityManagerWrapper.findAllEntities(entityClass);
        searchCriteriaComboBox.setItems(FXCollections.observableArrayList(
                entities.stream().map(nameExtractor).collect(Collectors.toList())
        ));
    }

    /**
     * Filters invoices based on search criteria.
     *
     * @param field the field to filter by (e.g., invoice date)
     * @param date the date value for filtering
     * @param searchOption the search option (supplier, employee, etc.)
     * @param searchCriteria the criteria for searching
     * @return a list of filtered invoices
     */
    protected List<InvoiceStore> ss(Field field, LocalDate date, SearchOption searchOption, String searchCriteria){
        return switch (searchOption) {
            case SUPPLIER -> filterInvoicesBySupplier(searchCriteria);
            case EMPLOYEE -> filterInvoicesByEmployee(searchCriteria);
            case DATE -> entityManagerWrapper.findEntityByValAll(InvoiceStore.class, field, date).y();
            case ALL -> loadInvoiceData();
        };
    }

    /**
     * Filters invoices by supplier name.
     *
     * @param supplierName the name of the supplier
     * @return a list of invoices for the specified supplier
     */
    private List<InvoiceStore> filterInvoicesBySupplier(String supplierName) {
        List<InvoiceStore> allInvoices = entityManagerWrapper.findAllEntities(InvoiceStore.class);
        return allInvoices.stream()
                .filter(invoice -> {
                    Nomenclature nomenclature = invoice.getNomenclatura();
                    Suppliers supplier = nomenclature.getSuppliers();
                    return supplier.getName().equalsIgnoreCase(supplierName);
                })
                .collect(Collectors.toList());
    }

    /**
     * Filters invoices by employee name.
     *
     * @param employeeName the name of the employee
     * @return a list of invoices for the specified employee
     */
    private List<InvoiceStore> filterInvoicesByEmployee(String employeeName) {
        List<InvoiceStore> allInvoices = entityManagerWrapper.findAllEntities(InvoiceStore.class);
        return allInvoices.stream()
                .filter(invoice -> {
                    Employee employee = invoice.getEmployee();
                    return employee.getName().equalsIgnoreCase(employeeName);
                })
                .collect(Collectors.toList());
    }

    /**
     * Loads all invoice data.
     *
     * @return a list of all invoices
     */
    protected List<InvoiceStore> loadInvoiceData() {
        return entityManagerWrapper.findAllEntities(InvoiceStore.class);
    }
}
