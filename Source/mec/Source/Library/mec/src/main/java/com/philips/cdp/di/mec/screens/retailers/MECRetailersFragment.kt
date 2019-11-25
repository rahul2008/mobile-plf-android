package com.philips.cdp.di.mec.screens.retailers


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList
import com.philips.cdp.di.mec.databinding.MecRetailersFragmentBinding
import com.philips.cdp.di.mec.utils.MECConstant


class MECRetailersFragment : BottomSheetDialogFragment() {

    private lateinit var ecsRetailerViewModel: ECSRetailerViewModel
    private lateinit var binding: MecRetailersFragmentBinding
    private lateinit var productCode: String

    private val eCSRetailerListObserver : Observer<ECSRetailerList> = Observer<ECSRetailerList> {
        binding.retailerList = it
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MecRetailersFragmentBinding.inflate(inflater, container, false)


        val bottomSheetBehavior = BottomSheetBehavior.from(binding.designBottomSheet)
        val metrics = resources.displayMetrics
        bottomSheetBehavior.peekHeight = metrics.heightPixels / 2
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val bundle = arguments
        productCode = bundle?.getString(MECConstant.MEC_KEY_PRODUCT).toString()

        ecsRetailerViewModel = this!!.activity?.let { ViewModelProviders.of(it).get(ECSRetailerViewModel::class.java) }!!

        ecsRetailerViewModel.ecsRetailerList.observe(this, eCSRetailerListObserver)
        binding.retailerList = ECSRetailerList()
        ecsRetailerViewModel.getRetailers(productCode)
        return binding.root
    }


}
