package com.philips.cdp.di.mec.common

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData
import com.philips.cdp.di.mec.auth.HybrisAuth

open class CommonViewModel : ViewModel() {
    val mecError = MutableLiveData<MecError>()

    fun authAndCallAPIagain(retryAPIcall: () -> Unit, authFailureCallback: (Exception, ECSError) -> Unit){
        val authCallback= object: ECSCallback<ECSOAuthData, Exception> {
            override fun onResponse(result: ECSOAuthData?) {
                retryAPIcall.invoke()
            }
            override fun onFailure(error: Exception, ecsError: ECSError) {
                authFailureCallback.invoke(error,ecsError)
            }
        }
        HybrisAuth.hybrisRefreshAuthentication(authCallback)
    }
}