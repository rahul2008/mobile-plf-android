package com.philips.cdp.di.mec.common

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.integration.MECLaunchInput
import com.philips.cdp.di.mec.screens.detail.MECProductDetailsFragment
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.catalog.MECCategorizedRetailerFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogCategorizedFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment
import com.philips.cdp.di.mec.screens.reviews.BazaarVoiceHelper
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import kotlinx.android.synthetic.main.mec_main_activity.*


class MECFragmentLauncher : MecBaseFragment() {

    lateinit var ecsLauncherViewModel: EcsLauncherViewModel
    private var bundle: Bundle? = null
    private var mecFlowOrdinal: Int = 0
    lateinit var mecFlows: MECLaunchInput.MECLandingView
    val TAG = MECFragmentLauncher::class.java.name

    private val isHybrisObserver: Observer<Boolean> = object : Observer<Boolean> {
        override fun onChanged(result: Boolean?) {
            hideProgressBar()
            if (MECDataHolder.INSTANCE.hybrisEnabled) {
                MECDataHolder.INSTANCE.hybrisEnabled = result!!
            }
            launchMECasFragment(MECDataHolder.INSTANCE.hybrisEnabled)
        }

    }

    private val configObserver: Observer<ECSConfig> = object : Observer<ECSConfig> {
        override fun onChanged(config: ECSConfig?) {
            hideProgressBar()

            if (MECDataHolder.INSTANCE.hybrisEnabled) {
                MECDataHolder.INSTANCE.hybrisEnabled = config?.isHybris ?: return
            }

            MECDataHolder.INSTANCE.locale = config!!.locale
            launchMECasFragment(MECDataHolder.INSTANCE.hybrisEnabled)
        }

    }

    private val productObserver: Observer<ECSProduct> = object : Observer<ECSProduct> {

        override fun onChanged(ecsProduct: ECSProduct?) {

            val mecProductDetailsFragment = MECProductDetailsFragment()

            val bundle = Bundle()
            bundle.putSerializable(MECConstant.MEC_KEY_PRODUCT, ecsProduct)
            mecProductDetailsFragment.arguments = bundle
            mecProductDetailsFragment.let { replaceFragment(it, "asd", false) }
            hideProgressBar()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        ecsLauncherViewModel = ViewModelProviders.of(this).get(EcsLauncherViewModel::class.java)
        ecsLauncherViewModel.isHybris.observe(this, isHybrisObserver)
        ecsLauncherViewModel.ecsConfig.observe(this, configObserver)
        ecsLauncherViewModel.mecError.observe(this, this)
        ecsLauncherViewModel.ecsProduct.observe(this, productObserver)


        //TODO we can also check if SDK is already initialized
        if (MECDataHolder.INSTANCE.bvClient == null) {
            val bazarvoiceSDK = BazaarVoiceHelper().getBazarvoiceClient(activity?.application!!)
            MECDataHolder.INSTANCE.bvClient = bazarvoiceSDK
        }

        return inflater.inflate(R.layout.mec_fragment_launcher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bundle = arguments
        mecFlowOrdinal = bundle!!.getInt(MECConstant.MEC_LANDING_SCREEN)
        mecFlows = MECLaunchInput.MECLandingView.values()[mecFlowOrdinal] // getting enum from previous fragment

    }

    override fun onStart() {
        super.onStart()
        executeConfigRequest()
    }

    private fun executeConfigRequest() {
        createCustomProgressBar(container, MEDIUM)
        ecsLauncherViewModel.getEcsConfig()
    }


    protected fun launchMECasFragment(result: Boolean) {
        val mecBaseFragment = getFragment(result, mecFlows)
        mecBaseFragment?.let { replaceFragment(it, "asd", false) }
    }

    private fun getFragment(isHybris: Boolean, screen: MECLaunchInput.MECLandingView): MecBaseFragment? {
        var fragment: MecBaseFragment? = null

        when (screen) {

            MECLaunchInput.MECLandingView.MEC_PRODUCT_DETAILS_VIEW -> {
                fetchProductDetailForCtn(isHybris)
            }

            MECLaunchInput.MECLandingView.MEC_PRODUCT_LIST_VIEW -> {
                fragment = MECProductCatalogFragment()
            }
            MECLaunchInput.MECLandingView.MEC_CATEGORIZED_PRODUCT_LIST_VIEW -> {
                fragment = getCategorizedFragment(isHybris)
            }
            else -> {
                fragment = MECProductCatalogFragment()
            }
        }
        fragment?.arguments = bundle
        return fragment
    }



    private fun fetchProductDetailForCtn(isHybris: Boolean) {
        createCustomProgressBar(container, MEDIUM)

        val ctn = bundle?.getString(MECConstant.MEC_PRODUCT_CTN_NUMBER_FROM_VERTICAL, "UNKNOWN")

        if (isHybris) {
            ecsLauncherViewModel.getProductDetailForCtn(ctn!!)
        } else {
            ecsLauncherViewModel.getRetailerProductDetailForCtn(ctn!!)
        }
    }

    private fun getCategorizedFragment(isHybris: Boolean): MecBaseFragment? {
        if (isHybris) {
            return MECProductCatalogCategorizedFragment()
        } else {
            return MECCategorizedRetailerFragment()
        }
    }

}