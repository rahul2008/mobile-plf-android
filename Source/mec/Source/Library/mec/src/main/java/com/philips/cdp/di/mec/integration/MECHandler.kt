/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log

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
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.analytics.MECAnalytics
import com.philips.cdp.di.mec.integration.serviceDiscovery.ServiceDiscoveryMapListener
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.catalog.MECCategorizedRetailerFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogCategorizedFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment
import com.philips.cdp.di.mec.screens.detail.MECLandingProductDetailsFragment
import com.philips.cdp.di.mec.screens.reviews.BazaarVoiceHelper


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

        if (MECDataHolder.INSTANCE.bvClient == null) {
            val bazarvoiceSDK = BazaarVoiceHelper().getBazarvoiceClient(mMECSetting.context.applicationContext as Application)
            MECDataHolder.INSTANCE.bvClient = bazarvoiceSDK
        }

        MECDataHolder.INSTANCE.blackListedRetailers = mLaunchInput.blackListedRetailerNames

        MECAnalytics.initMECAnalytics(mMECDependencies)

        getUrl()

        // get config

        ecsServices.configureECSToGetConfiguration(object: ECSCallback<ECSConfig, Exception>{

            override fun onResponse(config: ECSConfig?) {


                //set config data to singleton
                MECDataHolder.INSTANCE.config = config

                if (MECDataHolder.INSTANCE.hybrisEnabled) {
                    MECDataHolder.INSTANCE.hybrisEnabled = config?.isHybris ?: return
                }

                MECDataHolder.INSTANCE.locale = config!!.locale
                MECAnalytics.setCurrencyString(MECDataHolder.INSTANCE.locale)
                if(null!=config!!.rootCategory){
                    MECDataHolder.INSTANCE.rootCategory = config!!.rootCategory
                }

                // Launch fragment or activity
                if (mUiLauncher is ActivityLauncher) {
                    launchMECasActivity(config.isHybris)
                } else {
                    config?.isHybris?.let { launchMECasFragment(it) }
                }
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                Log.e("MEC","Config failed : Can't Launch MEC")
            }
        })



    }

    //TODO
    fun getUrl() {

        listOfServiceId = ArrayList()
        listOfServiceId!!.add(IAP_PRIVACY_URL)
        serviceUrlMapListener = ServiceDiscoveryMapListener()
        appInfra!!.serviceDiscovery.getServicesWithCountryPreference(listOfServiceId, serviceUrlMapListener, null)
    }


    protected fun launchMECasActivity(isHybris: Boolean) {
        val intent = Intent(mMECSetting.context, MECLauncherActivity::class.java)
        val activityLauncher = mUiLauncher as ActivityLauncher
        val bundle = getBundle()
        bundle.putSerializable(MECConstant.KEY_FLOW_CONFIGURATION, mLaunchInput.flowConfigurator)
        bundle.putBoolean(MECConstant.KEY_IS_HYBRIS,isHybris)
        bundle.putInt(MECConstant.MEC_KEY_ACTIVITY_THEME, activityLauncher.uiKitTheme)
        intent.putExtras(bundle)
        mMECSetting.context.startActivity(intent)

    }

    private fun launchMECasFragment(hybris: Boolean) {

        val bundle = getBundle()

        val mecLandingFragment =getFragment(hybris, mLaunchInput.flowConfigurator!!,bundle)
        val fragmentLauncher = mUiLauncher as FragmentLauncher
        bundle.putInt("fragment_container", fragmentLauncher.parentContainerResourceID) // frame_layout for fragment
        mecLandingFragment?.arguments = bundle


        MECDataHolder.INSTANCE.setActionBarListener(fragmentLauncher.actionbarListener, mLaunchInput.mecListener!!)
        val tag = mecLandingFragment?.javaClass?.name
        val transaction = fragmentLauncher.fragmentActivity.supportFragmentManager.beginTransaction()
        transaction.replace(fragmentLauncher.parentContainerResourceID, mecLandingFragment!!, tag)
        transaction.addToBackStack(tag)
        transaction.commitAllowingStateLoss()
    }


    private fun getFragment(isHybris: Boolean, mecFlowConfigurator: MECFlowConfigurator ,bundle: Bundle): MecBaseFragment? {
        var fragment: MecBaseFragment? = null

        when (mecFlowConfigurator.landingView) {

            MECFlowConfigurator.MECLandingView.MEC_PRODUCT_DETAILS_VIEW -> {
                fragment = MECLandingProductDetailsFragment()
                putCtnsToBundle(bundle,mecFlowConfigurator)
            }

            MECFlowConfigurator.MECLandingView.MEC_PRODUCT_LIST_VIEW -> {
                fragment = MECProductCatalogFragment()
            }
            MECFlowConfigurator.MECLandingView.MEC_CATEGORIZED_PRODUCT_LIST_VIEW -> {
                fragment = getCategorizedFragment(isHybris)
                putCtnsToBundle(bundle,mecFlowConfigurator)
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

    fun putCtnsToBundle(bundle: Bundle , mecFlowConfigurator: MECFlowConfigurator){

        var ctnList: ArrayList<String>? = ArrayList()

        if (mecFlowConfigurator.productCTNs != null) {

            for (ctn in mecFlowConfigurator.productCTNs!!) {
                ctnList?.add(ctn.replace("_", "/"))
            }
        }

        bundle?.putStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS, ctnList)

        val ecsProduct = ECSProduct()
        ecsProduct.code = ctnList?.get(0) ?: null

        bundle?.putSerializable(MECConstant.MEC_KEY_PRODUCT,ecsProduct)

    }


}
