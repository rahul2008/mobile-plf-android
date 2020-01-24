package com.philips.cdp.di.mec.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.integration.ECSOAuthProvider
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData
import com.philips.cdp.di.ecs.util.ECSConfiguration
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.utils.MECDataHolder

import com.philips.platform.pif.DataInterface.USR.UserDetailConstants
import com.philips.platform.pif.DataInterface.USR.enums.Error
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshSessionListener
import java.util.ArrayList


class HybrisAuth {



    companion object {

        private val sharedPrefFile = "mecRefreshJainrainTokenFile"
        val janrainRefreshToken: String = "jainRefTok"


        fun hybrisAuthentication(fragmentCallback : ECSCallback<ECSOAuthData, Exception>) {
            val oAuthInput = object : ECSOAuthProvider() {
                override fun getOAuthID(): String? {
                    return getMyJanRainID()
                }
            }

            val hybrisCallback= object: ECSCallback<ECSOAuthData, Exception> {
                /**
                 * On response.
                 *
                 * @param result the result
                 */
                override fun onResponse(result: ECSOAuthData?) {
                    ECSConfiguration.INSTANCE.setAuthToken(result!!.getAccessToken())
                    MECDataHolder.INSTANCE.refreshToken=result!!.refreshToken
                    Log.d("HYBRIS AUTH succ", result.accessToken)
                   /* val sharedPreferences: SharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor =  sharedPreferences.edit()*/
                    fragmentCallback.onResponse(result) // send call back to fragment or view

                }

                /**
                 * On failure.
                 * @param error     the error object
                 * @param ecsError the error code
                 */
                override fun onFailure(error: Exception?, ecsError: ECSError?) {

                    if (       ecsError!!.errorcode == ECSErrorEnum.ECSInvalidTokenError.errorCode
                            || ecsError!!.errorcode == ECSErrorEnum.ECSinvalid_grant.errorCode
                            || ecsError!!.errorcode == ECSErrorEnum.ECSinvalid_client.errorCode){
                        refreshToken(fragmentCallback);
                    } else  {

                        ECSConfiguration.INSTANCE.setAuthToken(null)
                        fragmentCallback.onFailure(error,ecsError)
                    }
                }
            }


            MecHolder.INSTANCE.eCSServices.hybrisOAthAuthentication(oAuthInput,hybrisCallback)
        }

        fun refreshToken(fragmentCallback : ECSCallback<ECSOAuthData, Exception>){
           val refreshSessionListener = object : RefreshSessionListener {
                override fun refreshSessionSuccess() {
                    //re OAuth after refreshSession for janrain
                    val oAuthInput = object : ECSOAuthProvider() {
                        override fun getOAuthID(): String {
                            return getMyJanRainID().toString()
                        }

                    }

                    //ReOAuth starts =======================
                    MecHolder.INSTANCE.eCSServices.hybrisRefreshOAuth(oAuthInput, object : ECSCallback<ECSOAuthData, Exception> {
                        override fun onResponse(result: ECSOAuthData) {
                            ECSConfiguration.INSTANCE.setAuthToken(result.accessToken)
                            ECSConfiguration.INSTANCE.authResponse = result
                            Log.d("ECS succ", result.accessToken)
                            fragmentCallback.onResponse(result)

                            try {
                               //todo mIapInterface.getProductCartCount(this@EcsDemoAppActivity)
                            } catch (e: Exception) {
                                ECSConfiguration.INSTANCE.setAuthToken(null)
                            }

                        }

                        override fun onFailure(error: Exception, ecsError: ECSError) {

                            Log.d("ECS Oauth failed", error.message + " :  " + ecsError)
                            ECSConfiguration.INSTANCE.setAuthToken(null)
                            fragmentCallback.onFailure(error,ecsError)

                        }
                    })

                    // ReOAuth ends  =====================

                }

                override fun refreshSessionFailed(error: Error?) {
                    TODO("not implemented if server failed to respond") //To change body of created functions use File | Settings | File Templates.
                }

                override fun forcedLogout() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }
            MECDataHolder.INSTANCE.userDataInterface.refreshSession(refreshSessionListener)
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
    }


}