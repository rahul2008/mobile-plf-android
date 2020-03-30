/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.platform.mec.integration

import com.android.volley.DefaultRetryPolicy
import com.philips.cdp.di.ecs.ECSServices
import com.philips.platform.mec.utils.MECDataHolder
import com.philips.platform.mec.utils.MECLog
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.BuildConfig
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface
import com.philips.platform.pif.DataInterface.MEC.MECDataInterface
import com.philips.platform.pif.DataInterface.USR.UserDataInterface
import com.philips.platform.uappframework.UappInterface
import com.philips.platform.uappframework.launcher.UiLauncher
import com.philips.platform.uappframework.uappinput.UappDependencies
import com.philips.platform.uappframework.uappinput.UappLaunchInput
import com.philips.platform.uappframework.uappinput.UappSettings

/**
 * MECInterface is the public class for any proposition to consume MEC micro app. Its the starting initialization point.
 * @since 1.0.0
 */
 class MECInterface : UappInterface {
    private var mMECSettings: MECSettings?=null
    private var mUappDependencies: UappDependencies? = null
    private var mUserDataInterface: UserDataInterface? = null
    val MEC_NOTATION = "mec"


    /**
     * @param uappDependencies Object of UappDependencies
     * @param uappSettings     Object of UppSettings
     *   * @since 2001.1
     */
    override fun init(uappDependencies: UappDependencies, uappSettings: UappSettings) {
        val MECDependencies = uappDependencies as MECDependencies
        mUserDataInterface = MECDependencies.userDataInterface


        if (null == mUserDataInterface)
            throw RuntimeException("UserDataInterface is not injected in MECDependencies.")

        mMECSettings = uappSettings as MECSettings
        mUappDependencies = uappDependencies


        MECDataHolder.INSTANCE.appinfra = MECDependencies.appInfra

        //enable appInfra logging
        MECLog.isLoggingEnabled = true
        MECLog.appInfraLoggingInterface = MECDependencies.appInfra.logging.createInstanceForComponent(MEC_NOTATION, BuildConfig.VERSION_NAME)

        MECDataHolder.INSTANCE.userDataInterface = MECDependencies.userDataInterface
        val configError = AppConfigurationInterface.AppConfigurationError()
        val propositionID = MECDependencies.appInfra.configInterface.getPropertyForKey("propositionid", "MEC", configError)
        var propertyForKey = ""
        if (propositionID != null) {
            propertyForKey = propositionID as String
        }

        var voucher :Boolean = true // if voucher key is not mentioned Appconfig then by default it will be considered True
        try {
            voucher = MECDependencies.appInfra.configInterface.getPropertyForKey("voucherCode.enable", "MEC", configError) as Boolean
        }catch(e: Exception){

        }

        MECDataHolder.INSTANCE.propositionId = propertyForKey
        MECDataHolder.INSTANCE.voucherEnabled = voucher
        val ecsServices = ECSServices(propertyForKey, MECDependencies.appInfra as AppInfra)

        MECDataHolder.INSTANCE.eCSServices = ecsServices // singleton
        val defaultRetryPolicy = DefaultRetryPolicy( // 10 second time out
                30000,
                0,
                0f)
        MECDataHolder.INSTANCE.eCSServices.setVolleyTimeoutAndRetryCount(defaultRetryPolicy)


    }

    /**
     * @param uiLauncher      Object of UiLauncherxx
     * @param uappLaunchInput Object of  UappLaunchInput
     * @throws RuntimeException
     */
    @Throws(RuntimeException::class)
    override fun launch(uiLauncher: UiLauncher, uappLaunchInput: UappLaunchInput) {
        val mecHandler = this!!.mMECSettings?.let { MECHandler((mUappDependencies as MECDependencies?)!!, it, uiLauncher, uappLaunchInput as MECLaunchInput) }
        mecHandler?.launchMEC()
    }




    companion object {
        /**
         * Get the Singleton MEC Data Interface to call MEC public API
         *
         * @since 2002.0
         */

        @JvmStatic
        open fun getMECDataInterface(): MECDataInterface {

            return MECDataProvider()
        }
    }


}



