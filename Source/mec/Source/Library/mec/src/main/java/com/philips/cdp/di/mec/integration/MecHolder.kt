package com.philips.cdp.di.mec.integration

import com.philips.cdp.di.ecs.ECSServices

enum class MecHolder {

    INSTANCE;

    lateinit var eCSServices: ECSServices


}