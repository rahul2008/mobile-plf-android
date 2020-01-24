package com.philips.cdp.di.mec.auth

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.integration.ECSOAuthProvider
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.platform.pif.DataInterface.USR.UserDataInterface
import com.philips.platform.pif.DataInterface.USR.UserDetailConstants
import java.util.ArrayList

class HybrisAuth {


    companion object {



        fun hybrisAuthentication(ecsCallback: ECSCallback<ECSOAuthData, Exception>) {
            val oAuthInput = object : ECSOAuthProvider() {
                override fun getOAuthID(): String? {
                    return getMyJanRainID()
                }
            }
            MecHolder.INSTANCE.eCSServices.hybrisOAthAuthentication(oAuthInput,ecsCallback)
        }

        fun refreshToken(ecsCallback: ECSCallback<ECSOAuthData, Exception>){

           // MECDataHolder.INSTANCE.userDataInterface.refreshSession(object RefreshSessionListener)



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