/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep.datamodels;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.philips.cdp2.commlib.core.port.PortProperties;

public class SessionInfoPortProperties implements PortProperties {

    private final String KEY_OLDEST_SESSIONS = "oldest";
    private final String KEY_NEWEST_SESSIONS = "newest";
    private final String KEY_IS_EMPTY = "isEmpty";

    @SerializedName(KEY_OLDEST_SESSIONS)
    private Long oldestSession;

    @SerializedName(KEY_NEWEST_SESSIONS)
    private Long newestSession;

    @SerializedName(KEY_IS_EMPTY)
    private Boolean isEmpty;

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
