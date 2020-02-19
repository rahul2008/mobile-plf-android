package com.philips.cdp.di.mec.screens.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.mec.databinding.MecDeliveryBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.retailers.MECRetailersFragment
import com.philips.cdp.di.mec.utils.MECConstant
import java.io.Serializable

class MECDeliveryFragment : MecBaseFragment() {
    override fun getFragmentTag(): String {
        return "MECDeliveryFragment"
    }

    lateinit var binding:MecDeliveryBinding

    lateinit var ecsAddresses:List<ECSAddress>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecDeliveryBinding.inflate(inflater, container, false)

        ecsAddresses = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESSES) as List<ECSAddress>

        binding.ecsAddressShipping = ecsAddresses[0]

        binding.tvShippingAddressName.setOnClickListener(object:View.OnClickListener{

            override fun onClick(v: View?) {
                onEditClick()
            }
        })

        binding.tvManageAddress.setOnClickListener(object:View.OnClickListener{

            override fun onClick(v: View?) {
                onManageAddressClick()
            }
        })

        return binding.root
    }


    fun onEditClick(){

        var editAddressFragment = EditAddressFragment()
        var bundle = Bundle()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESS, binding.ecsAddressShipping)
        editAddressFragment.arguments = bundle
        replaceFragment(editAddressFragment,editAddressFragment.getFragmentTag(),true)
    }

    private fun onManageAddressClick() {
        val bundle = Bundle()
        var bottomSheetFragment = ManageAddressFragment()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESSES, ecsAddresses as Serializable)
        bottomSheetFragment.arguments = bundle
        fragmentManager?.let { bottomSheetFragment.show(it, bottomSheetFragment.tag) }

    }
}