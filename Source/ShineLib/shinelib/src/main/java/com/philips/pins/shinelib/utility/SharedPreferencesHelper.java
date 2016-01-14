package com.philips.pins.shinelib.utility;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

public class SharedPreferencesHelper implements SharedPreferences {

    private static final String SHORT_VALUE = "SHORT_VALUE";

    @NonNull
    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public <T> void assureCorrectPersistence(
            @NonNull final String TAG,
            @NonNull final String key,
            @NonNull final T value) {
        if (!contains(key)) {
            put(key, value);
        } else {
            T local = get(key);
            if (local != value) {
                SHNLogger.wtf(TAG,
                        "IConfigEvents returned " + value + " for " + key + ", but locally stored is " + local);
            }
        }
    }

    public <T> void put(final String key, final T value) {
        SharedPreferences.Editor edit = edit();

        if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Short) {
            edit.putInt(key, (Short) value);
            edit.putBoolean(key + SHORT_VALUE, true);
        }

        edit.apply();
    }

    public <T> boolean putIfValueChanged(final String key, final T value) {
        T oldValue = get(key);
        boolean valueChanged = oldValue != value;

        if (valueChanged) {
            put(key, value);
        }

        return valueChanged;
    }

    public <T> T get(final String key) {
        boolean isShort = contains(key + SHORT_VALUE);

        Object o = getAll().get(key);
        if (isShort) {
            Integer integer = (Integer) o;
            o = integer.shortValue();
        }

        return (T) o;
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
