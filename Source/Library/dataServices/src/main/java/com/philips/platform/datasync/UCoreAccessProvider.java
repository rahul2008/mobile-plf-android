/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UCoreAccessProvider implements BackendIdProvider {

    public static final String MOMENT_LAST_SYNC_URL_KEY = "MOMENT_LAST_SYNC_URL_KEY";
    public static final String INSIGHT_LAST_SYNC_URL_KEY = "INSIGHT_LAST_SYNC_URL_KEY";

    @Inject
    SharedPreferences sharedPreferences;

    @NonNull
    protected UserRegistrationInterface userRegistrationInterface;

    @Inject
    public UCoreAccessProvider(@NonNull final UserRegistrationInterface userRegistrationInterface) {
        DataServicesManager.getInstance().getAppComponant().injectAccessProvider(this);
        this.userRegistrationInterface = userRegistrationInterface;
    }

    public boolean isLoggedIn() {
        if (userRegistrationInterface != null)
            return userRegistrationInterface.isUserLoggedIn();
        else
            return false;
    }

    public String getAccessToken() {
        if (userRegistrationInterface != null)
            return userRegistrationInterface.getHSDPAccessToken();
        else
            return null;
    }

    @Override
    public String getUserId() {
        if (userRegistrationInterface != null)
            return userRegistrationInterface.getUserProfile().getGUid();
        else
            return null;
    }

    @Override
    public String getSubjectId() {
        if (userRegistrationInterface != null) {
            return userRegistrationInterface.getUserProfile().getGUid();
        } else {
            return null;
        }
    }

    public String getMomentLastSyncTimestamp() {
        return sharedPreferences.getString(MOMENT_LAST_SYNC_URL_KEY, null);
    }

    public String getInsightLastSyncTimestamp() {
        return sharedPreferences.getString(INSIGHT_LAST_SYNC_URL_KEY, null);
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
