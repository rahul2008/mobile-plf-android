/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.philips.cdp2.commlib.core.port.PortProperties;

public class SessionInfoPortProperties implements PortProperties {

    private final String KEY_OLDEST_SESSIONS = "oldest";
    private final String KEY_NEWEST_SESSIONS = "newest";
    private final String KEY_IS_EMPTY = "isEmpty";

    @SerializedName(KEY_OLDEST_SESSIONS)
    @SuppressWarnings("WeakerAccess")
    Long oldestSession;

    @SerializedName(KEY_NEWEST_SESSIONS)
    @SuppressWarnings("WeakerAccess")
    Long newestSession;

    @SerializedName(KEY_IS_EMPTY)
    Boolean isEmpty;

    public SessionInfoPortProperties() {
        // Default constructor for Gson
    }

    @Nullable
    public Long getOldestSession() {
        return oldestSession;
    }

    @Nullable
    public Long getNewestSession() {
        return newestSession;
    }

    @Nullable
    public Boolean isEmpty() {
        return isEmpty;
    }
}
