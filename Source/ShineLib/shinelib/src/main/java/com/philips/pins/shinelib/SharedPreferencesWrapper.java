package com.philips.pins.shinelib;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Map;
import java.util.Set;

public class SharedPreferencesWrapper implements SharedPreferences {

    private static String TAG = SharedPreferencesProvider.class.getSimpleName();

    private static final int DELAY_MILLIS = 50;
    private SharedPreferences sharedPreferences;
    private Handler handler;
    private long internalThreadId;

    private Runnable timeOut = new Runnable() {
        @Override
        public void run() {
            SHNLogger.wtf(TAG, "The internal thread was not responding! Custom SharedPreference's execution time has exceeded expected!");

            assert (false);
        }
    };

    public SharedPreferencesWrapper(SharedPreferences sharedPreferences, Handler handler, long internalThreadId) {
        this.sharedPreferences = sharedPreferences;
        this.handler = handler;
        this.internalThreadId = internalThreadId;
    }

    @Override
    public Map<String, ?> getAll() {
        assertCorrectThread();

        handler.postDelayed(timeOut, DELAY_MILLIS);
        Map<String, ?> map = sharedPreferences.getAll();
        handler.removeCallbacks(timeOut);
        return map;
    }

    @Nullable
    @Override
    public String getString(String s, String s1) {
        assertCorrectThread();

        handler.postDelayed(timeOut, DELAY_MILLIS);
        String string = sharedPreferences.getString(s, s1);
        handler.removeCallbacks(timeOut);
        return string;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String s, Set<String> set) {
        assertCorrectThread();

        handler.postDelayed(timeOut, DELAY_MILLIS);
        Set<String> stringSet = sharedPreferences.getStringSet(s, set);
        handler.removeCallbacks(timeOut);
        return stringSet;
    }

    @Override
    public int getInt(String s, int i) {
        assertCorrectThread();

        handler.postDelayed(timeOut, DELAY_MILLIS);
        int anInt = sharedPreferences.getInt(s, i);
        handler.removeCallbacks(timeOut);
        return anInt;
    }

    @Override
    public long getLong(String s, long l) {
        assertCorrectThread();

        handler.postDelayed(timeOut, DELAY_MILLIS);
        long aLong = sharedPreferences.getLong(s, l);
        handler.removeCallbacks(timeOut);
        return aLong;
    }

    @Override
    public float getFloat(String s, float v) {
        assertCorrectThread();

        handler.postDelayed(timeOut, DELAY_MILLIS);
        float aFloat = sharedPreferences.getFloat(s, v);
        handler.removeCallbacks(timeOut);
        return aFloat;
    }

    @Override
    public boolean getBoolean(String s, boolean b) {
        assertCorrectThread();

        handler.postDelayed(timeOut, DELAY_MILLIS);
        boolean aBoolean = sharedPreferences.getBoolean(s, b);
        handler.removeCallbacks(timeOut);
        return aBoolean;
    }

    @Override
    public boolean contains(String s) {
        assertCorrectThread();

        handler.postDelayed(timeOut, DELAY_MILLIS);
        boolean contains = sharedPreferences.contains(s);
        handler.removeCallbacks(timeOut);
        return contains;
    }

    @Override
    public Editor edit() {
        assertCorrectThread();

        handler.postDelayed(timeOut, DELAY_MILLIS);
        Editor edit = sharedPreferences.edit();
        handler.removeCallbacks(timeOut);
        return edit;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        assertCorrectThread();

        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        assertCorrectThread();

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    private void assertCorrectThread() {
        if (Thread.currentThread().getId() != internalThreadId) {
            throw new RuntimeException("Thread violation, PersistentStorage should be accessed on the same thread as it was created on.");
        }
    }
}
