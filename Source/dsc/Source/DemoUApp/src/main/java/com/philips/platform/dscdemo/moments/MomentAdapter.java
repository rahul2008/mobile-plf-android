/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.moments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.dscdemo.R;

import java.util.ArrayList;
import java.util.List;

class MomentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<? extends Moment> mMomentList;
    private Drawable mOptionsDrawable;
    private final MomentPresenter mTemperaturePresenter;
    private boolean mIsOptions;

    MomentAdapter(final Context context, final ArrayList<? extends Moment> data, MomentPresenter mTemperaturePresenter, boolean isOptions) {
        this.mTemperaturePresenter = mTemperaturePresenter;
        mMomentList = data;
        mContext = context;
        mIsOptions = isOptions;

        if (mIsOptions)
            initDrawables();
    }

    @Override
    public MomentViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.temperature_timeline, parent, false);
        return new MomentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MomentViewHolder) {
            MomentViewHolder mSyncViewHolder = (MomentViewHolder) holder;

            MomentHelper helper = new MomentHelper();
            Moment moment = mMomentList.get(position);

            if (moment.getSynchronisationData() != null)
                mSyncViewHolder.mMomentID.setText(moment.getSynchronisationData().getGuid());
            else
                mSyncViewHolder.mMomentID.setText(R.string.fetching_text);

            mSyncViewHolder.mPhase.setText(helper.getTime(moment));
            mSyncViewHolder.mTemperature.setText(String.valueOf(helper.getTemperature(moment)));
            mSyncViewHolder.mLocation.setText(helper.getNotes(moment));
            mSyncViewHolder.mExpirationDate.setText(helper.getExpirationDate(moment));

            if (mIsOptions) {
                mSyncViewHolder.mOptions.setImageDrawable(mOptionsDrawable);
                mSyncViewHolder.mDotsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        mTemperaturePresenter.bindDeleteOrUpdatePopUp(mMomentList, view, holder.getAdapterPosition());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mMomentList != null)
            return mMomentList.size();
        else
            return 0;
    }

    public void setData(final ArrayList<? extends Moment> data) {
        this.mMomentList = data;
    }

    private class MomentViewHolder extends RecyclerView.ViewHolder {
        TextView mMomentID;
        TextView mTemperature;
        TextView mPhase;
        TextView mLocation;
        TextView mExpirationDate;
        ImageView mOptions;
        FrameLayout mDotsLayout;
        TextView mIsSynced;

        MomentViewHolder(final View itemView) {
            super(itemView);
            mMomentID = (TextView) itemView.findViewById(R.id.moment_id);
            mTemperature = (TextView) itemView.findViewById(R.id.time_line_data);
            mExpirationDate = (TextView) itemView.findViewById(R.id.expiration_date_detail);
            mPhase = (TextView) itemView.findViewById(R.id.phasedata);
            mLocation = (TextView) itemView.findViewById(R.id.location_detail);
            if (mIsOptions) {
                mOptions = (ImageView) itemView.findViewById(R.id.dots);
                mDotsLayout = (FrameLayout) itemView.findViewById(R.id.frame);
            }
            mIsSynced = (TextView) itemView.findViewById(R.id.is_synced);
        }
    }

    private void initDrawables() {
        mOptionsDrawable = VectorDrawableCompat.create(mContext.getResources(), R.drawable.dots, mContext.getTheme());
    }
}
