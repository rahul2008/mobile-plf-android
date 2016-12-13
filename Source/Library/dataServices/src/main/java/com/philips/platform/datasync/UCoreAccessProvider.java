/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.datasync.userprofile.ErrorHandler;

import javax.inject.Singleton;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Singleton
public class UCoreAccessProvider implements BackendIdProvider {

    public static final String MOMENT_LAST_SYNC_URL_KEY = "MOMENT_LAST_SYNC_URL_KEY";
    public static final String INSIGHT_LAST_SYNC_URL_KEY = "INSIGHT_LAST_SYNC_URL_KEY";
    public static final String INSIGHT_FOR_USER_LAST_SYNC_URL_KEY = "INSIGHT_FOR_USER_LAST_SYNC_URL_KEY";

    SharedPreferences sharedPreferences;

    @NonNull
    private final ErrorHandler errorHandler;

    public UCoreAccessProvider(@NonNull final ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public boolean isLoggedIn() {
        if (errorHandler != null)
            return errorHandler.isUserLoggedIn();
        else
            return false;
    }

    public String getAccessToken() {
        if (errorHandler != null)
            return errorHandler.getAccessToken();
        else
            return null;
    }

    @Override
    public String getUserId() {
        if (errorHandler != null)
            return errorHandler.getUserProfile().getGUid();
        else
            return null;
    }

    @Override
    public void injectSaredPrefs(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public String getSubjectId() {
        if(errorHandler!=null) {
            return errorHandler.getUserProfile().getGUid();
        }else {
            return null;
        }
    }

    public String getMomentLastSyncTimestamp() {
        return sharedPreferences.getString(MOMENT_LAST_SYNC_URL_KEY, null);
    }

    public String getInsightLastSyncTimestamp() {
        return sharedPreferences.getString(INSIGHT_LAST_SYNC_URL_KEY, null);
    }

    public String getInsightLastSyncTimestampForUser() {
        return sharedPreferences.getString(INSIGHT_FOR_USER_LAST_SYNC_URL_KEY, null);
    }

    public void saveLastSyncTimeStamp(String lastSyncUrl, String key) {
        if (lastSyncUrl != null && !lastSyncUrl.isEmpty()) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            int indexOf = lastSyncUrl.indexOf('=');
            lastSyncUrl = lastSyncUrl.substring(indexOf + 1);
            SharedPreferences.Editor editor = edit.putString(key, lastSyncUrl);
            editor = edit.putBoolean("isSynced", true);
            editor.commit();
        }
    }
}
