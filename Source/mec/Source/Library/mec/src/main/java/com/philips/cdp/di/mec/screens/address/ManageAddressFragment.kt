package com.philips.cdp.di.mec.screens.address

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
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.analytics.MECAnalytics
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.sendData
import com.philips.cdp.di.mec.databinding.MecAddressManageBinding


class ManageAddressFragment : BottomSheetDialogFragment(){

    private lateinit var binding: MecAddressManageBinding

    private lateinit var mecAddresses: MECAddresses

    companion object {
        val TAG:String="ManageAddressFragment"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecAddressManageBinding.inflate(inflater, container, false)

        var ecsAddresses = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESSES) as List<ECSAddress>

        mecAddresses = MECAddresses(ecsAddresses)

        binding.mecAddresses = mecAddresses
        return binding.root
    }


}
