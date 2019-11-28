package com.philips.cdp.di.mec.screens.retailers

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.retailers.ECSRetailer
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecRetailersItemBinding
import com.philips.cdp.di.mec.utils.MECDataHolder

class MECRetailersAdapter (private val mecRetailers: List<ECSRetailer>? ,val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(MecRetailersItemBinding.inflate(LayoutInflater.from(parent.context)),itemClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val retailer = mecRetailers?.get(position)
        val viewHolder = holder as ViewHolder
        viewHolder.bind(retailer!!)
    }

    private class ViewHolder(val binding: MecRetailersItemBinding , val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(binding.root){

        open fun bind(retailer: ECSRetailer) {
            binding.mecRetailer = retailer
            binding.itemClickListener = itemClickListener

            if(retailer.isPhilipsStore.equals("Y")){
                    itemView.visibility = View.GONE
                itemView.layoutParams = RecyclerView.LayoutParams(0,0)
                } else {
                    itemView.visibility = View.VISIBLE
                    itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                }
            if (TextUtils.isDigitsOnly(retailer.philipsOnlinePrice)) {
                binding.mecRetailerItemPrice.visibility = View.GONE
            } else {
                binding.mecRetailerItemPrice.visibility = View.VISIBLE
            }

        }
    }

    override fun getItemCount(): Int {
        return mecRetailers?.size!!
    }

}