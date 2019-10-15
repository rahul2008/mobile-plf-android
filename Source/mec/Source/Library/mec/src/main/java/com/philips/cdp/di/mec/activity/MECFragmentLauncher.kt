package com.philips.cdp.di.mec.activity

import android.databinding.DataBindingUtil.setContentView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.integration.MECLaunchInput
import com.philips.cdp.di.mec.integration.MECListener
import com.philips.cdp.di.mec.screens.InAppBaseFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.Utility
import com.philips.platform.uappframework.launcher.FragmentLauncher
import com.philips.platform.uappframework.launcher.UiLauncher

class MECFragmentLauncher : Fragment() {
    val TAG = MECFragmentLauncher::class.java!!.getName()
    lateinit var bundleInput: Bundle



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val bundle = arguments
        val landingFragment:Int =  bundle!!.getInt(MECConstant.MEC_LANDING_SCREEN);
        var mECLaunchInput = bundle.getSerializable("LaunchInput") as MECLaunchInput
        var mUiLauncher = bundle.getSerializable("UILauncher") as UiLauncher
        launchMECasFragment(landingFragment, mECLaunchInput,mUiLauncher);

        return inflater.inflate(R.layout.mec_fragment_launcher, container, false)
       // return super.onCreateView(inflater, container, savedInstanceState)
    }


    protected fun launchMECasFragment(landingFragment: Int) {
        val target = getFragment(landingFragment)
       // addFragment(target!!, mUiLauncher as FragmentLauncher, mLaunchInput.getMecListener())
    }


    protected fun getFragment(screen: Int, mecLaunchInput: MECLaunchInput): InAppBaseFragment? {
        var fragment: InAppBaseFragment? = null
        val bundle = Bundle()
        val ignoreRetailers = mecLaunchInput.ignoreRetailers
        val voucherCode = mecLaunchInput.voucher
        when (screen) {
            MECLaunchInput.MECFlows.MEC_SHOPPING_CART_VIEW -> {
                //fragment = new ShoppingCartFragment();
                bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailers)
                Utility.setVoucherCode(mecLaunchInput.voucher)
            }
            MECLaunchInput.MECFlows.MEC_PURCHASE_HISTORY_VIEW -> {
            }
            MECLaunchInput.MECFlows.MEC_PRODUCT_DETAIL_VIEW -> {
            }
            MECLaunchInput.MECFlows.MEC_BUY_DIRECT_VIEW -> {
            }
            MECLaunchInput.MECFlows.MEC_PRODUCT_CATALOG_VIEW -> {
                fragment = MECProductCatalogFragment()
                if (mecLaunchInput.mMECFlowInput != null && mecLaunchInput.mMECFlowInput.productCTNs != null) {
                    val CTNs = mecLaunchInput.mMECFlowInput.productCTNs
                    bundle.putStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS, CTNs)
                }

                bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailers)
                fragment.arguments = bundle
            }
            else -> {
                fragment = MECProductCatalogFragment()
                bundle.putString(MECConstant.CATEGORISED_PRODUCT_CTNS, null)
                bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailers)
                fragment.arguments = bundle
            }
        }//fragment = new PurchaseHistoryFragment();
        /*fragment = new ProductDetailFragment();
                if (mecLaunchInput.mMECFlowInput!= null  && mecLaunchInput.mMECFlowInput.getProductCTN() != null) {
                    bundle.putString(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL, mecLaunchInput.mMECFlowInput.getProductCTN());
                }
                bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailers);
                fragment.setArguments(bundle);*//*fragment = new BuyDirectFragment();
                if (mecLaunchInput.mMECFlowInput!= null  && mecLaunchInput.mMECFlowInput.getProductCTN() != null) {
                    bundle.putString(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL, mecLaunchInput.mMECFlowInput.getProductCTN());
                }
                bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailers);*/
        return fragment
    }

    protected fun addFragment(newFragment: InAppBaseFragment, fragmentLauncher: FragmentLauncher, mecListener: MECListener?) {
        //newFragment.setActionBarListener(fragmentLauncher.getActionbarListener(), mecListener);
        val tag = newFragment.javaClass.name
        val transaction = fragmentLauncher.fragmentActivity.supportFragmentManager.beginTransaction()
        transaction.replace(fragmentLauncher.parentContainerResourceID, newFragment, tag)
        transaction.addToBackStack(tag)
        transaction.commitAllowingStateLoss()
    }

}