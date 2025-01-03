package com.sparks.of.fabrication.oop2.utils;

import java.util.HashMap;

/**
 * A generic singleton class used to ensure only one instance of a given type exists in the application.
 */
public class Singleton<T> {

    private static final HashMap<Class<?>, Pair<Singleton<?>, Object>> instances = new HashMap<>();

    /**
     * Private constructor to prevent instantiation outside of this class.
     *
     * @param instanceObject The object instance to be held as the singleton.
     */
    private Singleton(T instanceObject) {}

    /**
     * Retrieves or creates a new singleton instance for the given class type with the provided object.
     *
     * @param tClass The class type for the singleton.
     * @param obj    The object to be assigned to the singleton.
     * @param <T>    The type of the object to be stored in the singleton.
     * @return A pair containing the singleton and the object instance.
     */
    public static synchronized <T> Pair<Singleton<?>, T> getInstance(Class<T> tClass, T obj) {
        if (!instances.containsKey(tClass)) {
            instances.put(tClass, new Pair<>(new Singleton<>(obj), obj));
        }
        return (Pair<Singleton<?>, T>) instances.get(tClass);
    }

    /**
     * Retrieves the singleton instance for the given class type.
     *
     * @param tClass The class type for the singleton.
     * @param <T>    The type of the object to be retrieved from the singleton.
     * @return The singleton object if it exists, otherwise null.
     */
    public static synchronized <T> T getInstance(Class<T> tClass) {
        if (instances.containsKey(tClass)) {
            return (T) instances.get(tClass).y();
        }
        return null;
    }
}
