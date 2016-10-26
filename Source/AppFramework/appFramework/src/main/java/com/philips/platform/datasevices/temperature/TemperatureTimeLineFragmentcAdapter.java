package com.philips.platform.datasevices.temperature;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.datasevices.database.table.OrmMoment;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.trackers.Tracker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class TemperatureTimeLineFragmentcAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<? extends Moment> mData;
    Context mContext;
    private UIKitListPopupWindow mPopupWindow;
    private Drawable mOptionsDrawable;
    private Resources mResources;
    final TemperaturePresenter mTemperaturePresenter;

     DataServicesManager mDataServices;


    public
    TemperatureTimeLineFragmentcAdapter(final Context context, final ArrayList<? extends Moment> data, TemperaturePresenter mTemperaturePresenter) {

        this.mTemperaturePresenter = mTemperaturePresenter;
        mDataServices = DataServicesManager.getInstance();
        mData = data;
        mContext = context;
        mResources = context.getResources();
        initDrawables();
    }

    @Override
    public DataSyncViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.temperature_timeline, parent, false);
        return new DataSyncViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof DataSyncViewHolder) {
            DataSyncViewHolder mSyncViewHolder = (DataSyncViewHolder) holder;
            mSyncViewHolder.mOptions.setImageDrawable(mOptionsDrawable);
            TemperatureMomentHelper helper = new TemperatureMomentHelper();
            Moment moment = (OrmMoment) mData.get(position);
            mSyncViewHolder.mPhase.setText(helper.getTime(moment));
            mSyncViewHolder.mTemperature.setText(String.valueOf(helper.getTemperature(moment)));
            mSyncViewHolder.mLocation.setText(helper.getNotes(moment));
            mSyncViewHolder.mDotsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    mTemperaturePresenter.bindDeleteOrUpdatePopUP(TemperatureTimeLineFragmentcAdapter.this,mData,view, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(final ArrayList<? extends Moment> data) {
        this.mData = data;
    }

    public class DataSyncViewHolder extends RecyclerView.ViewHolder {
        public TextView mTemperature;
        public  TextView mPhase;
        public  TextView mLocation;
        public ImageView mOptions;
        public FrameLayout mDotsLayout;
        public TextView mIsSynced;

        public DataSyncViewHolder(final View itemView) {
            super(itemView);
            mTemperature = (TextView) itemView.findViewById(R.id.time_line_data);
            mPhase = (TextView) itemView.findViewById(R.id.phasedata);
            mLocation = (TextView) itemView.findViewById(R.id.location_detail);
            mOptions = (ImageView)itemView.findViewById(R.id.dots);
            mDotsLayout = (FrameLayout) itemView.findViewById(R.id.frame);
            mIsSynced = (TextView) itemView.findViewById(R.id.is_synced);
        }
    }




    private void initDrawables() {
        mOptionsDrawable = VectorDrawable.create(mContext, R.drawable.dots);
    }



}
