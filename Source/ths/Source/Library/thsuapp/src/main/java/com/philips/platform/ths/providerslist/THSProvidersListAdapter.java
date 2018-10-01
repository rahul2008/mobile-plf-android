/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerslist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProvider;
import com.philips.platform.ths.appointment.THSAvailableProviderListBasedOnDateFragment;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.NotificationBadge;
import com.philips.platform.uid.view.widget.RatingBar;

import java.util.List;


public class THSProvidersListAdapter extends RecyclerView.Adapter<THSProvidersListAdapter.MyViewHolder> {

    private static String TAG = THSProvidersListAdapter.class.getSimpleName();
    private List<? extends THSProviderEntity> thsProviderInfos;
    private Practice mPractice;
    private OnProviderListItemClickListener onProviderItemClickListener;


    public void setOnProviderItemClickListener(OnProviderListItemClickListener onProviderItemClickListener) {
        this.onProviderItemClickListener = onProviderItemClickListener;
    }

    public THSProvidersListAdapter(List<? extends THSProviderEntity> thsProviderInfos, Practice mPractice) {
        this.thsProviderInfos = thsProviderInfos;
        this.mPractice = mPractice;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout doctorAvailableActionBar;
        public TextView name, practice, isAvailble;
        public Button selectDoctorButton, doctorSelectActionBar, doctorScheduleActionBar, scheduleDoctorButton;
        public RatingBar providerRating;
        public CircularImageView providerImage;
        public RelativeLayout relativeLayout;
        public NotificationBadge notificationBadge;

        public MyViewHolder(View view) {
            super(view);
            practice = (TextView) view.findViewById(R.id.practiceNameLabel);
            name = (TextView) view.findViewById(R.id.providerNameLabel);
            isAvailble = (TextView) view.findViewById(R.id.isAvailableLabel);
            providerRating = (RatingBar) view.findViewById(R.id.providerRating);
            providerImage = (CircularImageView) view.findViewById(R.id.providerImage);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.providerListItemLayout);
            notificationBadge = (NotificationBadge) view.findViewById(R.id.notification_badge);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (isProviderListABFlow1() || (onProviderItemClickListener instanceof THSAvailableProviderListBasedOnDateFragment)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ths_provider_list, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ths_firbase_flow2_provider_list, parent, false);
        }

        MyViewHolder viewHolder = new MyViewHolder(itemView);
        if (isDefaultFlow()) {
            viewHolder.doctorSelectActionBar = (Button) itemView.findViewById(R.id.doctor_select_action_bar);
            viewHolder.doctorScheduleActionBar = (Button) itemView.findViewById(R.id.doctor_schedule_action_bar);
            viewHolder.doctorAvailableActionBar = (LinearLayout) itemView.findViewById(R.id.doctor_available_action_bar);
            viewHolder.scheduleDoctorButton = (Button) itemView.findViewById(R.id.provider_schedule);
            viewHolder.selectDoctorButton = (Button) itemView.findViewById(R.id.provider_select);
        }

        return viewHolder;
    }

    private boolean isDefaultFlow() {
        return !isProviderListABFlow1() && !(onProviderItemClickListener instanceof THSAvailableProviderListBasedOnDateFragment);
    }


    private boolean isProviderListABFlow1() {
        return THSManager.getInstance().getProviderListABFlow().equalsIgnoreCase(THSConstants.THS_PROVIDERLIST_ABFLOW1);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final THSProviderInfo thsProviderInfo;
        String providerAvailabilityString = null;
        String providerVisibility;
        Context context;

        //handleActionBarEvents(holder);

        if ((getThsProviderEntity(holder.getAdapterPosition()) instanceof THSProviderInfo)) {
            thsProviderInfo = getThsProviderInfo(holder.getAdapterPosition());
            providerVisibility = thsProviderInfo.getVisibility().toString();
            context = holder.isAvailble.getContext();
            if (providerVisibility.equals(THSConstants.WEB_AVAILABLE)) {
                providerAvailabilityString = context.getResources().getString(R.string.ths_provider_available);
                holder.isAvailble.setTextColor(ContextCompat.getColor(context, com.philips.platform.uid.R.color.uid_signal_green_level_45));
            } else if (providerVisibility.equals(THSConstants.PROVIDER_OFFLINE)) {
                providerAvailabilityString = context.getResources().getString(R.string.ths_provider_available_for_appointment);
                holder.isAvailble.setTextColor(ContextCompat.getColor(context, com.philips.platform.uid.R.color.uid_signal_blue_level_45));
            } else if (providerVisibility.equals(THSConstants.PROVIDER_WEB_BUSY)) {
                providerAvailabilityString = context.getResources().getString(R.string.ths_provider_occupied);
                holder.isAvailble.setTextColor(ContextCompat.getColor(context, com.philips.platform.uid.R.color.uid_signal_orange_level_45));
            }
            initDoctorActionBar(thsProviderInfo, holder);
            holder.isAvailble.setText(providerAvailabilityString);

        } else {
            THSAvailableProvider thsAvailableProvider = (THSAvailableProvider) thsProviderInfos.get(position);
            thsProviderInfo = getThsProviderInfo(thsAvailableProvider);
            holder.isAvailble.setText(holder.isAvailble.getContext().getResources().getString(R.string.ths_provider_list_timeslot));
            holder.notificationBadge.setVisibility(View.VISIBLE);
            holder.notificationBadge.setText("" + thsAvailableProvider.getAvailableAppointmentTimeSlots().size());

            try {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.isAvailble.getLayoutParams();
                layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.notification_badge);
            } catch (Exception e) {
                AmwellLog.i(THSProvidersListAdapter.TAG, e.getMessage());
            }
        }


        try {
            holder.providerRating.setRating(thsProviderInfo.getRating());
            holder.name.setText(thsProviderInfo.getProviderInfo().getFullName());
            holder.practice.setText(getName(thsProviderInfo));


            if (thsProviderInfo.hasImage()) {
                try {
                    THSManager.getInstance().getAwsdk(holder.providerImage.getContext()).
                            getPracticeProvidersManager().
                            newImageLoader(thsProviderInfo.getProviderInfo(),
                                    holder.providerImage, ProviderImageSize.LARGE).placeholder
                            (holder.providerImage.getResources().getDrawable(R.drawable.doctor_placeholder, holder.providerImage.getContext().getTheme())).
                            build().load();
                } catch (AWSDKInstantiationException e) {

                }
            }
        } catch (Exception e) {
            AmwellLog.e(THSProvidersListAdapter.TAG, e.toString());
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onProviderItemClickListener.onItemClick(thsProviderInfos.get(position), v.getId());
            }
        };
        holder.relativeLayout.setOnClickListener(listener);

        if (isDefaultFlow()) {
            holder.doctorSelectActionBar.setOnClickListener(listener);
            holder.doctorScheduleActionBar.setOnClickListener(listener);
            holder.scheduleDoctorButton.setOnClickListener(listener);
            holder.selectDoctorButton.setOnClickListener(listener);
        }

    }


    private void initDoctorActionBar(THSProviderInfo thsProviderInfo, MyViewHolder holder) {
        if (isDefaultFlow()) {
            if (isProviderSelectable(thsProviderInfo.getVisibility()) && mPractice.isShowScheduling()) {
                holder.doctorScheduleActionBar.setVisibility(View.GONE);
                holder.doctorSelectActionBar.setVisibility(View.GONE);
                holder.selectDoctorButton.setText(holder.isAvailble.getContext().getResources().getString(R.string.ths_select_provider));
                holder.doctorAvailableActionBar.setVisibility(View.VISIBLE);
            } else if (isProviderSelectable(thsProviderInfo.getVisibility()) && !mPractice.isShowScheduling()) {
                holder.doctorScheduleActionBar.setVisibility(View.GONE);
                holder.doctorAvailableActionBar.setVisibility(View.GONE);
                holder.doctorSelectActionBar.setVisibility(View.VISIBLE);
                holder.doctorSelectActionBar.setText(holder.isAvailble.getContext().getResources().getString(R.string.ths_insurancedetail_selectprovider));
            } else if (!isProviderSelectable(thsProviderInfo.getVisibility()) && mPractice.isShowScheduling()) {
                holder.doctorSelectActionBar.setVisibility(View.GONE);
                holder.doctorAvailableActionBar.setVisibility(View.GONE);
                holder.doctorScheduleActionBar.setVisibility(View.VISIBLE);
                holder.doctorScheduleActionBar.setText(holder.isAvailble.getContext().getResources().getString(R.string.ths_provider_available_for_appointment));
            } else {
                holder.doctorScheduleActionBar.setVisibility(View.GONE);
                holder.doctorSelectActionBar.setVisibility(View.GONE);
                holder.doctorAvailableActionBar.setVisibility(View.GONE);
            }
        }
    }

    private boolean isProviderSelectable(String thsProviderInfoVisibility) {
        return thsProviderInfoVisibility.equals(THSConstants.WEB_AVAILABLE) || thsProviderInfoVisibility.equals(THSConstants.PROVIDER_WEB_BUSY) ||
                thsProviderInfoVisibility.equals(THSConstants.PROVIDER_ON_CALL);
    }

    @NonNull
    public THSProviderInfo getThsProviderInfo(THSAvailableProvider thsAvailableProvider) {
        THSProviderInfo thsProviderInfo;
        thsProviderInfo = new THSProviderInfo();
        thsProviderInfo.setTHSProviderInfo(thsAvailableProvider.getProviderInfo());
        return thsProviderInfo;
    }


    public String getName(THSProviderInfo thsProviderInfo) {
        return thsProviderInfo.getSpecialty().getName();
    }

    public THSProviderEntity getThsProviderEntity(int position) {
        return thsProviderInfos.get(position);
    }

    public THSProviderInfo getThsProviderInfo(int position) {
        return (THSProviderInfo) thsProviderInfos.get(position);
    }

    @Override
    public int getItemCount() {
        return thsProviderInfos.size();
    }
}
