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
import com.philips.cdp.di.mec.integration.MECFlowInput
import com.philips.cdp.di.mec.integration.MECLaunchInput
import com.philips.cdp.di.mec.screens.detail.MECProductDetailsFragment
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.catalog.MECCategorizedRetailerFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogCategorizedFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment
import com.philips.cdp.di.mec.screens.detail.MECProductReviewsFragment
import com.philips.cdp.di.mec.screens.reviews.BazaarVoiceHelper
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import kotlinx.android.synthetic.main.mec_main_activity.*
import java.io.Serializable

class MECFragmentLauncher : MecBaseFragment(){

    lateinit var  mFlowInput: MECFlowInput
    lateinit var ecsLauncherViewModel: EcsLauncherViewModel

    private val isHybrisObserver : Observer<Boolean> = object :Observer<Boolean>{
        override fun onChanged(result: Boolean?) {
            hideProgressBar()
            if(MECDataHolder.INSTANCE.hybrisEnabled) {
                MECDataHolder.INSTANCE.hybrisEnabled = result!!
            }
            launchMECasFragment(landingFragment, MECDataHolder.INSTANCE.hybrisEnabled)
        }

    }

    private val configObserver : Observer<ECSConfig> = object :Observer<ECSConfig>{
        override fun onChanged(config: ECSConfig?) {
            hideProgressBar()

            if(MECDataHolder.INSTANCE.hybrisEnabled) {
                MECDataHolder.INSTANCE.hybrisEnabled = config?.isHybris ?: return
            }

            MECDataHolder.INSTANCE.locale = config!!.locale
            launchMECasFragment(landingFragment, MECDataHolder.INSTANCE.hybrisEnabled)
        }

    }

    private val productObserver : Observer<ECSProduct> = object : Observer<ECSProduct> {

        override fun onChanged(ecsProduct: ECSProduct?) {

            val mecProductDetailsFragment = MECProductDetailsFragment()

            val bundle = Bundle()
            bundle.putSerializable(MECConstant.MEC_KEY_PRODUCT,ecsProduct)
            mecProductDetailsFragment.arguments = bundle

            mecProductDetailsFragment.let { replaceFragment(it,"asd",false) }
            hideProgressBar()
        }

    }

    private var bundle: Bundle? = null
    private var landingFragment: Int = 0
    val TAG = MECFragmentLauncher::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        ecsLauncherViewModel = ViewModelProviders.of(this).get(EcsLauncherViewModel::class.java)
        ecsLauncherViewModel.isHybris.observe(this, isHybrisObserver)
        ecsLauncherViewModel.ecsConfig.observe(this, configObserver)
        ecsLauncherViewModel.mecError.observe(this,this)
        ecsLauncherViewModel.ecsProduct.observe(this,productObserver)


        //TODO we can also check if SDK is already initialized
        if(MECDataHolder.INSTANCE.bvClient == null) {
            val bazarvoiceSDK = BazaarVoiceHelper().getBazarvoiceClient(activity?.application!!)
            MECDataHolder.INSTANCE.bvClient = bazarvoiceSDK
        }

        return inflater.inflate(R.layout.mec_fragment_launcher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bundle = arguments
//        mFlowInput = bundle?.getSerializable(MECConstant.FLOW_INPUT) as MECFlowInput
        landingFragment =  bundle!!.getInt(MECConstant.MEC_LANDING_SCREEN)

    }

    override fun onStart() {
        super.onStart()
        executeConfigRequest()
    }

    private fun executeConfigRequest() {
        createCustomProgressBar(container, MEDIUM)
        ecsLauncherViewModel.getEcsConfig()
    }


    protected fun launchMECasFragment(landingFragment: Int, result: Boolean) {
        val mecBaseFragment = getFragment(result,landingFragment)
        mecBaseFragment?.let { replaceFragment(it,"asd",false) }
    }

    protected fun getFragment(isHybris : Boolean,screen: Int): MecBaseFragment? {
        var fragment: MecBaseFragment? = null

        when (screen) {
            MECLaunchInput.MECFlows.MEC_SHOPPING_CART_VIEW -> {

            }
            MECLaunchInput.MECFlows.MEC_PURCHASE_HISTORY_VIEW -> {

            }
            MECLaunchInput.MECFlows.MEC_PRODUCT_DETAIL_VIEW -> {

                createCustomProgressBar(container, MEDIUM)

                val ctn = bundle?.getString(MECConstant.MEC_PRODUCT_CTN_NUMBER_FROM_VERTICAL,"UNKNOWN")

                if(isHybris){
                    ecsLauncherViewModel.getProductDetailForCtn(ctn!!)
                }else{
                    ecsLauncherViewModel.getRetailerProductDetailForCtn(ctn!!)
                }
            }
            MECLaunchInput.MECFlows.MEC_BUY_DIRECT_VIEW -> {

            }
            MECLaunchInput.MECFlows.MEC_PRODUCT_CATALOG_VIEW -> {

                fragment = MECProductCatalogFragment()

            }
            MECLaunchInput.MECFlows.MEC_CATEGORIZED_VIEW ->{

                val isCategorized: ArrayList<String>? = mFlowInput.productCTNs

                if(!isCategorized.isNullOrEmpty()){
                    fragment = if(isHybris){
                        MECProductCatalogCategorizedFragment()
                    }else{
                        MECCategorizedRetailerFragment()
                    }
                }
            }
            else -> {
                fragment = MECProductCatalogFragment().createInstance(Bundle())
            }
        }
        fragment?.arguments = bundle
        return fragment
     }

}