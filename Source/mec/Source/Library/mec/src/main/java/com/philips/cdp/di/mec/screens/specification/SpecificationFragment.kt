package com.philips.cdp.di.mec.screens.specification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.databinding.MecProductSpecsFragmentBinding
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.prxclient.datamodels.specification.*


class SpecificationFragment : MecBaseFragment() {

    override fun getFragmentTag(): String {
        return "SpecificationFragment"
    }

    private lateinit var binding: MecProductSpecsFragmentBinding
    private lateinit var prxSpecificationViewModel: SpecificationViewModel

    private val specificationObserver : Observer<SpecificationModel> = object : Observer<SpecificationModel> {

        override fun onChanged(specification: SpecificationModel?) {
            binding.specificationModel = specification
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecProductSpecsFragmentBinding.inflate(inflater, container, false)

        prxSpecificationViewModel = ViewModelProviders.of(this).get(SpecificationViewModel::class.java)

        prxSpecificationViewModel.mecError.observe(this,this)
        prxSpecificationViewModel.specification.observe(this,specificationObserver)

        val bundle = arguments
        val productCtn = bundle!!.getString(MECConstant.MEC_PRODUCT_CTN,"INVALID")

        context?.let { prxSpecificationViewModel.fetchSpecification(it,productCtn) }

        return binding.root
    }
    

}