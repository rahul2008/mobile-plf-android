package com.philips.cdp.di.mec.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.activity.ecsService.ECSConfigService
import com.philips.cdp.di.mec.integration.MECLaunchInput
import com.philips.cdp.di.mec.screens.InAppBaseFragment
import com.philips.cdp.di.mec.screens.catalog.MECCategorizedRetailerFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogCategorizedFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment
import com.philips.cdp.di.mec.utils.MECConstant

class MECFragmentLauncher : InAppBaseFragment(), ECSCallback<Boolean, Exception> {

    override fun onResponse(result: Boolean) {
        launchMECasFragment(landingFragment,result)
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show()
    }



    private var bundle: Bundle? = null
    private var landingFragment: Int = 0
    val TAG = MECFragmentLauncher::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ECSConfigService().configECS(this)
        return inflater.inflate(R.layout.mec_fragment_launcher, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bundle = arguments
        landingFragment =  bundle!!.getInt(MECConstant.MEC_LANDING_SCREEN)
    }


    protected fun launchMECasFragment(landingFragment: Int, result: Boolean) {
        val fragment = getFragment(result,landingFragment)
        val fragmentTransaction =  getActivity()!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container_launcher, fragment!!).commit()
    }

    protected fun getFragment(isHybris : Boolean,screen: Int): InAppBaseFragment? {
        var fragment: InAppBaseFragment? = null

        when (screen) {
            MECLaunchInput.MECFlows.MEC_SHOPPING_CART_VIEW -> {
            }
            MECLaunchInput.MECFlows.MEC_PURCHASE_HISTORY_VIEW -> {
            }
            MECLaunchInput.MECFlows.MEC_PRODUCT_DETAIL_VIEW -> {
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