package com.philips.cdp.di.mec.common

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.ecsService.ECSLauncherRepository
import com.philips.cdp.di.mec.integration.MecHolder

class EcsLauncherViewModel : CommonViewModel() {

    val isHybris = MutableLiveData<Boolean>()
    val ecsConfig = MutableLiveData<ECSConfig>()
    val ecsProduct = MutableLiveData<ECSProduct>()

    var ecsServices = MecHolder.INSTANCE.eCSServices

    var ecsLauncherRepository = ECSLauncherRepository(ecsServices,this)

    fun isHybris (){
        ecsLauncherRepository.configECS()
    }

    fun getEcsConfig(){
        ecsLauncherRepository.configECSToGetConfig()
    }

    fun getProductDetailForCtn(ctn:String){
        ecsLauncherRepository.getProductDetailForCtn(ctn)
    }

    fun getRetailerProductDetailForCtn(ctn:String){
        ecsLauncherRepository.getRetailerProductDetailForCtn(ctn)
    }


}