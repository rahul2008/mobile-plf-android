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
class MECProductInfoFragment : MecBaseFragment() {

    override fun getFragmentTag(): String {
       return "MECProductInfoFragment"
    }

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

        ecsProductDetailViewModel = activity?.let { ViewModelProviders.of(it).get(EcsProductDetailViewModel::class.java) }!!

        ecsProductDetailViewModel.ecsProduct.observe(activity!!, productObserver)

        return binding.root
    }

}
