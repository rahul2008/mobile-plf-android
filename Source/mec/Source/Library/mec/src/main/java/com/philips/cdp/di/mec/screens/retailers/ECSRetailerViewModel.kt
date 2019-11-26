package com.philips.cdp.di.mec.screens.retailers

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import com.philips.cdp.di.ecs.model.asset.Asset
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ErrorViewModel
import com.philips.cdp.di.mec.screens.detail.ImageAdapter
import com.philips.platform.uid.view.widget.Label

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

        @JvmStatic
        @BindingAdapter("setStock")
        fun setStock(stockLabel: Label, stock: String) {
            if (stock.equals("YES")) {
                stockLabel.setText(R.string.mec_in_stock)
                stockLabel.setTextColor(ContextCompat.getColor(stockLabel.context, R.color.uid_signal_green_level_60))
            } else {
                stockLabel.setText(R.string.mec_out_of_stock)
                stockLabel.setTextColor(ContextCompat.getColor(stockLabel.context, R.color.uid_signal_red_level_60))
            }
        }
    }



}