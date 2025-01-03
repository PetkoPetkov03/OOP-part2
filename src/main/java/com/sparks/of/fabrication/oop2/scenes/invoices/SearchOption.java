package com.sparks.of.fabrication.oop2.scenes.invoices;

import lombok.Getter;

/**
 * Enum representing different search options for filtering invoices.
 */
@Getter
public enum SearchOption {
    ALL("All"),
    SUPPLIER("Supplier"),
    EMPLOYEE("Employee"),
    DATE("Date");

    private final String displayName;

    /**
     * Constructor for the SearchOption enum.
     *
     * @param displayName the display name for the search option
     */
    SearchOption(String displayName) {
        this.displayName = displayName;
    }
}
