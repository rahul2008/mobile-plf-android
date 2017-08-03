/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.products.ProductCatalogData;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.platform.uid.text.utils.UIDStringUtils;
import com.philips.platform.uid.view.widget.SearchBox;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductCatalogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EMPTY_VIEW = 10;
    private final ImageLoader mImageLoader;
    private Context mContext;

    private ArrayList<ProductCatalogData> mProductCatalogList ;
    private ProductCatalogData mSelectedProduct;

    private Filter productFilter = new ProductListFilter();
    private CharSequence query;

    private ArrayList<ProductCatalogData> mProductCatalogListToFilter ;
    private String mCharacterText="";

    public boolean isSearchFocused() {
        return isSearchFocused;
    }

    public void setSearchFocused(boolean searchFocused) {
        isSearchFocused = searchFocused;
    }

    private boolean isSearchFocused = false;

    public ProductCatalogAdapter(Context pContext, ArrayList<ProductCatalogData> mProductCatalogList) {
        mContext = pContext;
        this.mProductCatalogList = mProductCatalogList;
        mProductCatalogListToFilter= mProductCatalogList;
        mImageLoader = NetworkImageLoader.getInstance(mContext).getImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        if (viewType == EMPTY_VIEW && isSearchFocused()) {
           View  v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
            EmptyViewHolder evh = new EmptyViewHolder(v);
            return evh;
        }

        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.iap_product_catalog_item, parent, false);
        return new ProductCatalogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof EmptyViewHolder && isSearchFocused()){
            CharSequence text = String.format((mContext.getResources().getText(R.string.iap_zero_results)).toString(), mCharacterText);
            ((EmptyViewHolder) holder).tvEmptyMsg.setText(text);
            mCharacterText="";
            return;
        }

        ProductCatalogData productCatalogData = mProductCatalogList.get(holder.getAdapterPosition());
        ProductCatalogViewHolder productHolder = (ProductCatalogViewHolder) holder;

        String imageURL = productCatalogData.getImageURL();
        if (imageURL == null) {
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                    IAPAnalyticsConstant.ERROR, IAPAnalyticsConstant.PRX + productCatalogData.getCtnNumber() + "_" + IAPAnalyticsConstant.NO_THUMB_NAIL_IMAGE);
        }
        String discountedPrice = productCatalogData.getDiscountedPrice();
        String formattedPrice = productCatalogData.getFormattedPrice();

        productHolder.mProductName.setText(productCatalogData.getProductTitle());
        if (imageURL == null) {
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                    IAPAnalyticsConstant.ERROR, IAPAnalyticsConstant.PRX + productCatalogData.getCtnNumber() + "_" + IAPAnalyticsConstant.PRODUCT_TITLE_MISSING);
        }
        productHolder.mProductImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.no_icon));
        productHolder.mPrice.setText(formattedPrice);

        String stockLevel = productCatalogData.getStockLevel();
        if (stockLevel != null && stockLevel.equalsIgnoreCase("outOfStock")) {
            productHolder.mProductOutOfStock.setText(mContext.getString(R.string.iap_out_of_stock));
            productHolder.mProductOutOfStock.setTextColor(ContextCompat.getColor(mContext, R.color.uid_signal_red_level_60));
        } else {
            productHolder.mProductOutOfStock.setVisibility(View.GONE);
        }

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
                setTheProductDataForDisplayingInProductDetailPage(holder.getAdapterPosition());
            }
        });
    }

    private void setTheProductDataForDisplayingInProductDetailPage(int position) {
        mSelectedProduct = mProductCatalogList.get(position);
        EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_LAUNCH_PRODUCT_DETAIL);
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
        //return mProductCatalogList.size();
        return mProductCatalogList.size() > 0 ? mProductCatalogList.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mProductCatalogList.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    public void tagProducts() {
        String productPrice = "";
        if (mProductCatalogList.size() != 0) {
            StringBuilder products = new StringBuilder();
            for (int i = 0; i < mProductCatalogList.size(); i++) {
                ProductCatalogData catalogData = mProductCatalogList.get(i);
                if (i > 0) {
                    products = products.append(",");
                }

                if (catalogData.getPriceValue() != null)
                    productPrice = catalogData.getPriceValue();

                products = products.append("Tuscany_Campaign").append(";")
                        .append(catalogData.getProductTitle()).append(";").append(";")
                        .append(productPrice);
            }
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                    IAPAnalyticsConstant.PRODUCTS, products.toString());
        }
    }

    public void setData(ArrayList<ProductCatalogData> mProductCatalogList){
     this.mProductCatalogList=mProductCatalogList;
    }

    // Filter Class
    public void filter(String charText) {
        setSearchFocused(true);
        mCharacterText=charText;
        charText = charText.toLowerCase(Locale.getDefault());
        ArrayList<ProductCatalogData> lFilteredProductList=new ArrayList<>();
        if (charText.length() == 0) {
            // Show no product found view
        } else {
            for (ProductCatalogData wp : mProductCatalogListToFilter) {
                if (wp.getProductTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    lFilteredProductList.add(wp);
                }
            }
        }

        setData(lFilteredProductList);
        notifyDataSetChanged();
    }

    private class ProductCatalogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        NetworkImageView mProductImage;
        TextView mProductName;
        TextView mProductOutOfStock;
        TextView mPrice;
        FontIconTextView mArrow;
        TextView mDiscountedPrice;

        ProductCatalogViewHolder(View itemView) {
            super(itemView);
            mProductImage = (NetworkImageView) itemView.findViewById(R.id.image);
            mProductName = (TextView) itemView.findViewById(R.id.iap_retailerItem_productName_lebel);
            mProductOutOfStock = (TextView) itemView.findViewById(R.id.iap_retaileritem_product_outOfStock_label);
            mPrice = (TextView) itemView.findViewById(R.id.iap_retailerItem_price_lebel);
            mArrow = (FontIconTextView) itemView.findViewById(R.id.arrow);
            mDiscountedPrice = (TextView) itemView.findViewById(R.id.iap_productCatalogItem_discountedPrice_lebel);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            boolean isSelected = this.itemView.isSelected();
            this.itemView.setSelected(!isSelected);
            this.itemView.setBackgroundColor(isSelected ? Color.TRANSPARENT : ContextCompat.getColor(this.itemView.getContext(), R.color.uid_recyclerview_background_selector));
            setTheProductDataForDisplayingInProductDetailPage(getAdapterPosition());
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmptyMsg;
        public EmptyViewHolder(View itemView) {
            super(itemView);
            tvEmptyMsg= (TextView) itemView.findViewById(R.id.tv_empty_list_msg);
        }
    }


    private class ProductListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<ProductCatalogData> resultList = new ArrayList<>();
            if (!TextUtils.isEmpty(constraint)) {
                for (ProductCatalogData product : mProductCatalogList) {
                    if (UIDStringUtils.indexOfSubString(true, product.getProductTitle(), constraint) >= 0) {
                        resultList.add(product);
                    }
                }
            }

            results.values = resultList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<ProductCatalogData> filteredList = (List<ProductCatalogData>) results.values;
            if ((query.length() > 0) && filteredList.size() <= 0) {
                // filteredList.add();
                //show default empty text
            }
            ProductCatalogAdapter.this.notifyDataSetChanged();
        }
    }

}


