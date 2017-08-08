/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.cart.ShoppingCartData;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.view.CountDropDown;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.ArrayList;

public class ShoppingCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static final int DELETE = 0;
    private static final int INFO = 1;

    private Context mContext;
    private Resources mResources;
    private ArrayList<ShoppingCartData> mData = new ArrayList<>();
    private OutOfStockListener mOutOfStock;
    //private DeliveryModes mDeliveryMode;
    private UIKitListPopupWindow mPopupWindow;
    private ShoppingCartData shoppingCartDataForProductDetailPage;

    private Drawable countArrow;
    // private Drawable mOptionsDrawable;
    private Drawable mTrashDrawable;
    private Drawable mInfoDrawable;
    private Drawable mEditDrawable;

    private boolean mIsFreeDelivery;
    private int mSelectedItemPosition = -1;
    private int mQuantityStatus;
    private int mNewCount;

    public interface OutOfStockListener {
        void onOutOfStock(boolean isOutOfStock);
    }

    public ShoppingCartAdapter(Context context, ArrayList<ShoppingCartData> shoppingCartData,
                               OutOfStockListener isOutOfStock) {
        mContext = context;
        mResources = context.getResources();
        mData = shoppingCartData;
        setCountArrow(context, true);
        initDrawables();
        mOutOfStock = isOutOfStock;
    }

    private void initDrawables() {
        //  mOptionsDrawable = VectorDrawable.create(mContext, R.drawable.iap_options_icon_5x17);
        mTrashDrawable = VectorDrawable.create(mContext, R.drawable.iap_trash_bin);
        mInfoDrawable = VectorDrawable.create(mContext, R.drawable.iap_info);
        mEditDrawable = VectorDrawable.create(mContext, R.drawable.pencil_01);
    }

    private void setCountArrow(final Context context, final boolean isEnable) {
        if (isEnable)
            countArrow = VectorDrawable.create(context, R.drawable.iap_product_count_drop_down);
        else
            countArrow = VectorDrawable.create(context, R.drawable.iap_product_disable_count_drop_down);
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_shopping_cart_footer, parent, false);
            return new FooterShoppingCartViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_shopping_cart_data, parent, false);
            return new ShoppingCartProductHolder(v);
        }
        return null;
    }

    private void bindCountView(final View view, final int position) {
        mSelectedItemPosition = position;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ShoppingCartData data = mData.get(position);

                CountDropDown countPopUp = new CountDropDown(v, data.getStockLevel(), data
                        .getQuantity(), new CountDropDown.CountUpdateListener() {
                    @Override
                    public void countUpdate(final int oldCount, final int newCount) {
                        //mSelectedItemPosition = position;
                        mQuantityStatus = getQuantityStatus(newCount, oldCount);
                        mNewCount = newCount;
                        EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_UPDATE_PRODUCT_COUNT);
                    }
                });
                mPopupWindow = countPopUp.getPopUpWindow();
                countPopUp.show();
            }
        });
    }

    public int getNewCount() {
        return mNewCount;
    }

    public int getQuantityStatusInfo() {
        return mQuantityStatus;
    }

    private int getQuantityStatus(int newCount, int oldCount) {
        if (newCount > oldCount)
            return 1;
        else if (newCount < oldCount)
            return 0;
        else
            return -1;
    }

    private void bindDeleteOrInfoPopUP(final View view, final int selectedItem) {
        setTheProductDataForDisplayingInProductDetailPage(selectedItem);
/*        List<RowItem> rowItems = new ArrayList<>();

        mSelectedItemPosition = selectedItem;
        String delete = mResources.getString(R.string.iap_delete);
        String info = mResources.getString(R.string.iap_info);
        final String[] descriptions = new String[]{delete, info};

        rowItems.add(new RowItem(mTrashDrawable, descriptions[0]));
        rowItems.add(new RowItem(mInfoDrawable, descriptions[1]));
        mPopupWindow = new UIKitListPopupWindow(mContext, view, UIKitListPopupWindow.UIKIT_Type.UIKIT_BOTTOMLEFT, rowItems);

        mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                switch (position) {
                    case DELETE:
                        EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_DELETE_PRODUCT);
                        mPopupWindow.dismiss();
                        break;
                    case INFO:
                        setTheProductDataForDisplayingInProductDetailPage(selectedItem);
                        break;
                    default:
                }
            }
        });
        mPopupWindow.show();*/
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
            //Product Layout
            ShoppingCartProductHolder shoppingCartProductHolder = (ShoppingCartProductHolder) holder;
            String imageURL = cartData.getImageURL();
            shoppingCartProductHolder.mTvAcualPrice.setText(cartData.getFormattedTotalPrice());
            shoppingCartProductHolder.mTvAcualPrice.setPaintFlags(shoppingCartProductHolder.mTvAcualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            shoppingCartProductHolder.mTvPrice.setText(cartData.getProductTitle());
            shoppingCartProductHolder.mTvQuantity.setText(cartData.getQuantity() + "");
            shoppingCartProductHolder.mTvAfterDiscountPrice.setText(cartData.getFormattedTotalPrice());

            checkForOutOfStock(cartData.getStockLevel(), cartData.getQuantity(), shoppingCartProductHolder);

            getNetworkImage(shoppingCartProductHolder, imageURL);

            shoppingCartProductHolder.mIvOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    bindDeleteOrInfoPopUP(view, holder.getAdapterPosition());
                }
            });
            //Add arrow mark
            shoppingCartProductHolder.mTvQuantity.setCompoundDrawables(null, null, countArrow, null);
            bindCountView(shoppingCartProductHolder.mQuantityLayout, holder.getAdapterPosition());

            shoppingCartProductHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_DELETE_PRODUCT);
                }
            });

           /* System.out.println(" 111 getFormattedTotalPriceWithTax : " +cartData.getFormattedTotalPriceWithTax());
            System.out.println(" 111 getFormattedTotalPrice : " +cartData.getFormattedTotalPrice());
            System.out.println(" 111 getValuePrice : " +cartData.getValuePrice());
            System.out.println(" 111 getVatActualValue : " +cartData.getVatActualValue());*/
            System.out.println(" 111 getDiscountPrice : " +cartData.getDiscountPrice());

        } else {
            //Footer Layout
            FooterShoppingCartViewHolder shoppingCartFooter = (FooterShoppingCartViewHolder) holder;
            ShoppingCartData data;
            if (mData.get(0) != null) {
                data = mData.get(0);

                shoppingCartFooter.mTotalItems.setText(mContext.getString(R.string.iap_total)
                        + " (" + data.getTotalItems() + " " + mContext.getString(R.string.iap_items) + ")");
                shoppingCartFooter.mVatInclusiveValue.setText
                        (mContext.getString(R.string.iap_including_vat));

                shoppingCartFooter.mTotalCost.setText(data.getFormattedTotalPriceWithTax());
                if (null != data.getDeliveryMode()) {
                    handleTax(data, shoppingCartFooter);

                    String deliveryCost = data.getDeliveryMode().getDeliveryCost().getFormattedValue();
                    String deliveryMethod = data.getDeliveryMode().getName();
                    if ((deliveryCost.substring(1, (deliveryCost.length()))).equalsIgnoreCase("0.00")) {
                        mIsFreeDelivery = true;
                    }
                    shoppingCartFooter.mDeliveryPrice.setText(deliveryCost);

                    if (deliveryMethod != null) {
                        shoppingCartFooter.mDeliveryVia.setText(deliveryMethod);
                    } else {
                        shoppingCartFooter.mDeliveryVia.setText(R.string.iap_delivery_via);
                    }

                    //shoppingCartFooter.mEditIconLayout.setVisibility(View.VISIBLE);
                    shoppingCartFooter.mEditIcon.setImageDrawable(mEditDrawable);
                    /*shoppingCartFooter.mEditIconLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_EDIT_DELIVERY_MODE);
                        }
                    });*/
                } else {
                    mIsFreeDelivery = true;
                   /* shoppingCartFooter.mDeliveryVia.setVisibility(View.GONE);
                    shoppingCartFooter.mDeliveryPrice.setVisibility(View.GONE);
                    shoppingCartFooter.mDailyCollectionView.setVisibility(View.GONE);*/
                }

                String freeDeliverySpendAmount = "200";
                String freeDeliverySpendOn = mContext.getResources().getString(R.string.iap_delivery_free_description);
                freeDeliverySpendOn = String.format(freeDeliverySpendOn, freeDeliverySpendAmount);
                shoppingCartFooter.mDeliveryFreeSpend.setText(freeDeliverySpendOn);

                System.out.println(" 222 getFormattedTotalPriceWithTax : " +data.getFormattedTotalPriceWithTax());
                System.out.println(" 222 getFormattedTotalPrice : " +data.getFormattedTotalPrice());
                System.out.println(" 222 getValuePrice : " +data.getValuePrice());
                System.out.println(" 222 getVatActualValue : " +data.getVatActualValue());
                System.out.println(" 222 getVatValue : " +data.getVatValue());
                //System.out.println(" **** getDeliveryCost : " +data.getDeliveryMode().getDeliveryCost().getFormattedValue());

                shoppingCartFooter.mDailyCollectionView.setText(data.getFormattedPrice());
                shoppingCartFooter.mAdvanceCollectionView.setText(data.getFormattedPrice());
            }
        }
    }

    private void handleTax(ShoppingCartData data, FooterShoppingCartViewHolder shoppingCartFooter) {
        if (!data.isVatInclusive()) {
            shoppingCartFooter.mVatValue.setVisibility(View.GONE);
            shoppingCartFooter.mVAT.setVisibility(View.GONE);

            if (data.getVatValue() != null) {
                shoppingCartFooter.mVatInclusiveValue.setVisibility(View.VISIBLE);
                shoppingCartFooter.mVatValueUK.setVisibility(View.VISIBLE);
                shoppingCartFooter.mVatValueUK.setText(data.getVatValue());
            }
        } else {
            shoppingCartFooter.mVatInclusiveValue.setVisibility(View.GONE);
            shoppingCartFooter.mVatValueUK.setVisibility(View.GONE);

            if (data.getVatValue() != null) {
                shoppingCartFooter.mVatValue.setVisibility(View.VISIBLE);
                shoppingCartFooter.mVAT.setVisibility(View.VISIBLE);
                shoppingCartFooter.mVatValue.setText(data.getVatValue());
            }
        }
    }

    private void checkForOutOfStock(int pStockLevel, int pQuantity, ShoppingCartProductHolder pShoppingCartProductHolder) {
        if (pStockLevel == 0) {
            pShoppingCartProductHolder.mTvAfterDiscountPrice.setVisibility(View.VISIBLE);
            pShoppingCartProductHolder.mTvAfterDiscountPrice.setText(mResources.getString(R.string.iap_out_of_stock));
            pShoppingCartProductHolder.mQuantityLayout.setEnabled(false);
            pShoppingCartProductHolder.mQuantityLayout.setClickable(false);
            setCountArrow(mContext, false);
            setCountArrow(mContext, false);
            mOutOfStock.onOutOfStock(true);
        } else if (pStockLevel < pQuantity) {
            pShoppingCartProductHolder.mQuantityLayout.setEnabled(false);
            pShoppingCartProductHolder.mQuantityLayout.setClickable(false);
            pShoppingCartProductHolder.mTvAfterDiscountPrice.setVisibility(View.VISIBLE);
            pShoppingCartProductHolder.mTvAfterDiscountPrice.setText("Only " + pStockLevel + " left");
            setCountArrow(mContext, false);
            mOutOfStock.onOutOfStock(true);
        } else {
            pShoppingCartProductHolder.mQuantityLayout.setEnabled(true);
            pShoppingCartProductHolder.mQuantityLayout.setClickable(true);
            pShoppingCartProductHolder.mTvQuantity.setEnabled(true);
            setCountArrow(mContext, true);
        }
    }

    private void getNetworkImage(final ShoppingCartProductHolder shoppingCartProductHolder, final String imageURL) {
        ImageLoader mImageLoader;
        // Instantiate the RequestQueue.
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
        // FrameLayout mDotsLayout;
        TextView mTvPrice;
        TextView mTvAcualPrice;
        RelativeLayout mQuantityLayout;
        TextView mTvAfterDiscountPrice;
        TextView mTvQuantity;
        TextView mIvOptions;
        Button deleteBtn;

        ShoppingCartProductHolder(final View itemView) {
            super(itemView);

            mNetworkImage = (NetworkImageView) itemView.findViewById(R.id.image);
            // mDotsLayout = (FrameLayout) itemView.findViewById(R.id.frame);
            mTvPrice = (TextView) itemView.findViewById(R.id.price_label);
            mTvAcualPrice = (TextView) itemView.findViewById(R.id.actual_price);
            mQuantityLayout = (RelativeLayout) itemView.findViewById(R.id.quantity_count_layout);
            mTvAfterDiscountPrice = (TextView) itemView.findViewById(R.id.after_discount_price);
            mTvQuantity = (TextView) itemView.findViewById(R.id.quantity_val);
            mIvOptions = (TextView) itemView.findViewById(R.id.right_arrow);
            deleteBtn = (Button) itemView.findViewById(R.id.delete_btn);
        }
    }

    private class FooterShoppingCartViewHolder extends RecyclerView.ViewHolder {
        TextView mDeliveryPrice;
        TextView mVatValue;
        TextView mVatInclusiveValue;
        TextView mTotalItems;
        TextView mTotalCost;
        TextView mDailyCollectionView;
        TextView mDeliveryVia;
        TextView mVatValueUK;
        TextView mVAT;
        ImageView mEditIcon;
        TextView mDeliveryFreeSpend;
        TextView mAdvanceCollectionView;
        // RelativeLayout mEditIconLayout;

        FooterShoppingCartViewHolder(View itemView) {
            super(itemView);
            mDeliveryPrice = (TextView) itemView.findViewById(R.id.iap_tv_delivery_price);
            mVatValue = (TextView) itemView.findViewById(R.id.iap_tv_including_tax);
            mVatInclusiveValue = (TextView) itemView.findViewById(R.id.iap_tv_vat_inclusive);
            mTotalItems = (TextView) itemView.findViewById(R.id.iap_tv_total);
            mTotalCost = (TextView) itemView.findViewById(R.id.iap_tv_total_cost);
            mDailyCollectionView = (TextView) itemView.findViewById(R.id.iap_daily_collection_val);
            mDeliveryVia = (TextView) itemView.findViewById(R.id.iap_tv_delivery_via_ups);
            mVatValueUK = (TextView) itemView.findViewById(R.id.iap_delivery_ups_val);
            mVAT = (TextView) itemView.findViewById(R.id.iap_tv_vat);
            mEditIcon = (ImageView) itemView.findViewById(R.id.edit_icon);
            mDeliveryFreeSpend = (TextView) itemView.findViewById(R.id.delivery_free_description);
            mAdvanceCollectionView = (TextView) itemView.findViewById(R.id.advance_collection_value);
            // mEditIconLayout = (RelativeLayout) itemView.findViewById(R.id.edit_icon_layout);
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
