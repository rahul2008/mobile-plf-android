package com.philips.cdp.di.mec.screens.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.mec.databinding.MecAddressCardBinding
import com.philips.cdp.di.mec.databinding.MecAddressCreateCardBinding


class AddressBottomSheetRecyclerAdapter (private val mecAddresses: MECAddresses) : RecyclerView.Adapter<AddressBottomSheetViewHolder>() {

    private val VIEW_TYPE_FOOTER = 1
    lateinit var binding: ViewDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressBottomSheetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = MecAddressCardBinding.inflate(inflater)

        if(viewType == VIEW_TYPE_FOOTER){
            binding = MecAddressCreateCardBinding.inflate(inflater)
        }

        return AddressBottomSheetViewHolder(binding)
    }

    override fun getItemCount(): Int {

        if( mecAddresses.ecsAddresses == null) return 0
        if( mecAddresses.ecsAddresses.isEmpty()) return 0

        return mecAddresses.ecsAddresses.size + 1
    }

    override fun onBindViewHolder(viewHolder : AddressBottomSheetViewHolder, position: Int) {
       viewHolder.bind(mecAddresses.ecsAddresses[position])
    }

    override fun getItemViewType(position: Int): Int {

        if(position == itemCount) return VIEW_TYPE_FOOTER
        return super.getItemViewType(position)
    }
}