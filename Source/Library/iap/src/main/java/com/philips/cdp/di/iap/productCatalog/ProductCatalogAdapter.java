/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.productCatalog;

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
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.tagging.Tagging;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;

public class ProductCatalogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ProductCatalogPresenter.LoadListener {

    private final ImageLoader mImageLoader;
    private Context mContext;
    private ArrayList<ProductCatalogData> mData = new ArrayList<>();
    ProductCatalogData productCatalogDataForProductDetailPage;

    public ProductCatalogAdapter(Context pContext, ArrayList<ProductCatalogData> pArrayList) {
        mContext = pContext;
        mData = pArrayList;
        mImageLoader = NetworkImageLoader.getInstance(mContext)
            .getImageLoader();
}


    @Override
    public void onLoadFinished(final ArrayList<ProductCatalogData> data) {
        mData = data;
        notifyDataSetChanged();
        tagProducts();
    }

    @Override
    public void onLoadError(IAPNetworkError error) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_product_catalog_item, parent, false);
        return new ProductCatalogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ProductCatalogData productCatalogData = mData.get(position);
        ProductCatalogViewHolder productHolder = (ProductCatalogViewHolder) holder;
        String imageURL = productCatalogData.getImageURL();
        productHolder.mProductName.setText(productCatalogData.getProductTitle());
        productHolder.mProductImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.no_icon));
        productHolder.mPrice.setText(productCatalogData.getFormatedPrice());
        productHolder.mCTN.setText(productCatalogData.getCtnNumber());
        if(productCatalogData.getDiscountedPrice()==null || productCatalogData.getDiscountedPrice()==""){
            productHolder.mDiscountedPrice.setVisibility(View.GONE);
            productHolder.mPrice.setTextColor(Utility.getThemeColor(mContext));
        }else {
            productHolder.mDiscountedPrice.setVisibility(View.VISIBLE);
            productHolder.mDiscountedPrice.setText(productCatalogData.getDiscountedPrice());
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
        productCatalogDataForProductDetailPage = mData.get(position);
        EventHelper.getInstance().notifyEventOccurred(IAPConstant.PRODUCT_DETAIL_FRAGMENT_CATALOG);
    }

    public ProductCatalogData getTheProductDataForDisplayingInProductDetailPage() {
        return productCatalogDataForProductDetailPage;
    }

    private void getNetworkImage(final ProductCatalogViewHolder productCartProductHolder, final String imageURL) {
        mImageLoader.get(imageURL, ImageLoader.getImageListener(productCartProductHolder.mProductImage,
                R.drawable.no_icon, android.R.drawable
                        .ic_dialog_alert));
        productCartProductHolder.mProductImage.setImageUrl(imageURL, mImageLoader);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void tagProducts(){
        if(mData.size() != 0){
            StringBuilder products = new StringBuilder();
            for (int i = 0; i < mData.size(); i++) {
                ProductCatalogData catalogData = mData.get(i);
                if (i > 0) {
                    products = products.append(",");
                }
                products = products.append("Tuscany_Campaign").append(";")
                        .append(catalogData.getProductTitle()).append(";").append(";")
                        .append(catalogData.getPriceValue());
            }
            Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.PRODUCTS, products);
        }
    }

    public class ProductCatalogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        NetworkImageView mProductImage;
        TextView mProductName;
        TextView mCTN;
        TextView mPrice;
        FontIconTextView mArrow;
        TextView mDiscountedPrice;

        public ProductCatalogViewHolder(View itemView) {
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


