package com.philips.cdp.di.iap.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrderProductHolder extends RecyclerView.ViewHolder {
    TextView mTvProductName;
    TextView mTvtotalPrice;
    NetworkImageView mNetworkImage;
    TextView mTvQuantity;

    public OrderProductHolder(final View itemView) {
        super(itemView);
        mTvProductName = (TextView) itemView.findViewById(R.id.tv_productName);
        mNetworkImage = (NetworkImageView) itemView.findViewById(R.id.iv_product_image);
        mTvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
        mTvtotalPrice = (TextView) itemView.findViewById(R.id.tv_total_price);
    }
}
