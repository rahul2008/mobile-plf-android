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


class MECRetailersFragment : BottomSheetDialogFragment() {
    private lateinit var binding: MecRetailersFragmentBinding
    private lateinit var retailers: ECSRetailerList


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
        return binding.root
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


}
