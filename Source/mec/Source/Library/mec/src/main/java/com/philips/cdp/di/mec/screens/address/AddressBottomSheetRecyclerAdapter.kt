package com.philips.cdp.di.mec.screens.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecAddressCardBinding
import com.philips.cdp.di.mec.databinding.MecAddressCreateCardBinding
import kotlinx.android.synthetic.main.mec_address_card.view.*


class AddressBottomSheetRecyclerAdapter(private val mecAddresses: MECAddresses, val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val totalItem = mecAddresses.ecsAddresses.size + 1

    private val CREATE_ADDRESS = "CREATE_ADDRESS"
    private var mSelectedItem = 0

    var mSelectedAddress = mecAddresses.ecsAddresses[0]

    private val VIEW_TYPE_FOOTER = 1
    lateinit var binding: ViewDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        if (viewType == VIEW_TYPE_FOOTER) {
            binding = MecAddressCreateCardBinding.inflate(inflater)

            binding.root.setOnClickListener {
                itemClickListener.onItemClick(CREATE_ADDRESS)
            }

            return AddressBottomSheetFooterViewHolder(binding)
        }else {
            binding = MecAddressCardBinding.inflate(inflater)
            return AddressBottomSheetViewHolder(binding)
        }

    }

    override fun getItemCount(): Int {
        return totalItem
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        if (viewHolder is AddressBottomSheetViewHolder) {
            viewHolder.bind(mecAddresses.ecsAddresses[position])

            if(position == mSelectedItem){
                //textBoxxDefaultValidator background
                // border color  - textBoxxDefaultValidator border
                // corner radious 4 - border width - 1 dp
                //textcolor - textBoxDefaultValidator text
                viewHolder.binding.root.tv_name.setTextColor(R.attr.uidTextBoxDefaultValidatedTextColor)
                viewHolder.binding.root.tv_address_text.setTextColor(R.attr.uidTextBoxDefaultValidatedTextColor)
                viewHolder.binding.root.ll_rl_address.setBackgroundResource(R.drawable.address_selector)
            }else{
                // background - ContentPrimary
                //border colorr - separatorContent background
                //corner same
                // text color - contentItemPrimary text

                viewHolder.binding.root.tv_name.setTextColor(R.attr.uidContentItemPrimaryNormalTextColor)
                viewHolder.binding.root.tv_address_text.setTextColor(R.attr.uidContentItemPrimaryNormalTextColor)
                viewHolder.binding.root.ll_rl_address.setBackgroundResource(R.drawable.address_deselector)
            }

            viewHolder.binding.root.setOnClickListener {
                mSelectedAddress = mecAddresses.ecsAddresses[position]
                mSelectedItem = position
                notifyDataSetChanged()
            }

        }
    }

    override fun getItemViewType(position: Int): Int {

        if (position == totalItem - 1) return VIEW_TYPE_FOOTER
        return super.getItemViewType(position)

    }

}