/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.tagging;

public class Tag {

    public static class KEY {
        public static final String IN_APP_NOTIFICATION = "inAppNotification";
        public static final String CONNECTED_PRODUCT_NAME = "connectedProductName";
        public static final String TECHNICAL_ERROR = "technicalError";
        public static final String USER_ERROR = "userError";
        public static final String MACHINE_ID = "machineId";
        public static final String PRODUCT_NAME = "connectedProductName";
        public static final String PRODUCT_MODEL = "productModel";
    }

    public static class VALUE {
        public static final String CONN_ERROR_NOTIFICATION = "Connection unsuccessful:Cannot connect to device's WiFi signal";
        public static final String WIFI_SINGLE_ERROR = "Cannot connect to device's WiFi signal";
        public static final String WRONG_PASSWORD_NOTIFICATION = "Connection unsuccessful:Wrong password or device out of range";
        public static final String WRONG_PASSWORD_ERROR = "Wrong password or device out of range";
        public static final String WIFI_ROUTER_ERROR = "unable to connect to device to their home network";
        public static final String WRONG_WIFI = "Device connected to WiFi but phone has re-connected to a different network";
    }

    public static class ACTION {
        public static final String CONNECTION_UNSUCCESSFUL = "connectionUnsuccessful";
        public static final String SEND_DATA = "sendData";
        public static final String CONNECTION_SUCCESS = "successConnection";
        public static final String CONNECTION_START = "startConnection";
        public static final String TIME_TO_CONNECT = "timeToConnect";
    }
}
