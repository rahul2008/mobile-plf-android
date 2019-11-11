package com.philips.cdp.di.mec.screens.Detail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecProductDetailsBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment

/**
 * A simple [Fragment] subclass.
 */
class MECProductDetailsFragment : MecBaseFragment() {

    private lateinit var binding: MecProductDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MecProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_product_detail, true)
    }

    override fun handleBackEvent(): Boolean {
        return super.handleBackEvent()
    }

    public fun createInstance(args: Bundle): MECProductDetailsFragment {
        val fragment = MECProductDetailsFragment()
        fragment.arguments = args
        return fragment
    }


}
