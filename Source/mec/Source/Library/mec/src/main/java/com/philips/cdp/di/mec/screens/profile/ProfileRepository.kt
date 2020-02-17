package com.philips.cdp.di.mec.screens.profile

import com.philips.cdp.di.ecs.ECSServices

class ProfileRepository(val ecsServices: ECSServices) {


    fun fetchUserProfile(ecsUserProfileCallBack: ECSUserProfileCallBack) {
        ecsServices.fetchUserProfile(ecsUserProfileCallBack)
    }
}