package com.philips.cdp.di.mec.common

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.mec.common.ecsService.ECSConfigServiceRepository

class EcsConfigViewModel : ErrorViewModel() {

    val isHybris = MutableLiveData<Boolean>()
    val ecsConfig = MutableLiveData<ECSConfig>()

    fun isHybris (){
        ECSConfigServiceRepository().configECS(this)
    }

    fun getEcsConfig(){

    }

}