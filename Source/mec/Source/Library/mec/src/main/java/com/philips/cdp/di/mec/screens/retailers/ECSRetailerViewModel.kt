package com.philips.cdp.di.mec.screens.retailers

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import com.philips.cdp.di.ecs.model.asset.Asset
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList
import com.philips.cdp.di.mec.common.ErrorViewModel
import com.philips.cdp.di.mec.screens.detail.ImageAdapter

class ECSRetailerViewModel : ErrorViewModel() {

    val ecsRetailerList = MutableLiveData<ECSRetailerList>()

    fun getRetailers (ctn:String){
        ECSRetailersRepository().getRetailers(ctn,this)
    }

    companion object DataBindingAdapter {
        @JvmStatic
        @BindingAdapter("retailers")
        fun setAdapter(recyclerView: RecyclerView, ecsRetailerList: ECSRetailerList) {
            recyclerView.adapter = MECRetailersAdapter(ecsRetailerList.retailers)
        }
    }
}