/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.visit.Appointment;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProvider;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class THSScheduledVisitsAdapter extends RecyclerView.Adapter<THSScheduledVisitsAdapter.CustomViewHolder> {
    List<Appointment> mAppointmentList;

    public THSScheduledVisitsAdapter(List<Appointment> appointments) {
        mAppointmentList = appointments;
    }

    @Override
    public THSScheduledVisitsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ths_scheduled_visits_list_item, parent, false);

        return new THSScheduledVisitsAdapter.CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(THSScheduledVisitsAdapter.CustomViewHolder holder, int position) {

        final Appointment appointment = mAppointmentList.get(position);
        final Provider assignedProvider = appointment.getAssignedProvider();
        final String date = new SimpleDateFormat(THSConstants.DATE_TIME_FORMATTER, Locale.getDefault()).format(appointment.getSchedule().getScheduledStartTime()).toString();
        holder.mLabelAppointmrntDate.setText(date);

        holder.mLabelPracticeName.setText(assignedProvider.getSpecialty().getName());
        holder.mLabelProviderName.setText(assignedProvider.getFullName());

        if (assignedProvider.hasImage()) {
            try {
                THSManager.getInstance().getAwsdk(holder.mImageViewCircularImageView.getContext()).
                        getPracticeProvidersManager().
                        newImageLoader(assignedProvider,
                                holder.mImageViewCircularImageView, ProviderImageSize.LARGE).placeholder
                        (holder.mImageViewCircularImageView.getResources().getDrawable(R.drawable.doctor_placeholder)).
                        build().load();
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public int getItemCount() {
        return mAppointmentList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        Label mLabelAppointmrntDate;
        CircularImageView mImageViewCircularImageView;
        Label mLabelProviderName;
        Label mLabelPracticeName;

        public CustomViewHolder(View view) {
            super(view);
            this.mLabelAppointmrntDate = (Label) view.findViewById(R.id.ths_appointment_date);
            this.mImageViewCircularImageView = (CircularImageView) view.findViewById(R.id.ths_providerImage);
            this.mLabelProviderName = (Label) view.findViewById(R.id.providerNameLabel);
            this.mLabelPracticeName = (Label) view.findViewById(R.id.practiceNameLabel);
        }
    }
}
