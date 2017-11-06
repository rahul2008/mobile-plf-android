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

        public static final String SEND_DATA = "sendData";
        public static final String SPECIAL_EVENTS = "specialEvents";
        public static final String COMPONENT_VERSION = "componentVersion";
        public static final String NOTIFICATION = "inAppNotificationResponse";

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

        public static final String GET_STARTED = "getStartedToConnectWiFi";
        public static final String CONFIRM_NETWORK = "connectToExistingNetwork";
        public static final String CHANGE_NETWORK = "changeNetworkToConnect";
        public static final String HAVING_ISSUES_CONNECTING = "havingIssuesConnecting";
        public static final String USER_NEEDS_HELP = "helpMeEnablingSetupMode";
        public static final String WIFI_BLINKING = "wifiBlinking";
        public static final String WIFI_NOT_BLINKING = "wifiNotBlinking";

        public static final String YES = "yes";
        public static final String NO = "no";
        public static final String DONE = "done";
        public static final String CLOSE = "close";

        public static final String CONNECTION_UNSUCCESSFUL = "connectionUnsuccessful";
        public static final String SEND_DATA = "sendData";
        public static final String CONNECTION_SUCCESS = "successConnection";
        public static final String CONNECTION_START = "startConnection";
        public static final String TIME_TO_CONNECT = "timeToConnect";
    }

    public static class ERROR {
        public static final String DEVICE_PORT_ERROR = "EWS:Network:AWSDK:devicePortError";
        public static final String WIFI_PORT_ERROR = "EWS:Network:AWSDK:wifiPortError";

    }
}
