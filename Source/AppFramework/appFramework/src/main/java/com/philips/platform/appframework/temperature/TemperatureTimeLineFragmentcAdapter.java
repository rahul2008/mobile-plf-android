package com.philips.platform.appframework.temperature;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.RowItem;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.trackers.Tracker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.database.table.OrmMoment;

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
    private static final int DELETE = 0;
    private static final int UPDATE = 1;
    @Inject
    Tracker tracker;


    public
    TemperatureTimeLineFragmentcAdapter(final Context context, final ArrayList<? extends Moment> data) {
        ((AppFrameworkApplication) context.getApplicationContext()).getAppComponent().injectTemperatureAdapter(this);
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
            OrmMoment moment = (OrmMoment) mData.get(position);
            mSyncViewHolder.mPhase.setText(helper.getTime(moment));
            mSyncViewHolder.mTemperature.setText(String.valueOf(helper.getTemperature(moment)));
            mSyncViewHolder.mLocation.setText(helper.getNotes(moment));

            if(!moment.isSynced()){
             mSyncViewHolder.mIsSynced.setVisibility(View.VISIBLE);
            }else {
                mSyncViewHolder.mIsSynced.setVisibility(View.GONE);
            }

            mSyncViewHolder.mDotsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    bindDeleteOrUpdatePopUP(view, position);
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

    private void removeMoment(int adapterPosition) {
        try {
            tracker.deleteMoment(mData.get(adapterPosition));
            /*mData.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            notifyDataSetChanged();*/
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void updateMoment(final int position) {

        final Moment moment = mData.get(position);
        TemperatureMomentHelper helper = new TemperatureMomentHelper();

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.af_datasync_create_moment_pop_up);
        dialog.setTitle("Create A Moment");

        final EditText temperature = (EditText) dialog.findViewById(R.id.temperature_detail);
        final EditText location = (EditText) dialog.findViewById(R.id.location_detail);
        final EditText phase = (EditText) dialog.findViewById(R.id.phase_detail);
        final Button dialogButton = (Button) dialog.findViewById(R.id.save);
        dialogButton.setEnabled(false);

        temperature.setText(String.valueOf(helper.getTemperature(moment)));
        location.setText(helper.getNotes(moment));
        phase.setText(helper.getTime(moment));

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
              //  final String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                updateAndSaveMoment(phase.getText().toString(), temperature.getText().toString(),
                        location.getText().toString(), position);
                dialog.dismiss();
            }
        });

        phase.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if(phase.getText().toString()!=null && !phase.getText().toString().isEmpty() && temperature.getText().toString()!=null && !temperature.getText().toString().isEmpty() && location.getText().toString()!=null && !location.getText().toString().isEmpty()){
                    dialogButton.setEnabled(true);
                }else{
                    dialogButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        temperature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if(phase.getText().toString()!=null && !phase.getText().toString().isEmpty() && temperature.getText().toString()!=null && !temperature.getText().toString().isEmpty() && location.getText().toString()!=null && !location.getText().toString().isEmpty()){
                    dialogButton.setEnabled(true);
                }else{
                    dialogButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if(phase.getText().toString()!=null && !phase.getText().toString().isEmpty() && temperature.getText().toString()!=null && !temperature.getText().toString().isEmpty() && location.getText().toString()!=null && !location.getText().toString().isEmpty()){
                    dialogButton.setEnabled(true);
                }else{
                    dialogButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        dialog.show();
    }

    private void updateAndSaveMoment(final String phaseInput, final String temperatureInput, final String locationInput, int position) {

        try {
            final Moment moment;
            TemperatureMomentHelper helper = new TemperatureMomentHelper();
            moment = helper.updateMoment(mData.get(position), phaseInput, temperatureInput, locationInput);
            tracker.update(moment);
        } catch (Exception ArrayIndexOutOfBoundsException) {

        }
    }

    private void initDrawables() {
        mOptionsDrawable = VectorDrawable.create(mContext, R.drawable.dots);
    }

    private void bindDeleteOrUpdatePopUP(final View view, final int selectedItem) {
        List<RowItem> rowItems = new ArrayList<>();

        final String delete = mResources.getString(R.string.delete);
        String update = mResources.getString(R.string.update);
        final String[] descriptions = new String[]{delete, update};

        rowItems.add(new RowItem(descriptions[0]));
        rowItems.add(new RowItem(descriptions[1]));
        mPopupWindow = new UIKitListPopupWindow(mContext, view, UIKitListPopupWindow.UIKIT_Type.UIKIT_BOTTOMLEFT, rowItems);

        mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                switch (position) {
                    case DELETE:
                        removeMoment(selectedItem);
                        mPopupWindow.dismiss();
                        break;
                    case UPDATE:
                        updateMoment(selectedItem);
                        mPopupWindow.dismiss();
                        break;
                    default:
                }
            }
        });
        mPopupWindow.show();
    }

}
