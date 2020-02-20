package com.philips.cdp.di.mec.screens.address

import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecDeliveryModeItemBinding


class MECDeliveryModeHolder(val binding: MecDeliveryModeItemBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(deliveryMode :ECSDeliveryMode ,  itemClickListener: ItemClickListener){
        binding.deliveryMode=deliveryMode

    }
}