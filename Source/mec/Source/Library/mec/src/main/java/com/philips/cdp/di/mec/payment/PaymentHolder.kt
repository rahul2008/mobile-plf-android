package com.philips.cdp.di.mec.payment

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.ecs.model.payment.ECSPayment
import com.philips.cdp.di.mec.databinding.MecPaymentCardBinding


class PaymentHolder (val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MECPayment) {
        val mecPaymentCardBinding = binding as MecPaymentCardBinding
        mecPaymentCardBinding.mecPayment = item
    }
}