/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 *
 */

package com.philips.platform.baseapp.base;

import android.app.Activity;

import com.philips.platform.appinfra.tagging.AppTaggingInterface;

/*
 * This class is wrapper which is implementing all the possible Analytics related tracking part. It is considered that
 * Page/Action tagging can be taken care from any place of the source code. Necessary APIs to track page/action are
 * provided. This class requires to have application context because tagging can be required by any class. So once
 * it is initialized then same instance can be reutilized anytime anywhere in the codebase. We need to consider
 * background/foreground of activities as well.
 */
public class AppFrameworkTagging {
    private static AppFrameworkTagging appFrameworkTagging;
    private AppTaggingInterface appTaggingInterface;
    private AppFrameworkApplication context;
    private static String previousPage;

    private AppFrameworkTagging() {
    }

    /*
     * Single instance of Tagging is required.
     */
    public static AppFrameworkTagging getInstance() {
        if (appFrameworkTagging == null) {
            appFrameworkTagging = new AppFrameworkTagging();
        }
        return appFrameworkTagging;
    }

    /*
     * Initialize this method with context. Recommended from application level.
     */
    public void initAppTaggingInterface(AppFrameworkApplication ctx) {
        context = ctx;
//        appTaggingInterface = getTaggingInterface(context);
    }

    protected AppTaggingInterface getTaggingInterface(AppFrameworkApplication ctx) {
        return ctx.getAppInfra().getTagging();
    }

    public void trackPage(String currPage) {
        if (currPage == null || currPage.equalsIgnoreCase(previousPage)) {
            return;
        }
        previousPage=currPage;
        if (getTaggingInterface(context) != null) {
            getTaggingInterface(context).trackPageWithInfo(currPage, null);
        }
    }

    public void trackAction(String currPage) {
        if (currPage == null) {
            return;
        }
        getTaggingInterface(context).trackActionWithInfo(currPage, null);
    }

    public void pauseCollectingLifecycleData() {
        getTaggingInterface(context).pauseLifecycleInfo();
    }

    public void collectLifecycleData(Activity activity) {
        if (activity == null) {
            return;
        }
        getTaggingInterface(context).collectLifecycleInfo(activity);
    }
}
