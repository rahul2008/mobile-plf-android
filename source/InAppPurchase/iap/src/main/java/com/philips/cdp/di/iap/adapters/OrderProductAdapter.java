package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPLog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrderProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ShoppingCartPresenter.LoadListener {
    private List<ShoppingCartData> mShoppingCartDataList;
    private Context mContext;
    private ShoppingCartData cartData;
    private AddressFields mBillingAddress;
    private PaymentMethod mPaymentMethod;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public OrderProductAdapter(Context pContext, ArrayList<ShoppingCartData> pShoppingCartDataList,
                               AddressFields pBillingAddress, PaymentMethod pPaymentMethod) {
        mContext = pContext;
        mShoppingCartDataList = pShoppingCartDataList;
        mBillingAddress = pBillingAddress;
        mPaymentMethod = pPaymentMethod;
    }

    private int getActualCount() {
        int count = 0;
        for (ShoppingCartData data : mShoppingCartDataList) {
            if (!TextUtils.isEmpty(data.getCtnNumber()))
                count++;
        }
        return count;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_order_summary_footer_item, parent, false);
            return new FooterOrderSummaryViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_order_summary_shopping_item, parent, false);
            return new OrderProductHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        cartData = mShoppingCartDataList.get(position);
        if (holder instanceof FooterOrderSummaryViewHolder) {
            FooterOrderSummaryViewHolder footerHolder = (FooterOrderSummaryViewHolder) holder;
            AddressFields shippingAddressFields = CartModelContainer.getInstance().getShippingAddressFields();
            footerHolder.mShippingFirstName.setText(shippingAddressFields.getFirstName());
            footerHolder.mShippingAddress.setText(shippingAddressFields.getLine1() + "\n" + shippingAddressFields.getLine1());
            if (null != mBillingAddress) {
                footerHolder.mBillingFirstName.setText(mBillingAddress.getFirstName());
                footerHolder.mBillingAddress.setText(mBillingAddress.getLine1() + "\n" + mBillingAddress.getLine1());
            }
            if (null != mPaymentMethod) {
                footerHolder.mLLPaymentMode.setVisibility(View.VISIBLE);
                footerHolder.mPaymentCardName.setText(mPaymentMethod.getCardNumber());
                footerHolder.mPaymentCardHolderName.setText(mPaymentMethod.getAccountHolderName());
            }
            footerHolder.mDeliveryPrice.setText(getLastValidItem().getDeliveryCost().getFormattedValue());
            footerHolder.mTotalPriceLable.setText(mContext.getString(R.string.iap_total) + " (" + getLastValidItem().getTotalItems() + " " + mContext.getString(R.string.iap_items) + ")");
            //footerHolder.mTotalPrice.setText(String.valueOf(getLastValidItem().getTotalPriceWithTax()));
            footerHolder.mTotalPrice.setText(getLastValidItem().getTotalPriceWithTaxFormatedPrice());
        } else {
            OrderProductHolder orderProductHolder = (OrderProductHolder) holder;
            IAPLog.d(IAPLog.ORDER_SUMMARY_FRAGMENT, "Size of ShoppingCarData is " + String.valueOf(getActualCount()));

            String imageURL = cartData.getImageURL();
            ImageLoader mImageLoader = NetworkImageLoader.getInstance(mContext)
                    .getImageLoader();
            orderProductHolder.mNetworkImage.setImageUrl(imageURL, mImageLoader);
            orderProductHolder.mTvProductName.setText(cartData.getProductTitle());
            String price = cartData.getTotalPriceFormatedPrice();
            //String price = NumberFormat.getNumberInstance(NetworkConstants.STORE_LOCALE).format(cartData.getTotalPrice());

            orderProductHolder.mTvtotalPrice.setText(price);
            orderProductHolder.mTvtotalPrice.setTypeface(null, Typeface.BOLD);
            orderProductHolder.mTvQuantity.setText(String.valueOf(cartData.getQuantity()));
        }
    }

    private ShoppingCartData getLastValidItem() {
        return mShoppingCartDataList.get(getActualCount() - 1);
    }

    @Override
    public int getItemViewType(final int position) {
        IAPLog.d(IAPLog.ORDER_SUMMARY_FRAGMENT, "getItemViewType= " + position);
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == getActualCount();
    }

    @Override
    public int getItemCount() {
        if (mShoppingCartDataList.size() == 0) {
            return 0;
        } else {
            return getActualCount() + 1;
        }
    }

    @Override
    public void onLoadFinished(final ArrayList<ShoppingCartData> data) {
        mShoppingCartDataList = data;
        notifyDataSetChanged();
    }

    public class OrderProductHolder extends RecyclerView.ViewHolder {
        TextView mTvProductName;
        NetworkImageView mNetworkImage;
        TextView mTvQuantity;
        TextView mTvtotalPrice;

        public OrderProductHolder(final View itemView) {
            super(itemView);
            mTvProductName = (TextView) itemView.findViewById(R.id.tv_productName);
            mNetworkImage = (NetworkImageView) itemView.findViewById(R.id.iv_product_image);
            mTvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            mTvtotalPrice = (TextView) itemView.findViewById(R.id.tv_total_price);
        }
    }

    public class FooterOrderSummaryViewHolder extends RecyclerView.ViewHolder {
        TextView mShippingFirstName;
        TextView mShippingAddress;
        TextView mBillingFirstName;
        TextView mBillingAddress;
        LinearLayout mLLPaymentMode;
        TextView mPaymentCardName;
        TextView mPaymentCardHolderName;
        TextView mDeliveryPrice;
        TextView mTotalPriceLable;
        TextView mTotalPrice;

        public FooterOrderSummaryViewHolder(View itemView) {
            super(itemView);
            mShippingFirstName = (TextView) itemView.findViewById(R.id.tv_shipping_first_name);
            mShippingAddress = (TextView) itemView.findViewById(R.id.tv_shipping_address);
            mBillingFirstName = (TextView) itemView.findViewById(R.id.tv_billing_first_name);
            mBillingAddress = (TextView) itemView.findViewById(R.id.tv_billing_address);
            mLLPaymentMode = (LinearLayout) itemView.findViewById(R.id.ll_payment_mode);
            mPaymentCardName = (TextView) itemView.findViewById(R.id.tv_card_type);
            mPaymentCardHolderName = (TextView) itemView.findViewById(R.id.tv_card_holder_name);
            mDeliveryPrice = (TextView) itemView.findViewById(R.id.tv_delivery_price);
            mTotalPriceLable = (TextView) itemView.findViewById(R.id.tv_total_lable);
            mTotalPrice = (TextView) itemView.findViewById(R.id.tv_total_price);
        }
    }
}
