package com.philips.cdp.di.mec.screens.retailers

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecRetailersFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface
import java.util.*
import android.widget.LinearLayout
import android.widget.RelativeLayout


class MECRetailersFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: MecRetailersFragmentBinding
    private lateinit var retailers: ECSRetailerList
    lateinit var appInfra: AppInfra
    lateinit var param : String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MecRetailersFragmentBinding.inflate(inflater, container, false)


        val bottomSheetBehavior = BottomSheetBehavior.from(binding.designBottomSheet)
        val metrics = resources.displayMetrics
        bottomSheetBehavior.peekHeight = metrics.heightPixels / 2
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val bundle = arguments
        retailers = bundle?.getSerializable(MECConstant.MEC_KEY_PRODUCT) as ECSRetailerList

        binding.retailerList = retailers
        binding.retailerRecyclerView.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View?) {
         param = retailers.retailers.get(0).xactparam
        val bundle = Bundle()
        bundle.putString(MECConstant.MEC_BUY_URL, uuidWithSupplierLink(retailers.retailers.get(0).buyURL))
        bundle.putString(MECConstant.MEC_STORE_NAME, retailers.retailers.get(0).name)
//        bundle.putBoolean(MECConstant.MEC_IS_PHILIPS_SHOP, Utility().isPhilipsShop(storeEntity))
        val fragment = WebBuyFromRetailersFragment()
        fragment.arguments = bundle
        replaceFragment(fragment,"detail",true)
    }

    fun replaceFragment(newFragment: WebBuyFromRetailersFragment,
                        newFragmentTag: String, isReplaceWithBackStack: Boolean) {
        if (MECDataHolder.INSTANCE.actionbarUpdateListener == null || MECDataHolder.INSTANCE.mecListener == null)
            RuntimeException("ActionBarListner and IAPListner cant be null")
        else {
            if (!activity!!.isFinishing) {

                val transaction = activity!!.supportFragmentManager.beginTransaction()
                val simpleName = newFragment.javaClass.simpleName

                if (isReplaceWithBackStack) {
                    transaction.addToBackStack(newFragmentTag)
                }

                transaction.replace(id, newFragment, simpleName)
                transaction.commitAllowingStateLoss()
            }
        }
    }

    private fun uuidWithSupplierLink(buyURL: String): String {
        val mConfigInterface = appInfra.configInterface
        val configError = AppConfigurationInterface.AppConfigurationError()

        val propositionId = mConfigInterface.getPropertyForKey("propositionid", "IAP", configError) as String

        if (configError.errorCode != null) {
            //IAPLog.e(IAPLog.LOG, "VerticalAppConfig ==loadConfigurationFromAsset " + configError.errorCode.toString())
        }

        val supplierLinkWithUUID = "$buyURL&wtbSource=mobile_$propositionId&$param="

        return supplierLinkWithUUID + UUID.randomUUID().toString()
    }


}
