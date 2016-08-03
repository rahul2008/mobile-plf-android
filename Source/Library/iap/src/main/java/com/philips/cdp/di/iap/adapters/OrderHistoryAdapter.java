/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.response.orders.Orders;
import com.philips.cdp.di.iap.response.orders.ProductData;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = OrderHistoryAdapter.class.getName();
    final private Context mContext;
    final private List<Orders> mOrders;
    final private List<ProductData> mProductDetails;
    final private List<OrderDetail> mOrderDetails;
    private int mSelectedIndex;

    public OrderHistoryAdapter(final Context context, final List<Orders> orders, final List<ProductData> product, final List<OrderDetail> orderDetails) {
        mContext = context;
        mOrders = orders;
        mProductDetails = product;
        mOrderDetails = orderDetails;
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
        String orderStatus = order.getStatusDisplay();
        orderHistoryHolder.mOrderState.setText(orderStatus.substring(0, 1).toUpperCase() + orderStatus.substring(1));
        orderHistoryHolder.mOrderNumber.setText(order.getCode());

        int totalQuantity = 0;
        for (ProductData data : mProductDetails) {
            if (data.getOrderCode() != null && data.getOrderCode().equals(order.getCode())) {
                //Inflate the Dynamic Layout Information View
                View hiddenInfo = View.inflate(mContext, R.layout.iap_order_history_product_details, null);
                orderHistoryHolder.mProductDetailsLayout.addView(hiddenInfo);
                ((TextView) hiddenInfo.findViewById(R.id.tv_productName)).setText(data.getProductTitle());
                ((TextView) hiddenInfo.findViewById(R.id.tv_product_number)).setText(data.getCtnNumber());
                getNetworkImage(((NetworkImageView) hiddenInfo.findViewById(R.id.iv_product_image)),
                        data.getImageURL());
                totalQuantity += data.getQuantity();
            }
        }

        //this is added because in acc, the data was not available in prx but hybris has the data.
        if (totalQuantity == 0) {
            for (OrderDetail detail : mOrderDetails) {
                if (detail.getCode() != null && detail.getCode().equals(order.getCode())) {
                    totalQuantity = detail.getDeliveryItemsQuantity();
                    break;
                }
            }
        }

        if (totalQuantity > 1) {
            orderHistoryHolder.mTvQuantity.setText(" (" + totalQuantity + " items)");
        } else {
            orderHistoryHolder.mTvQuantity.setText(" (" + totalQuantity + " item)");
        }
        orderHistoryHolder.mTvtotalPrice.setText(order.getTotal().getFormattedValue());

    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ((OrderHistoryHolder) holder).mProductDetailsLayout.removeAllViews();
    }

    private void getNetworkImage(final NetworkImageView imageView, final String imageUrl) {
        ImageLoader imageLoader;
        // Instantiate the RequestQueue.
        imageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();

        imageLoader.get(imageUrl, ImageLoader.getImageListener(imageView,
                R.drawable.no_icon, android.R.drawable
                        .ic_dialog_alert));
        imageView.setImageUrl(imageUrl, imageLoader);
    }

    @Override
    public int getItemCount() {
        if(mOrders!=null) {
            return mOrders.size();
        }
        return 0;
    }

    public int getSelectedPosition() {
        return mSelectedIndex;
    }

    public class OrderHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTvQuantity;
        private TextView mTvtotalPrice;
        private TextView mTime;
        private TextView mOrderNumber;
        private TextView mOrderState;
        private RelativeLayout mOrderSummaryLayout;
        private LinearLayout mProductDetailsLayout;

        public OrderHistoryHolder(final View itemView) {
            super(itemView);
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
