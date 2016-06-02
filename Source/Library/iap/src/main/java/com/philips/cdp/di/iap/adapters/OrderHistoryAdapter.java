/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.response.orders.Orders;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = OrderHistoryAdapter.class.getName();
    private Context mContext;
    private List<Orders> mOrders;

    public OrderHistoryAdapter(final Context context, final List<Orders> orders) {
        mContext = context;
        mOrders = orders;
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

    }

    @Override
    public int getItemCount() {
        if(mOrders!=null)
            return mOrders.size();
        return 0;
    }

    public class OrderHistoryHolder extends RecyclerView.ViewHolder {
        TextView mTvProductName;
        NetworkImageView mNetworkImage;
        TextView mTvQuantity;
        TextView mTvtotalPrice;
        TextView mTime;
        TextView mOrderNumber;
        TextView mOrderState;

        public OrderHistoryHolder(final View itemView) {
            super(itemView);
            mTvProductName = (TextView) itemView.findViewById(R.id.tv_productName);
            mNetworkImage = (NetworkImageView) itemView.findViewById(R.id.iv_product_image);
            mTvQuantity = (TextView) itemView.findViewById(R.id.tv_total_item);
            mTvtotalPrice = (TextView) itemView.findViewById(R.id.tv_total_price);
            mTime = (TextView) itemView.findViewById(R.id.tv_time);
            mOrderNumber = (TextView) itemView.findViewById(R.id.tv_order_number);
            mOrderState = (TextView) itemView.findViewById(R.id.tv_order_state);
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
