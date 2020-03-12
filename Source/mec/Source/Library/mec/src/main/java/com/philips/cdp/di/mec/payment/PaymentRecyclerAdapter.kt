package com.philips.cdp.di.mec.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecBillingAddressCreateCardBinding
import com.philips.cdp.di.mec.databinding.MecPaymentCardBinding
import com.philips.cdp.di.mec.utils.MECConstant
import kotlinx.android.synthetic.main.mec_address_card.view.ll_rl_address
import kotlinx.android.synthetic.main.mec_address_card.view.mec_address_card_view
import kotlinx.android.synthetic.main.mec_address_card.view.tv_address_text
import kotlinx.android.synthetic.main.mec_address_card.view.tv_name
import kotlinx.android.synthetic.main.mec_billing_address_edit_card.view.*

class PaymentRecyclerAdapter(val items: MECPayments, val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var totalItem = if (items.isNewCardPresent()) items.payments.size else items.payments.size + 1

    private var mSelectedAddress: MECPayment? = null

    private val VIEW_TYPE_FOOTER = 1
    lateinit var binding: ViewDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == VIEW_TYPE_FOOTER) {
            // Create billing address
            binding = MecBillingAddressCreateCardBinding.inflate(inflater)
            binding.root.setOnClickListener { itemClickListener.onItemClick(MECConstant.CREATE_BILLING_ADDRESS) }
            return AddressBillingCreateFooterHolder(binding)

        } else {
            binding = MecPaymentCardBinding.inflate(inflater)
            PaymentHolder(binding)
        }

    }

    override fun getItemCount(): Int {
        return totalItem
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        if (viewHolder is PaymentHolder) {

            val mecPayment = items.payments[position]

            viewHolder.bind(mecPayment)

            //TODO pabitra ..take this code to binding utility
            if (mecPayment.isSelected) {
                viewHolder.binding.root.tv_name.setTextColor(R.attr.uidTextBoxDefaultValidatedTextColor)
                viewHolder.binding.root.tv_address_text.setTextColor(R.attr.uidTextBoxDefaultValidatedTextColor)
                viewHolder.binding.root.ll_rl_address.setBackgroundResource(R.drawable.address_selector)
                viewHolder.binding.root.mec_address_card_view.cardElevation = 30f
            } else {
                viewHolder.binding.root.tv_name.setTextColor(R.attr.uidContentItemPrimaryNormalTextColor)
                viewHolder.binding.root.tv_address_text.setTextColor(R.attr.uidContentItemPrimaryNormalTextColor)
                viewHolder.binding.root.ll_rl_address.setBackgroundResource(R.drawable.address_deselector)
                viewHolder.binding.root.mec_address_card_view.cardElevation = 15f
            }

            val mecAddressEditIcon = viewHolder.binding.root.mec_address_edit_icon

            if (mecPayment.ecsPayment.id.equals(MECConstant.NEW_CARD_PAYMENT, true)) {
                mecAddressEditIcon.visibility = View.VISIBLE
                mecAddressEditIcon.isClickable = true
                mecAddressEditIcon.setOnClickListener { itemClickListener.onItemClick(mecPayment) }

            } else {
                mecAddressEditIcon.visibility = View.GONE
                mecAddressEditIcon.isClickable = false
            }

            viewHolder.binding.root.setOnClickListener {
                mSelectedAddress = mecPayment
                mSelectedAddress!!.isSelected = true
                itemClickListener.onItemClick(mecPayment)
                notifyDataSetChanged()
            }
        }

    }

    override fun getItemViewType(position: Int): Int {

        if (position == totalItem - 1 && !items.isNewCardPresent()) return VIEW_TYPE_FOOTER  //if New crad is not present show option to add New Card

        return super.getItemViewType(position)
    }
}