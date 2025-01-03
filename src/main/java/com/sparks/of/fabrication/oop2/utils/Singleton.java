package com.sparks.of.fabrication.oop2.utils;

import java.util.HashMap;

public class Singleton<T> {

    private static final HashMap<Class<?>, Pair<Singleton<?>, Object>> instances = new HashMap<>();

    private Singleton(T instanceObject) {}

    public static synchronized <T> Pair<Singleton<?>, T> getInstance(Class<T> tClass, T obj) {
        if (!instances.containsKey(tClass)) {
            instances.put(tClass, new Pair<>(new Singleton<>(obj), obj));
        }
        return (Pair<Singleton<?>, T>) instances.get(tClass);
    }

    public static synchronized <T> T getInstance(Class<T> tClass) {
        if (instances.containsKey(tClass)) {
            return (T) instances.get(tClass).y();
        }
        return null;
    }
}
