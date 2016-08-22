/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class implementing a wrapper around the {@link android.content.SharedPreferences} interface.
 * <p/>
 * Use this class to store any information within BlueLib.
 */
public class PersistentStorage implements SharedPreferences {
    private static final String TAG = "PersistentStorage";

    private static final String SHORT_VALUE = "SHORT_VALUE";
    private static final String ENUM_NAME = "ENUM_NAME";
    private static final String DOUBLE_VALUE = "DOUBLE_VALUE";
    private static final String LIST_BASE = "_LIST_";
    private static final String SIZE = "SIZE";

    @NonNull
    private SharedPreferences sharedPreferences;

    public PersistentStorage(@NonNull final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    /**
     * Stores a value in SharedPreferences for the specified key.
     *
     * @param key   to use for storage
     * @param value to store
     * @param <T>   type of the value to store
     */
    @SuppressLint("CommitPrefEdits")
    public <T> void put(@NonNull final String key, @Nullable final T value) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (value == null) {
            edit.remove(key);
            edit.remove(key + SHORT_VALUE);
            edit.remove(key + ENUM_NAME);
            edit.remove(key + DOUBLE_VALUE);
            removeList(key, edit);
            edit.commit(); // Explicitly store value synchronously so it is always stored after the method call.
        } else {
            put(key, edit, value);
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

    /**
     * Retrieves a value from SharedPreferences for the specified key. When there is no value associated
     * with the key or the value stored is of a different type than {@link T}, then the provided
     * {@code defaultValue} will be returned.
     *
     * @param key          used for storage
     * @param defaultValue returned in case the key is not found
     * @param <T>          type of the return value
     * @return when the type matches and a value was stored the value previously stored else the {@code defaultValue}.
     */
    public <T> T get(@NonNull final String key, @Nullable T defaultValue) {
        T value = get(key);
        return (value != null ? value : defaultValue);
    }

    /**
     * Retrieves a value from SharedPreferences for the specified key. When there is no value associated
     * with the key or the value stored is of a different type than {@link T}, then {@code null} will be returned.
     *
     * @param key used for storage
     * @param <T> type of the return value
     * @return when the type matches and a value was stored the value previously stored else {@code null}.
     */
    public <T> T get(@NonNull final String key) {
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

    /**
     * Clear this storage.
     */
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    /**
     * Pass through method
     *
     * @see android.content.SharedPreferences#getAll()
     */
    @Override
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    /**
     * Pass through method
     *
     * @see android.content.SharedPreferences#getString(String, String)
     */
    @Nullable
    @Override
    public String getString(final String key, final String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * Pass through method
     *
     * @see android.content.SharedPreferences#getStringSet(String, Set)
     */
    @Nullable
    @Override
    public Set<String> getStringSet(final String key, final Set<String> defValues) {
        return sharedPreferences.getStringSet(key, defValues);
    }

    /**
     * Pass through method
     *
     * @see android.content.SharedPreferences#getInt(String, int)
     */
    @Override
    public int getInt(final String key, final int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * Pass through method
     *
     * @see android.content.SharedPreferences#getLong(String, long)
     */
    @Override
    public long getLong(final String key, final long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    /**
     * Pass through method
     *
     * @see android.content.SharedPreferences#getFloat(String, float)
     */
    @Override
    public float getFloat(final String key, final float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    /**
     * Pass through method
     *
     * @see android.content.SharedPreferences#getBoolean(String, boolean)
     */
    @Override
    public boolean getBoolean(final String key, final boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * Pass through method
     *
     * @see android.content.SharedPreferences#contains(String)
     */
    @Override
    public boolean contains(final String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * Pass through method
     *
     * @see android.content.SharedPreferences#registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener)
     */
    @Override
    public void registerOnSharedPreferenceChangeListener(
            final OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Pass through method
     *
     * @see android.content.SharedPreferences#unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener)
     */
    @Override
    public void unregisterOnSharedPreferenceChangeListener(
            final OnSharedPreferenceChangeListener listener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Throws IllegalAccessError. For threading purposes usage of Editor is prohibited. Use generic put instead.
     */
    @Override
    @Deprecated
    public Editor edit() {
        throw new IllegalAccessError("For threading purposes usage of Editor is prohibited. Use generic put instead.");
    }
}
