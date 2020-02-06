package com.philips.cdp.di.mec.screens.shoppingCart

import android.support.v7.widget.RecyclerView
import com.philips.cdp.di.ecs.model.voucher.ECSVoucher
import com.philips.cdp.di.mec.databinding.MecVoucherItemBinding

class MECVoucherHolder(val binding: MecVoucherItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(voucher : ECSVoucher){
        binding.voucher = voucher
    }
}