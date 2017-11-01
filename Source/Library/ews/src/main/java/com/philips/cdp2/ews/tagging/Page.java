/*
 * Copyright (c) Mobiquityinc, 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.tagging;

public final class Page {

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
