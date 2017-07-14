package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
import com.shamanland.fonticon.FontIconTextView;

import java.util.ArrayList;

public class BuyFromRetailersAdapter extends RecyclerView.Adapter<BuyFromRetailersAdapter.RetailerViewHolder> {
    private Context mContext;
    private FragmentManager mFragmentManager;
    //private int mThemeBaseColor;
    private final ImageLoader mImageLoader;
    private ArrayList<StoreEntity> mStoreList;
    private BuyFromRetailersListener mBuyFromRetailersListener;

    public interface BuyFromRetailersListener {
        void onClickAtRetailer(String buyURL, String name);
    }

    public BuyFromRetailersAdapter(Context context, FragmentManager fragmentManager,
                                   ArrayList<StoreEntity> storeList, BuyFromRetailersListener pBuyFromRetailersListener) {
        mContext = context;
        mFragmentManager = fragmentManager;
        // mThemeBaseColor = Utility.getThemeColor(context);
        mImageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();
        mStoreList = storeList;
        mBuyFromRetailersListener = pBuyFromRetailersListener;
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
        String productAvailability = storeEntity.getAvailability();

        if (productAvailability.equalsIgnoreCase("yes")) {
            holder.mProductAvailability.setText(mContext.getString(R.string.iap_in_stock));
            // holder.mProductAvailability.setTextColor(mThemeBaseColor);
            holder.mProductAvailability.setTextColor(ContextCompat.getColor(mContext, R.color.uid_signal_green_level_60));
        } else {
            holder.mProductAvailability.setText(mContext.getString(R.string.iap_out_of_stock));
            // holder.mProductAvailability.setTextColor(ContextCompat.getColor(mContext, R.color.uikit_enricher4));
            holder.mProductAvailability.setTextColor(ContextCompat.getColor(mContext, R.color.uid_signal_red_level_60));
        }

        final String buyURL = storeEntity.getBuyURL()+"source = mobile";
        holder.mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!NetworkUtility.getInstance().isNetworkAvailable(mContext)) {
                    NetworkUtility.getInstance().showErrorDialog(mContext, mFragmentManager,
                            mContext.getString(R.string.iap_ok), mContext.getString(R.string.iap_you_are_offline), mContext.getString(R.string.iap_no_internet));
                } else {
                    tagOnSelectRetailer(storeEntity);
                    mBuyFromRetailersListener.onClickAtRetailer(buyURL, storeEntity.getName());
                }
            }
        });

        getNetworkImage(holder, imageURL);
    }

    private void tagOnSelectRetailer(StoreEntity storeEntity) {
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.RETAILER_SELECTED,
                storeEntity.getName());
    }

    private void getNetworkImage(final RetailerViewHolder retailerHolder, final String imageURL) {
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
        FontIconTextView mArrow;

        public RetailerViewHolder(View itemView) {
            super(itemView);
            mLogo = (NetworkImageView) itemView.findViewById(R.id.iap_retailer_image);
            mStoreName = (TextView) itemView.findViewById(R.id.iap_retailerItem_onlineStoreName_lebel);
            mProductAvailability = (TextView) itemView.findViewById(R.id.iap_retailerItem_onlineStoreAvailability_lebel);
            mArrow = (FontIconTextView) itemView.findViewById(R.id.retailer_arrow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            if (!NetworkUtility.getInstance().isNetworkAvailable(mContext)) {
                NetworkUtility.getInstance().showErrorDialog(mContext, mFragmentManager, mContext.getString(R.string.iap_ok), mContext.getString(R.string.iap_you_are_offline), mContext.getString(R.string.iap_no_internet));
            } else {
                final String buyURL = mStoreList.get(getAdapterPosition()).getBuyURL();
                IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                        IAPAnalyticsConstant.RETAILER_SELECTED,
                        mStoreList.get(getAdapterPosition()).getName());
                boolean isSelected = this.itemView.isSelected();
                this.itemView.setSelected(!isSelected);
                this.itemView.setBackgroundColor(isSelected ? Color.TRANSPARENT : ContextCompat.getColor(this.itemView.getContext(), R.color.uid_recyclerview_background_selector));
                mBuyFromRetailersListener.onClickAtRetailer(buyURL, mStoreList.get(getAdapterPosition()).getName());
            }
        }
    }

}
