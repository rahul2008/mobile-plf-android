package com.philips.cdp.di.mec.integration

import com.philips.cdp.di.ecs.ECSServices
import com.philips.platform.appinfra.AppInfra

enum class MecHolder {

    INSTANCE;


    lateinit var eCSServices: ECSServices


}