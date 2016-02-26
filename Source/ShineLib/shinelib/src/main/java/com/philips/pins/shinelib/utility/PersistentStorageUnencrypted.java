package com.philips.pins.shinelib.utility;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PersistentStorageUnencrypted implements SharedPreferences, PersistentStorage {
    private static final String TAG = "PersistentStorage";

    private static final String SHORT_VALUE = "SHORT_VALUE";
    public static final String ENUM_NAME = "ENUM_NAME";
    public static final String DOUBLE_VALUE = "DOUBLE_VALUE";
    public static final String LIST_BASE = "_LIST_";
    public static final String SIZE = "SIZE";

    @NonNull
    private SharedPreferences sharedPreferences;

    private final Object lock = new Object();

    public PersistentStorageUnencrypted(@NonNull final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public <T> void put(@NonNull final String key, @Nullable final T value) {
        synchronized (lock) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            if (value == null) {
                edit.remove(key);
                edit.remove(key + SHORT_VALUE);
                edit.remove(key + ENUM_NAME);
                edit.remove(key + DOUBLE_VALUE);
                removeList(key, edit);
                edit.commit();
            } else {
                put(key, edit, value);
            }
        }
    }

    private <T> void put(@NonNull final String key, @NonNull final Editor edit, @NonNull final T value) {
        if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else if (value instanceof Double) {
            edit.putLong(key, Double.doubleToLongBits((Double) value));
            edit.putBoolean(key + DOUBLE_VALUE, true);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof List<?>) {
            putList(key, edit, (List<?>) value);
        } else if (value instanceof Set<?>) {
            putStringSet(key, edit, (Set<?>) value);
        } else if (value instanceof Enum<?>) {
            edit.putInt(key, ((Enum<?>) value).ordinal());
            edit.putString(key + ENUM_NAME, ((Enum<?>) value).getClass().getName());
        } else if (value instanceof Short) {
            edit.putInt(key, (Short) value);
            edit.putBoolean(key + SHORT_VALUE, true);
        } else {
            SHNLogger.wtf(TAG, "Trying to store an unsupported data type in Shared preferences!");
        }

        edit.commit();
    }

    private <T> void putStringSet(@NonNull final String key, @NonNull final Editor edit, @NonNull final Set<?> set) {
        if (set.isEmpty()) {
            edit.putStringSet(key, new HashSet<String>());
        } else {
            if ((set.iterator().next() instanceof String)) {
                Set<String> values = uncheckedCast(set);
                edit.putStringSet(key, values);
            } else {
                SHNLogger.wtf(TAG, "Set should be a set of Strings");
            }
        }
    }

    public <T> T get(@NonNull final String key, @Nullable T defaultValue) {
        synchronized (lock) {
            T value = get(key);
            return (value != null ? value : defaultValue);
        }
    }

    public <T> T get(@NonNull final String key) {
        synchronized (lock) {
            Object value = getAll().get(key);
            if (value == null) {
                return null;
            }

            if (contains(getListSizeKey(key))) {
                value = getList(key);
            } else if (contains(key + DOUBLE_VALUE)) {
                Long longValue = (Long) value;
                value = Double.longBitsToDouble(longValue);
            } else if (contains(key + SHORT_VALUE)) {
                Integer integer = (Integer) value;
                value = integer.shortValue();
            } else if (contains(key + ENUM_NAME)) {
                try {
                    String className = get(key + ENUM_NAME);
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isEnum()) {
                        Object[] enumConstants = clazz.getEnumConstants();
                        value = enumConstants[(Integer) value];
                    }
                } catch (ClassNotFoundException e) {
                    SHNLogger.wtf(TAG, "Could not instantiate " + get(key + ENUM_NAME), e);
                }
            }
            return uncheckedCast(value);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T uncheckedCast(final Object value) {
        try {
            return (T) value;
        } catch (ClassCastException e) {
            SHNLogger.wtf(TAG, "uncheckedCast " + value, e);
        }
        return null;
    }

    @NonNull
    private String getListEntryKey(String baseKey, int i) {
        return baseKey + LIST_BASE + i;
    }

    @NonNull
    private String getListSizeKey(String baseKey) {
        return baseKey + LIST_BASE + SIZE;
    }

    private <T> void putList(String key, Editor edit, List<T> values) {
        removeList(key, edit);
        edit.remove(key);

        if (values == null) {
            return;
        }

        put(getListSizeKey(key), edit, values.size());
        for (int i = 0; i < values.size(); i++) {
            put(getListEntryKey(key, i), edit, values.get(i));
        }
        put(key, edit, LIST_BASE);
        edit.commit();
    }

    private void removeList(String key, Editor edit) {
        String listSizeKey = getListSizeKey(key);
        if (contains(listSizeKey)) {
            int listSize = get(listSizeKey);

            for (int i = 0; i < listSize; i++) {
                edit.remove(getListEntryKey(key, i));
            }
            edit.remove(listSizeKey);
        }
    }

    private <T> List<T> getList(final String key) {
        String listSizeKey = getListSizeKey(key);
        if (!contains(listSizeKey)) {
            return null;
        }

        int listSize = get(listSizeKey);
        List<T> storedList = new ArrayList<>();

        for (int i = 0; i < listSize; i++) {
            T value = get(getListEntryKey(key, i));
            storedList.add(value);
        }

        return storedList;
    }

    public void clear() {
        synchronized (lock) {
            sharedPreferences.edit().clear().commit();
        }
    }

    // Pass through methods

    @Override
    public Map<String, ?> getAll() {
        synchronized (lock) {
            return sharedPreferences.getAll();
        }
    }

    @Nullable
    @Override
    public String getString(final String key, final String defValue) {
        synchronized (lock) {
            return sharedPreferences.getString(key, defValue);
        }
    }

    @Nullable
    @Override
    public Set<String> getStringSet(final String key, final Set<String> defValues) {
        synchronized (lock) {
            return sharedPreferences.getStringSet(key, defValues);
        }
    }

    @Override
    public int getInt(final String key, final int defValue) {
        synchronized (lock) {
            return sharedPreferences.getInt(key, defValue);
        }
    }

    @Override
    public long getLong(final String key, final long defValue) {
        synchronized (lock) {
            return sharedPreferences.getLong(key, defValue);
        }
    }

    @Override
    public float getFloat(final String key, final float defValue) {
        synchronized (lock) {
            return sharedPreferences.getFloat(key, defValue);
        }
    }

    @Override
    public boolean getBoolean(final String key, final boolean defValue) {
        synchronized (lock) {
            return sharedPreferences.getBoolean(key, defValue);
        }
    }

    @Override
    public boolean contains(final String key) {
        synchronized (lock) {
            return sharedPreferences.contains(key);
        }
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(
            final OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(
            final OnSharedPreferenceChangeListener listener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    @Deprecated
    public Editor edit() {
        throw new IllegalAccessError("For threading purposes usage of Editor is prohibited. Use generic put instead.");
    }
}
