package com.philips.cdp.prodreg.adaptor;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.prodreg.fragments.ProdRegProductsFragment;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.product_registration_lib.R;

import java.util.List;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ProdRegProductsAdapter extends RecyclerView.Adapter<ProdRegListRowHolder> {

    Context mContext;
    ProdRegProductsFragment.OnItemClickListener onItemClickListener;
    private List<RegisteredProduct> registeredProducts;

    public ProdRegProductsAdapter(Context mContext, List<RegisteredProduct> registeredProducts, final ProdRegProductsFragment.OnItemClickListener onItemClickListener) {
        this.registeredProducts = registeredProducts;
        this.mContext = mContext;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ProdRegListRowHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        View view = null;
        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prodreg_product_row, null);
        }
        ProdRegListRowHolder listRowHolder = new ProdRegListRowHolder(view);
        return listRowHolder;
    }

    @Override
    public void onBindViewHolder(final ProdRegListRowHolder holder, final int position) {
        RegisteredProduct registeredProduct = registeredProducts.get(position);
        holder.mCtn.setText("CTN : " + registeredProduct.getCtn());
        final String s = "Serial No : ";
        final String text = s.concat(registeredProduct.getSerialNumber() != null ? registeredProduct.getSerialNumber() : "");
        holder.mSerailNumber.setText(text);
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
