package com.sparks.of.fabrication.oop2.utils;

import java.util.List;

/**
 * An interface that defines a method for validating a list of inputs based on specified validation types.
 */
public interface Validation {

    /**
     * Validates a list of input fields according to the specified validation types.
     *
     * @param vTypes A list of validation types to be applied to the corresponding inputs.
     * @param inputs A list of input values to be validated.
     * @return A Pair containing a boolean indicating overall validation success and a list of response messages.
     */
    Pair<Boolean, List<String>> validate(List<ValidationTypes> vTypes, List<String> inputs);
}
