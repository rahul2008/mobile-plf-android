/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.Fragments.DeliveryModeDialog;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.core.ShoppingCartAPI;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.CountDropDown;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.RowItem;

import java.util.ArrayList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShoppingCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DeliveryModeDialog.DialogListener {

    private Context mContext;
    private Resources mResources;
    private ArrayList<ShoppingCartData> mData = new ArrayList<>();
    private ShoppingCartAPI mPresenter;
    private Drawable countArrow;
    private UIKitListPopupWindow mPopupWindow;
    private ShoppingCartData shoppingCartDataForProductDetailPage;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static final int DELETE = 0;
    private static final int INFO = 1;
    private Drawable mOptionsDrawable;
    private OutOfStockListener mOutOfStock;

    private Drawable mTrashDrawable;
    private Drawable mInfoDrawable;
    private boolean mIsFreeDelivery;

    private Drawable mEditDrawable;

    private DeliveryModes mDeliveryMode;
    private DeliveryModeDialog mDialog;

//    public boolean mIsDeliveryAddressSet;

    public interface OutOfStockListener {
        void onOutOfStock(boolean isOutOfStock);
    }

    public ShoppingCartAdapter(Context context, ArrayList<ShoppingCartData> shoppingCartData,
                               OutOfStockListener iOutOfStock, final ShoppingCartAPI shoppingCartAPI) {
        mContext = context;
        mResources = context.getResources();
        mData = shoppingCartData;
        mPresenter = shoppingCartAPI;
        setCountArrow(context);
        initDrawables();
        mOutOfStock = iOutOfStock;
    }

    private void initDrawables() {
        mOptionsDrawable = VectorDrawable.create(mContext, R.drawable.iap_options_icon_5x17);
        mTrashDrawable = VectorDrawable.create(mContext, R.drawable.iap_trash_bin);
        mInfoDrawable = VectorDrawable.create(mContext, R.drawable.iap_info);
        mEditDrawable = VectorDrawable.create(mContext, R.drawable.pencil_01);
    }

    private void setCountArrow(final Context context) {
        countArrow = VectorDrawable.create(context, R.drawable.iap_product_count_drop_down);
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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ShoppingCartData data = mData.get(position);

                CountDropDown countPopUp = new CountDropDown(v, data.getStockLevel(), data
                        .getQuantity(), new CountDropDown.CountUpdateListener() {
                    @Override
                    public void countUpdate(final int oldCount, final int newCount) {
                        int quantityStatus = getQuantityStatus(newCount, oldCount);
                        if (!Utility.isProgressDialogShowing()) {
                            Utility.showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
//                            mIsDeliveryAddressSet = true;
                            mPresenter.updateProductQuantity(mData.get(position), newCount, quantityStatus);
                        }
                    }
                });
                mPopupWindow = countPopUp.getPopUpWindow();
                countPopUp.show();
            }
        });
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
        List<RowItem> rowItems = new ArrayList<>();

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
                        if (!Utility.isProgressDialogShowing()) {
                            Utility.showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
                            mPresenter.deleteProduct(mData.get(selectedItem));
                            mPopupWindow.dismiss();
                        }
                        break;
                    case INFO:
                        setTheProductDataForDisplayingInProductDetailPage(selectedItem);
                        break;
                    default:
                }
            }
        });
        mPopupWindow.show();
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
            //Product Layout
            final ShoppingCartData cartData = mData.get(position);
            ShoppingCartProductHolder shoppingCartProductHolder = (ShoppingCartProductHolder) holder;
            String imageURL = cartData.getImageURL();
            shoppingCartProductHolder.mTvProductTitle.setText(cartData.getProductTitle());
            shoppingCartProductHolder.mIvOptions.setImageDrawable(mOptionsDrawable);
            shoppingCartProductHolder.mTvPrice.setText(cartData.getTotalPriceFormatedPrice());
            shoppingCartProductHolder.mTvQuantity.setText(cartData.getQuantity() + "");

            checkForOutOfStock(cartData.getStockLevel(), cartData.getQuantity(), shoppingCartProductHolder);

            getNetworkImage(shoppingCartProductHolder, imageURL);

            shoppingCartProductHolder.mDotsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    bindDeleteOrInfoPopUP(view, position);
                }
            });
            //Add arrow mark
            shoppingCartProductHolder.mTvQuantity.setCompoundDrawables(null, null, countArrow, null);
            bindCountView(shoppingCartProductHolder.mQuantityLayout, position);
        } else {
            //Footer Layout
            FooterShoppingCartViewHolder shoppingCartFooter = (FooterShoppingCartViewHolder) holder;
            ShoppingCartData data;
            if (mData.get(0) != null) {
                data = mData.get(0);

                shoppingCartFooter.mTotalItems.setText(mContext.getString(R.string.iap_total)
                        + " (" + data.getTotalItems() + " " + mContext.getString(R.string.iap_items) + ")");
                shoppingCartFooter.mVatInclusiveValue.setText
                        (String.format(mContext.getString(R.string.iap_vat_inclusive_text),
                                mContext.getString(R.string.iap_vat)));

                if (!data.isVatInclusive()) {
                    shoppingCartFooter.mVatValue.setVisibility(View.GONE);
                    shoppingCartFooter.mVAT.setVisibility(View.GONE);

                    shoppingCartFooter.mVatInclusiveValue.setVisibility(View.VISIBLE);
                    shoppingCartFooter.mVatValueUK.setVisibility(View.VISIBLE);
                    shoppingCartFooter.mVatValueUK.setText(data.getVatValue());
                } else {
                    shoppingCartFooter.mVatInclusiveValue.setVisibility(View.GONE);
                    shoppingCartFooter.mVatValueUK.setVisibility(View.GONE);

                    shoppingCartFooter.mVatValue.setVisibility(View.VISIBLE);
                    shoppingCartFooter.mVAT.setVisibility(View.VISIBLE);
                    shoppingCartFooter.mVatValue.setText(data.getVatValue());
                }

                shoppingCartFooter.mTotalCost.setText(data.getTotalPriceWithTaxFormatedPrice());
                if (null != data.getDeliveryMode()) {
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

                    shoppingCartFooter.mEditIconLayout.setVisibility(View.VISIBLE);
                    shoppingCartFooter.mEditIcon.setImageDrawable(mEditDrawable);
                    shoppingCartFooter.mEditIconLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog = new DeliveryModeDialog(mContext, ShoppingCartAdapter.this, (AddressController.AddressListener) mOutOfStock);
                            mDialog.showDialog();

                        }
                    });
                } else {
                    mIsFreeDelivery = true;
                    //shoppingCartFooter.mDeliveryPrice.setText("0.0");
                    shoppingCartFooter.mDeliveryVia.setVisibility(View.GONE);
                    shoppingCartFooter.mDeliveryPrice.setVisibility(View.GONE);
                    shoppingCartFooter.mDeliveryView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void checkForOutOfStock(int pStockLevel, int pQuantity, ShoppingCartProductHolder pShoppingCartProductHolder) {
        if (pStockLevel == 0) {
            pShoppingCartProductHolder.mTvStock.setVisibility(View.VISIBLE);
            pShoppingCartProductHolder.mTvStock.setText(mResources.getString(R.string.iap_out_of_stock));
            mOutOfStock.onOutOfStock(true);
        } else if (pStockLevel < pQuantity) {
            pShoppingCartProductHolder.mTvStock.setVisibility(View.VISIBLE);
            pShoppingCartProductHolder.mTvStock.setText("Only " + pStockLevel + " left");
            mOutOfStock.onOutOfStock(true);
        } else {
            pShoppingCartProductHolder.mTvStock.setVisibility(View.GONE);
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

    public DeliveryModes getDeliveryMode() {
        return mDeliveryMode;
    }

    @Override
    public void onItemClick(int position) {
        final List<DeliveryModes> deliveryModes = CartModelContainer.getInstance().getDeliveryModes();
        mDeliveryMode = deliveryModes.get(position);

    }
/*
    @Override
    public long getItemId(final int position) {
        return position;
    }*/

    private class ShoppingCartProductHolder extends RecyclerView.ViewHolder {
        NetworkImageView mNetworkImage;
        FrameLayout mDotsLayout;
        TextView mTvPrice;
        TextView mTvProductTitle;
        RelativeLayout mQuantityLayout;
        TextView mTvStock;
        TextView mTvQuantity;
        ImageView mIvOptions;

        ShoppingCartProductHolder(final View itemView) {
            super(itemView);
            mNetworkImage = (NetworkImageView) itemView.findViewById(R.id.image);
            mDotsLayout = (FrameLayout) itemView.findViewById(R.id.frame);
            mTvPrice = (TextView) itemView.findViewById(R.id.price);
            mTvProductTitle = (TextView) itemView.findViewById(R.id.text1Name);
            mQuantityLayout = (RelativeLayout) itemView.findViewById(R.id.quantity_count_layout);
            mTvStock = (TextView) itemView.findViewById(R.id.out_of_stock);
            mTvQuantity = (TextView) itemView.findViewById(R.id.text2value);
            mIvOptions = (ImageView) itemView.findViewById(R.id.dots);
        }
    }

    private class FooterShoppingCartViewHolder extends RecyclerView.ViewHolder {
        TextView mDeliveryPrice;
        TextView mVatValue;
        TextView mVatInclusiveValue;
        TextView mTotalItems;
        TextView mTotalCost;
        View mDeliveryView;
        TextView mDeliveryVia;
        TextView mVatValueUK;
        TextView mVAT;
        ImageView mEditIcon;
        RelativeLayout mEditIconLayout;

        FooterShoppingCartViewHolder(View itemView) {
            super(itemView);
            mDeliveryPrice = (TextView) itemView.findViewById(R.id.iap_tv_delivery_price);
            mVatValue = (TextView) itemView.findViewById(R.id.iap_tv_vat_value);
            mVatInclusiveValue = (TextView) itemView.findViewById(R.id.iap_tv_vat_inclusive);
            mTotalItems = (TextView) itemView.findViewById(R.id.iap_tv_totalItems);
            mTotalCost = (TextView) itemView.findViewById(R.id.iap_tv_totalcost);
            mDeliveryView = itemView.findViewById(R.id.iap_divider_bottom_delivery);
            mDeliveryVia = (TextView) itemView.findViewById(R.id.iap_tv_delivery_via_ups);
            mVatValueUK = (TextView) itemView.findViewById(R.id.iap_tv_vat_value_uk_shopping_cart);
            mVAT = (TextView) itemView.findViewById(R.id.iap_tv_vat);
            mEditIcon = (ImageView) itemView.findViewById(R.id.edit_icon);
            mEditIconLayout = (RelativeLayout) itemView.findViewById(R.id.edit_icon_layout);
        }
    }

    public void tagProducts() {
        StringBuilder products = new StringBuilder();
        //  CartsEntity cart = mCartData.getCarts().get(0);
        for (int i = 0; i < mData.size(); i++) {
            //EntriesEntity entriesEntity = cart.getEntries().get(i);
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

    /*public void setDeliveryAddress(boolean isDeliveryAddressSet) {
        mIsDeliveryAddressSet = isDeliveryAddressSet;
    }*/

}
