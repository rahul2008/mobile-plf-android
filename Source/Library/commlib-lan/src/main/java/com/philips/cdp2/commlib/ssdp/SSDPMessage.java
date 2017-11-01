/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import android.support.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
class SSDPMessage {

    static final String SEPARATOR = ": ";
    static final String NEWLINE = "\r\n";

    static final String SSDP_HOST = "239.255.255.250";
    static final int SSDP_PORT = 1900;

    private static final int TYPE_SEARCH = 0;
    private static final int TYPE_NOTIFY = 1;
    private static final int TYPE_FOUND = 2;

    static final String MESSAGE_TYPE_SEARCH = "M-SEARCH * HTTP/1.1";
    static final String MESSAGE_TYPE_NOTIFY = "NOTIFY * HTTP/1.1";
    static final String MESSAGE_TYPE_FOUND = "HTTP/1.1 200 OK";

    private static final String[] MESSAGE_TYPES = {
            MESSAGE_TYPE_SEARCH,
            MESSAGE_TYPE_NOTIFY,
            MESSAGE_TYPE_FOUND
    };

    static final String USN = "USN";
    static final String CACHE_CONTROL = "CACHE-CONTROL";
    static final String HOST = "HOST";
    static final String LOCATION = "LOCATION";
    static final String NAMESPACE = "MAN";
    static final String MAX_WAIT_TIME = "MX";
    static final String SEARCH_TARGET = "ST";
    static final String SERVER = "SERVER";
    static final String NOTIFICATION_TYPE = "NT";
    static final String NOTIFICATION_SUBTYPE = "NTS";
    static final String USER_AGENT = "USER-AGENT";

    static final String NAMESPACE_DISCOVER = "\"ssdp:discover\"";

    static final String NOTIFICATION_SUBTYPE_ALIVE = "ssdp:alive";
    static final String NOTIFICATION_SUBTYPE_BYEBYE = "ssdp:byebye";
    static final String NOTIFICATION_SUBTYPE_UPDATE = "ssdp:update";

    static final String SEARCH_TARGET_ALL = "ssdp:all";
    static final String SEARCH_TARGET_DICOMM = "urn:philips-com:device:DiProduct:1";
    static final String SEARCH_TARGET_UPNP_ROOTDEVICE = "upnp:rootdevice";
    static final String SEARCH_TARGET_URN = "urn:%s";
    static final String SEARCH_TARGET_UUID = "uuid:%s";

    private final int type;
    private Map<String, String> headers;

    SSDPMessage(@NonNull final String messageString) {
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

    synchronized Map<String, String> getHeaders() {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        return this.headers;
    }

    public String get(String key) {
        return getHeaders().get(key);
    }

    public String put(String key, String value) {
        return getHeaders().put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(MESSAGE_TYPES[this.type]).append(NEWLINE);

        for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
            builder.append(entry.getKey())
                    .append(SEPARATOR)
                    .append(entry.getValue())
                    .append(NEWLINE);
        }
        builder.append(NEWLINE);

        return builder.toString();
    }
}
