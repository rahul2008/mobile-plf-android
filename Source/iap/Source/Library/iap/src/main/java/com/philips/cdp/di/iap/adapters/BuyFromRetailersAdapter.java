package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.response.retailers.StoreEntity;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.platform.appinfra.logging.LoggingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class BuyFromRetailersAdapter extends RecyclerView.Adapter<BuyFromRetailersAdapter.RetailerViewHolder> {
    private  Context mContext;
    private  FragmentManager mFragmentManager;
    private  ImageLoader mImageLoader;
    private ArrayList<StoreEntity> mStoreList;
    private BuyFromRetailersListener mBuyFromRetailersListener;
    private Bundle mBundle;

    public interface BuyFromRetailersListener {
        void onClickAtRetailer(String buyURL, StoreEntity storeEntity);
    }

    public BuyFromRetailersAdapter(Context context, FragmentManager fragmentManager,
                                   ArrayList<StoreEntity> storeList, BuyFromRetailersListener pBuyFromRetailersListener, Bundle bundle) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mStoreList = storeList;
        mBuyFromRetailersListener = pBuyFromRetailersListener;
        mBundle= bundle;
    }

    @Override
    public RetailerViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.iap_retailer_item, null);
        return new RetailerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RetailerViewHolder holder, final int position) {
        final StoreEntity storeEntity = mStoreList.get(position);
        String imageURL = storeEntity.getLogoURL();
        holder.mStoreName.setText(storeEntity.getName());
        holder.mLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.no_icon));
        final String productAvailability = storeEntity.getAvailability();


        if (productAvailability.equalsIgnoreCase("yes")) {
            holder.mProductAvailability.setText(mContext.getString(R.string.iap_in_stock));
            holder.mProductAvailability.setTextColor(ContextCompat.getColor(mContext, R.color.uid_signal_green_level_60));
        } else {
            holder.mProductAvailability.setText(mContext.getString(R.string.iap_out_of_stock));
            holder.mProductAvailability.setTextColor(ContextCompat.getColor(mContext, R.color.uid_signal_red_level_60));
        }

        // For arabic, Hebrew and Perssian the back arrow change from left to right
        if((Locale.getDefault().getLanguage().contentEquals("ar")) || (Locale.getDefault().getLanguage().contentEquals("fa")) || (Locale.getDefault().getLanguage().contentEquals("he"))) {
            holder.mArrow.setRotation(180);
        }

        final String buyURL = storeEntity.getBuyURL();
        holder.mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (!NetworkUtility.getInstance().isNetworkAvailable(connectivityManager)) {
                    NetworkUtility.getInstance().showErrorDialog(mContext, mFragmentManager,
                            mContext.getString(R.string.iap_ok), mContext.getString(R.string.iap_you_are_offline), mContext.getString(R.string.iap_no_internet));
                } else {


                    tagStockStatus(storeEntity.getName(),productAvailability);
                    mBuyFromRetailersListener.onClickAtRetailer(buyURL, storeEntity);
                }
            }
        });

        getNetworkImage(holder, imageURL);
    }



    private void getNetworkImage(final RetailerViewHolder retailerHolder, final String imageURL) {
        mImageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();
        mImageLoader.get(imageURL, ImageLoader.getImageListener(retailerHolder.mLogo,
                R.drawable.no_icon, android.R.drawable
                        .ic_dialog_alert));
        retailerHolder.mLogo.setImageUrl(imageURL, mImageLoader);
    }

    @Override
    public int getItemCount() {
        return mStoreList.size();
    }

    public class RetailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        NetworkImageView mLogo;
        TextView mStoreName;
        TextView mProductAvailability;
        TextView mArrow;

        public RetailerViewHolder(View itemView) {
            super(itemView);
            mLogo = itemView.findViewById(R.id.iap_retailer_image);
            mStoreName = itemView.findViewById(R.id.iap_retailerItem_onlineStoreName_lebel);
            mProductAvailability = itemView.findViewById(R.id.iap_retailerItem_onlineStoreAvailability_lebel);
            mArrow = itemView.findViewById(R.id.retailer_arrow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (!NetworkUtility.getInstance().isNetworkAvailable(connectivityManager)) {
                NetworkUtility.getInstance().showErrorDialog(mContext, mFragmentManager, mContext.getString(R.string.iap_ok), mContext.getString(R.string.iap_you_are_offline), mContext.getString(R.string.iap_no_internet));
            } else {
                final String buyURL = mStoreList.get(getAdapterPosition()).getBuyURL();
                tagStockStatus( mStoreList.get(getAdapterPosition()).getName(),mStoreList.get(getAdapterPosition()).getAvailability());
                boolean isSelected = this.itemView.isSelected();
                this.itemView.setSelected(!isSelected);
                this.itemView.setBackgroundColor(isSelected ? Color.TRANSPARENT : ContextCompat.getColor(this.itemView.getContext(), R.color.uid_list_item_background_selector));
                mBuyFromRetailersListener.onClickAtRetailer(buyURL, mStoreList.get(getAdapterPosition()));
            }
        }


    }
    private void tagStockStatus(String retailerName, String stockStatus){
        String stockStatusToTag = stockStatus.equalsIgnoreCase("YES")? "available" : "out of stock";
        final HashMap<String, String> map = new HashMap<>();
        map.put(IAPAnalyticsConstant.RETAILER_SELECTED, retailerName);
        map.put(IAPAnalyticsConstant.STOCK_STATUS, stockStatusToTag);
        String ProductInfo = IAPAnalytics.mCategory;
        ProductInfo+=";"+mBundle.getString("productCTN","");
        ProductInfo+=";"+mBundle.getInt("productStock",0);
        ProductInfo+=";"+mBundle.getString("productPrice","0.0");
        map.put(IAPAnalyticsConstant.PRODUCTS, ProductInfo);
        IAPAnalytics.trackMultipleActions(IAPAnalyticsConstant.SEND_DATA, map);
    }

}
