package com.philips.cdp.di.mec.screens.Detail


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.products.ECSProduct

import com.philips.cdp.di.mec.databinding.MecProductInfoFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.detail.EcsProductDetailViewModel

/**
 * A simple [Fragment] subclass.
 */
class MECProductInfoFragment : MecBaseFragment() {

    private lateinit var binding:MecProductInfoFragmentBinding
    lateinit var ecsProductDetailViewModel: EcsProductDetailViewModel

    private val productObserver : Observer<ECSProduct> = object : Observer<ECSProduct> {

        override fun onChanged(ecsProduct: ECSProduct?) {
            binding.product = ecsProduct
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecProductInfoFragmentBinding.inflate(inflater, container, false)

        ecsProductDetailViewModel = this!!.activity?.let { ViewModelProviders.of(it).get(EcsProductDetailViewModel::class.java) }!!

        ecsProductDetailViewModel.ecsProduct.observe(this, productObserver)

        return binding.root
    }


}
