/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.cart.ShoppingCartData;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.uid.view.widget.UIPicker;

import java.util.ArrayList;

import static com.philips.cdp.di.iap.R.id.delivery_via_ups;

public class CheckOutHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private Context mContext;
    private Resources mResources;
    private ArrayList<ShoppingCartData> mData = new ArrayList<>();
    private OutOfStockListener mOutOfStock;
    private UIPicker mPopupWindow;
    private ShoppingCartData shoppingCartDataForProductDetailPage;

    private Drawable countArrow;
    private boolean mIsFreeDelivery;
    private int mSelectedItemPosition = -1;
    private int mQuantityStatus;
    private int mNewCount;
    private OrderSummaryUpdateListner orderSummaryUpdateListner;
    private AddressFields mBillingAddress;

    public void setOrderSummaryUpdateListner(OrderSummaryUpdateListner orderSummaryUpdateListner) {
        this.orderSummaryUpdateListner = orderSummaryUpdateListner;
    }

    public interface OrderSummaryUpdateListner {
        void onGetCartUpdate();
    }

    public interface OutOfStockListener {
        void onOutOfStock(boolean isOutOfStock);
    }

    public CheckOutHistoryAdapter(Context context, ArrayList<ShoppingCartData> shoppingCartData,
                                  OutOfStockListener isOutOfStock) {
        mContext = context;
        mResources = context.getResources();
        mData = shoppingCartData;
        setCountArrow(context, true);
        mOutOfStock = isOutOfStock;
    }

    private void setCountArrow(final Context context, final boolean isEnable) {
        if (isEnable) {
            countArrow = context.getDrawable(R.drawable.iap_product_count_drop_down);
            countArrow.setColorFilter(new
                    PorterDuffColorFilter(mContext.getResources().getColor(R.color.uid_quiet_button_icon_selector), PorterDuff.Mode.MULTIPLY));
        } else {
            countArrow = VectorDrawable.create(context, R.drawable.iap_product_disable_count_drop_down);
        }
        int width = (int) mResources.getDimension(R.dimen.iap_count_drop_down_icon_width);
        int height = (int) mResources.getDimension(R.dimen.iap_count_drop_down_icon_height);
        countArrow.setBounds(0, 0, width, height);
    }

    @Override
    public int getItemViewType(final int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(final int position) {
        return position == mData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_checkout_history_footer, parent, false);
            return new FooterShoppingCartViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_checkout_history_data, parent, false);
            return new ShoppingCartProductHolder(v);
        }
        return null;
    }


    public int getNewCount() {
        return mNewCount;
    }

    public int getQuantityStatusInfo() {
        return mQuantityStatus;
    }

    private void showProductDetails(final int selectedItem) {
        mSelectedItemPosition = selectedItem;
        setTheProductDataForDisplayingInProductDetailPage(selectedItem);
    }

    public int getSelectedItemPosition() {
        return mSelectedItemPosition;
    }

    private void setTheProductDataForDisplayingInProductDetailPage(int position) {
        shoppingCartDataForProductDetailPage = mData.get(position);
        EventHelper.getInstance().notifyEventOccurred(IAPConstant.PRODUCT_DETAIL_FRAGMENT);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (mData.size() == 0)
            return;

        if (holder instanceof ShoppingCartProductHolder) {

            final ShoppingCartData cartData = mData.get(holder.getAdapterPosition());
            ShoppingCartProductHolder shoppingCartProductHolder = (ShoppingCartProductHolder) holder;
            String imageURL = cartData.getImageURL();
            shoppingCartProductHolder.mTvPrice.setText(cartData.getProductTitle());
            shoppingCartProductHolder.mTvAfterDiscountPrice.setText(cartData.getFormattedPrice());

            checkForOutOfStock(cartData.getStockLevel(), cartData.getQuantity(), shoppingCartProductHolder);
            getNetworkImage(shoppingCartProductHolder, imageURL);
        } else {
            //Footer Layout
            FooterShoppingCartViewHolder shoppingCartFooter = (FooterShoppingCartViewHolder) holder;
            ShoppingCartData data;

            if (mData.get(0) != null) {
                data = mData.get(0);

                shoppingCartFooter.mTotalCost.setText(data.getFormattedTotalPriceWithTax());
                shoppingCartFooter.mVatValue.setText(data.getVatValue());
                if (null != data.getDeliveryMode()) {
                    handleTax(data, shoppingCartFooter);

                    String deliveryCost = data.getDeliveryMode().getDeliveryCost().getFormattedValue();
                    String deliveryMethod = data.getDeliveryMode().getName();
                    if ((deliveryCost.substring(1, (deliveryCost.length()))).equalsIgnoreCase("0.00")) {
                        mIsFreeDelivery = true;
                    }

                    shoppingCartFooter.mDeliveryPrice.setText(deliveryCost);
                    shoppingCartFooter.mDeliveryUpsVal.setText(deliveryCost);

                    if (deliveryMethod != null) {
                        shoppingCartFooter.mDeliveryVia.setText(deliveryMethod);
                        String freeDeliverySpendOn = mContext.getResources().getString(R.string.iap_delivery_ups_parcel);
                        freeDeliverySpendOn = String.format(freeDeliverySpendOn, deliveryMethod);
                        shoppingCartFooter.mDeliveryVia.setText(freeDeliverySpendOn);
                        shoppingCartFooter.mDeliveryTitle.setText(freeDeliverySpendOn);
                    } else {
                        shoppingCartFooter.mDeliveryVia.setText(R.string.iap_delivery_via);
                        shoppingCartFooter.mDeliveryTitle.setText(R.string.iap_delivery_via);
                    }

                    shoppingCartFooter.mDeliveryVia.setVisibility(View.VISIBLE);
                    shoppingCartFooter.mDeliveryUpsVal.setVisibility(View.VISIBLE);

                    shoppingCartFooter.mDeliveryUPSParcelContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_EDIT_DELIVERY_MODE);
                        }
                    });

                } else {
                    shoppingCartFooter.mExtraOption.setVisibility(View.GONE);
                    shoppingCartFooter.mDeliveryUPSParcelContainer.setVisibility(View.GONE);
                    mIsFreeDelivery = true;
                }

                if (data.getDeliveryAddressEntity() != null) {
                    shoppingCartFooter.mShippingName.setText(data.getDeliveryAddressEntity().getFirstName() + " " + data.getDeliveryAddressEntity().getLastName());
                    final String formattedAddress = data.getDeliveryAddressEntity().getFormattedAddress();
                    shoppingCartFooter.mShippingAddress.setText(Utility.formatAddress(formattedAddress));
                }

                mBillingAddress = CartModelContainer.getInstance().getBillingAddress();
                if (null != mBillingAddress) {
                    String billingName = mBillingAddress.getFirstName() + " " + mBillingAddress.getLastName();
                    shoppingCartFooter.mBillingName.setText(billingName);
                    shoppingCartFooter.mBillingAddress.setText(Utility.getAddressToDisplay(mBillingAddress));
                }

                for (int i = 0; i < mData.size(); i++) {
                    View priceInfo = View.inflate(mContext, R.layout.iap_price_item, null);
                    TextView mProductName = (TextView) priceInfo.findViewById(R.id.product_name);
                    TextView mProductPrice = (TextView) priceInfo.findViewById(R.id.product_price);
                    mProductName.setText(Integer.toString(mData.get(i).getQuantity()) + "x " + mData.get(i).getProductTitle().toString());
                    mProductPrice.setText(mData.get(i).getFormattedTotalPrice().toString());
                    shoppingCartFooter.mPriceContainer.addView(priceInfo);
                }
            }
        }
        orderSummaryUpdateListner.onGetCartUpdate();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProductDetails(holder.getAdapterPosition());
            }
        });

    }

    private void handleTax(ShoppingCartData data, FooterShoppingCartViewHolder shoppingCartFooter) {
        if (!data.isVatInclusive()) {
            shoppingCartFooter.mVatValue.setVisibility(View.GONE);
            if (data.getVatValue() != null) {
                shoppingCartFooter.mVatInclusiveValue.setVisibility(View.VISIBLE);
                shoppingCartFooter.mDeliveryUpsVal.setVisibility(View.VISIBLE);
                shoppingCartFooter.mDeliveryUpsVal.setText(data.getVatValue());
            }
        } else {
            if (data.getVatValue() != null) {
                shoppingCartFooter.mVatValue.setVisibility(View.VISIBLE);
                shoppingCartFooter.mVatValue.setText(data.getVatValue());
            }
        }
    }

    private void checkForOutOfStock(int pStockLevel, int pQuantity, ShoppingCartProductHolder pShoppingCartProductHolder) {
        if (pStockLevel == 0) {
            pShoppingCartProductHolder.mTvAfterDiscountPrice.setVisibility(View.VISIBLE);
            pShoppingCartProductHolder.mTvAfterDiscountPrice.setText(mResources.getString(R.string.iap_out_of_stock));
            setCountArrow(mContext, false);
            setCountArrow(mContext, false);
            mOutOfStock.onOutOfStock(true);
        } else if (pStockLevel < pQuantity) {
            pShoppingCartProductHolder.mTvAfterDiscountPrice.setVisibility(View.VISIBLE);
            pShoppingCartProductHolder.mTvAfterDiscountPrice.setText("Only " + pStockLevel + " left");
            setCountArrow(mContext, false);
            mOutOfStock.onOutOfStock(true);
        }
    }

    private void getNetworkImage(final ShoppingCartProductHolder shoppingCartProductHolder, final String imageURL) {
        ImageLoader mImageLoader;
        mImageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();

        mImageLoader.get(imageURL, ImageLoader.getImageListener(shoppingCartProductHolder.mNetworkImage,
                R.drawable.no_icon, android.R.drawable
                        .ic_dialog_alert));
        shoppingCartProductHolder.mNetworkImage.setImageUrl(imageURL, mImageLoader);
    }

    public void onStop() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public ShoppingCartData getTheProductDataForDisplayingInProductDetailPage() {
        return shoppingCartDataForProductDetailPage;
    }

    public boolean isFreeDelivery() {
        return mIsFreeDelivery;
    }

    @Override
    public int getItemCount() {
        if (mData.size() == 0) {
            return 0;
        } else {
            return mData.size() + 1;
        }
    }

    private class ShoppingCartProductHolder extends RecyclerView.ViewHolder {
        NetworkImageView mNetworkImage;
        TextView mTvPrice;
        TextView mTvActualPrice;
        TextView mTvAfterDiscountPrice;
        TextView mIvOptions;

        ShoppingCartProductHolder(final View itemView) {
            super(itemView);
            mNetworkImage = (NetworkImageView) itemView.findViewById(R.id.image);
            mTvPrice = (TextView) itemView.findViewById(R.id.price_label);
            mTvActualPrice = (TextView) itemView.findViewById(R.id.actual_price);
            mTvAfterDiscountPrice = (TextView) itemView.findViewById(R.id.after_discount_price);
            mIvOptions = (TextView) itemView.findViewById(R.id.right_arrow);
        }
    }

    private class FooterShoppingCartViewHolder extends RecyclerView.ViewHolder {
        TextView mDeliveryPrice;
        TextView mVatValue;
        TextView mVatInclusiveValue;
        TextView mTotalItems;
        TextView mTotalCost;
        TextView mDeliveryVia;
        TextView mDeliveryUpsVal;
        ImageView mEditIcon;
        TextView mExtraOption;
        TextView mDeliveryTitle;
        TextView mShippingName;
        TextView mShippingAddress;
        TextView mBillingName;
        TextView mBillingAddress;
        LinearLayout mPriceContainer;
        RelativeLayout mDeliveryUPSParcelContainer;

        FooterShoppingCartViewHolder(View itemView) {
            super(itemView);
            mDeliveryPrice = (TextView) itemView.findViewById(R.id.delivery_price);
            mVatValue = (TextView) itemView.findViewById(R.id.including_tax_val);
            mVatInclusiveValue = (TextView) itemView.findViewById(R.id.vat_inclusive);
            mTotalItems = (TextView) itemView.findViewById(R.id.total_label);
            mTotalCost = (TextView) itemView.findViewById(R.id.total_cost_val);
            mDeliveryVia = (TextView) itemView.findViewById(delivery_via_ups);
            mDeliveryUpsVal = (TextView) itemView.findViewById(R.id.delivery_ups_val);
            mExtraOption = (TextView) itemView.findViewById(R.id.extra_option);
            mEditIcon = (ImageView) itemView.findViewById(R.id.edit_icon);
            mDeliveryTitle = (TextView) itemView.findViewById(R.id.delivery_ups_title);
            mShippingName = (TextView) itemView.findViewById(R.id.tv_shipping_name);
            mShippingAddress = (TextView) itemView.findViewById(R.id.tv_shipping_address);
            mBillingName = (TextView) itemView.findViewById(R.id.tv_billing_name);
            mBillingAddress = (TextView) itemView.findViewById(R.id.tv_billing_address);
            mPriceContainer = (LinearLayout) itemView.findViewById(R.id.price_container);
            mDeliveryUPSParcelContainer = (RelativeLayout) itemView.findViewById(R.id.delivery_ups_parcel_container);

        }
    }

    public void tagProducts() {
        StringBuilder products = new StringBuilder();
        for (int i = 0; i < mData.size(); i++) {
            if (i > 0) {
                products = products.append(",");
            }
            products = products.append(mData.get(i).getCategory()).append(";")
                    .append(mData.get(i).getProductTitle()).append(";").append(String.valueOf(mData.get(i).getQuantity()))
                    .append(";").append(mData.get(i).getValuePrice());
        }
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.PRODUCTS, products.toString());
    }
}
