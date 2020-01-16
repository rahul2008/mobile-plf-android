package com.philips.cdp.di.mec.analytics

import android.app.Activity
import android.util.Log
import com.philips.platform.appinfra.tagging.AppTaggingInterface
import com.philips.cdp.di.mec.integration.MECDependencies
import com.philips.platform.appinfra.BuildConfig


class MECAnalytics {

    companion object {

        var mAppTaggingInterface: AppTaggingInterface? = null
        var previousPageName = "uniquePageName";

        @JvmStatic
        fun initMECAnalytics(dependencies: MECDependencies) {
            mAppTaggingInterface = dependencies.appInfra.tagging.createInstanceForComponent(MECAnalyticsConstant.COMPONENT_NAME, BuildConfig.VERSION_NAME)
        }

        @JvmStatic
        fun trackPage(currentPage: String) {
            if (mAppTaggingInterface != null && currentPage != null) {
                val map = HashMap<String, String>()
                if (currentPage != previousPageName) {
                    previousPageName = currentPage
                    Log.v("MEC_LOG", "trackPage" + currentPage);
                    mAppTaggingInterface!!.trackPageWithInfo(currentPage, map)
                }
            }
        }


        @JvmStatic
        fun trackAction(state: String, key: String, value: Any) {
            val valueObject = value as String
            Log.v("MEC_LOG", "trackAction" + valueObject);
            if (mAppTaggingInterface != null)
                mAppTaggingInterface!!.trackActionWithInfo(state, key, valueObject)
        }

        @JvmStatic
        fun trackMultipleActions(state: String, map: Map<String, String>) {
            if (mAppTaggingInterface != null)
                mAppTaggingInterface!!.trackActionWithInfo(state, map)
        }


        @JvmStatic
        fun pauseCollectingLifecycleData() {
            if (mAppTaggingInterface != null)
                mAppTaggingInterface!!.pauseLifecycleInfo()
        }


        @JvmStatic
        fun collectLifecycleData(activity: Activity) {
            if (mAppTaggingInterface != null)
                mAppTaggingInterface!!.collectLifecycleInfo(activity)
        }
    }

}