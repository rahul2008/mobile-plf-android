package com.philips.cdp.di.mec.screens.orderSummary


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.ecs.model.cart.AppliedVoucherEntity
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecOrderSummaryVoucherItemBinding
import com.philips.cdp.di.mec.databinding.MecVoucherItemBinding

class MECOrderSummaryVouchersAdapter(private val voucherList: MutableList<AppliedVoucherEntity>): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {


    private lateinit var voucher: AppliedVoucherEntity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MECOrderSummaryVoucherHolder(MecOrderSummaryVoucherItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return voucherList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        voucher = voucherList.get(position)
        val mecVoucherHolder = holder as MECOrderSummaryVoucherHolder
        mecVoucherHolder.bind(voucher)
    }

}