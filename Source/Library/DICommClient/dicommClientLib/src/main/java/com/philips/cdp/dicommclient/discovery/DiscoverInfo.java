/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

public class DiscoverInfo {

    private String State = null;
    private String[] ClientIds = null;

    public DiscoverInfo() {
        // NOP
    }

    public DiscoverInfo(String state) {
        State = state;
        ClientIds = new String[0];
    }

    public static DiscoverInfo getMarkAllOnlineDiscoverInfo() {
        // No devices offline, will cause all to go online
        return new DiscoverInfo("disconnected");
    }

    public static DiscoverInfo getMarkAllOfflineDiscoverInfo() {
        // No devices online will cause all to go offline
        return new DiscoverInfo("connected");
    }

    public String[] getClientIds() {
        return ClientIds;
    }

    public boolean isConnected() {
        return State.toLowerCase().equals("connected");
    }

    public boolean isValid() {
        if (State == null || State.isEmpty()) return false;
        if (!State.toLowerCase().equals("connected") && !State.toLowerCase().equals("disconnected"))
            return false;
        return !(ClientIds == null || ClientIds.length <= 0);
    }
}
