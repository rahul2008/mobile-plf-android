package com.philips.cdp.di.mec.screens.profile


import androidx.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.address.ECSUserProfile
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.utils.MECDataHolder

class ProfileViewModel : CommonViewModel() {


    val userProfile = MutableLiveData<ECSUserProfile>()

    private var ecsUserProfileCallBack = ECSUserProfileCallBack(this)

    var ecsServices = MecHolder.INSTANCE.eCSServices

    var profileRepository = ProfileRepository(ecsServices)

    fun fetchUserProfile(){
        profileRepository.fetchUserProfile(ecsUserProfileCallBack)
    }


}