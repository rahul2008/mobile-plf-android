/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration

import android.app.Application
import android.content.Intent
import android.os.Bundle

import com.philips.cdp.di.ecs.ECSServices

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
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.mec.analytics.MECAnalytics
import com.philips.cdp.di.mec.integration.serviceDiscovery.ServiceDiscoveryMapListener
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.catalog.MECCategorizedRetailerFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogCategorizedFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment
import com.philips.cdp.di.mec.screens.detail.MECProductDetailsFragment
import com.philips.cdp.di.mec.screens.reviews.BazaarVoiceHelper
import com.philips.cdp.di.mec.screens.shoppingCart.MECShoppingCartFragment


internal class MECHandler(private val mMECDependencies: MECDependencies, private val mMECSetting: MECSettings, private val mUiLauncher: UiLauncher, private val mLaunchInput: MECLaunchInput) {
    private var appInfra: AppInfra? = null
    private var listOfServiceId: ArrayList<String>? = null
    lateinit var serviceUrlMapListener: OnGetServiceUrlMapListener
    lateinit var fragmentTag :String


    private val IAP_PRIVACY_URL = "iap.privacyPolicy"
    private lateinit var mecFlows: MECFlowConfigurator.MECLandingView


    //mBundle.putSerializable(MECConstant.FLOW_INPUT,mLaunchInput.getFlowConfigurator());
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
        MECDataHolder.INSTANCE.userDataInterface = mMECDependencies.userDataInterface


        MECAnalytics.initMECAnalytics(mMECDependencies)

         if (MECDataHolder.INSTANCE.bvClient == null) {
                   val bazarvoiceSDK = BazaarVoiceHelper().getBazarvoiceClient(mMECSetting.context.applicationContext as Application)
                   MECDataHolder.INSTANCE.bvClient = bazarvoiceSDK
               }

     /*   ecsServices.configureECSToGetConfiguration(object: ECSCallback<ECSConfig, Exception>{

            override fun onResponse(config: ECSConfig?) {
                if (MECDataHolder.INSTANCE.hybrisEnabled) {
                    MECDataHolder.INSTANCE.hybrisEnabled = config?.isHybris ?: return

                }
                MECDataHolder.INSTANCE.locale = config!!.locale
                MECAnalytics.setCurrencyString(MECDataHolder.INSTANCE.locale)
                if(null!=config!!.rootCategory){
                    MECDataHolder.INSTANCE.rootCategory = config!!.rootCategory
                }
                if (mUiLauncher is ActivityLauncher) {
                    launchMECasActivity()
                } else {
                    launchMECasFragment()
                }
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        } )*/

        if (mUiLauncher is ActivityLauncher) {
            launchMECasActivity()
        } else {
            launchMECasFragment()
        }

        getUrl()

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
        if (mLaunchInput.flowConfigurator == null){
            MECFlowConfigurator()
        }
        val fragmentLauncher = mUiLauncher as FragmentLauncher
        val bundle = getBundle()

        bundle.putInt("fragment_container", fragmentLauncher.parentContainerResourceID) // frame_layout for fragment

        launchMECasFragment(MECDataHolder.INSTANCE.hybrisEnabled, bundle)
    }



    protected fun launchMECasFragment(result: Boolean, bundle :Bundle) {

        /////////



        //////////
        val str = Gson().toJson(mLaunchInput.flowConfigurator)
        bundle.putString(MECConstant.FLOW_INPUT, str)
        var mecConfiguration = Gson().fromJson(str, MECFlowConfigurator::class.java)
        ///////
        // only for Product detail launch
        var ctnList: ArrayList<String>? = ArrayList()

        if (mecConfiguration.productCTNs != null) {

            for (ctn in mecConfiguration.productCTNs!!) {
                ctnList?.add(ctn.replace("_", "/"))
            }
            bundle?.putStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS, ctnList)
        }

        ///////
        mecFlows = mecConfiguration.landingView!!

        val mecBaseFragment = getFragment(result, mecFlows)
        mecBaseFragment?.arguments = bundle


        val fragmentLauncher = mUiLauncher as FragmentLauncher
        MECDataHolder.INSTANCE.setActionBarListener(fragmentLauncher.actionbarListener, mLaunchInput.mecListener!!)

        val transaction = fragmentLauncher.fragmentActivity.supportFragmentManager.beginTransaction()
        transaction.replace(fragmentLauncher.parentContainerResourceID, mecBaseFragment!!, fragmentTag)
        transaction.addToBackStack(fragmentTag)
        transaction.commitAllowingStateLoss()
    }

    private fun getFragment(isHybris: Boolean, screen: MECFlowConfigurator.MECLandingView): MecBaseFragment? {
        var fragment: MecBaseFragment? = null

        when (screen) {

            MECFlowConfigurator.MECLandingView.MEC_PRODUCT_DETAILS_VIEW -> {
                fragmentTag=MECProductDetailsFragment.TAG
                fragment=MECProductDetailsFragment()
            }

            MECFlowConfigurator.MECLandingView.MEC_PRODUCT_LIST_VIEW -> {
                fragmentTag=MECProductCatalogFragment.TAG
                fragment = MECProductCatalogFragment()
            }
            MECFlowConfigurator.MECLandingView.MEC_CATEGORIZED_PRODUCT_LIST_VIEW -> {
                fragmentTag=MECProductCatalogCategorizedFragment.TAG
                fragment = MECProductCatalogCategorizedFragment()
            }
            MECFlowConfigurator.MECLandingView.MEC_SHOPPING_CART -> {
                fragmentTag=MECShoppingCartFragment.TAG
                TODO("launch shopping cart")
            }
        }

        return fragment
    }

    private fun getCategorizedFragment(isHybris: Boolean): MecBaseFragment? {
        if (isHybris) {
            return MECProductCatalogCategorizedFragment()
        } else {
            return MECCategorizedRetailerFragment()
        }
    }

}
