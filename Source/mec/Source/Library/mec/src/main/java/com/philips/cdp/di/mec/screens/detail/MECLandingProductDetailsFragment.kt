package com.philips.cdp.di.mec.screens.detail

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import kotlinx.android.synthetic.main.mec_main_activity.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
 class MECLandingProductDetailsFragment : MECProductDetailsFragment() {
    companion object {
        val TAG:String="MECLandingProductDetailsFragment"
    }

  override fun executeRequest() {

    fetchProductDetailForCtn(MECDataHolder.INSTANCE.hybrisEnabled)
  }

  private fun fetchProductDetailForCtn(isHybris: Boolean) {

    createCustomProgressBar(container, MEDIUM)

    if (isHybris) {

      fetchProductForHybris()

    } else {

       fetchProductForRetailers()


    }
  }

  private fun fetchProductForRetailers() {

    MecHolder.INSTANCE.eCSServices.fetchProductSummaries(Arrays.asList(product.code) , object :  ECSCallback<List<ECSProduct>, Exception>{
      override fun onResponse(result: List<ECSProduct>?) {
        product = result?.get(0) ?: product
        callParentExecute()
      }

      override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError)
        processError(mecError)
      }
    })
  }

  private fun fetchProductForHybris() {
    MecHolder.INSTANCE.eCSServices.fetchProduct(product.code, object : ECSCallback<ECSProduct, Exception> {
      override fun onResponse(result: ECSProduct?) {
        product = result!!
        callParentExecute()
      }

      override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError)
        processError(mecError)
      }
    })
  }

  private fun callParentExecute(){
    super.executeRequest()
  }

}
