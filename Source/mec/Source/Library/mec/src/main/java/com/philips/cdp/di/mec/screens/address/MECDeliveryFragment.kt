package com.philips.cdp.di.mec.screens.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.mec.databinding.MecDeliveryBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant

class MECDeliveryFragment : MecBaseFragment() {
    override fun getFragmentTag(): String {
        return "MECDeliveryFragment"
    }

    lateinit var binding:MecDeliveryBinding

    lateinit var ecsAddress:ECSAddress

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecDeliveryBinding.inflate(inflater, container, false)

        ecsAddress = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESS) as ECSAddress

        binding.ecsAddressShipping = ecsAddress

        binding.tvShippingAddressEdit.setOnClickListener(object:View.OnClickListener{

            override fun onClick(v: View?) {
                onEditClick()
            }
        })

        return binding.root
    }


    fun onEditClick(){

        var editAddressFragment = EditAddressFragment()
        var bundle = Bundle()
        bundle.putSerializable(MECConstant.KEY_ECS_ADDRESS, ecsAddress)
        editAddressFragment.arguments = bundle
        replaceFragment(editAddressFragment,"EditAddressFragment",true)
    }
}