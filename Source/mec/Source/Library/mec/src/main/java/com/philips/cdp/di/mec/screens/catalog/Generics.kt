package com.philips.cdp.di.mec.screens.catalog

import androidx.lifecycle.MutableLiveData

fun mutableList(mutableLiveData: MutableLiveData<MutableList<Any>>): MutableList<Any> {
    if (mutableLiveData.value.isNullOrEmpty()) mutableLiveData.value = mutableListOf()
    return mutableLiveData.value!!
}