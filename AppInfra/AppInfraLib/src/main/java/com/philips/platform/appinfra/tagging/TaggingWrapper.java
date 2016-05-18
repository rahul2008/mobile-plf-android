package com.philips.platform.appinfra.tagging;

import android.content.Context;

import java.util.Locale;
import java.util.Map;

/**
 * Created by 310238655 on 4/27/2016.
 */
public class TaggingWrapper {

    private static String prevPage;

    public String trackingIdentifier;
    private String componentVersionVersionValue;

    public static void trackPage(String currPage, String key, String value) {
        if (null != prevPage) {
            Tagging.trackPage(currPage, prevPage, key, value);
        } else {
            Tagging.trackPage(currPage, null, key,value);
        }
        prevPage = currPage;


    }

    public static void trackMultiplePages(String currPage, Map<String, Object> map) {
            Tagging.trackMultipleState(currPage, map);
    }

    public static void trackMultipleActions(String state, Map<String, Object> map) {
        Tagging.trackMultipleActions(state, map);
    }
    public enum AIATMobilePrivacyStatus {
        MOBILE_PRIVACY_STATUS_OPT_IN(0),
        MOBILE_PRIVACY_STATUS_OPT_OUT(1),
        MOBILE_PRIVACY_STATUS_UNKNOWN(2);

        private final int value;

        private AIATMobilePrivacyStatus(int value) {
            this.value = value;
        }

        protected int getValue() {
            return this.value;
        }
    }

    public static void setPrivacyStatus(AIATMobilePrivacyStatus privacyStatus){
        Tagging.setPrivacyStatus(privacyStatus);
    }
    public static void getPrivacyStatus(){
        Tagging.getPrivacyStatus();
    }

    public static void Initialize(Locale local, Context context,String appName ) {

    Tagging.init(local, context, appName);

    }
    public static void enableAppTagging(Boolean val) {

        Tagging.enableAppTagging(val);

    }
    public static void setLaunchingPageName(String name) {

        Tagging.setLaunchingPageName(name);

    }
    public static void pauseCollectingLifecycleData() {

        Tagging.pauseCollectingLifecycleData();

    }
    public static void collectLifecycleData() {

        Tagging.collectLifecycleData();

    }


    public static void trackFirstPage(String currPage) {
        if (null != Tagging.getLaunchingPageName()) {
            Tagging.trackPage(currPage, Tagging.getLaunchingPageName(), "", "");
        } else {
            Tagging.trackPage(currPage, null, "", "");
        }
        prevPage = currPage;
    }

    public static void trackAction(String state, String key, Object value) {
        Tagging.trackAction(state, key, value);
    }


    public static void setComponentId(String state) {
        Tagging.setComponentID(state);
    }
    public static void setTrackingIdentifier(String state) {
        Tagging.setTrackingIdentifier(state);
    }
    public static void setComponentVersionVersionValue(String state) {
        Tagging.setComponentVersionVersionValue(state);
    }
}
