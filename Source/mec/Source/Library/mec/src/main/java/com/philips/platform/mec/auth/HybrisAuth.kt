/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.platform.mec.auth

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.integration.ECSOAuthProvider
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData
import com.philips.cdp.di.ecs.util.ECSConfiguration
import com.philips.platform.mec.utils.MECDataHolder
import com.philips.platform.mec.utils.MECutility
import com.philips.platform.appinfra.securestorage.SecureStorageInterface
import com.philips.platform.pif.DataInterface.USR.UserDetailConstants
import com.philips.platform.pif.DataInterface.USR.enums.Error
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshSessionListener
import java.util.*


class HybrisAuth {


    companion object {

        private val refreshTokenKey: String = "mec_hybrisRefreshTokenKey"

        fun getJanrainAuthInput(): ECSOAuthProvider {
            val oAuthInput = object : ECSOAuthProvider() {
                override fun getOAuthID(): String? {
                    return com.philips.platform.mec.auth.HybrisAuth.Companion.getMyJanRainID()
                }
            }
            return oAuthInput
        }

        fun getMyJanRainID(): String? {
            val detailsKey = ArrayList<String>()
            detailsKey.add(UserDetailConstants.ACCESS_TOKEN)
            try {
                val userDetailsMap = MECDataHolder.INSTANCE.userDataInterface.getUserDetails(detailsKey)
                return userDetailsMap.get(UserDetailConstants.ACCESS_TOKEN)!!.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }


        fun hybrisAuthentication(fragmentCallback: ECSCallback<ECSOAuthData, Exception>) {

            val hybrisCallback = object : ECSCallback<ECSOAuthData, Exception> {

                override fun onResponse(result: ECSOAuthData?) {
                    MECDataHolder.INSTANCE.refreshToken = result!!.refreshToken

                    // store refreshTokenKey in secure storage
                    val sse = SecureStorageInterface.SecureStorageError() // to get error code if any
                    MECDataHolder.INSTANCE.appinfra.secureStorage.storeValueForKey(com.philips.platform.mec.auth.HybrisAuth.Companion.refreshTokenKey, result!!.refreshToken, sse)
                    fragmentCallback.onResponse(result)
                }

                override fun onFailure(error: Exception?, ecsError: ECSError?) {
                    if (MECutility.isAuthError(ecsError)){
                        com.philips.platform.mec.auth.HybrisAuth.Companion.refreshJainrain(fragmentCallback);
                    } else {
                        fragmentCallback.onFailure(error, ecsError)
                    }
                }
            }

            MECDataHolder.INSTANCE.eCSServices.hybrisOAthAuthentication(com.philips.platform.mec.auth.HybrisAuth.Companion.getJanrainAuthInput(), hybrisCallback)
        }


        fun hybrisRefreshAuthentication(fragmentCallback: ECSCallback<ECSOAuthData, Exception>) {

            val oAuthInput = object : ECSOAuthProvider() {
                override fun getOAuthID(): String? {
                    return MECDataHolder.INSTANCE.refreshToken
                }

            }
            val hybrisCallback = object : ECSCallback<ECSOAuthData, Exception> {
                override fun onResponse(result: ECSOAuthData?) {
                    ECSConfiguration.INSTANCE.setAuthToken(result!!.accessToken)
                    MECDataHolder.INSTANCE.refreshToken = result.refreshToken
                    // store refreshTokenKey in secure storage
                    val sse = SecureStorageInterface.SecureStorageError() // to get error code if any
                    MECDataHolder.INSTANCE.appinfra.secureStorage.storeValueForKey(com.philips.platform.mec.auth.HybrisAuth.Companion.refreshTokenKey, result!!.refreshToken, sse)
                    fragmentCallback.onResponse(result) // send call back to fragment or view

                }

                override fun onFailure(error: Exception?, ecsError: ECSError?) {
                    if (MECutility.isAuthError(ecsError)) {
                        com.philips.platform.mec.auth.HybrisAuth.Companion.refreshJainrain(fragmentCallback);
                    } else {

                        ECSConfiguration.INSTANCE.setAuthToken(null)
                        fragmentCallback.onFailure(error, ecsError)
                    }
                }
            }
            MECDataHolder.INSTANCE.eCSServices.hybrisRefreshOAuth(oAuthInput, hybrisCallback)
        }


        fun refreshJainrain(fragmentCallback: ECSCallback<ECSOAuthData, Exception>) {
            val refreshSessionListener = object : RefreshSessionListener {
                override fun refreshSessionSuccess() {

                    com.philips.platform.mec.auth.HybrisAuth.Companion.hybrisAuthentication(fragmentCallback)

                }

                override fun refreshSessionFailed(error: Error?) {
                    val ecsError = ECSError(5000, ECSErrorEnum.ECSinvalid_grant.name)
                    val exception = java.lang.Exception()
                    fragmentCallback.onFailure(exception, ecsError)
                }

                override fun forcedLogout() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }
            MECDataHolder.INSTANCE.userDataInterface.refreshSession(refreshSessionListener)
        }


        fun authHybrisIfNotAlready() {
            // if Hybris is enabled and user logged in
            if (MECDataHolder.INSTANCE.hybrisEnabled && MECDataHolder.INSTANCE.userDataInterface != null && MECDataHolder.INSTANCE.userDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN) {
                if (null == MECDataHolder.INSTANCE.refreshToken) {
                    val sse = SecureStorageInterface.SecureStorageError() // to get error code if any
                    val hybrisRrefreshToken: String? = MECDataHolder.INSTANCE.appinfra.secureStorage.fetchValueForKey(com.philips.platform.mec.auth.HybrisAuth.Companion.refreshTokenKey, sse)
                    if (null == sse.errorCode && null != hybrisRrefreshToken) {
                        //if refresh token is already saved in device secure storage and save it to instance
                        MECDataHolder.INSTANCE.refreshToken = hybrisRrefreshToken
                    } else {
                        // if refresh token is NOT already present then call Auth Hybris
                        val hybrisAuthCallback = object : ECSCallback<ECSOAuthData, Exception> {
                            override fun onResponse(eCSOAuthData: ECSOAuthData?) {

                            }

                            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                                // control should not come here in normal circumstances
                            }
                        }
                        com.philips.platform.mec.auth.HybrisAuth.Companion.hybrisAuthentication(hybrisAuthCallback)

                    }
                }

            }

        }
    }


}