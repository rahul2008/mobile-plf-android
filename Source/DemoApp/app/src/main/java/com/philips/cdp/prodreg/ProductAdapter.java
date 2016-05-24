package com.philips.cdp.prodreg;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.prodreg.register.RegisteredProduct;

import java.util.List;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ProductAdapter  extends RecyclerView.Adapter<ListRowHolder>  {

    Context mContext;
    RegisteredProductsList.OnItemClickListener onItemClickListener;
    private List<RegisteredProduct> registeredProducts;

    public ProductAdapter(Context mContext, List<RegisteredProduct> registeredProducts, final RegisteredProductsList.OnItemClickListener onItemClickListener) {
        this.registeredProducts = registeredProducts;
        this.mContext = mContext;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public ListRowHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        View view=null;
        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_row, null);
        }
        ListRowHolder listRowHolder = new ListRowHolder(view);
        return listRowHolder;
    }

    @Override
    public void onBindViewHolder(final ListRowHolder holder, final int position) {
        RegisteredProduct registeredProduct = registeredProducts.get(position);
        holder.mCtn.setText("CTN : " + registeredProduct.getCtn());
        holder.mSerailNumber.setText("Serial No : " + registeredProduct.getSerialNumber());
        if (registeredProduct.getRegistrationState().toString().equalsIgnoreCase("PENDING") || registeredProduct.getRegistrationState().toString().equalsIgnoreCase("REGISTERING")) {
            holder.mStatus.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
        } else if (registeredProduct.getRegistrationState().toString().equalsIgnoreCase("REGISTERED")) {
            holder.mStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        } else {
            holder.mStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        }
        holder.mStatus.setText(Html.fromHtml("<font color='#222'>Status : </font>" + registeredProduct.getRegistrationState()));
        if (registeredProduct.getProdRegError() != null)
            holder.mErrorStatus.setText(Html.fromHtml("<font color='#222'>Error : </font>" + registeredProduct.getProdRegError()));
        else
            holder.mErrorStatus.setText("");

        holder.bind(registeredProducts.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return registeredProducts.size();
    }
}
