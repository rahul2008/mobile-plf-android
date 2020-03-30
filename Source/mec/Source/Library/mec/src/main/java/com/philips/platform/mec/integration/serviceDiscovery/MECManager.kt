/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.platform.mec.integration.serviceDiscovery

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData
import com.philips.platform.mec.utils.MECDataHolder
import com.philips.platform.mec.utils.MECutility
import com.philips.platform.pif.DataInterface.MEC.listeners.MECCartUpdateListener
import com.philips.platform.pif.DataInterface.MEC.listeners.MECFetchCartListener
import com.philips.platform.pif.DataInterface.MEC.listeners.MECHybrisAvailabilityListener

/*
* Implementation of MECInterface exposed API
* @since 2002.0
* */

class MECManager {

    // to be called by Proposition to check if Hybris available
    fun ishybrisavailableWorker(mECHybrisAvailabilityListener : MECHybrisAvailabilityListener){
        if(null!= MECDataHolder.INSTANCE.eCSServices) {
            MECDataHolder.INSTANCE.eCSServices.configureECS(object : ECSCallback<Boolean,java.lang.Exception> {
                override fun onResponse(result: Boolean) {
                    mECHybrisAvailabilityListener.isHybrisAvailable(result)
                }
                override fun onFailure(error: java.lang.Exception, ecsError: ECSError) {
                    mECHybrisAvailabilityListener.isHybrisAvailable(false)
                }
            })
        }
    }

    // to be called by Proposition getProductCartCount() API call to show cart count
    fun getProductCartCountWorker(mECFetchCartListener: MECFetchCartListener){
        if(null!= MECDataHolder.INSTANCE.eCSServices) {
            MECDataHolder.INSTANCE.eCSServices.configureECSToGetConfiguration(object : ECSCallback<ECSConfig, Exception> {
                override fun onResponse(result: ECSConfig) {
                    if (result.isHybris && null != result!!.rootCategory) {

                        getShoppingCartData(object : MECCartUpdateListener {
                            override fun onUpdateCartCount(count: Int) {
                                mECFetchCartListener.onGetCartCount(count)
                            }
                            override fun shouldShowCart(shouldShow: Boolean?) {
                               // do nothing
                            }
                        })
                    } else {
                        //hybris not available
                        mECFetchCartListener.onFailure(Exception(ECSErrorEnum.ECSHybrisNotAvailable.localizedErrorString))
                    }
                }
                override fun onFailure(error: Exception, ecsError: ECSError) {
                    mECFetchCartListener.onFailure(error)
                }
            })
        }
    }

    //to be called by Catalog and Product Detail screen to show cart count
    fun getShoppingCartData(mECCartUpdateListener: MECCartUpdateListener){
        MECDataHolder.INSTANCE.eCSServices.fetchShoppingCart(object : ECSCallback<ECSShoppingCart, Exception> {
            override fun onResponse(carts: ECSShoppingCart?) {
                if (carts != null) {
                    val quantity = MECutility.getQuantity(carts)
                    mECCartUpdateListener.onUpdateCartCount(quantity)

                }
            }

            override fun onFailure(error: Exception, ecsError: ECSError) {
                if (     MECutility.isAuthError(ecsError)) {
                    var authCallBack = object: ECSCallback<ECSOAuthData, Exception> {

                        override fun onResponse(result: ECSOAuthData?) {
                            getShoppingCartData(mECCartUpdateListener)
                        }

                        override fun onFailure(error: Exception, ecsError: ECSError) {
                            //do nothing on auth failure
                        }
                    }
                    com.philips.platform.mec.auth.HybrisAuth.hybrisAuthentication(authCallBack)

                }
            }
        })

    }
}