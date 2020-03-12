package com.philips.cdp.di.mec.screens.orderSummary


import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.ecs.model.cart.AppliedVoucherEntity
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecOrderSummaryVoucherItemBinding

class MECOrderSummaryVoucherHolder(val binding: MecOrderSummaryVoucherItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(voucher: AppliedVoucherEntity) {
        binding.voucher = voucher
    }
}