package com.philips.cdp.di.mec.screens.Detail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecProductReviewFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment

/**
 * A simple [Fragment] subclass.
 */
class MECProductReviewsFragment : MecBaseFragment() {

    private lateinit var binding: MecProductReviewFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MecProductReviewFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


}
