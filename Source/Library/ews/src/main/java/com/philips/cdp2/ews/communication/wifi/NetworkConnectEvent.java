/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.communication.wifi;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.annotations.NetworkType;

@Keep
public class NetworkConnectEvent {

    private final String networkSSID;

    @NetworkType
    private int networkType;

    public NetworkConnectEvent(@NetworkType final int networkType, @NonNull final String networkSSID) {
        this.networkType = networkType;
        this.networkSSID = networkSSID;
    }

    @NetworkType
    public int getNetworkType() {
        return networkType;
    }

    @NonNull
    public String getNetworkSSID() {
        return networkSSID;
    }
}
