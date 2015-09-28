package com.philips.pins.shinelib.utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310188215 on 16/09/15.
 */
public class SHNServiceRegistry {
    private Map<Class, Object> registry;
    private static SHNServiceRegistry instance;

    private SHNServiceRegistry() {
        registry = new HashMap<>();
    }

    public static SHNServiceRegistry getInstance() {
        if (instance == null) {
            instance = new SHNServiceRegistry();
        }
        return instance;
    }

    public static void releaseInstance() {
        instance = null;
    }

    public void add(Object object) {
        registry.put(object.getClass(), object);
    }

    public <T> void add(Object object, Class<T> clazz) {
        registry.put(clazz, object);
    }

    public <T> T get(Class<T> clazz) {
        return (T)registry.get(clazz);
    }

}
