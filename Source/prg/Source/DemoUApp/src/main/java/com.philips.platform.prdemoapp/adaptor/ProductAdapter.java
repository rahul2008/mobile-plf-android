package com.philips.platform.prdemoapp.adaptor;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.platform.prdemoapp.fragment.ProductListFragment;
import com.philips.platform.prdemoapplibrary.R;

import java.util.List;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ProductAdapter  extends RecyclerView.Adapter<ListRowHolder>  {

    Context mContext;
    ProductListFragment.OnItemClickListener onItemClickListener;
    private List<RegisteredProduct> registeredProducts;

    public ProductAdapter(Context mContext, List<RegisteredProduct> registeredProducts, final ProductListFragment.OnItemClickListener onItemClickListener) {
        this.registeredProducts = registeredProducts;
        this.mContext = mContext;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public ListRowHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        View view=null;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_row, null);
        ListRowHolder listRowHolder = new ListRowHolder(view);
        return listRowHolder;
    }

    @Override
    public void onBindViewHolder(final ListRowHolder holder, final int position) {
        RegisteredProduct registeredProduct = registeredProducts.get(position);
        holder.mCtn.setText("CTN : " + registeredProduct.getCtn());
        final String s = "Serial No : ";
        final String text = s.concat(registeredProduct.getSerialNumber() != null ? registeredProduct.getSerialNumber() : "");
        holder.mSerailNumber.setText(text);
        if (registeredProduct.getRegistrationState().toString().equalsIgnoreCase("PENDING") || registeredProduct.getRegistrationState().toString().equalsIgnoreCase("REGISTERING")) {
            holder.mStatus.setTextColor(mContext.getColor(R.color.gray));
        } else if (registeredProduct.getRegistrationState().toString().equalsIgnoreCase("REGISTERED")) {
            holder.mStatus.setTextColor(mContext.getColor(R.color.green));
        } else {
            holder.mStatus.setTextColor(mContext.getColor(R.color.red));
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
