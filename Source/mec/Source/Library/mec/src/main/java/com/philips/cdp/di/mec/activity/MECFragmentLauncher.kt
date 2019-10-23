package com.philips.cdp.di.mec.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.integration.MECLaunchInput
import com.philips.cdp.di.mec.integration.MECListener
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.platform.uappframework.launcher.FragmentLauncher

class MECFragmentLauncher : MecBaseFragment() {
    val TAG = MECFragmentLauncher::class.java!!.getName()
    lateinit var bundleInput: Bundle



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mec_fragment_launcher, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val landingFragment:Int =  bundle!!.getInt(MECConstant.MEC_LANDING_SCREEN);
        launchMECasFragment(landingFragment);
    }


    protected fun launchMECasFragment(landingFragment: Int) {
        val fragment = getFragment(landingFragment)
        val fragmentTransaction =  getActivity()!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container_launcher, fragment!!)
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commitAllowingStateLoss()

    }

    protected fun getFragment(screen: Int): MecBaseFragment? {
        var fragment: MecBaseFragment? = null
        val bundle = Bundle()
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
                fragment = MECProductCatalogFragment()

                fragment.arguments = bundle
            }
            else -> {
                fragment = MECProductCatalogFragment().createInstance(Bundle())
                fragment.arguments = bundle
            }
        }
        return fragment
    }

    protected fun addrFragment(newFragment: MecBaseFragment, fragmentLauncher: FragmentLauncher, mecListener: MECListener?) {
        val tag = newFragment.javaClass.name
        val transaction = fragmentLauncher.fragmentActivity.supportFragmentManager.beginTransaction()
        transaction.replace(fragmentLauncher.parentContainerResourceID, newFragment, tag)
        transaction.addToBackStack(tag)
        transaction.commitAllowingStateLoss()
    }

}