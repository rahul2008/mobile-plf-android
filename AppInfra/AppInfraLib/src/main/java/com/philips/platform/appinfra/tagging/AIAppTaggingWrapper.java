package com.philips.platform.appinfra.tagging;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;

import java.util.Locale;
import java.util.Map;

/**
 * Created by 310238655 on 4/27/2016.
 */
public class AIAppTaggingWrapper extends AIAppTagging{

    private static String prevPage;

    public String trackingIdentifier;
    private String componentVersionVersionValue;

    public AIAppTaggingWrapper(AppInfra aAppInfra, String componentId, String componentVersion) {
        super(aAppInfra);
        mComponentID = componentId;
        mComponentVersion = componentVersion;
        AIAppTagging.init(Locale.getDefault(), aAppInfra.getAppInfraContext(), "AppFramework");
    }



//    public static void trackPage(String currPage, String key, String value) {
//        if (null != prevPage) {
//            AIAppTagging.trackPage(currPage, prevPage, key, value);
//        } else {
//            AIAppTagging.trackPage(currPage, null, key,value);
//        }
//        prevPage = currPage;
//
//
//    }

//    public static void trackMultiplePages(String currPage, Map<String, Object> map) {
//            AIAppTagging.trackMultipleState(currPage, map);
//    }

//    public static void trackMultipleActions(String state, Map<String, Object> map) {
//        AIAppTagging.trackMultipleActions(state, map);
//    }
//    public enum AIATMobilePrivacyStatus {
//        MOBILE_PRIVACY_STATUS_OPT_IN(0),
//        MOBILE_PRIVACY_STATUS_OPT_OUT(1),
//        MOBILE_PRIVACY_STATUS_UNKNOWN(2);
//
//        private final int value;
//
//        private AIATMobilePrivacyStatus(int value) {
//            this.value = value;
//        }
//
//        protected int getValue() {
//            return this.value;
//        }
//    }

//    public static void setPrivacyStatus(AIATMobilePrivacyStatus privacyStatus){
//        AIAppTagging.setPrivacyStatus(privacyStatus);
//    }
//    public static void getPrivacyStatus(){
//        AIAppTagging.getPrivacyStatus();
//    }

//    public static void Initialize(Locale local, Context context,String appName ) {
//
//    AIAppTagging.init(local, context, appName);
//
//    }


//    public static void enableAppTagging(Boolean val) {
//
//        AIAppTagging.enableAppTagging(val);
//
//    }
//    public static void setLaunchingPageName(String name) {
//
//        AIAppTagging.setLaunchingPageName(name);
//
//    }
//    public static void pauseCollectingLifecycleData() {
//
//        AIAppTagging.pauseCollectingLifecycleData();
//
//    }
//    public static void collectLifecycleData() {
//
//        AIAppTagging.collectLifecycleData();
//
//    }


//    public static void trackFirstPage(String currPage) {
//        if (null != AIAppTagging.getLaunchingPageName()) {
//            AIAppTagging.trackPage(currPage, AIAppTagging.getLaunchingPageName(), "", "");
//        } else {
//            AIAppTagging.trackPage(currPage, null, "", "");
//        }
//        prevPage = currPage;
//    }

//    public static void trackAction(String state, String key, Object value) {
//        AIAppTagging.trackAction(state, key, value);
//    }


//    public static void setComponentId(String state) {
//        AIAppTagging.setComponentID(state);
//    }
//    public static void setTrackingIdentifier(String state) {
//        AIAppTagging.setTrackingIdentifier(state);
//    }
//    public static void setComponentVersionVersionValue(String state) {
//        AIAppTagging.setComponentVersionVersionValue(state);
//    }
}
