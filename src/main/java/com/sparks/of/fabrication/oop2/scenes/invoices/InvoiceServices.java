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

public class InvoiceServices {
    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    protected <T> void loadValues(Class<T> entityClass, Function<T,String> nameExtractor, ComboBox<String> searchCriteriaComboBox ) {
        List<T> entities = entityManagerWrapper.findAllEntities(entityClass);
        searchCriteriaComboBox.setItems(FXCollections.observableArrayList(
                entities.stream().map(nameExtractor).collect(Collectors.toList())
        ));
    }
    protected List<InvoiceStore> ss(Field field, LocalDate date, SearchOption searchOption, String searchCriteria){
        return switch (searchOption) {
            case SUPPLIER -> filterInvoicesBySupplier(searchCriteria);
            case EMPLOYEE -> filterInvoicesByEmployee(searchCriteria);
            case DATE -> entityManagerWrapper.findEntityByValAll(InvoiceStore.class, field, date).y();
            case ALL -> loadInvoiceData();
        };
    }
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

    private List<InvoiceStore> filterInvoicesByEmployee(String employeeName) {
        List<InvoiceStore> allInvoices = entityManagerWrapper.findAllEntities(InvoiceStore.class);
        return allInvoices.stream()
                .filter(invoice -> {
                    Employee employee = invoice.getEmployee();
                    return employee.getName().equalsIgnoreCase(employeeName);
                })
                .collect(Collectors.toList());
    }
    protected List<InvoiceStore> loadInvoiceData() {
        return entityManagerWrapper.findAllEntities(InvoiceStore.class);
    }
}
