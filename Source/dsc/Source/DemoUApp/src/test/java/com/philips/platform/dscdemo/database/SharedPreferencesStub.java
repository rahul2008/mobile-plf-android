package com.philips.platform.dscdemo.database;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

class SharedPreferencesStub implements SharedPreferences {
    @Override
    public Map<String, ?> getAll() {
        return null;
    }

    @Nullable
    @Override
    public String getString(final String s, @Nullable final String s1) {
        return null;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(final String s, @Nullable final Set<String> set) {
        return null;
    }

    @Override
    public int getInt(final String s, final int i) {
        return 0;
    }

    @Override
    public long getLong(final String s, final long l) {
        return 0;
    }

    @Override
    public float getFloat(final String s, final float v) {
        return 0;
    }

    @Override
    public boolean getBoolean(final String s, final boolean b) {
        return false;
    }

    @Override
    public boolean contains(final String s) {
        return false;
    }

    @Override
    public Editor edit() {
        return null;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {

    }
}
