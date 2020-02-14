package com.philips.cdp.di.mec.integration

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData
import com.philips.cdp.di.mec.auth.HybrisAuth
import com.philips.cdp.di.mec.integration.serviceDiscovery.MECManager
import com.philips.cdp.di.mec.screens.catalog.ECSCatalogRepository
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface
import com.philips.platform.pif.DataInterface.USR.UserDataInterface
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState
import com.philips.platform.uappframework.UappInterface
import com.philips.platform.uappframework.launcher.UiLauncher
import com.philips.platform.uappframework.uappinput.UappDependencies
import com.philips.platform.uappframework.uappinput.UappLaunchInput
import com.philips.platform.uappframework.uappinput.UappSettings
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * MECInterface is the public class for any proposition to consume MEC micro app. Its the starting initialization point.
 * @since 1.0.0
 */
class MECInterface : UappInterface {
    private var mMECSettings: MECSettings?=null
    private var mUappDependencies: UappDependencies? = null
    private var mUserDataInterface: UserDataInterface? = null

    /**
     * @param uappDependencies Object of UappDependencies
     * @param uappSettings     Object of UppSettings
     */
    override fun init(uappDependencies: UappDependencies, uappSettings: UappSettings) {
        val MECDependencies = uappDependencies as MECDependencies
        mUserDataInterface = MECDependencies.userDataInterface


        if (null == mUserDataInterface)
            throw RuntimeException("UserDataInterface is not injected in IAPDependencies.")

        mMECSettings = uappSettings as MECSettings
        mUappDependencies = uappDependencies


        MECDataHolder.INSTANCE.appinfra = MECDependencies.appInfra
        MECDataHolder.INSTANCE.userDataInterface = MECDependencies.userDataInterface
        val configError = AppConfigurationInterface.AppConfigurationError()
        val propositionID = MECDependencies.appInfra.configInterface.getPropertyForKey("propositionid", "MEC", configError) as String
        val voucher = MECDependencies.appInfra.configInterface.getPropertyForKey("voucher", "MEC", configError) as Boolean
        var propertyForKey = ""
        if (propositionID != null) {
            propertyForKey = propositionID
        }
        MECDataHolder.INSTANCE.propositionId = propertyForKey
        MECDataHolder.INSTANCE.voucherEnabled = voucher
        val ecsServices = ECSServices(propertyForKey, MECDependencies.appInfra as AppInfra)
        MecHolder.INSTANCE.eCSServices = ecsServices // singleton
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

    /**
     * @param mecListener
     */
    fun getProductCartCount(mecListener: MECListener) {
        GlobalScope.launch {
           var  mecManager: MECManager = MECManager()
            mecManager.getProductCartCountWorker(mecListener)
        }

    }






}



