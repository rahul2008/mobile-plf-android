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
import com.philips.cdp.di.iap.Fragments.BaseAnimationSupportFragment;
import com.philips.cdp.di.iap.Fragments.WebBuyFromRetailers;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.response.retailers.StoreEntity;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;

public class BuyFromRetailersAdapter extends RecyclerView.Adapter<BuyFromRetailersAdapter.RetailerViewHolder> {
    private Context mContext;
    private ArrayList<StoreEntity> mStoreEntities;
    private FragmentManager mFragmentManager;
    private int mThemeBaseColor;
    private int mContainerID;
    private final ImageLoader mImageLoader;

    public BuyFromRetailersAdapter(Context context, ArrayList<StoreEntity> storeEntities, FragmentManager fragmentManager, int id) {
        mContext = context;
        mStoreEntities = storeEntities;
        mFragmentManager = fragmentManager;
        mThemeBaseColor = Utility.getThemeColor(context);
        mImageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();
        mContainerID = id;
    }


    @Override
    public RetailerViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.iap_retailer_item, null);
        return new RetailerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RetailerViewHolder holder, final int position) {
        final StoreEntity storeEntity = mStoreEntities.get(position);
        String imageURL = storeEntity.getLogoURL();
        holder.mStoreName.setText(storeEntity.getName());
        holder.mLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.no_icon));
        String availability = storeEntity.getAvailability();
        if (availability.equalsIgnoreCase("yes")) {
            holder.mAvailibility.setText(mContext.getString(R.string.iap_in_stock));
            holder.mAvailibility.setTextColor(mThemeBaseColor);
        } else {
            holder.mAvailibility.setText(mContext.getString(R.string.iap_out_of_stock));
            holder.mAvailibility.setTextColor(ContextCompat.getColor(mContext, R.color.uikit_enricher4));
        }

        final String buyURL = storeEntity.getBuyURL();
        holder.mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!NetworkUtility.getInstance().isNetworkAvailable(mContext)) {
                    NetworkUtility.getInstance().showErrorDialog(mContext, mFragmentManager, mContext.getString(R.string.iap_ok), mContext.getString(R.string.iap_you_are_offline), mContext.getString(R.string.iap_no_internet));
                    return;
                } else {
                    tagOnSelectRetailer(storeEntity);
                    addWebBuyFromRetailers(buyURL, storeEntity.getName());
                }
            }
        });

        getNetworkImage(holder, imageURL);
    }

    private void tagOnSelectRetailer(StoreEntity storeEntity) {
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.RETAILER_SELECTED,
                storeEntity.getName());
    }

    private void addWebBuyFromRetailers(String buyUrl, String storeName) {
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.IAP_BUY_URL, buyUrl);
        bundle.putString(IAPConstant.IAP_STORE_NAME, storeName);
        WebBuyFromRetailers webBuyFromRetailers = new WebBuyFromRetailers();
        webBuyFromRetailers.setArguments(bundle);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(mContainerID, webBuyFromRetailers, WebBuyFromRetailers.class.getName());
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void getNetworkImage(final RetailerViewHolder retailerHolder, final String imageURL) {
        mImageLoader.get(imageURL, ImageLoader.getImageListener(retailerHolder.mLogo,
                R.drawable.no_icon, android.R.drawable
                        .ic_dialog_alert));
        retailerHolder.mLogo.setImageUrl(imageURL, mImageLoader);
    }

    @Override
    public int getItemCount() {
        return mStoreEntities.size();
    }

    public class RetailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        NetworkImageView mLogo;
        TextView mStoreName;
        TextView mAvailibility;
        FontIconTextView mArrow;

        public RetailerViewHolder(View itemView) {
            super(itemView);
            mLogo = (NetworkImageView) itemView.findViewById(R.id.iap_retailer_image);
            mStoreName = (TextView) itemView.findViewById(R.id.iap_online_store_name);
            mAvailibility = (TextView) itemView.findViewById(R.id.iap_online_store_availability);
            mArrow = (FontIconTextView) itemView.findViewById(R.id.retailer_arrow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            if (!NetworkUtility.getInstance().isNetworkAvailable(mContext)) {
                NetworkUtility.getInstance().showErrorDialog(mContext, mFragmentManager, mContext.getString(R.string.iap_ok), mContext.getString(R.string.iap_you_are_offline), mContext.getString(R.string.iap_no_internet));
                return;
            } else {
                final String buyURL = mStoreEntities.get(getAdapterPosition()).getBuyURL();
                IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                        IAPAnalyticsConstant.RETAILER_SELECTED,
                        mStoreEntities.get(getAdapterPosition()).getName());
                addWebBuyFromRetailers(buyURL, mStoreEntities.get(getAdapterPosition()).getName());
            }
        }
    }
}
