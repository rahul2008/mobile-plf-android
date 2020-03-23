package com.philips.cdp.di.mec.screens.orderSummary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail
import com.philips.cdp.di.ecs.model.payment.ECSPayment
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.databinding.MecCvcCodeFragmentBinding
import com.philips.cdp.di.mec.paymentServices.PaymentViewModel
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.payment.MECPaymentConfirmationFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECLog
import com.philips.cdp.di.mec.utils.MECutility
import kotlinx.android.synthetic.main.mec_cvc_code_fragment.view.*

class MECCVVFragment: BottomSheetDialogFragment() {

    private lateinit var binding: MecCvcCodeFragmentBinding
    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var orderNumber :String

    companion object {
        const val TAG:String="MECCVVFragment"
    }

    private val orderDetailObserver: Observer<ECSOrderDetail> = Observer(fun(ecsOrderDetail: ECSOrderDetail) {
        MECLog.d(javaClass.simpleName ,ecsOrderDetail.code)
        orderNumber=ecsOrderDetail.getCode()
        binding.root.mec_progress.visibility = View.GONE
        gotoPaymentConfirmationFragment()
    })

    private val errorObserver: Observer<MecError> = Observer(fun(mecError: MecError?) {
        MECutility.tagAndShowError(mecError, true, fragmentManager, context)
        binding.root.mec_progress.visibility = View.GONE
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MecCvcCodeFragmentBinding.inflate(inflater,container,false)
        binding.fragment=this
        var bundle= arguments
        var ecsPayment=bundle?.getSerializable(MECConstant.MEC_PAYMENT_METHOD) as ECSPayment
        binding.paymentMethod=ecsPayment

        paymentViewModel =  ViewModelProviders.of(this).get(PaymentViewModel::class.java)
        paymentViewModel.ecsOrderDetail.observe(this,orderDetailObserver)
        paymentViewModel.mecError.observe(this,errorObserver)

        return binding.root
    }

    fun onClickContinue(){ // TODO pass the text from editText through the method
        binding.root.mec_progress.visibility = View.VISIBLE
        var cvv=binding.root.mec_cvv_digits.text.toString()
        if(cvv.trim().isNotEmpty()) paymentViewModel.submitOrder(cvv) else binding.root.mec_cvv_digits.startAnimation(MECutility.getShakeAnimation())

    }

    private fun gotoPaymentConfirmationFragment(){
        val mecPaymentConfirmationFragment : MECPaymentConfirmationFragment = MECPaymentConfirmationFragment()
        val bundle = Bundle()
        bundle.putString(MECConstant.ORDER_NUMBER, orderNumber)
        bundle.putBoolean(MECConstant.PAYMENT_SUCCESS_STATUS, java.lang.Boolean.TRUE)
        mecPaymentConfirmationFragment.arguments = bundle
        replaceFragment(mecPaymentConfirmationFragment, false)
    }

    private fun replaceFragment(newFragment: MecBaseFragment, isReplaceWithBackStack: Boolean) {
        if (MECDataHolder.INSTANCE.actionbarUpdateListener == null || MECDataHolder.INSTANCE.mecListener == null)
            RuntimeException("ActionBarListner and MECListner cant be null")
        else {
            if (null!=activity && !activity!!.isFinishing) {

                val transaction = activity!!.supportFragmentManager.beginTransaction()
                val simpleName = newFragment.javaClass.simpleName
                transaction.replace(id, newFragment, simpleName)
                if (isReplaceWithBackStack) {
                    transaction.addToBackStack(simpleName)
                }
                transaction.commitAllowingStateLoss()
            }
        }
    }

}