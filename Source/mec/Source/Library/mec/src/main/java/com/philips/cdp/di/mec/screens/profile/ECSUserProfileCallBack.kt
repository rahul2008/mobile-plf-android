package com.philips.cdp.di.mec.screens.profile

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.address.ECSUserProfile
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError

class ECSUserProfileCallBack(private var ecsProfileViewModel: ProfileViewModel) :ECSCallback<ECSUserProfile, Exception> {

    var mECRequestType : MECRequestType?=MECRequestType.MEC_FETCH_USER_PROFILE
    override fun onResponse(userProfile: ECSUserProfile) {
        ecsProfileViewModel.userProfile.value = userProfile
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError,mECRequestType)
        ecsProfileViewModel.mecError.value = mecError
    }
}