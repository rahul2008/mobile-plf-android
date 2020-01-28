package com.philips.cdp.di.mec.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class CommonViewModel : ViewModel() {
    val mecError = MutableLiveData<MecError>()
}