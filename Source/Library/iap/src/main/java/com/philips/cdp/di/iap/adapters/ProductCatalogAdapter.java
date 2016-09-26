/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogData;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;

public class ProductCatalogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ImageLoader mImageLoader;
    private Context mContext;

    private ArrayList<ProductCatalogData> mProductCatalogList = new ArrayList<>();
    private ProductCatalogData mSelectedProduct;

    public ProductCatalogAdapter(Context pContext, ArrayList<ProductCatalogData> pArrayList) {
        mContext = pContext;
        mProductCatalogList = pArrayList;
        mImageLoader = NetworkImageLoader.getInstance(mContext).getImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.iap_product_catalog_item, parent, false);
        return new ProductCatalogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ProductCatalogData productCatalogData = mProductCatalogList.get(position);
        ProductCatalogViewHolder productHolder = (ProductCatalogViewHolder) holder;

        String imageURL = productCatalogData.getImageURL();
        String discountedPrice = productCatalogData.getDiscountedPrice();
        String formattedPrice = productCatalogData.getFormattedPrice();

        productHolder.mProductName.setText(productCatalogData.getProductTitle());
        productHolder.mProductImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.no_icon));
        productHolder.mPrice.setText(formattedPrice);
        productHolder.mCTN.setText(productCatalogData.getCtnNumber());

        if (discountedPrice == null || discountedPrice.equalsIgnoreCase("")) {
            productHolder.mDiscountedPrice.setVisibility(View.GONE);
            productHolder.mPrice.setTextColor(Utility.getThemeColor(mContext));
        } else if (formattedPrice != null && discountedPrice.equalsIgnoreCase(formattedPrice)) {
            productHolder.mPrice.setVisibility(View.GONE);
            productHolder.mDiscountedPrice.setVisibility(View.VISIBLE);
            productHolder.mDiscountedPrice.setText(discountedPrice);
        } else {
            productHolder.mDiscountedPrice.setVisibility(View.VISIBLE);
            productHolder.mDiscountedPrice.setText(discountedPrice);
            productHolder.mPrice.setPaintFlags(productHolder.mPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        getNetworkImage(productHolder, imageURL);

        productHolder.mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                setTheProductDataForDisplayingInProductDetailPage(position);
            }
        });
    }

    private void setTheProductDataForDisplayingInProductDetailPage(int position) {
        mSelectedProduct = mProductCatalogList.get(position);
        EventHelper.getInstance().notifyEventOccurred(IAPConstant.PRODUCT_DETAIL_FRAGMENT_CATALOG);
    }

    public ProductCatalogData getTheProductDataForDisplayingInProductDetailPage() {
        return mSelectedProduct;
    }

    private void getNetworkImage(final ProductCatalogViewHolder productCartProductHolder,
                                 final String imageURL) {
        mImageLoader.get(imageURL, ImageLoader.getImageListener(productCartProductHolder.mProductImage,
                R.drawable.no_icon, android.R.drawable
                        .ic_dialog_alert));
        productCartProductHolder.mProductImage.setImageUrl(imageURL, mImageLoader);
    }

    @Override
    public int getItemCount() {
        return mProductCatalogList.size();
    }

    public void tagProducts() {
        if (mProductCatalogList.size() != 0) {
            StringBuilder products = new StringBuilder();
            for (int i = 0; i < mProductCatalogList.size(); i++) {
                ProductCatalogData catalogData = mProductCatalogList.get(i);
                if (i > 0) {
                    products = products.append(",");
                }
                products = products.append("Tuscany_Campaign").append(";")
                        .append(catalogData.getProductTitle()).append(";").append(";")
                        .append(catalogData.getPriceValue());
            }
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                    IAPAnalyticsConstant.PRODUCTS, products.toString());
        }
    }

    private class ProductCatalogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        NetworkImageView mProductImage;
        TextView mProductName;
        TextView mCTN;
        TextView mPrice;
        FontIconTextView mArrow;
        TextView mDiscountedPrice;

        ProductCatalogViewHolder(View itemView) {
            super(itemView);
            mProductImage = (NetworkImageView) itemView.findViewById(R.id.image);
            mProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            mCTN = (TextView) itemView.findViewById(R.id.tv_ctn);
            mPrice = (TextView) itemView.findViewById(R.id.tv_price);
            mArrow = (FontIconTextView) itemView.findViewById(R.id.arrow);
            mDiscountedPrice = (TextView) itemView.findViewById(R.id.tv_discounted_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            setTheProductDataForDisplayingInProductDetailPage(getAdapterPosition());
        }
    }
}


