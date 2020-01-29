package com.philips.cdp.di.mec.screens.retailers

import android.os.Bundle
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
import com.philips.cdp.di.ecs.model.retailers.ECSRetailer
import kotlin.collections.ArrayList
import android.app.Activity
import android.content.Intent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.analytics.MECAnalytics
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.sendData


class MECRetailersFragment : BottomSheetDialogFragment(), ItemClickListener{

    override fun onItemClick(item: Any) {

        val ecsRetailer = item as ECSRetailer

        val bundle = Bundle()
        bundle.putSerializable(MECConstant.SELECTED_RETAILER, ecsRetailer)

        val intent = Intent().putExtras(bundle)

        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

        this.dismiss()
    }

    private lateinit var binding: MecRetailersFragmentBinding
    private lateinit var retailers: ECSRetailerList
    private lateinit var product: ECSProduct
    lateinit var appInfra: AppInfra


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MecRetailersFragmentBinding.inflate(inflater, container, false)


        val bottomSheetBehavior = BottomSheetBehavior.from(binding.designBottomSheet)
        val metrics = resources.displayMetrics
        bottomSheetBehavior.peekHeight = metrics.heightPixels / 2

        val bundle = arguments
        retailers = bundle?.getSerializable(MECConstant.MEC_KEY_PRODUCT) as ECSRetailerList
        product = bundle?.getSerializable(MECConstant.MEC_PRODUCT) as ECSProduct

        binding.retailerList = retailers
        binding.itemClickListener = this
        tagRetailerList(retailers,product)
        return binding.root
    }

    private fun tagRetailerList(retailers: ECSRetailerList, product :ECSProduct  ){
        if(retailers!=null && retailers.retailers!=null && retailers.retailers.size>0) {
            val mutableRetailersIterator = retailers.retailers.iterator()
            var retailerListString: String = ""
            for (ecsRetailer in mutableRetailersIterator) {
                retailerListString += "|" + ecsRetailer.name
            }
            retailerListString = retailerListString.substring(1, retailerListString.length - 1)

            val map = HashMap<String, String>()
            map.put(MECAnalyticsConstant.retailerList, retailerListString)
            val productInfo: String = MECAnalytics.getProductInfo(product)
            map.put(MECAnalyticsConstant.mecProducts, productInfo)
            MECAnalytics.trackMultipleActions(sendData, map)

        }
    }

}
