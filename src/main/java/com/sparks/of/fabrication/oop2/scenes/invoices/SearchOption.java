package com.sparks.of.fabrication.oop2.scenes.invoices;

import lombok.Getter;

@Getter
public enum SearchOption {
    ALL("All"),
    SUPPLIER("Supplier"),
    EMPLOYEE("Employee"),
    DATE("Date");

    private final String displayName;

    SearchOption(String displayName) {
        this.displayName = displayName;
    }

}
