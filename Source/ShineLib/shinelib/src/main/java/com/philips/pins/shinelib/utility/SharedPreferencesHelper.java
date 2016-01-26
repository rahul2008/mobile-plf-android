package com.philips.pins.shinelib.utility;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesHelper implements SharedPreferences {
    private static final String TAG = "SharedPreferencesHelper";

    private static final String SHORT_VALUE = "SHORT_VALUE";
    public static final String ENUM_NAME = "ENUM_NAME";
    public static final String DOUBLE_VALUE = "DOUBLE_VALUE";
    public static final String LIST_VALUE = "LIST_VALUE";
    public static final String LIST_BASE = "_LIST_";
    public static final String SIZE = "SIZE";

    @NonNull
    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(@NonNull final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public <T> void put(final String key, final T value) {
        SharedPreferences.Editor edit = edit();
        put(key, edit, value);
    }

    public <T> void put(final String key, final Editor edit, final T value) {

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
            SHNLogger.e(TAG, "Trying to store an unsupported data type in Shared preferences!");
            assert false;
        }

        edit.apply();
    }

    private <T> void putStringSet(final String key, final Editor edit, final Set<?> set) {
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

    public <T> T get(final String key, T defaultValue) {
        T value = get(key);
        return (value != null ? value : defaultValue);
    }

    public <T> T get(final String key) {
        boolean isList = contains(getListSizeKey(key));
        if (isList) {
            return uncheckedCast(getList(key));
        }

        boolean isShort = contains(key + SHORT_VALUE);
        boolean isEnum = contains(key + ENUM_NAME);
        boolean isDouble = contains(key + DOUBLE_VALUE);

        Object value = getAll().get(key);
        if (isDouble) {
            Long longValue = (Long) value;
            value = Double.longBitsToDouble(longValue);
        } else if (isShort) {
            Integer integer = (Integer) value;
            value = integer.shortValue();
        } else if (isEnum) {
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
        edit.apply();
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
            T value = get( getListEntryKey(key, i) );
            storedList.add(value);
        }

        return storedList;
    }


    // Pass through methods

    @Override
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    @Nullable
    @Override
    public String getString(final String key, final String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(final String key, final Set<String> defValues) {
        return sharedPreferences.getStringSet(key, defValues);
    }

    @Override
    public int getInt(final String key, final int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    @Override
    public long getLong(final String key, final long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    @Override
    public float getFloat(final String key, final float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    @Override
    public boolean getBoolean(final String key, final boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    @Override
    public boolean contains(final String key) {
        return sharedPreferences.contains(key);
    }

    @Override
    public Editor edit() {
        return sharedPreferences.edit();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener listener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
