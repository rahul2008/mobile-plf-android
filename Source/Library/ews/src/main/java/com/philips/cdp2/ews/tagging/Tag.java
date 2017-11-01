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

        public static final String PRODUCT_NAME_SMARTSLEEP = "SmartSleep";
        public static final String PRODUCT_NAME_SOMNEO = "Philips Somneo";
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

    public static class PAGE{

        //old Tag - need to replace with new one
        public static final String WIFI_SETUP = "Get started:Connect wakeup light to home wifi";
        public static final String CONFIRM_WIFI = "Wifi setup:Confirm wifi connection";
        public static final String PLUGIN_DEVICE = "Wifi setup:Plugin device";
        public static final String PRESS_PLAY_AND_FOLLOW_SETUP = "Wifi setup:PressPlayAndFollowSetupScreen";
        public static final String CONNECT_WIFI = "Connect wakeup light to home wifi";
        public static final String WIFI_PAIRED = "Connection was successful";
        public static final String HOME_WIFI_OFF = "Wifi setup:Select your home Wifi network";
        public static final String INCORRECT_PASSWORD = "Connection Error:Wrong password or Wakeup light out of range";
        public static final String ROUTER_SETTINGS = "Troubleshooting:Check router settings";
        public static final String ROUTER_SETTINGS_WRONG_WIFI = "Troubleshooting:Check router settings, Wrong Wifi";
        public static final String WRONG_WIFI = "Your phone has reconnected to a different network";
        //    public static final String CONNECTION_UNSUCCESSFUL = "Connection Error";
//    public static final String RESET_DEVICE = "EWS Reset Device";
        public static final String CHOOSE_SETUP_STATE = "Choose EWS Setup State";
        public static final String BLINKING_ACCESS_POINT = "Blinking Access Point";

        //should use below Tag
        public static final String GET_STARTED = "getStarted";      //EWS_01_00
        public static final String CONFIRM_WIFI_NETWORK = "confirmWifiNetwork";     //EWS_01_01
        public static final String SELECT_HOME_WIFI = "selectHomeWifi";     //EWS_01_01b
        public static final String SETUP_STEP1 = "setupStep1";      //EWS_02_01
        public static final String SETUP_STEP2 = "setupStep2";      //EWS_02_02
        public static final String SETUP_STEP3 = "setupStep3";      //EWS_02_03
        public static final String CONNECTING_WITH_DEVICE = "connectingWithDevice";     //EWS_03_00
        public static final String PHONE_TO_DEVICE_CONNECTION_FAILED = "phoneToDeviceConnectionFailed";     //EWS_03_00.a
        public static final String CONNECT_WITH_PASSWORD = "connectWithPassword";       //EWS_03_01a
        public static final String CONNECT_TO_WRONG_PHONE = "connectToWrongPhone";      //EWS_H_03.01
        public static final String RESET_CONNECTION = "resetConnection";        //EWS_H_03.02
        public static final String RESET_DEVICE = "resetDevice";        //EWS_H_03.03
        public static final String SETUP_ACCESS_POINT_MODE = "setupAccessPointMode";        //EWS_H_03.04
        public static final String CONNECTING_DEVICE_WITH_WIFI = "connectingDeviceWithWifi";        //EWS_04_00
        public static final String WRONG_WIFI_NETWORK = "wrongWifiNetwork";     //EWS_04_00.a
        public static final String CONNECTION_UNSUCCESSFUL = "connectionUnsuccessful";      //EWS_04_00.b
        public static final String CONNECTION_SUCCESSFUL = "connectionSuccessful";      //EWS_04_01
        public static final String CANCEL_WIFI_SETUP = "cancelWifiSetup";       //EWS_04_02
    }
}
