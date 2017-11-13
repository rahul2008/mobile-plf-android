/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.visit.Appointment;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSWelcomeBackFragment;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_SCHEDULE_APPOINTMENT;
import static com.philips.platform.ths.utility.THSConstants.THS_EARLY_FOR_APPOINTMENT;
import static com.philips.platform.ths.visit.THSWaitingRoomFragment.CANCEL_VISIT_ALERT_DIALOG_TAG;

public class THSScheduledVisitsAdapter extends RecyclerView.Adapter<THSScheduledVisitsAdapter.CustomViewHolder>  {
    List<Appointment> mAppointmentList;
    THSScheduledVisitsFragment mThsScheduledVisitsFragment;
    AlertDialogFragment alertDialogFragment;
    final int FIFTEEN = 15;

    public THSScheduledVisitsAdapter(List<Appointment> appointments, THSScheduledVisitsFragment thsScheduledVisitsFragment) {
        mAppointmentList = appointments;
        mThsScheduledVisitsFragment = thsScheduledVisitsFragment;
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
        final PracticeInfo practiceInfo = appointment.getAssignedProvider().getPracticeInfo();
        final Long scheduledStartTime = appointment.getSchedule().getScheduledStartTime();

        final Date dateScheduled = new Date(scheduledStartTime);
        final String date = new SimpleDateFormat(THSConstants.DATE_TIME_FORMATTER, Locale.getDefault()).format(dateScheduled).toString();
        holder.mLabelAppointmrntDate.setText(date);

        holder.mLabelPracticeName.setText(assignedProvider.getSpecialty().getName());
        holder.mLabelProviderName.setText(assignedProvider.getFullName());

        if (assignedProvider.hasImage()) {
            try {
                final Drawable drawable = ContextCompat.getDrawable(mThsScheduledVisitsFragment.getContext(), R.drawable.doctor_placeholder);
                THSManager.getInstance().getAwsdk(holder.mImageViewCircularImageView.getContext()).
                        getPracticeProvidersManager().
                        newImageLoader(assignedProvider,
                                holder.mImageViewCircularImageView, ProviderImageSize.LARGE).placeholder(drawable).build().load();
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }

        holder.mCancelVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelDialog(appointment,true, true, true);
            }
        });

        holder.mStartVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar currentCalendar = Calendar.getInstance();
                Date dateCurrent = currentCalendar.getTime();
                final long utcCurrentMilliseconds = dateCurrent.getTime();

                Calendar scheduledCalendar = Calendar.getInstance();
                scheduledCalendar.setTime(dateScheduled);
                scheduledCalendar.add(Calendar.MINUTE, 15);
                Long utcScheduledMilliseconds = scheduledCalendar.getTime().getTime();

                if(utcCurrentMilliseconds>utcScheduledMilliseconds){
                    mThsScheduledVisitsFragment.doTagging(ANALYTICS_SCHEDULE_APPOINTMENT,mThsScheduledVisitsFragment.getString(R.string.late_for_appointment),false);
                    mThsScheduledVisitsFragment.showError(mThsScheduledVisitsFragment.getString(R.string.late_for_appointment));
                }else if(isUserArrivedEarly(utcCurrentMilliseconds, utcScheduledMilliseconds)){
                    mThsScheduledVisitsFragment.doTagging(ANALYTICS_SCHEDULE_APPOINTMENT,mThsScheduledVisitsFragment.getString(R.string.early_for_appointment),false);
                    mThsScheduledVisitsFragment.showError(mThsScheduledVisitsFragment.getString(R.string.early_for_appointment));
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putLong(THSConstants.THS_DATE, scheduledStartTime);
                    bundle.putParcelable(THSConstants.THS_PRACTICE_INFO, practiceInfo);
                    bundle.putParcelable(THSConstants.THS_PROVIDER, appointment.getAssignedProvider());
                    mThsScheduledVisitsFragment.addFragment(new THSWelcomeBackFragment(), THSWelcomeBackFragment.TAG, bundle, false);
                }
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(THSConstants.THS_PROVIDER,appointment.getAssignedProvider());
                bundle.putParcelable(THSConstants.THS_PRACTICE_INFO,practiceInfo);
                mThsScheduledVisitsFragment.addFragment(new THSProviderDetailsFragment(),THSProviderDetailsFragment.TAG,bundle, false);
            }
        };
        holder.mProviderLayout.setOnClickListener(listener);

    }

    private boolean isUserArrivedEarly(long utcCurrentMilliseconds, Long utcScheduledMilliseconds) {
        if (Math.abs(utcScheduledMilliseconds - utcCurrentMilliseconds) > TimeUnit.MINUTES.toMillis(FIFTEEN)) {
            return true;
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return mAppointmentList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        Label mLabelAppointmrntDate;
        CircularImageView mImageViewCircularImageView;
        Label mLabelProviderName;
        Label mLabelPracticeName;
        Button mCancelVisit;
        Button mStartVisit;
        RelativeLayout mProviderLayout;

        public CustomViewHolder(View view) {
            super(view);
            this.mLabelAppointmrntDate = (Label) view.findViewById(R.id.ths_appointment_date);
            this.mImageViewCircularImageView = (CircularImageView) view.findViewById(R.id.ths_providerImage);
            this.mLabelProviderName = (Label) view.findViewById(R.id.providerNameLabel);
            this.mLabelPracticeName = (Label) view.findViewById(R.id.practiceNameLabel);
            this.mStartVisit = (Button) view.findViewById(R.id.ths_start_visit);
            this.mCancelVisit = (Button) view.findViewById(R.id.ths_cancel_visit);
            this.mProviderLayout = (RelativeLayout) view.findViewById(R.id.provider_details_layout_container);
        }
    }

    void showCancelDialog(final Appointment appointment, final boolean showLargeContent, final boolean isWithTitle, final boolean showIcon) {


        final View.OnClickListener positiveButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                THSScheduledVisitsPresenter presenter = new THSScheduledVisitsPresenter(mThsScheduledVisitsFragment);
                presenter.cancelAppointment(appointment);
                presenter.setProgressBarVisibility(false);
                alertDialogFragment.dismiss();
            }
        };

        final View.OnClickListener negativeButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogFragment.dismiss();
            }
        };


        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(mThsScheduledVisitsFragment.getFragmentActivity())
                .setMessage(showLargeContent ? "Your visit shall be cancelled" : "Your visit shall be cancelled").
                        setPositiveButton(" Yes ", positiveButtonListener).
                        setNegativeButton(" No  ", negativeButtonListener);
        if (isWithTitle) {
            builder.setTitle("Do you really want to cancel your visit");
            if (showIcon) {
                builder.setIcon(R.drawable.uid_ic_cross_icon);

            }
        }
        alertDialogFragment = builder.setCancelable(false).create();
        alertDialogFragment.show(mThsScheduledVisitsFragment.getFragmentManager(), CANCEL_VISIT_ALERT_DIALOG_TAG);
        alertDialogFragment.setPositiveButtonListener(positiveButtonListener);
        alertDialogFragment.setNegativeButtonListener(negativeButtonListener);
    }
}
