package com.philips.cdp.di.mec.screens.address

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.mec.databinding.MecAddressCardBinding

class AddressBottomSheetViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(ecsAddress: ECSAddress) {
        val mecAddressCardBinding = binding as MecAddressCardBinding
        mecAddressCardBinding.ecsAddress = ecsAddress
    }
}