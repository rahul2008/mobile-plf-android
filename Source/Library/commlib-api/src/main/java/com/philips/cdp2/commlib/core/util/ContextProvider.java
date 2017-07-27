/*
 * Copyright (c) Koninklijke Philips N.V. 2017
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

/**
 * The type ContextProvider.
 * <p>
 * <p>This class provides an Android Context in a static way.
 *
 * @see <a href=https://firebase.googleblog.com/2016/12/how-does-firebase-initialize-on-android.html>How does Firebase initialize on Android?</a></p>
 */
public class ContextProvider extends ContentProvider {

    private static ContextProvider INSTANCE;

    public ContextProvider() {
        INSTANCE = this;
    }

    public static Context get() {
        return INSTANCE.getContext();
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
