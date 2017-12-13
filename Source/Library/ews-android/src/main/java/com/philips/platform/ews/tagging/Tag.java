/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.tagging;

public class Tag {

    public static class KEY {
        public static final String IN_APP_NOTIFICATION = "inAppNotification";
        public static final String IN_APP_NOTIFICATION_RESPONSE = "inAppNotificationResponse";
        public static final String CONNECTED_PRODUCT_NAME = "connectedProductName";
        public static final String TECHNICAL_ERROR = "technicalError";
        public static final String PRODUCT_NAME = "connectedProductName";

        public static final String SEND_DATA = "sendData";
        public static final String SPECIAL_EVENTS = "specialEvents";

    }

    public static class VALUE {
        public static final String CONN_ERROR_NOTIFICATION = "Connection unsuccessful:Cannot connect to device's WiFi signal";
        public static final String LOCATION_PERMISSION_NOTIFICATION = "Location Permission";
        public static final String LOCATION_DISABLED_NOTIFICATION = "Location Disabled";

    }

    public static class ACTION {

        public static final String GET_STARTED = "getStartedToConnectWiFi";
        public static final String CONFIRM_NETWORK = "connectToExistingNetwork";
        public static final String CHANGE_NETWORK = "changeNetworkToConnect";
        public static final String USER_NEEDS_HELP = "helpMeEnablingSetupMode";
        public static final String WIFI_BLINKING = "wifiBlinking";
        public static final String WIFI_NOT_BLINKING = "wifiNotBlinking";

        public static final String CONNECTION_UNSUCCESSFUL = "connectionUnsuccessful";
        public static final String CONNECTION_START = "startConnection";
        public static final String TIME_TO_CONNECT = "timeToConnect";

        public static final String ALLOW = "Allow";
        public static final String CANCEL_SETUP = "Cancel setup";
        public static final String OPEN_LOCATION_SETTINGS = "openLocationSettings";
    }

    public static class ERROR {
        public static final String WIFI_PORT_ERROR = "EWS:Network:AWSDK:wifiPortError";

    }
}
