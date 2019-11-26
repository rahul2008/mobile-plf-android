package com.philips.cdp.di.mec.screens.retailers

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log
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


class MECRetailersFragment : BottomSheetDialogFragment(),ItemClickListener {

    override fun onItemClick(item: Object) {

        Log.d("TAG","ONclick")
    }

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
        binding.itemClickListener = this

        return binding.root
    }

}
