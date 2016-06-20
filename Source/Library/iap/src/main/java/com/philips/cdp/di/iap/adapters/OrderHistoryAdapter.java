/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.response.orders.Orders;
import com.philips.cdp.di.iap.response.orders.ProductData;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = OrderHistoryAdapter.class.getName();
    private Context mContext;
    private List<Orders> mOrders;
    private List<ProductData> mProductDetails;
    private int mSelectedIndex;

    public OrderHistoryAdapter(final Context context, final List<Orders> orders, final List<ProductData> product) {
        mContext = context;
        mOrders = orders;
        mProductDetails = product;
        mSelectedIndex = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(mContext, R.layout.iap_order_history_shopping_item, null);
        return new OrderHistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Orders order = mOrders.get(position);
        OrderHistoryHolder orderHistoryHolder = (OrderHistoryHolder) holder;
        orderHistoryHolder.mTime.setText(getFormattedDate(order.getPlaced()));
        orderHistoryHolder.mOrderState.setText(order.getStatusDisplay());
        orderHistoryHolder.mOrderNumber.setText(order.getCode());

        int totalQuantity = 0;
        for(ProductData data : mProductDetails)
        {
            if(data.getOrderCode() != null && data.getOrderCode().equals(order.getCode()))
            {
                //Inflate the Dynamic Layout Information View
                View hiddenInfo = View.inflate(mContext, R.layout.iap_order_history_product_details, null);
                orderHistoryHolder.mProductDetailsLayout.addView(hiddenInfo);
                ((TextView)hiddenInfo.findViewById(R.id.tv_productName)).setText(data.getProductTitle());
                ((TextView)hiddenInfo.findViewById(R.id.tv_product_number)).setText(data.getCtnNumber());
                getNetworkImage(((NetworkImageView)hiddenInfo.findViewById(R.id.iv_product_image)), data.getImageURL());
                totalQuantity += data.getQuantity();
            }
        }
        if(totalQuantity > 1)
            orderHistoryHolder.mTvQuantity.setText("(" + totalQuantity + " items)");
        else
            orderHistoryHolder.mTvQuantity.setText(" (" + totalQuantity + " item)");
        orderHistoryHolder.mTvtotalPrice.setText(order.getTotal().getFormattedValue());

    }

    private void getNetworkImage(final NetworkImageView imageView, final String imageURL) {
        ImageLoader mImageLoader;
        // Instantiate the RequestQueue.
        mImageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();

        mImageLoader.get(imageURL, ImageLoader.getImageListener(imageView,
                R.drawable.no_icon, android.R.drawable
                        .ic_dialog_alert));
        imageView.setImageUrl(imageURL, mImageLoader);
    }

    @Override
    public int getItemCount() {
        if(mOrders!=null)
            return mOrders.size();
        return 0;
    }

    public int getSelectedPosition() {
        return mSelectedIndex;
    }

    public class OrderHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTvProductName;
        NetworkImageView mNetworkImage;
        TextView mTvQuantity;
        TextView mTvtotalPrice;
        TextView mTime;
        TextView mOrderNumber;
        TextView mOrderState;
        RelativeLayout mOrderSummaryLayout;
        LinearLayout mProductDetailsLayout;

        public OrderHistoryHolder(final View itemView) {
            super(itemView);
            mTvProductName = (TextView) itemView.findViewById(R.id.tv_productName);
            mNetworkImage = (NetworkImageView) itemView.findViewById(R.id.iv_product_image);
            mTvQuantity = (TextView) itemView.findViewById(R.id.tv_total_item);
            mTvtotalPrice = (TextView) itemView.findViewById(R.id.tv_total_price);
            mTime = (TextView) itemView.findViewById(R.id.tv_time);
            mOrderNumber = (TextView) itemView.findViewById(R.id.tv_order_number);
            mOrderState = (TextView) itemView.findViewById(R.id.tv_order_state);
            mOrderSummaryLayout = (RelativeLayout) itemView.findViewById(R.id.order_summary);
            mOrderSummaryLayout.setOnClickListener(this);
            mProductDetailsLayout = (LinearLayout) itemView.findViewById(R.id.product_detail);
        }


        @Override
        public void onClick(View v) {
            mSelectedIndex = getAdapterPosition();
            if(v.getId() == R.id.order_summary)
            {
                EventHelper.getInstance().notifyEventOccurred(IAPConstant.PURCHASE_HISTORY_DETAIL);
            }
        }
    }

    private String getFormattedDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date convertedDate = null;
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            IAPLog.d(OrderHistoryAdapter.TAG, e.getMessage());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy"); // Set your date format
        return sdf.format(convertedDate);
    }
}
