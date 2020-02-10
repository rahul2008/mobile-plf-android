package com.philips.cdp.di.mec.screens.shoppingCart

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.cart.AppliedVoucherEntity
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecVoucherItemBinding

class MECVouchersAdapter(private val voucherList: MutableList<AppliedVoucherEntity>,private val itemClickListener : ItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {


    private lateinit var voucher: AppliedVoucherEntity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MECVoucherHolder(MecVoucherItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return voucherList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        voucher = voucherList.get(position)
        val mecVoucherHolder = holder as MECVoucherHolder
        mecVoucherHolder.bind(voucher,itemClickListener)

    }

    fun getVoucher(): AppliedVoucherEntity {
        return voucher
    }


}