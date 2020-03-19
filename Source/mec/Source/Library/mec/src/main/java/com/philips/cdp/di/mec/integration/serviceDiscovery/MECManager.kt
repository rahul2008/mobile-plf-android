package com.philips.cdp.di.mec.integration.serviceDiscovery

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData
import com.philips.cdp.di.mec.auth.HybrisAuth
import com.philips.cdp.di.mec.integration.MECListener
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECutility

/*
* Implementation of MECInterface exposed API
* */

class MECManager {

    // to be called by Proposition getProductCartCount() API call to show cart count
    fun getProductCartCountWorker(mecListener: MECListener){
        if(null!= MECDataHolder.INSTANCE.eCSServices) {
            MECDataHolder.INSTANCE.eCSServices.configureECSToGetConfiguration(object : ECSCallback<ECSConfig, Exception> {
                override fun onResponse(result: ECSConfig) {
                    if (result.isHybris && null != result!!.rootCategory) {
                        getShoppingCartData(mecListener)
                    } else {
                        //hybris not available
                        mecListener.onFailure(Exception(ECSErrorEnum.ECSHybrisNotAvailable.localizedErrorString))
                    }
                }

                override fun onFailure(error: Exception, ecsError: ECSError) {
                    mecListener.onFailure(error)
                }
            })
        }
    }

    //to be called by Catalog and Product Detail screen to show cart count
    fun getShoppingCartData(mecListener: MECListener){
        MECDataHolder.INSTANCE.eCSServices.fetchShoppingCart(object : ECSCallback<ECSShoppingCart, Exception> {
            override fun onResponse(carts: ECSShoppingCart?) {
                if (carts != null) {
                    val quantity = MECutility.getQuantity(carts)
                    mecListener.onGetCartCount(quantity)
                } else {
                    mecListener.onFailure(Exception(ECSErrorEnum.ECSsomethingWentWrong.localizedErrorString))
                }
            }

            override fun onFailure(error: Exception, ecsError: ECSError) {
                if (     MECutility.isAuthError(ecsError)) {
                    var authCallBack = object: ECSCallback<ECSOAuthData, Exception> {

                        override fun onResponse(result: ECSOAuthData?) {
                            getProductCartCountWorker(mecListener)
                        }

                        override fun onFailure(error: Exception, ecsError: ECSError) {
                            mecListener.onFailure(error)
                        }

                    }
                    HybrisAuth.hybrisAuthentication(authCallBack)

                }else {
                    mecListener.onFailure(error)
                }
            }
        })

    }
}