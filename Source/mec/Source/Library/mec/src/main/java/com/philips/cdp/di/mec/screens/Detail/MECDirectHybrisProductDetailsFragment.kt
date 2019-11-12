package com.philips.cdp.di.mec.screens.detail


import android.support.v4.app.Fragment
import com.philips.cdp.di.mec.utils.MECConstant
import kotlinx.android.synthetic.main.mec_main_activity.*

/**
 * A simple [Fragment] subclass.
 */
class MECDirectHybrisProductDetailsFragment : MECProductDetailsFragment() {

    lateinit var ctn:String

    override fun executeRequest() {
        createCustomProgressBar(container, MEDIUM)
        val bundle = arguments
        var ctn = bundle!!.getString(MECConstant.MEC_PRODUCT_CTN_NUMBER_FROM_VERTICAL)
        ecsProductDetailViewModel.getProductDetailForCtn(ctn)
    }

}
