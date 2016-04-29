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

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductAdapter  extends RecyclerView.Adapter<ListRowHolder>  {

    Context mContext;
    private List<RegisteredProduct> registeredProducts;

    public ProductAdapter(Context mContext, List<RegisteredProduct> registeredProducts) {
        this.registeredProducts = registeredProducts;
        this.mContext = mContext;
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
        holder.mCtn.setText("CTN : " +registeredProduct.getCtn());
        holder.mSerailNumber.setText("Serial No : " + registeredProduct.getSerialNumber());
        if (registeredProduct.getRegistrationState().toString().equalsIgnoreCase("PENDING")){
            holder.mStatus.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
        }else if(registeredProduct.getRegistrationState().toString().equalsIgnoreCase("REGISTERED")){
            holder.mStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        }else {
            holder.mStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        }
        holder.mStatus.setText(Html.fromHtml("<font color='#222'>Status : </font>"+registeredProduct.getRegistrationState()));
    if (registeredProduct.getProdRegError()!=null)
       holder.mErrorStatus.setText(Html.fromHtml("<font color='#222'>Error : </font>"+registeredProduct.getProdRegError()));
    }

    @Override
    public int getItemCount() {
        return registeredProducts.size();
    }
}
