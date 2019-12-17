package com.philips.cdp.di.mec.screens.catalog

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.products.ECSProducts

fun mutableList(mutableLiveData: MutableLiveData<MutableList<ECSProducts>>): MutableList<ECSProducts> {
    if (mutableLiveData.value.isNullOrEmpty()) mutableLiveData.value = mutableListOf<ECSProducts>()
    return mutableLiveData.value!!
}