package com.sparks.of.fabrication.oop2.utils;

/**
 * A generic record that holds a pair of values of types T and Y.
 *
 * @param <T> The type of the first value in the pair.
 * @param <Y> The type of the second value in the pair.
 */
public record Pair<T, Y>(T x, Y y) {
}
