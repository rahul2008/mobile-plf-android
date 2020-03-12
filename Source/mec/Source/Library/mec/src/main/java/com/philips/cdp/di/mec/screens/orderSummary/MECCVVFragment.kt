package com.philips.cdp.di.mec.screens.orderSummary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.philips.cdp.di.ecs.model.payment.ECSPayment
import com.philips.cdp.di.mec.databinding.MecCvcCodeFragmentBinding
import com.philips.cdp.di.mec.utils.MECConstant
import kotlinx.android.synthetic.main.mec_cvc_code_fragment.view.*

class MECCVVFragment: BottomSheetDialogFragment() {

   private  lateinit var ecsPayment: ECSPayment
    private lateinit var binding: MecCvcCodeFragmentBinding
    private lateinit var securityCode: String

    companion object {
        val TAG:String="MECCVVFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MecCvcCodeFragmentBinding.inflate(inflater,container,false)

        var bundle= arguments
        ecsPayment=bundle?.getSerializable(MECConstant.MEC_PAYMENT_METHOD) as ECSPayment
        binding.paymentMethod=ecsPayment

        return binding.root
    }

    fun onClickContinue(){
        securityCode=binding.root.mec_cvv_digits.toString()
        if(securityCode.trim().length>0){
            //todo
        }

    }


    /*fun onClickCancel(){
        this.dismiss()
    }*/
}