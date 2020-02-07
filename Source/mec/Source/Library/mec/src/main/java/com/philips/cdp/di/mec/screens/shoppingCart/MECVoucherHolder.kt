package com.philips.cdp.di.mec.screens.shoppingCart

import android.support.v7.widget.RecyclerView
import com.philips.cdp.di.ecs.model.cart.AppliedVoucherEntity
import com.philips.cdp.di.mec.databinding.MecVoucherItemBinding

class MECVoucherHolder(val binding: MecVoucherItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(voucher: AppliedVoucherEntity){
        binding.voucher = voucher
    }
}