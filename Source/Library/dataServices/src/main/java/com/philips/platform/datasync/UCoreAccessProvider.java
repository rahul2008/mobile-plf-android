/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.joda.time.DateTime;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UCoreAccessProvider implements BackendIdProvider {

    public static final String MOMENT_LAST_SYNC_URL_KEY = "MOMENT_LAST_SYNC_URL_KEY";
    public static final String INSIGHT_LAST_SYNC_URL_KEY = "INSIGHT_LAST_SYNC_URL_KEY";

    private static final String START_DATE = "timestampStart";
    private static final String END_DATE = "timestampEnd";
    private static final String LAST_MODIFIED_START_DATE = "lastModifiedStart";
    private static final String LAST_MODIFIED_END_DATE = "lastModifiedEnd";

    @Inject
    SharedPreferences sharedPreferences;

    @NonNull
    protected UserRegistrationInterface userRegistrationInterface;

    @Inject
    public UCoreAccessProvider(@NonNull final UserRegistrationInterface userRegistrationInterface) {
        DataServicesManager.getInstance().getAppComponent().injectAccessProvider(this);
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
            edit.putString(key, lastSyncUrl);
            edit = edit.putBoolean("isSynced", true);
            edit.apply();
        }
    }

    public Map<String, String> getLastSyncTimeStampByDateRange(String syncUrl) {
        Map<String, String> timeStampMap = new HashMap<>();
        if (syncUrl != null && !syncUrl.isEmpty()) {
            int indexOf = syncUrl.indexOf('?');
            syncUrl = syncUrl.substring(indexOf + 1);
            String[] timestampPair = syncUrl.split("&");
            for (String timestamp : timestampPair) {
                int index = timestamp.indexOf("=");
                timeStampMap.put(timestamp.substring(0, index), timestamp.substring(index + 1));
            }
        }
        return timeStampMap;
    }

    public Map<String, String> getLastSyncTimeStampByDateRange(String startDate, String endDate) {
        Map<String, String> timeStampMap = new HashMap<>();
        try {
            timeStampMap.put(START_DATE, URLEncoder.encode(startDate, "UTF-8"));
            timeStampMap.put(END_DATE, URLEncoder.encode(endDate, "UTF-8"));
            timeStampMap.put(LAST_MODIFIED_START_DATE, URLEncoder.encode(startDate, "UTF-8"));
            timeStampMap.put(LAST_MODIFIED_END_DATE, URLEncoder.encode(new DateTime().toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //Debug log
        }
        return timeStampMap;
    }

}
