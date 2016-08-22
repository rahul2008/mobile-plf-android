/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.content.SharedPreferences;

import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Set;

class TimeGuardedEditorWrapper implements SharedPreferences.Editor {

    private interface Putter<T, E> {
        E put(SharedPreferences.Editor editor, String key, T value);
    }

    private static String TAG = "EditorWrapper";

    private SharedPreferences.Editor editor;

    public TimeGuardedEditorWrapper(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    private <T, E> E putValue(String key, T value, Putter<T, E> putter) {
        long startTime = getCurrentTimeInMillis();
        E val = putter.put(editor, key, value);
        long dif = getCurrentTimeInMillis() - startTime;

        if (dif > TimeGuardedSharedPreferencesProviderWrapper.DELAY_MILLIS) {
            final String msg = "The internal thread is not responding! Custom SharedPreference's execution time has exceeded expected execution time of 50 ms! Execution time is " + dif;
            SHNLogger.wtf(TAG, msg);
            if (BuildConfig.DEBUG)
                throw new AssertionError(msg);
        }

        return val;
    }

    private <T, E> E putValue(Putter<T, E> putter) {
        return putValue("", null, putter);
    }

    protected long getCurrentTimeInMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public SharedPreferences.Editor putString(String key, String value) {
        return putValue(key, value, new Putter<String, SharedPreferences.Editor>() {
            @Override
            public SharedPreferences.Editor put(SharedPreferences.Editor editor, String key, String value) {
                editor.putString(key, value);
                return TimeGuardedEditorWrapper.this;
            }
        });
    }

    @Override
    public SharedPreferences.Editor putStringSet(String key, Set<String> set) {
        return putValue(key, set, new Putter<Set<String>, SharedPreferences.Editor>() {
            @Override
            public SharedPreferences.Editor put(SharedPreferences.Editor editor, String key, Set<String> value) {
                editor.putStringSet(key, value);
                return TimeGuardedEditorWrapper.this;
            }
        });
    }

    @Override
    public SharedPreferences.Editor putInt(String key, int value) {
        return putValue(key, value, new Putter<Integer, SharedPreferences.Editor>() {
            @Override
            public SharedPreferences.Editor put(SharedPreferences.Editor editor, String key, Integer value) {
                editor.putInt(key, value);
                return TimeGuardedEditorWrapper.this;
            }
        });
    }

    @Override
    public SharedPreferences.Editor putLong(String key, long value) {
        return putValue(key, value, new Putter<Long, SharedPreferences.Editor>() {
            @Override
            public SharedPreferences.Editor put(SharedPreferences.Editor editor, String key, Long value) {
                editor.putLong(key, value);
                return TimeGuardedEditorWrapper.this;
            }
        });
    }

    @Override
    public SharedPreferences.Editor putFloat(String key, float value) {
        return putValue(key, value, new Putter<Float, SharedPreferences.Editor>() {
            @Override
            public SharedPreferences.Editor put(SharedPreferences.Editor editor, String key, Float value) {
                editor.putFloat(key, value);
                return TimeGuardedEditorWrapper.this;
            }
        });
    }

    @Override
    public SharedPreferences.Editor putBoolean(String key, boolean value) {
        return putValue(key, value, new Putter<Boolean, SharedPreferences.Editor>() {
            @Override
            public SharedPreferences.Editor put(SharedPreferences.Editor editor, String key, Boolean value) {
                editor.putBoolean(key, value);
                return TimeGuardedEditorWrapper.this;
            }
        });
    }

    @Override
    public SharedPreferences.Editor remove(String key) {
        return putValue(key, null, new Putter<Boolean, SharedPreferences.Editor>() {
            @Override
            public SharedPreferences.Editor put(SharedPreferences.Editor editor, String key, Boolean value) {
                editor.remove(key);
                return TimeGuardedEditorWrapper.this;
            }
        });
    }

    @Override
    public SharedPreferences.Editor clear() {
        return putValue(new Putter<Boolean, SharedPreferences.Editor>() {
            @Override
            public SharedPreferences.Editor put(SharedPreferences.Editor editor, String key, Boolean value) {
                editor.clear();
                return TimeGuardedEditorWrapper.this;
            }
        });
    }

    @Override
    public boolean commit() {
        return putValue(new Putter<Void, Boolean>() {
            @Override
            public Boolean put(SharedPreferences.Editor editor, String key, Void value) {
                return editor.commit();
            }
        });
    }

    @Override
    public void apply() {
        putValue(new Putter<Void, Void>() {
            @Override
            public Void put(SharedPreferences.Editor editor, String key, Void value) {
                editor.apply();
                return null;
            }
        });
    }
}
