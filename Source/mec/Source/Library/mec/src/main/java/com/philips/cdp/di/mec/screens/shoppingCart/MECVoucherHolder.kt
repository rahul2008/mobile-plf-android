package com.philips.cdp.di.mec.screens.shoppingCart


import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.ecs.model.cart.AppliedVoucherEntity
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecVoucherItemBinding

class MECVoucherHolder(val binding: MecVoucherItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(voucher: AppliedVoucherEntity, itemClickListener: ItemClickListener){
        binding.voucher = voucher
        binding.mecCross.setOnClickListener { itemClickListener.onItemClick(voucher as Object) }
    }
}