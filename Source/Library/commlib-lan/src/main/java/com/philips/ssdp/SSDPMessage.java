/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.ssdp;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public final class SSDPMessage {

    private static final String NEWLINE = "\r\n";

    public static final String SSDP_HOST = "239.255.255.250";
    public static final int SSDP_PORT = 1900;

    private static final int TYPE_SEARCH = 0;
    private static final int TYPE_NOTIFY = 1;
    private static final int TYPE_FOUND = 2;

    public static final String MESSAGE_TYPE_SEARCH = "M-SEARCH * HTTP/1.1";
    public static final String MESSAGE_TYPE_NOTIFY = "NOTIFY * HTTP/1.1";
    public static final String MESSAGE_TYPE_FOUND = "HTTP/1.1 200 OK";

    private static final String[] MESSAGE_TYPES = {
            MESSAGE_TYPE_SEARCH,
            MESSAGE_TYPE_NOTIFY,
            MESSAGE_TYPE_FOUND
    };

    public static final String USN = "USN";
    public static final String CACHE_CONTROL = "CACHE-CONTROL";
    public static final String HOST = "HOST";
    public static final String LOCATION = "LOCATION";
    public static final String NAMESPACE = "MAN";
    public static final String MAX_WAIT_TIME = "MX";
    public static final String SEARCH_TARGET = "ST";
    public static final String SERVER = "SERVER";
    public static final String NOTIFICATION_TYPE = "NT";
    public static final String NOTIFICATION_SUBTYPE = "NTS";
    public static final String USER_AGENT = "USER-AGENT";

    public static final String BOOT_ID = "BOOTID.UPNP.ORG";
    public static final String CONFIG_ID = "CONFIGID.UPNP.ORG";
    public static final String NEXT_BOOT_ID = "NEXTBOOTID.UPNP.ORG";
    public static final String SEARCH_PORT = "SEARCHPORT.UPNP.ORG";

    public static final String NAMESPACE_DISCOVER = "\"ssdp:discover\"";

    public static final String NOTIFICATION_SUBTYPE_ALIVE = "ssdp:alive";
    public static final String NOTIFICATION_SUBTYPE_BYEBYE = "ssdp:byebye";
    public static final String NOTIFICATION_SUBTYPE_UPDATE = "ssdp:update";

    public static final String SEARCH_TARGET_ALL = "ssdp:all";
    public static final String SEARCH_TARGET_UPNP_ROOTDEVICE = "upnp:rootdevice";
    public static final String SEARCH_TARGET_URN = "urn:%s";
    public static final String SEARCH_TARGET_UUID = "uuid:%s";

    private final int type;
    private Map<String, String> headers;

    public SSDPMessage(@NonNull final String messageString) {
        final String lines[] = messageString.split(NEWLINE);
        String line = lines[0].trim();

        if (line.startsWith(MESSAGE_TYPE_SEARCH)) {
            this.type = TYPE_SEARCH;
        } else if (line.startsWith(MESSAGE_TYPE_NOTIFY)) {
            this.type = TYPE_NOTIFY;
        } else if (line.startsWith(MESSAGE_TYPE_FOUND)) {
            this.type = TYPE_FOUND;
        } else {
            throw new IllegalArgumentException("Invalid message type.");
        }

        for (int i = 1; i < lines.length; i++) {
            line = lines[i].trim();
            int separatorIndex = line.indexOf(':');

            if (separatorIndex > 0) {
                final String key = line.substring(0, separatorIndex).trim();
                final String value = line.substring(separatorIndex + 1).trim();

                getHeaders().put(key, value);
            }
        }
    }

    public synchronized Map<String, String> getHeaders() {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        return this.headers;
    }

    public int getType() {
        return this.type;
    }

    public String get(String key) {
        return getHeaders().get(key);
    }

    public String put(String key, String value) {
        return getHeaders().put(key, value);
    }

    public boolean isSearchTarget(String searchTarget) {
        return getHeaders().get(SEARCH_TARGET).equals(searchTarget);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(MESSAGE_TYPES[this.type]).append(NEWLINE);

        for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
            builder.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(NEWLINE);
        }
        builder.append(NEWLINE);

        return builder.toString();
    }
}
