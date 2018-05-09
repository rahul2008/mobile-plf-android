/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.practice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.americanwell.sdk.entity.practice.Practice;
import com.philips.platform.ths.R;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_FETCH_PRACTICE_IMAGE;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SERVER_ERROR;


public class THSPracticeRecyclerViewAdapter extends RecyclerView.Adapter<THSPracticeRecyclerViewAdapter.CustomViewHolder> {

    private THSPracticeList mTHSPractice;
    private Context mContext;
    private OnPracticeItemClickListener mOnPracticeItemClickListener;


    //TODO: Review Comment - Spoorti - rename it to getOnPracticeItemClickListener
    public OnPracticeItemClickListener getOnPracticeItemClickListener() {
        return mOnPracticeItemClickListener;
    }

    //TODO: Review Comment - Spoorti - rename it to setOnPracticeItemClickListener
    public void setOnPracticeItemClickListener(OnPracticeItemClickListener mOnPracticeItemClickListener) {
        this.mOnPracticeItemClickListener = mOnPracticeItemClickListener;
    }

    public THSPracticeRecyclerViewAdapter(Context context, THSPracticeList THSPractice) {
        this.mTHSPractice = THSPractice;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ths_practice_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Practice practice = mTHSPractice.getPractices().get(i);


        customViewHolder.label.setText(practice.getName());

        try {
            final Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.child_icon);
            THSManager.getInstance().getAwsdk(customViewHolder.logo.getContext()).getPracticeProvidersManager()
                    .newImageLoader(practice, customViewHolder.logo, false)
                    .placeholder(drawable)
                    .build().load();
        } catch (Exception e) {
           //
            final String errorTag = THSTagUtils.createErrorTag(ANALYTIC_FETCH_PRACTICE_IMAGE, e.getMessage());
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SERVER_ERROR, errorTag);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPracticeItemClickListener.onItemClick(practice);
            }
        };
        customViewHolder.relativeLayout.setOnClickListener(listener);


    }

    @Override
    public int getItemCount() {
        return (null != mTHSPractice && mTHSPractice.getPractices() != null ? mTHSPractice.getPractices().size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView logo;
        protected TextView label;
        protected RelativeLayout relativeLayout;

        public CustomViewHolder(View view) {
            super(view);
            this.logo = (ImageView) view.findViewById(R.id.pth_practice_logo);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.pth_practice_row_layout);
            this.label = (TextView) view.findViewById(R.id.pth_practice_name);

        }
    }


}
