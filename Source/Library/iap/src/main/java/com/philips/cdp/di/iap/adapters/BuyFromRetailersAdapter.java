package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.Fragments.WebBuyFromRetailers;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.response.retailers.StoreEntity;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BuyFromRetailersAdapter extends RecyclerView.Adapter<BuyFromRetailersAdapter.RetailerViewHolder>{
    Context mContext;
    ArrayList<StoreEntity> mStoreEntities;
    private final ImageLoader mImageLoader;
    private FragmentManager mFragmentManager;
    private int mThemeBaseColor;

    public BuyFromRetailersAdapter(Context context, ArrayList<StoreEntity> storeEntities, FragmentManager fragmentManager){
        mContext = context;
        mStoreEntities = storeEntities;
        mFragmentManager = fragmentManager;
        mThemeBaseColor = Utility.getThemeColor(context);
        mImageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();
    }

    @Override
    public RetailerViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.iap_retailer_item, null);
        return new RetailerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RetailerViewHolder holder, final int position) {
        StoreEntity storeEntity = mStoreEntities.get(position);
        String imageURL = storeEntity.getLogoURL();
        holder.mStoreName.setText(storeEntity.getName());
        holder.mLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.toothbrush));
        String availability = storeEntity.getAvailability();
        if(availability.equalsIgnoreCase("yes")){
            holder.mAvaililibility.setText(mContext.getString(R.string.iap_in_stock));
            holder.mAvaililibility.setTextColor(mThemeBaseColor);
        }else{
            holder.mAvaililibility.setText(mContext.getString(R.string.iap_out_of_stock));
            holder.mAvaililibility.setTextColor(ContextCompat.getColor(mContext,R.color.uikit_enricher4));
        }

        final String buyURL = storeEntity.getBuyURL();
        holder.mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                addWebBuyFromRetailers(buyURL);
            }
        });

        getNetworkImage(holder, imageURL);
    }

    private void addWebBuyFromRetailers(String buyUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.IAP_BUY_URL,buyUrl);
        WebBuyFromRetailers webBuyFromRetailers = new WebBuyFromRetailers();
        webBuyFromRetailers.setArguments(bundle);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fl_mainFragmentContainer, webBuyFromRetailers,WebBuyFromRetailers.class.getName());
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void getNetworkImage(final RetailerViewHolder retailerHolder, final String imageURL) {
        mImageLoader.get(imageURL, ImageLoader.getImageListener(retailerHolder.mLogo,
                R.drawable.toothbrush, android.R.drawable
                        .ic_dialog_alert));
        retailerHolder.mLogo.setImageUrl(imageURL, mImageLoader);
    }


    @Override
    public int getItemCount() {
        int count = mStoreEntities.size();
        return count;
    }

    public class RetailerViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView mLogo;
        TextView mStoreName;
        TextView mAvaililibility;
        FontIconTextView mArrow;

        public RetailerViewHolder(View itemView) {
            super(itemView);
            mLogo = (NetworkImageView) itemView.findViewById(R.id.iap_retailer_image);
            mStoreName = (TextView) itemView.findViewById(R.id.iap_online_store_name);
            mAvaililibility = (TextView) itemView.findViewById(R.id.iap_online_store_availability);
            mArrow = (FontIconTextView) itemView.findViewById(R.id.retailer_arrow);
        }
    }
}
