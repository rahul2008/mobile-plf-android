package com.philips.cdp.di.iap.adapters;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.AddressController.AddressListener;
import com.philips.cdp.di.iap.response.addresses.DeliveryCost;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.response.orders.DeliveryMode;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrderProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ShoppingCartPresenter.LoadListener<ShoppingCartData> {
    private final static String TAG = OrderProductAdapter.class.getSimpleName();

    private List<ShoppingCartData> mShoppingCartDataList;
    private Context mContext;
    private ShoppingCartData cartData;
    private AddressFields mBillingAddress;
    private PaymentMethod mPaymentMethod;
    private AddressListener mListener;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public OrderProductAdapter(Context pContext, AddressListener listener, ArrayList<ShoppingCartData> pShoppingCartDataList,
                               AddressFields pBillingAddress, PaymentMethod pPaymentMethod) {
        mContext = pContext;
        mListener = listener;
        mShoppingCartDataList = pShoppingCartDataList;
        mBillingAddress = pBillingAddress;
        mPaymentMethod = pPaymentMethod;
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
        if (holder instanceof FooterOrderSummaryViewHolder) {
            FooterOrderSummaryViewHolder footerHolder = (FooterOrderSummaryViewHolder) holder;
            footerHolder.mTitleBillingAddress.setText(R.string.iap_billing_address);
            footerHolder.mTitleDeliveryAddress.setText(R.string.iap_shipping_address);
            footerHolder.mTitleVat.setText(R.string.iap_vat);
            footerHolder.mTitleTotalPrice.setText(R.string.iap_total_val);
            AddressFields shippingAddress = CartModelContainer.getInstance().getShippingAddressFields();
            String shippingName = shippingAddress.getFirstName() + " " + shippingAddress.getLastName();
            footerHolder.mShippingFirstName.setText(shippingName);
            footerHolder.mShippingAddress.setText(Utility.createAddress(shippingAddress));
            if (null != mBillingAddress) {
                String billingName = mBillingAddress.getFirstName() + " " + mBillingAddress.getLastName();
                footerHolder.mBillingFirstName.setText(billingName);
                footerHolder.mBillingAddress.setText(Utility.createAddress(mBillingAddress));
            }
            if (null != mPaymentMethod) {
                String paymentBillingName = mPaymentMethod.getBillingAddress().getFirstName() + " " + mPaymentMethod.getBillingAddress().getLastName();
                footerHolder.mBillingFirstName.setText(paymentBillingName);
                footerHolder.mBillingAddress.setText(Utility.createAddress(mPaymentMethod.getBillingAddress()));

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
                if(deliveryMethod!=null){
                    footerHolder.mTitleDelivery.setText(deliveryMethod);
                }else{
                    footerHolder.mTitleDelivery.setText(R.string.iap_delivery_via);
                }
            } else {
                //footerHolder.mDeliveryPrice.setText("0.0");
                footerHolder.mTitleDelivery.setVisibility(View.GONE);
                footerHolder.mDeliveryPrice.setVisibility(View.GONE);
                footerHolder.mDeliveryView.setVisibility(View.GONE);
            }
            footerHolder.mTotalPriceLable.setText(mContext.getString(R.string.iap_total) + " (" + getLastValidItem().getTotalItems() + " " + mContext.getString(R.string.iap_items) + ")");
            footerHolder.mTotalPrice.setText(getLastValidItem().getTotalPriceWithTaxFormatedPrice());
            footerHolder.mVatValue.setText(getLastValidItem().getVatValue());
            if (!getLastValidItem().isVatInclusive()) {
                footerHolder.mVatInclusive.setVisibility(View.VISIBLE);
                footerHolder.mVatInclusive.setText(String.format(mContext.getString(R.string.iap_vat_inclusive_text), mContext.getString(R.string.iap_vat)));
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

            final List<DeliveryModes> mDeliveryModes = CartModelContainer.getInstance().getDeliveryModes();
            footerHolder.mEditIcon.setVisibility(View.VISIBLE);
            footerHolder.mEditIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    View convertView = (LayoutInflater.from(mContext).inflate(R.layout.iap_delivery_dialog, null));
                    alertDialog.setView(convertView);
                    ListView lv = (ListView) convertView.findViewById(R.id.lv);
                    DeliveryModeAdapter adapter = new DeliveryModeAdapter(mContext,R.layout.iap_delivery_mode_spinner_item, mDeliveryModes);
                    lv.setClickable(true);
                    lv.setAdapter(adapter);

                    final Dialog dialog = alertDialog.create();
                    dialog.show();

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dialog.dismiss();
                            AddressController addressController = new AddressController(mContext, mListener);
                            addressController.setDeliveryMode(mDeliveryModes.get(position).getCode());

                        }
                    });



                }
            });


        } else {
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
    public void onLoadListenerError(IAPNetworkError error) {
        //NOP
    }

    @Override
    public void onRetailerError(IAPNetworkError errorMsg) {
        //NOP
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
