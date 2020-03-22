package com.philips.cdp.di.mec.screens.payment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecPaymentConfirmationBinding
import com.philips.cdp.di.mec.payment.MECWebPaymentFragment
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
        if (arguments != null && arguments.containsKey(MECConstant.ORDER_NUMBER)) {
            binding.tvOrderNumber.visibility=View.VISIBLE
            binding.tvOrderNumberVal.visibility=View.VISIBLE
            arguments?.getString(MECConstant.ORDER_NUMBER)
            binding.tvOrderNumberVal.text=arguments?.getString(MECConstant.ORDER_NUMBER)
        }
        val detailKeys = ArrayList<String>()
        detailKeys.add(UserDetailConstants.EMAIL)
        val userDetails = MECDataHolder.INSTANCE.userDataInterface.getUserDetails(detailKeys)
        val email: String = userDetails.get(UserDetailConstants.EMAIL).toString();


        ///////////
        val emailConfirmation = getString(R.string.mec_confirmation_email_msg)
        val boldCount: Spanned
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            boldCount = Html.fromHtml("$emailConfirmation  <b>$email</b>", Html.FROM_HTML_MODE_LEGACY)
        } else {
            boldCount = Html.fromHtml("$emailConfirmation  <b>$email</b>")
        }
        //////////


        binding.tvConfirmationEmailShortly.text = boldCount
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        context?.let { mecPaymentConfirmationService.getTitle(PaymentStatus.SUCCESS, it) }
    }

    fun onClickOk(){
        showProductCatalogFragment(MECWebPaymentFragment.TAG)
    }


}