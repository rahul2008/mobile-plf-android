/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.app.tagging;

import android.app.Activity;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;


public class AppTagging {

    private static AppTaggingInterface appTaggingInterface = RegistrationConfiguration.getInstance().getComponent().getAppTaggingInterface();

    public static void trackPage(String currPage , User user) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        appTaggingInterface.trackPageWithInfo(currPage, commonGoalsMap);

        if(isHsdPIDExist(user)){
            trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.KEY_HSDP_ID,
                    user.getHsdpUUID());
        }
    }

    private static boolean isHsdPIDExist(User user){
        return user!=null && user.isUserSignIn() && user.getHsdpUUID()!=null ;
    }
    public static void trackFirstPage(String currPage) {
        trackPage(currPage,null);
    }
    public static void trackAction(String state, String key, String value) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.put(key, value);
        appTaggingInterface.trackActionWithInfo(state, commonGoalsMap);
    }

    public static void trackMultipleActions(String state, Map<String, String> map) {
        final Map<String, String> commonGoalsMap = getCommonGoalsMap();
        commonGoalsMap.putAll(map);
        appTaggingInterface.trackActionWithInfo(state, map);
    }

    private static Map<String, String> getCommonGoalsMap() {
        return new HashMap<>();
    }

    public static void pauseCollectingLifecycleData() {
        appTaggingInterface.pauseLifecycleInfo();
    }

    public static void collectLifecycleData(Activity activity) {
        appTaggingInterface.collectLifecycleInfo(activity);
    }


}
