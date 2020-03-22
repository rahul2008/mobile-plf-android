package com.philips.cdp.di.mec.screens.payment

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecPaymentConfirmationBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.platform.pif.DataInterface.USR.UserDetailConstants
import java.util.*

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
        val arguments = arguments
        if (arguments == null || !arguments.containsKey(MECConstant.ORDER_NUMBER)) {
            arguments?.getString(MECConstant.ORDER_NUMBER)
            binding.tvOrderNumber.text=arguments?.getString(MECConstant.ORDER_NUMBER)
        }
        val detailKeys = ArrayList<String>()
        detailKeys.add(UserDetailConstants.EMAIL)
        val userDetails = MECDataHolder.INSTANCE.userDataInterface.getUserDetails(detailKeys)
        val email: String = userDetails.get(UserDetailConstants.EMAIL).toString();


        val mailText = SpannableString(getString(R.string.mec_confirmation_email_msg))
        val mailID = SpannableString(email)
        val  bolStyle:StyleSpan =  StyleSpan(Typeface.BOLD);
        mailID.setSpan(mailID,0,mailID.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        val result:String = TextUtils.concat(mailText,"\n" ,mailID).toString()
        binding.tvConfirmationEmailShortly.text = result
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        context?.let { mecPaymentConfirmationService.getTitle(PaymentStatus.SUCCESS, it) }
    }


}