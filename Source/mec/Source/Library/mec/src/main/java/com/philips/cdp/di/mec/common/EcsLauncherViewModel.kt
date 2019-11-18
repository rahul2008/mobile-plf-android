package com.philips.cdp.di.mec.common

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.ecsService.ECSLauncherRepository

class EcsLauncherViewModel : ErrorViewModel() {

    val isHybris = MutableLiveData<Boolean>()
    val ecsConfig = MutableLiveData<ECSConfig>()
    val ecsProduct = MutableLiveData<ECSProduct>()

    fun isHybris (){
        ECSLauncherRepository().configECS(this)
    }

    fun getEcsConfig(){
        ECSLauncherRepository().configECSToGetConfig(this)
    }

    fun getProductDetailForCtn(ctn:String){
        ECSLauncherRepository().getProductDetailForCtn(ctn,this)
    }

    fun getRetailerProductDetailForCtn(ctn:String){
        ECSLauncherRepository().getRetailerProductDetailForCtn(ctn,this)
    }


}