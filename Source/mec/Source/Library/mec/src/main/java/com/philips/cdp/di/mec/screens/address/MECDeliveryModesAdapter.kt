package com.philips.cdp.di.mec.screens.address


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecDeliveryModeItemBinding

class MECDeliveryModesAdapter(private val deliveryModes : MutableList<ECSDeliveryMode>,private val itemClickListener : ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var deliveryMode: ECSDeliveryMode

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MECDeliveryModeHolder(MecDeliveryModeItemBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun getItemCount(): Int {
        return deliveryModes.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        deliveryMode=deliveryModes.get(position)
       val mECDeliveryModeHolder = holder as MECDeliveryModeHolder
        mECDeliveryModeHolder.bind(deliveryMode,itemClickListener)
    }

    fun getSelectedDeliveryMode() : ECSDeliveryMode{
        return deliveryMode
    }
}