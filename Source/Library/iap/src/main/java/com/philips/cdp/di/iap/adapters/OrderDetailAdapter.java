package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.response.orders.ProductData;
import com.philips.cdp.di.iap.session.NetworkImageLoader;

import java.util.List;


public class OrderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = OrderHistoryAdapter.class.getName();
    private Context mContext;
    private List<ProductData> mOrders;
    private int mSelectedIndex;

    public OrderDetailAdapter(final Context context, final List<ProductData> orders) {
        mContext = context;
        mOrders = orders;
        mSelectedIndex = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(mContext, R.layout.iap_order_details_item, null);
        return new OrderHistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductData order = mOrders.get(position);
        OrderHistoryHolder orderHistoryHolder = (OrderHistoryHolder) holder;
        orderHistoryHolder.mTvProductName.setText(order.getProductTitle());
        orderHistoryHolder.mTvQuantity.setText(String.valueOf(order.getQuantity()));
        orderHistoryHolder.mTvtotalPrice.setText(order.getFormatedPrice());
        orderHistoryHolder.mTvCtnNo.setText(order.getCtnNumber());
        getNetworkImage(orderHistoryHolder, order.getImageURL());

    }

    @Override
    public int getItemCount() {
        if (mOrders != null)
            return mOrders.size();
        return 0;
    }

    public int getSelectedPosition() {
        return mSelectedIndex;
    }

    private void getNetworkImage(final OrderHistoryHolder orderHistoryHolder, final String imageURL) {
        ImageLoader mImageLoader;
        // Instantiate the RequestQueue.
        mImageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();

        mImageLoader.get(imageURL, ImageLoader.getImageListener(orderHistoryHolder.mNetworkImage,
                R.drawable.no_icon, android.R.drawable
                        .ic_dialog_alert));
        orderHistoryHolder.mNetworkImage.setImageUrl(imageURL, mImageLoader);
    }

    public class OrderHistoryHolder extends RecyclerView.ViewHolder {
        TextView mTvProductName;
        NetworkImageView mNetworkImage;
        TextView mTvQuantity;
        TextView mTvtotalPrice;
        TextView mTvCtnNo;

        public OrderHistoryHolder(final View itemView) {
            super(itemView);
            mTvProductName = (TextView) itemView.findViewById(R.id.tv_productName);
            mNetworkImage = (NetworkImageView) itemView.findViewById(R.id.iv_product_image);
            mTvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            mTvtotalPrice = (TextView) itemView.findViewById(R.id.tv_total_price);
            mTvCtnNo = (TextView) itemView.findViewById(R.id.ctn_no);
        }

    }

}
