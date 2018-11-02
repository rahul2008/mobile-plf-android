/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.cart.ShoppingCartData;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.response.voucher.GetAppliedValue;
import com.philips.cdp.di.iap.response.voucher.Vouchers;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.stock.IAPStockAvailabilityHelper;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.CountDropDown;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.UIPicker;

import java.util.ArrayList;
import java.util.List;

import static com.philips.cdp.di.iap.utils.IAPConstant.IAP_APPLY_VOUCHER;

public class AppliedVoucherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private Context mContext;
    private Resources mResources;
    private List<Vouchers> mData = new ArrayList<>();
    private OutOfStockListener mOutOfStock;
    private UIPicker mPopupWindow;
    private GetAppliedValue shoppingCartDataForProductDetailPage;

    private Drawable countArrow;
    private boolean mIsFreeDelivery;
    private int mSelectedItemPosition = -1;
    private int mQuantityStatus;
    private int mNewCount;

    public interface OutOfStockListener {
        void onOutOfStock(boolean isOutOfStock);
    }

    public AppliedVoucherAdapter(Context context, List<Vouchers> vouchers) {
        mContext = context;
        mResources = context.getResources();
        mData = vouchers;
    }


    @Override
    public int getItemViewType(final int position) {
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(final int position) {
        return position == mData.size();
    }
    public int getSelectedItemPosition() {
        return mSelectedItemPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_applied_voucher_item, parent, false);
            return new AppliedVoucherViewHolder(view);
    }




    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (mData.size() == 0) {
            return;
        }

            final Vouchers vouchers = mData.get(holder.getAdapterPosition());

            AppliedVoucherViewHolder appliedVoucherViewHolder = (AppliedVoucherViewHolder) holder;
            appliedVoucherViewHolder.mIapVoucherItemLabel.setText(R.string.iap_voucher_code);
            appliedVoucherViewHolder.mIapDiscountedPrecentageLabel.setText(vouchers.getVoucherCode());
            appliedVoucherViewHolder.mIapDiscountedPriceLabel.setText(vouchers.getAppliedValue().getValue());



        appliedVoucherViewHolder.mIapCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    mSelectedItemPosition = holder.getAdapterPosition();
                    if(mSelectedItemPosition!=-1){
                        EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_DELETE_VOUCHER);
                    }
                }
            });


    }


    public void onStop() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }



     class AppliedVoucherViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mIappVoucherItemLayout;
        RelativeLayout mIapAppliedVocuherLayout;
        Label mIapVoucherItemLabel;
        Label mIapDiscountedPrecentageLabel;
        Label mIapDiscountedPriceLabel;
        Label mIapCross;


         AppliedVoucherViewHolder(View itemView) {
            super(itemView);
            mIapVoucherItemLabel = itemView.findViewById(R.id.iap_voucherItem_label);
            mIapDiscountedPrecentageLabel = itemView.findViewById(R.id.iap_discountedPrecentage_label);
            mIapDiscountedPriceLabel = itemView.findViewById(R.id.iap_discountedPrice_label);
            mIapCross = itemView.findViewById(R.id.iap_cross);
            mIapAppliedVocuherLayout = itemView.findViewById(R.id.iap_applied_vocuher_layout);
            mIappVoucherItemLayout = itemView.findViewById(R.id.iap_voucherItem_layout);
        }
    }

}
