package com.philips.cdp.di.mec.screens.features

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecProductFeaturesFragmentBinding
import com.philips.cdp.di.mec.databinding.MecProductSpecsFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.prxclient.datamodels.features.FeaturesModel

class MECProductFeaturesFragment : MecBaseFragment() {

    private lateinit var binding: MecProductFeaturesFragmentBinding
    private lateinit var productFeaturesViewModel: ProductFeaturesViewModel

    private val featuresObserver : Observer<FeaturesModel> = object : Observer<FeaturesModel> {

        override fun onChanged(featuresModel: FeaturesModel?) {
           binding.featureModel = featuresModel
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecProductFeaturesFragmentBinding.inflate(inflater, container, false)

        productFeaturesViewModel = ViewModelProviders.of(this).get(ProductFeaturesViewModel::class.java)

        productFeaturesViewModel.mecError.observe(this,this)
        productFeaturesViewModel.features.observe(this,featuresObserver)

        val bundle = arguments
        val productCtn = bundle!!.getString(MECConstant.MEC_PRODUCT_CTN,"INVALID")

        context?.let { productFeaturesViewModel.fetchProductFeatures(it,productCtn) }

        return binding.root
    }

}