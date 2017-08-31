/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.lang.ref.WeakReference;

/**
 * The type ContextProvider.
 * <p>
 * <p>This class provides an Android Context in a static way.
 *
 * @see <a href=https://firebase.googleblog.com/2016/12/how-does-firebase-initialize-on-android.html>How does Firebase initialize on Android?</a></p>
 */
public class ContextProvider extends ContentProvider {

    private static ContextProvider INSTANCE;
    @Nullable
    private static WeakReference<Context> TESTING_CONTEXT;

    public ContextProvider() {
        INSTANCE = this;
    }

    public static Context get() {
        if (TESTING_CONTEXT != null) {
            Context testContext = TESTING_CONTEXT.get();
            if (testContext != null) {
                return testContext;
            }
        }
        return INSTANCE.getContext();
    }

    @VisibleForTesting
    public static void setTestingContext(@NonNull final Context context) {
        TESTING_CONTEXT = new WeakReference<>(context);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
