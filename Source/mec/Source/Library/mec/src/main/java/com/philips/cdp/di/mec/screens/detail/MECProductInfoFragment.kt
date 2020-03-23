package com.philips.cdp.di.mec.screens.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.philips.cdp.di.ecs.model.products.ECSProduct

import com.philips.cdp.di.mec.databinding.MecProductInfoFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment

/**
 * A simple [Fragment] subclass.
 */
class MECProductInfoFragment(val product: ECSProduct) : MecBaseFragment() {



    override fun getFragmentTag(): String {
       return "MECProductInfoFragment"
    }

    private lateinit var binding:MecProductInfoFragmentBinding
    lateinit var ecsProductDetailViewModel: EcsProductDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecProductInfoFragmentBinding.inflate(inflater, container, false)
        binding.product = product
        return binding.root
    }

}
