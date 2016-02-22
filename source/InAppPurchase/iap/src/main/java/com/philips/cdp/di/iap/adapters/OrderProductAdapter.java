package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.NetworkImageLoader;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductHolder> {
    private List<ShoppingCartData> mShoppingCartDataList;
    private Context mContext;

    public OrderProductAdapter(Context pContext, ArrayList<ShoppingCartData> pShoppingCartDataList) {
        mContext = pContext;
        mShoppingCartDataList = pShoppingCartDataList;
    }

    @Override
    public OrderProductHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.iap_order_summary_shopping_item, null);
        return new OrderProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderProductHolder holder, final int position) {
        ShoppingCartData cartData = mShoppingCartDataList.get(position);
        String imageURL = cartData.getImageURL();
        ImageLoader mImageLoader;
        // Instantiate the RequestQueue.
        mImageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();

        mImageLoader.get(imageURL, ImageLoader.getImageListener(holder.mNetworkImage,
                R.drawable.toothbrush, android.R.drawable
                        .ic_dialog_alert));
        holder.mNetworkImage.setImageUrl(imageURL, mImageLoader);
        holder.mTvProductName.setText(cartData.getProductTitle());
        String price = NumberFormat.getNumberInstance(NetworkConstants.STORE_LOCALE).format(cartData.getTotalPrice());

        holder.mTvtotalPrice.setText(cartData.getCurrency() + " " + price);
        holder.mTvtotalPrice.setTypeface(null, Typeface.BOLD);
        holder.mTvQuantity.setText(cartData.getQuantity());
    }

    @Override
    public int getItemCount() {
        return mShoppingCartDataList.size();
    }
}
