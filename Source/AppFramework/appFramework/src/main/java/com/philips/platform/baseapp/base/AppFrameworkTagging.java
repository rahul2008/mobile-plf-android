/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 *
 */

package com.philips.platform.baseapp.base;

import android.app.Activity;
import android.content.Context;

import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

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

    private AppFrameworkTagging() {
    }

    public static AppFrameworkTagging getInstance() {
        if(appFrameworkTagging == null) {
            appFrameworkTagging = new AppFrameworkTagging();
        }
        return appFrameworkTagging;
    }

    public void initAppTaggingInterface(Context ctx) {
        if (appTaggingInterface == null) {
            appTaggingInterface = ((AppFrameworkApplication)ctx).getAppInfra().getTagging();
        }
    }

    public void trackPage(String currPage) {
        appTaggingInterface.trackPageWithInfo(currPage, null);
    }

    public void trackAction(String state) {
        appTaggingInterface.trackActionWithInfo(state, null);
    }

    public void pauseCollectingLifecycleData() {
        appTaggingInterface.pauseLifecycleInfo();
    }

    public void collectLifecycleData(Activity activity) {
        appTaggingInterface.collectLifecycleInfo(activity);
    }
}
