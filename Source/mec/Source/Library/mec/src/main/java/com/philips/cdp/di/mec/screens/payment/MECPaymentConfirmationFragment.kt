package com.philips.cdp.di.mec.screens.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecPaymentConfirmationBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment

class MECPaymentConfirmationFragment : MecBaseFragment() {
    companion object {
        val TAG = "MECPaymentConfirmationFragment"
    }
    private lateinit var binding: MecPaymentConfirmationBinding
    private var mecPaymentConfirmationService = MECPaymentConfirmationService()

    override fun getFragmentTag(): String {
        return "MECPaymentConfirmationFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MecPaymentConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        context?.let { mecPaymentConfirmationService.getTitle(PaymentStatus.SUCCESS, it) }
    }


}