/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import java.util.Locale;

public class DiscoverInfo {

    private String State = null;
    private String[] ClientIds = null;

    private static final String CONNECTED = "connected";
    private static final String DISCONNECTED = "disconnected";

    public DiscoverInfo() {
        // NOP
    }

    public DiscoverInfo(String state) {
        State = state;
        ClientIds = new String[0];
    }

    public String[] getClientIds() {
        return ClientIds;
    }

    public boolean isConnected() {
        if (State.toLowerCase(Locale.US).equals(CONNECTED)) return true;
        return false;
    }

    public boolean isValid() {
        if (State == null || State.isEmpty()) return false;
        if (!State.toLowerCase(Locale.US).equals(CONNECTED) && !State.toLowerCase(Locale.US).equals(DISCONNECTED))
            return false;
        if (ClientIds == null || ClientIds.length <= 0) return false;
        return true;
    }

    public static DiscoverInfo getMarkAllOnlineDiscoverInfo() {
        // No devices offline, will cause all to go online
        return new DiscoverInfo(DISCONNECTED);
    }

    public static DiscoverInfo getMarkAllOfflineDiscoverInfo() {
        // No devices online will cause all to go offline
        return new DiscoverInfo(CONNECTED);
    }

}
