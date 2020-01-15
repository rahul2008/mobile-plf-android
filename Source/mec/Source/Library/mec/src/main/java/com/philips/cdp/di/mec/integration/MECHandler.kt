/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration

import android.content.Intent
import android.os.Bundle

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.mec.common.MECFragmentLauncher
import com.philips.cdp.di.mec.common.MECLauncherActivity
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface.*
import com.philips.platform.uappframework.launcher.ActivityLauncher
import com.philips.platform.uappframework.launcher.FragmentLauncher
import com.philips.platform.uappframework.launcher.UiLauncher

import java.util.ArrayList
import com.google.gson.Gson
import com.philips.cdp.di.mec.integration.serviceDiscovery.ServiceDiscoveryMapListener


internal class MECHandler(private val mMECDependencies: MECDependencies, private val mMECSetting: MECSettings, private val mUiLauncher: UiLauncher, private val mLaunchInput: MECLaunchInput) {
    private var appInfra: AppInfra? = null
    private var listOfServiceId: ArrayList<String>? = null
    lateinit var serviceUrlMapListener: OnGetServiceUrlMapListener


    private val IAP_PRIVACY_URL = "iap.privacyPolicy"


    // mBundle.putSerializable(MECConstant.FLOW_INPUT,mLaunchInput.getFlowConfigurator());
    fun getBundle(): Bundle {
            val mBundle = Bundle()
            if (mLaunchInput.flowConfigurator != null) {

                mBundle.putSerializable(MECConstant.FLOW_INPUT,mLaunchInput.flowConfigurator)

                if (mLaunchInput.flowConfigurator!!.productCTNs != null) {
                    mBundle.putStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS,
                            mLaunchInput.flowConfigurator!!.productCTNs)
                }
            }
            return mBundle
        }


    fun launchMEC() {
        appInfra = mMECDependencies.appInfra as AppInfra
        val configInterface = appInfra!!.configInterface
        val configError = AppConfigurationInterface.AppConfigurationError()
        val propositionID = configInterface.getPropertyForKey("propositionid", "MEC", configError)
        var propertyForKey = ""
        if(propositionID!=null) {
            propertyForKey = propositionID as String
        }
        val ecsServices = ECSServices(propertyForKey, appInfra!!)
        MecHolder.INSTANCE.eCSServices = ecsServices // singleton
        MECDataHolder.INSTANCE.appinfra = appInfra as AppInfra
        MECDataHolder.INSTANCE.propositionId = propertyForKey
        MECDataHolder.INSTANCE.mecBannerEnabler = mLaunchInput.mecBannerConfigurator!!
        MECDataHolder.INSTANCE.hybrisEnabled = mLaunchInput.supportsHybris
        MECDataHolder.INSTANCE.retailerEnabled = mLaunchInput.supportsRetailer
        MECDataHolder.INSTANCE.mecBazaarVoiceInput = mLaunchInput.mecBazaarVoiceInput!!
        MECDataHolder.INSTANCE.blackListedRetailers = mLaunchInput.blackListedRetailerNames


        getUrl()
        if (mUiLauncher is ActivityLauncher) {
            launchMECasActivity()
        } else {
            launchMECasFragment()
        }
    }

    fun getUrl() {

        listOfServiceId = ArrayList()
        listOfServiceId!!.add(IAP_PRIVACY_URL)
        serviceUrlMapListener = ServiceDiscoveryMapListener()
        appInfra!!.serviceDiscovery.getServicesWithCountryPreference(listOfServiceId, serviceUrlMapListener, null)
    }


    protected fun launchMECasActivity() {
        val intent = Intent(mMECSetting.context, MECLauncherActivity::class.java)
      //  intent.putExtra(MECConstant.MEC_LANDING_SCREEN, mLaunchInput.landingView) //TODO
        val activityLauncher = mUiLauncher as ActivityLauncher
        val bundle = getBundle()
        if (mLaunchInput.flowConfigurator == null){
            MECFlowConfigurator()
        }
        val str = Gson().toJson(mLaunchInput.flowConfigurator)
        bundle.putString(MECConstant.FLOW_INPUT, str)
        bundle.putInt(MECConstant.MEC_KEY_ACTIVITY_THEME, activityLauncher.uiKitTheme)
        intent.putExtras(bundle)
        mMECSetting.context.startActivity(intent)

    }

    private fun launchMECasFragment() {
        val fragmentLauncher = mUiLauncher as FragmentLauncher
        val bundle = getBundle()
        bundle.putInt("fragment_container", fragmentLauncher.parentContainerResourceID) // frame_layout for fragment
        loadDecisionFragment(bundle)
    }

    private fun loadDecisionFragment(bundle: Bundle) {

        if (mLaunchInput.flowConfigurator == null){
            MECFlowConfigurator()
        }
        val str = Gson().toJson(mLaunchInput.flowConfigurator)
        bundle.putString(MECConstant.FLOW_INPUT, str)

        val mecFragmentLauncher = MECFragmentLauncher()
        mecFragmentLauncher.arguments = bundle

        val fragmentLauncher = mUiLauncher as FragmentLauncher
        MECDataHolder.INSTANCE.setActionBarListener(fragmentLauncher.actionbarListener, mLaunchInput.mecListener!!)
        val tag = mecFragmentLauncher.javaClass.name
        val transaction = fragmentLauncher.fragmentActivity.supportFragmentManager.beginTransaction()
        transaction.replace(fragmentLauncher.parentContainerResourceID, mecFragmentLauncher, tag)
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()

    }

}
