package com.sparks.of.fabrication.oop2.utils;

import java.util.List;

public interface Validation {
    Pair<Boolean, List<String>> validate(List<ValidationTypes> vTypes, List<String> inputs);
}
