/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.mec.common.MECFragmentLauncher
import com.philips.cdp.di.mec.common.MECLauncherActivity
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface.*
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface.OnErrorListener.*
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.*
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService
import com.philips.platform.uappframework.launcher.ActivityLauncher
import com.philips.platform.uappframework.launcher.FragmentLauncher
import com.philips.platform.uappframework.launcher.UiLauncher

import java.util.ArrayList
import java.util.Objects
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_NETWORK as NO_NETWORK1

internal class MECHandler(private val mMECDependencies: MECDependencies, private val mMECSetting: MECSettings, private val mUiLauncher: UiLauncher, private val mLaunchInput: MECLaunchInput) {
    private var appInfra: AppInfra? = null
    private var listOfServiceId: ArrayList<String>? = null
    lateinit var serviceUrlMapListener: OnGetServiceUrlMapListener

    // mBundle.putSerializable(MECConstant.FLOW_INPUT,mLaunchInput.getMMECFlowInput());
    fun getBundle(): Bundle {
            val mBundle = Bundle()
            if (mLaunchInput.mMECFlowInput != null) {

                mBundle.putSerializable(MECConstant.FLOW_INPUT,mLaunchInput.mMECFlowInput)

                if (mLaunchInput.mMECFlowInput!!.productCTN != null) {
                    mBundle.putString(MECConstant.MEC_PRODUCT_CTN_NUMBER_FROM_VERTICAL,
                            mLaunchInput.mMECFlowInput!!.productCTN)
                }
                if (mLaunchInput.mMECFlowInput!!.productCTNs != null) {
                    mBundle.putStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS,
                            mLaunchInput.mMECFlowInput!!.productCTNs)
                }
                mBundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, mLaunchInput.ignoreRetailers)
            }
            return mBundle
        }


    fun launchMEC() {
        appInfra = mMECDependencies.appInfra as AppInfra
        val configInterface = appInfra!!.configInterface
        val configError = AppConfigurationInterface.AppConfigurationError()
        val propertyForKey = configInterface.getPropertyForKey("propositionid", "MEC", configError) as String
        val ecsServices = ECSServices(propertyForKey, appInfra!!)
        MecHolder.INSTANCE.eCSServices = ecsServices // singleton
        MECDataHolder.INSTANCE.appinfra = appInfra as AppInfra
        MECDataHolder.INSTANCE.propositionId = propertyForKey
        MECDataHolder.INSTANCE.mecBannerEnabler = mLaunchInput.mecBannerEnabler
        MECDataHolder.INSTANCE.hybrisEnabled = mLaunchInput.hybrisEnabled
        MECDataHolder.INSTANCE.mecBazaarVoiceInput = mLaunchInput.mecBazaarVoiceInput
        MECDataHolder.INSTANCE.blackListedRetailers = Objects.requireNonNull<ArrayList<String>>(mLaunchInput.ignoreRetailers)


        getUrl()
        if (mUiLauncher is ActivityLauncher) {
            launchMECasActivity()
        } else {
            launchMECasFragment()
        }
    }

    fun getUrl() {

        listOfServiceId = ArrayList()
        listOfServiceId!!.add(IAP_BASE_URL)
        listOfServiceId!!.add(IAP_PRIVACY_URL)
        listOfServiceId!!.add(IAP_FAQ_URL)
        listOfServiceId!!.add(IAP_TERMS_URL)
        serviceUrlMapListener = object : OnGetServiceUrlMapListener {
            override fun onSuccess(map: Map<String, ServiceDiscoveryService>) {
                val collection = map.values


                val list = ArrayList<ServiceDiscoveryService>()
                list.addAll(collection)

                val discoveryService = map[IAP_PRIVACY_URL]!!
                val privacyUrl = discoveryService.configUrls
                if (privacyUrl != null) {
                    MECDataHolder.INSTANCE.setPrivacyUrl(privacyUrl)
                }

            }

            override fun onError(errorvalues: ERRORVALUES, s: String) {
               /* if (errorvalues.name == NO_NETWORK1) {
                }*/
            }
        }
        appInfra!!.serviceDiscovery.getServicesWithCountryPreference(listOfServiceId, serviceUrlMapListener, null)
    }


    protected fun launchMECasActivity() {
        val intent = Intent(mMECSetting.context, MECLauncherActivity::class.java)
        intent.putExtra(MECConstant.MEC_LANDING_SCREEN, mLaunchInput.mLandingView)
        val activityLauncher = mUiLauncher as ActivityLauncher
        val bundle = getBundle()
        bundle.putInt(MECConstant.MEC_KEY_ACTIVITY_THEME, activityLauncher.uiKitTheme)
        intent.putExtras(bundle)
        mMECSetting.context.startActivity(intent)

    }

    protected fun launchMECasFragment() {
        val fragmentLauncher = mUiLauncher as FragmentLauncher
        val bundle = getBundle()
        bundle.putInt(MECConstant.MEC_LANDING_SCREEN, mLaunchInput.mLandingView)
        bundle.putInt("fragment_container", fragmentLauncher.parentContainerResourceID) // frame_layout for fragment
        loadDecisionFragment(bundle)


    }

    fun loadDecisionFragment(bundle: Bundle) {
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

    companion object {
        private val IAP_PRIVACY_URL = "iap.privacyPolicy"
        private val IAP_FAQ_URL = "iap.faq"
        private val IAP_TERMS_URL = "iap.termOfUse"
        private val IAP_BASE_URL = "iap.baseurl"
    }


}
