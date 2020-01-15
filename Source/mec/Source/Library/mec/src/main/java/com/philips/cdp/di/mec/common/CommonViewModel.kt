package com.philips.cdp.di.mec.common

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

open class CommonViewModel : ViewModel() {
    val mecError = MutableLiveData<MecError>()
}