/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.productCatalog;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;

public class ProductCatalogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ProductCatalogPresenter.LoadListener {

    private final ImageLoader mImageLoader;
    private Context mContext;
    private ArrayList<ProductCatalogData> mData = new ArrayList<>();
    private FragmentManager mFragmentManager;
    ProductCatalogData productCatalogDataForProductDetailPage;

    public ProductCatalogAdapter(Context pContext, ArrayList<ProductCatalogData> pArrayList, FragmentManager pFragmentManager) {
        mContext = pContext;
        mData = pArrayList;
        mFragmentManager = pFragmentManager;
        // Instantiate the RequestQueue.
        mImageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();
    }

    @Override
    public void onLoadFinished(final ArrayList<ProductCatalogData> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_product_catalog_item, parent, false);
        return new ProductCatalogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (mData.size() == 0)
            return;
        ProductCatalogData productCatalogData = mData.get(position);
        ProductCatalogViewHolder productHolder = (ProductCatalogViewHolder) holder;
        String imageURL = productCatalogData.getImageURL();
        productHolder.mProductName.setText(productCatalogData.getProductTitle());
        productHolder.mProductImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.toothbrush));
        productHolder.mPrice.setText(productCatalogData.getFormatedPrice());
        productHolder.mCTN.setText(productCatalogData.getCtnNumber());
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
                R.drawable.toothbrush, android.R.drawable
                        .ic_dialog_alert));
        productCartProductHolder.mProductImage.setImageUrl(imageURL, mImageLoader);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ProductCatalogViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView mProductImage;
        TextView mProductName;
        TextView mCTN;
        TextView mPrice;
        FontIconTextView mArrow;


        public ProductCatalogViewHolder(View itemView) {
            super(itemView);
            mProductImage = (NetworkImageView) itemView.findViewById(R.id.image);
            mProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            mCTN = (TextView) itemView.findViewById(R.id.tv_ctn);
            mPrice = (TextView) itemView.findViewById(R.id.tv_price);
            mArrow = (FontIconTextView) itemView.findViewById(R.id.arrow);
        }
    }
}
