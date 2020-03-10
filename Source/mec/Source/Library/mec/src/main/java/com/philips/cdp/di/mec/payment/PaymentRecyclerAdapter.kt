package com.philips.cdp.di.mec.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.payment.ECSPayment
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecBillingAddressCreateCardBinding
import com.philips.cdp.di.mec.databinding.MecBillingAddressEditCardBinding
import com.philips.cdp.di.mec.databinding.MecPaymentCardBinding
import com.philips.cdp.di.mec.utils.MECConstant
import kotlinx.android.synthetic.main.mec_address_card.view.ll_rl_address
import kotlinx.android.synthetic.main.mec_address_card.view.mec_address_card_view
import kotlinx.android.synthetic.main.mec_address_card.view.tv_address_text
import kotlinx.android.synthetic.main.mec_address_card.view.tv_name
import kotlinx.android.synthetic.main.mec_billing_address_edit_card.view.*

class PaymentRecyclerAdapter (val items: MECPayments , val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val totalItem = items.payments.size + 1
    private var mSelectedItem = 0

    var ecsBillingAddress: ECSAddress? = null

    var mSelectedAddress : ECSPayment? = null

    private val VIEW_TYPE_FOOTER = 1
    lateinit var binding: ViewDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        if (viewType == VIEW_TYPE_FOOTER) {

            if(ecsBillingAddress == null) {
                //Create billing address
                binding = MecBillingAddressCreateCardBinding.inflate(inflater)
                binding.root.setOnClickListener { itemClickListener.onItemClick(MECConstant.CREATE_BILLING_ADDRESS) }
                return AddressBillingCreateFooterHolder(binding)
            }else{
                // Edit billing address
                binding = MecBillingAddressEditCardBinding.inflate(inflater)
                binding.root.mec_address_edit_icon.setOnClickListener { itemClickListener.onItemClick(MECConstant.EDIT_BILLING_ADDRESS) }
                return AddressBillingEditFooterHolder(binding)
            }

        }else {
            binding = MecPaymentCardBinding.inflate(inflater)
            return PaymentHolder(binding)
        }

    }

    override fun getItemCount(): Int {
        return totalItem
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        if (viewHolder is PaymentHolder) {
            viewHolder.bind(items.payments[position])

            if(position == mSelectedItem){
                viewHolder.binding.root.tv_name.setTextColor(R.attr.uidTextBoxDefaultValidatedTextColor)
                viewHolder.binding.root.tv_address_text.setTextColor(R.attr.uidTextBoxDefaultValidatedTextColor)
                viewHolder.binding.root.ll_rl_address.setBackgroundResource(R.drawable.address_selector)
                viewHolder.binding.root.mec_address_card_view.cardElevation= 30f
            }else{
                viewHolder.binding.root.tv_name.setTextColor(R.attr.uidContentItemPrimaryNormalTextColor)
                viewHolder.binding.root.tv_address_text.setTextColor(R.attr.uidContentItemPrimaryNormalTextColor)
                viewHolder.binding.root.ll_rl_address.setBackgroundResource(R.drawable.address_deselector)
                viewHolder.binding.root.mec_address_card_view.cardElevation= 15f
            }

            viewHolder.binding.root.setOnClickListener {
                mSelectedAddress = items.payments[position]
                mSelectedItem = position
                notifyDataSetChanged()
            }
        }else if( viewHolder is AddressBillingEditFooterHolder){

            ecsBillingAddress?.let { viewHolder.bind(it) }
        }

    }

    override fun getItemViewType(position: Int): Int {

        if (position == totalItem - 1) return VIEW_TYPE_FOOTER
        return super.getItemViewType(position)

    }
}