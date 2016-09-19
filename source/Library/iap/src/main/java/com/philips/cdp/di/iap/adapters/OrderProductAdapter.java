/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.Fragments.DeliveryModeDialog;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.controller.AddressController.AddressListener;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.ArrayList;
import java.util.List;


public class OrderProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ShoppingCartPresenter.LoadListener<ShoppingCartData>, DeliveryModeDialog.DialogListener {
    private final static String TAG = OrderProductAdapter.class.getSimpleName();

    private List<ShoppingCartData> mShoppingCartDataList;
    private Context mContext;
    private ShoppingCartData cartData;
    private AddressFields mBillingAddress;
    private PaymentMethod mPaymentMethod;
    private AddressListener mListener;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private Drawable mEditDrawable;

    public OrderProductAdapter(Context pContext, AddressListener listener, ArrayList<ShoppingCartData> pShoppingCartDataList,
                               AddressFields pBillingAddress, PaymentMethod pPaymentMethod) {
        mContext = pContext;
        mListener = listener;
        mShoppingCartDataList = pShoppingCartDataList;
        mBillingAddress = pBillingAddress;
        mPaymentMethod = pPaymentMethod;
        initDrawables();
    }

    void initDrawables() {
        mEditDrawable = VectorDrawable.create(mContext, R.drawable.pencil_01);
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
        if (mShoppingCartDataList.size() == 0) return;

        if (holder instanceof OrderProductHolder) {
            OrderProductHolder orderProductHolder = (OrderProductHolder) holder;
            IAPLog.d(TAG, "Size of ShoppingCarData is " + String.valueOf(mShoppingCartDataList.size()));
            cartData = mShoppingCartDataList.get(position);
            String imageURL = cartData.getImageURL();
            ImageLoader mImageLoader = NetworkImageLoader.getInstance(mContext)
                    .getImageLoader();
            orderProductHolder.mNetworkImage.setImageUrl(imageURL, mImageLoader);
            orderProductHolder.mTvProductName.setText(cartData.getProductTitle());
            String price = cartData.getTotalPriceFormatedPrice();

            orderProductHolder.mTvtotalPrice.setText(price);
            orderProductHolder.mTvQuantity.setText(String.valueOf(cartData.getQuantity()));
        } else {
            String shippingAddress = cartData.getDeliveryAddressEntity().getFormattedAddress();
            FooterOrderSummaryViewHolder footerHolder = (FooterOrderSummaryViewHolder) holder;
            footerHolder.mTitleBillingAddress.setText(R.string.iap_billing_address);
            footerHolder.mTitleDeliveryAddress.setText(R.string.iap_shipping_address);
            footerHolder.mTitleVat.setText(R.string.iap_vat);
            footerHolder.mTitleTotalPrice.setText(R.string.iap_total_val);
            String shippingName = cartData.getDeliveryAddressEntity().getFirstName() + " " + cartData.getDeliveryAddressEntity().getLastName();
            footerHolder.mShippingFirstName.setText(shippingName);
            footerHolder.mShippingAddress.setText(Utility.formatAddress(shippingAddress));
            if (null != mBillingAddress) {
                String billingName = mBillingAddress.getFirstName() + " " + mBillingAddress.getLastName();
                footerHolder.mBillingFirstName.setText(billingName);
                footerHolder.mBillingAddress.setText(Utility.createAddress(mBillingAddress));
            }
            if (null != mPaymentMethod) {
                String paymentBillingName = mPaymentMethod.getBillingAddress().getFirstName() + " " + mPaymentMethod.getBillingAddress().getLastName();
                footerHolder.mBillingFirstName.setText(paymentBillingName);
                AddressFields billingAddressFields = Utility.prepareAddressFields(mPaymentMethod.getBillingAddress(), null);
                footerHolder.mBillingAddress.setText(Utility.createAddress(billingAddressFields));

                footerHolder.mLLPaymentMode.setVisibility(View.VISIBLE);
                footerHolder.mPaymentCardName.setText(mPaymentMethod.getCardType().getCode() + " " + mPaymentMethod.getCardNumber());

                footerHolder.mPaymentCardHolderName.setText(mPaymentMethod.getAccountHolderName()
                        + "\n" + (mContext.getResources().getString(R.string.iap_valid_until)) + " "
                        + mPaymentMethod.getExpiryMonth() + "/" + mPaymentMethod.getExpiryYear());
            }
            if (getLastValidItem().getDeliveryMode() != null) {
                String deliveryCost = getLastValidItem().getDeliveryMode().getDeliveryCost().getFormattedValue();
                String deliveryMethod = getLastValidItem().getDeliveryMode().getName();
                footerHolder.mDeliveryPrice.setText(deliveryCost);
                if (deliveryMethod != null) {
                    footerHolder.mTitleDelivery.setText(deliveryMethod);
                } else {
                    footerHolder.mTitleDelivery.setText(R.string.iap_delivery_via);
                }
            } else {
                footerHolder.mTitleDelivery.setVisibility(View.GONE);
                footerHolder.mDeliveryPrice.setVisibility(View.GONE);
                footerHolder.mDeliveryView.setVisibility(View.GONE);
            }
            footerHolder.mTotalPriceLable.setText(mContext.getString(R.string.iap_total) + " (" + getLastValidItem().getTotalItems() + " " + mContext.getString(R.string.iap_items) + ")");
            footerHolder.mTotalPrice.setText(getLastValidItem().getTotalPriceWithTaxFormatedPrice());
            footerHolder.mVatValue.setText(getLastValidItem().getVatValue());
            if (!getLastValidItem().isVatInclusive()) {
                footerHolder.mVatInclusive.setVisibility(View.VISIBLE);
                footerHolder.mVatInclusive.setText(String.format(mContext.getString(R.string.iap_including_vat), mContext.getString(R.string.iap_vat)));
                footerHolder.mVatValueUK.setVisibility(View.VISIBLE);
                footerHolder.mVatValueUK.setText(getLastValidItem().getVatValue());
                footerHolder.mVatValue.setVisibility(View.GONE);
                footerHolder.mTitleVat.setVisibility(View.GONE);
            } else {
                footerHolder.mVatValue.setVisibility(View.VISIBLE);
                footerHolder.mTitleVat.setVisibility(View.VISIBLE);
                footerHolder.mVatInclusive.setVisibility(View.GONE);
                footerHolder.mVatValueUK.setVisibility(View.GONE);
            }

            footerHolder.mEditIcon.setVisibility(View.VISIBLE);
            footerHolder.mEditIcon.setImageDrawable(mEditDrawable);
            footerHolder.mEditIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeliveryModeDialog dialog = new DeliveryModeDialog(mContext, OrderProductAdapter.this, mListener);
                    dialog.showDialog();
                }
            });


        }
    }

    private ShoppingCartData getLastValidItem() {
        return mShoppingCartDataList.get(mShoppingCartDataList.size() - 1);
    }

    @Override
    public int getItemViewType(final int position) {
        IAPLog.d(TAG, "getItemViewType= " + position);
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == mShoppingCartDataList.size();
    }

    @Override
    public int getItemCount() {
        return mShoppingCartDataList.size() + 1;
    }

    @Override
    public void onLoadFinished(final ArrayList<ShoppingCartData> data) {
        mShoppingCartDataList = data;
        notifyDataSetChanged();
    }

    @Override
    public void onLoadListenerError(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg,((FragmentActivity)mContext).getSupportFragmentManager(), mContext);
        } else {
            NetworkUtility.getInstance().showErrorDialog(mContext, ((FragmentActivity)mContext).getSupportFragmentManager(), mContext.getString(R.string.iap_ok),
                    mContext.getString(R.string.iap_server_error), mContext.getString(R.string.iap_something_went_wrong));
        }
    }

    @Override
    public void onRetailerError(IAPNetworkError errorMsg) {
        //NOP
    }

    @Override
    public void onItemClick(int position) {

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

        TextView mTitleDeliveryAddress;
        TextView mTitleBillingAddress;
        TextView mShippingFirstName;
        TextView mShippingAddress;
        TextView mBillingFirstName;
        TextView mBillingAddress;
        LinearLayout mLLPaymentMode;
        TextView mPaymentCardName;
        TextView mPaymentCardHolderName;
        TextView mTitleDelivery;
        TextView mDeliveryPrice;
        TextView mTotalPriceLable;
        TextView mTotalPrice;
        TextView mTitleVat;
        TextView mVatValue;
        TextView mTitleTotalPrice;
        TextView mVatInclusive;
        View mDeliveryView;
        TextView mVatValueUK;
        ImageView mEditIcon;

        public FooterOrderSummaryViewHolder(View itemView) {
            super(itemView);
            mTitleDeliveryAddress = (TextView) itemView.findViewById(R.id.tv_title_delivery_address);
            mTitleBillingAddress = (TextView) itemView.findViewById(R.id.tv_title_billing_address);
            mShippingFirstName = (TextView) itemView.findViewById(R.id.tv_shipping_first_name);
            mShippingAddress = (TextView) itemView.findViewById(R.id.tv_shipping_address);
            mBillingFirstName = (TextView) itemView.findViewById(R.id.tv_billing_first_name);
            mBillingAddress = (TextView) itemView.findViewById(R.id.tv_billing_address);
            mLLPaymentMode = (LinearLayout) itemView.findViewById(R.id.ll_payment_mode);
            mPaymentCardName = (TextView) itemView.findViewById(R.id.tv_card_type);
            mPaymentCardHolderName = (TextView) itemView.findViewById(R.id.tv_card_holder_name);
            mTitleDelivery = (TextView) itemView.findViewById(R.id.tv_delivery);
            mDeliveryPrice = (TextView) itemView.findViewById(R.id.tv_delivery_price);
            mTotalPriceLable = (TextView) itemView.findViewById(R.id.tv_total_lable);
            mTotalPrice = (TextView) itemView.findViewById(R.id.tv_total_price);
            mTitleVat = (TextView) itemView.findViewById(R.id.tv_vat);
            mVatValue = (TextView) itemView.findViewById(R.id.tv_vat_price);
            mTitleTotalPrice = (TextView) itemView.findViewById(R.id.tv_total_lable);
            mVatInclusive = (TextView) itemView.findViewById(R.id.tv_vat_inclusive);
            mDeliveryView = (View) itemView.findViewById(R.id.delivery_view);
            mVatValueUK = (TextView) itemView.findViewById(R.id.iap_tv_vat_value_uk_order_summary);
            mEditIcon = (ImageView) itemView.findViewById(R.id.edit_icon);
        }
    }
}
