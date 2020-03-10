package com.philips.cdp.di.mec.payment

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.mec.databinding.MecBillingAddressEditCardBinding


class AddressBillingEditFooterHolder (val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ecsAddress: ECSAddress) {
        val mecPaymentCardBinding = binding as MecBillingAddressEditCardBinding
        mecPaymentCardBinding.ecsAddress = ecsAddress
    }
}