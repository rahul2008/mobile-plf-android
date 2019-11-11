package com.philips.cdp.di.mec.common

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.integration.MECLaunchInput
import com.philips.cdp.di.mec.screens.detail.MECProductDetailsFragment
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.catalog.MECCategorizedRetailerFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogCategorizedFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment
import com.philips.cdp.di.mec.utils.MECConstant
import kotlinx.android.synthetic.main.mec_main_activity.*

class MECFragmentLauncher : MecBaseFragment(){

    lateinit var ecsConfigViewModel: EcsConfigViewModel

    val isHybrisObserver : Observer<Boolean> = object :Observer<Boolean>{
        override fun onChanged(result: Boolean?) {
            hideProgressBar()
            launchMECasFragment(landingFragment, result!!)
        }

    }

    private var bundle: Bundle? = null
    private var landingFragment: Int = 0
    val TAG = MECFragmentLauncher::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        ecsConfigViewModel = ViewModelProviders.of(this).get(EcsConfigViewModel::class.java)

        ecsConfigViewModel.isHybris.observe(this, isHybrisObserver)
        ecsConfigViewModel.mecError.observe(this,this)

        return inflater.inflate(R.layout.mec_fragment_launcher, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bundle = arguments
        landingFragment =  bundle!!.getInt(MECConstant.MEC_LANDING_SCREEN)

    }

    override fun onStart() {
        super.onStart()
        executeConfigRequest()
    }

    private fun executeConfigRequest() {
        createCustomProgressBar(container, BIG)
        ecsConfigViewModel.isHybris()
    }


    protected fun launchMECasFragment(landingFragment: Int, result: Boolean) {
        val mecBaseFragment = getFragment(result,landingFragment)
        mecBaseFragment?.let { replaceFragment(it,"asd",false) }

    }

    protected fun getFragment(isHybris : Boolean,screen: Int): MecBaseFragment? {
        var fragment: MecBaseFragment? = null
        bundle = arguments
        when (screen) {
            MECLaunchInput.MECFlows.MEC_SHOPPING_CART_VIEW -> {
            }
            MECLaunchInput.MECFlows.MEC_PURCHASE_HISTORY_VIEW -> {
            }
            MECLaunchInput.MECFlows.MEC_PRODUCT_DETAIL_VIEW -> {
                fragment = MECProductDetailsFragment()
            }
            MECLaunchInput.MECFlows.MEC_BUY_DIRECT_VIEW -> {
            }
            MECLaunchInput.MECFlows.MEC_PRODUCT_CATALOG_VIEW -> {

                val isCategorized: ArrayList<String>? = bundle!!.getStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS)

                fragment = MECProductCatalogFragment()

                if(isCategorized?.isNotEmpty() == true){

                    if(isHybris){
                        fragment = MECProductCatalogCategorizedFragment()
                    }else{
                        fragment = MECCategorizedRetailerFragment()
                    }

                }
                fragment.arguments = bundle
            }
            else -> {
                fragment = MECProductCatalogFragment().createInstance(Bundle())
                fragment.arguments = bundle
            }
        }
        return fragment
     }


}