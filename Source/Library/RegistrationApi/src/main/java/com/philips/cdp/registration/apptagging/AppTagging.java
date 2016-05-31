
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.apptagging;

import com.philips.cdp.tagging.Tagging;

import java.util.Map;

public class AppTagging {
    private static String prevPage;

    public static void trackPage(String currPage) {
        if (null != prevPage) {
            Tagging.trackPage(currPage, prevPage);
        } else {
            Tagging.trackPage(currPage, null);
        }
        prevPage = currPage;
    }

    public static void trackFirstPage(String currPage) {
        if (null != Tagging.getLaunchingPageName()) {
            Tagging.trackPage(currPage, Tagging.getLaunchingPageName());
        } else {
            Tagging.trackPage(currPage, null);
        }
        prevPage = currPage;
    }

    public static void trackAction(String state, String key, Object value) {
        Tagging.trackAction(state, key, value);
    }

    public static void trackMultipleActions(String state, Map<String, Object> map) {
        Tagging.trackMultipleActions(state, map);
    }
}
