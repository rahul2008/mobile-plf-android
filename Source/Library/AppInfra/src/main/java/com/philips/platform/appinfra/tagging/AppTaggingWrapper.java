/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;

import com.philips.platform.appinfra.AppInfra;

/**
 * Created by 310238655 on 4/27/2016.
 */
public class AppTaggingWrapper extends AppTagging {

//    private static String prevPage;
//
//    public String trackingIdentifier;
//    private String componentVersionVersionValue;

    public AppTaggingWrapper(AppInfra aAppInfra, String componentId, String componentVersion) {
        super(aAppInfra);
        mComponentID = componentId;
        mComponentVersion = componentVersion;
//        AppTagging.init(Locale.getDefault(), aAppInfra.getAppInfraContext(), "AppFramework");
    }



//    public static void trackPage(String currPage, String key, String value) {
//        if (null != prevPage) {
//            AppTagging.trackPage(currPage, prevPage, key, value);
//        } else {
//            AppTagging.trackPage(currPage, null, key,value);
//        }
//        prevPage = currPage;
//
//
//    }

//    public static void trackMultiplePages(String currPage, Map<String, Object> map) {
//            AppTagging.trackMultipleState(currPage, map);
//    }

//    public static void trackMultipleActions(String state, Map<String, Object> map) {
//        AppTagging.trackMultipleActions(state, map);
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
//        AppTagging.setPrivacyStatus(privacyStatus);
//    }
//    public static void getPrivacyStatus(){
//        AppTagging.getPrivacyStatus();
//    }

//    public static void Initialize(Locale local, Context context,String appName ) {
//
//    AppTagging.init(local, context, appName);
//
//    }


//    public static void enableAppTagging(Boolean val) {
//
//        AppTagging.enableAppTagging(val);
//
//    }
//    public static void setLaunchingPageName(String name) {
//
//        AppTagging.setLaunchingPageName(name);
//
//    }
//    public static void pauseCollectingLifecycleData() {
//
//        AppTagging.pauseCollectingLifecycleData();
//
//    }
//    public static void collectLifecycleData() {
//
//        AppTagging.collectLifecycleData();
//
//    }


//    public static void trackFirstPage(String currPage) {
//        if (null != AppTagging.getLaunchingPageName()) {
//            AppTagging.trackPage(currPage, AppTagging.getLaunchingPageName(), "", "");
//        } else {
//            AppTagging.trackPage(currPage, null, "", "");
//        }
//        prevPage = currPage;
//    }

//    public static void trackAction(String state, String key, Object value) {
//        AppTagging.trackAction(state, key, value);
//    }


//    public static void setComponentId(String state) {
//        AppTagging.setComponentID(state);
//    }
//    public static void setTrackingIdentifier(String state) {
//        AppTagging.setTrackingIdentifier(state);
//    }
//    public static void setComponentVersionVersionValue(String state) {
//        AppTagging.setComponentVersionVersionValue(state);
//    }
}
