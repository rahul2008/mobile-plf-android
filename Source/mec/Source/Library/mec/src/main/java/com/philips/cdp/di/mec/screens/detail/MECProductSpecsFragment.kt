package com.philips.cdp.di.mec.screens.detail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecProductSpecsFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment


class MECProductSpecsFragment : MecBaseFragment() {

    private lateinit var binding : MecProductSpecsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MecProductSpecsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


}
