/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.cdp.di.mec.integration.serviceDiscovery

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData
import com.philips.cdp.di.mec.auth.HybrisAuth
import com.philips.cdp.di.mec.integration.CartListener
import com.philips.cdp.di.mec.integration.MECCartUpdateListener
import com.philips.cdp.di.mec.integration.MECFetchCartListener
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECutility

/*
* Implementation of MECInterface exposed API
* */

class MECManager {

    // to be called by Proposition getProductCartCount() API call to show cart count
    fun getProductCartCountWorker(cartListener: CartListener){
        if(null!= MECDataHolder.INSTANCE.eCSServices) {
            MECDataHolder.INSTANCE.eCSServices.configureECSToGetConfiguration(object : ECSCallback<ECSConfig, Exception> {
                override fun onResponse(result: ECSConfig) {
                    if (result.isHybris && null != result!!.rootCategory) {
                        getShoppingCartData(cartListener)
                    } else {
                        //hybris not available
                        cartListener.onFailure(Exception(ECSErrorEnum.ECSHybrisNotAvailable.localizedErrorString))
                    }
                }

                override fun onFailure(error: Exception, ecsError: ECSError) {
                    cartListener.onFailure(error)
                }
            })
        }
    }

    //to be called by Catalog and Product Detail screen to show cart count
    fun getShoppingCartData(cartListener: CartListener){
        MECDataHolder.INSTANCE.eCSServices.fetchShoppingCart(object : ECSCallback<ECSShoppingCart, Exception> {
            override fun onResponse(carts: ECSShoppingCart?) {
                if (carts != null) {
                    val quantity = MECutility.getQuantity(carts)
                    if(cartListener is MECFetchCartListener){
                        cartListener.onGetCartCount(quantity)
                    }else if(cartListener is MECCartUpdateListener){
                        cartListener.onUpdateCartCount(quantity)
                    }
                } else {
                    cartListener.onFailure(Exception(ECSErrorEnum.ECSsomethingWentWrong.localizedErrorString))
                }
            }

            override fun onFailure(error: Exception, ecsError: ECSError) {
                if (     MECutility.isAuthError(ecsError)) {
                    var authCallBack = object: ECSCallback<ECSOAuthData, Exception> {

                        override fun onResponse(result: ECSOAuthData?) {
                            getProductCartCountWorker(cartListener)
                        }

                        override fun onFailure(error: Exception, ecsError: ECSError) {
                            cartListener.onFailure(error)
                        }

                    }
                    HybrisAuth.hybrisAuthentication(authCallBack)

                }else {
                    cartListener.onFailure(error)
                }
            }
        })

    }
}