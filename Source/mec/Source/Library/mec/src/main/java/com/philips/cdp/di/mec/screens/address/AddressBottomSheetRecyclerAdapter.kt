package com.philips.cdp.di.mec.screens.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.mec.databinding.MecAddressCardBinding


class AddressBottomSheetRecyclerAdapter (private val mecAddresses: MECAddresses) : RecyclerView.Adapter<AddressBottomSheetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): AddressBottomSheetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MecAddressCardBinding.inflate(inflater)
        return AddressBottomSheetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mecAddresses.ecsAddresses.size
    }

    override fun onBindViewHolder(viewHolder : AddressBottomSheetViewHolder, position: Int) {
       viewHolder.bind(mecAddresses.ecsAddresses[position])
    }
}