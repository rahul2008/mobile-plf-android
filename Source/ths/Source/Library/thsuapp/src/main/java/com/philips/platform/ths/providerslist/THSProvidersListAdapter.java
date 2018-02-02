/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerslist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProvider;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.NotificationBadge;
import com.philips.platform.uid.view.widget.RatingBar;

import java.util.List;

public class THSProvidersListAdapter extends RecyclerView.Adapter<THSProvidersListAdapter.MyViewHolder> {

    private static String TAG = THSProvidersListAdapter.class.getSimpleName();
    private List<? extends THSProviderEntity> thsProviderInfos;
    private OnProviderListItemClickListener onProviderItemClickListener;


    public void setOnProviderItemClickListener(OnProviderListItemClickListener onProviderItemClickListener) {
        this.onProviderItemClickListener = onProviderItemClickListener;
    }

    public THSProvidersListAdapter(List<? extends THSProviderEntity> thsProviderInfos) {
        this.thsProviderInfos = thsProviderInfos;
        //  mTHSAvailableProviderList = availableProvidersList;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, practice, isAvailble;
        public RatingBar providerRating;
        public CircularImageView providerImage;
        public RelativeLayout relativeLayout;
        public NotificationBadge notificationBadge;
        public ImageView isAvailableStatus;

        public MyViewHolder(View view) {
            super(view);
            practice = (TextView) view.findViewById(R.id.practiceNameLabel);
            name = (TextView) view.findViewById(R.id.providerNameLabel);
            isAvailble = (TextView) view.findViewById(R.id.isAvailableLabel);
            providerRating = (RatingBar) view.findViewById(R.id.providerRating);
            providerImage = (CircularImageView) view.findViewById(R.id.providerImage);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.providerListItemLayout);
            notificationBadge = (NotificationBadge) view.findViewById(R.id.notification_badge);
            isAvailableStatus = (ImageView) view.findViewById(R.id.isAvailableImage);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ths_provider_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        THSProviderInfo thsProviderInfo;
        String providerAvailabilityString = null;
        String providerVisibility;
        Context context;
        if ((getThsProviderEntity(holder.getAdapterPosition()) instanceof THSProviderInfo)) {
            thsProviderInfo = getThsProviderInfo(holder.getAdapterPosition());
            providerVisibility = thsProviderInfo.getVisibility().toString();
            context = holder.isAvailableStatus.getContext();
            if (providerVisibility.equals(THSConstants.WEB_AVAILABLE)) {
                providerAvailabilityString = context.getResources().getString(R.string.ths_provider_available);
                holder.isAvailableStatus.setImageResource(R.mipmap.green_available_icon);
            } else if (providerVisibility.equals(THSConstants.PROVIDER_OFFLINE)) {
                providerAvailabilityString = context.getResources().getString(R.string.ths_provider_offline);
                holder.isAvailableStatus.setImageResource(R.mipmap.provider_offline_icon);
            } else if (providerVisibility.equals(THSConstants.PROVIDER_WEB_BUSY)) {
                providerAvailabilityString = context.getResources().getString(R.string.ths_provider_busy);
                holder.isAvailableStatus.setImageResource(R.mipmap.waiting_patient_icon);
            }

            holder.isAvailble.setText(providerAvailabilityString);

        } else {
            THSAvailableProvider thsAvailableProvider = (THSAvailableProvider) thsProviderInfos.get(position);
            thsProviderInfo = getThsProviderInfo(thsAvailableProvider);
            holder.isAvailble.setText(holder.isAvailble.getContext().getResources().getString(R.string.ths_provider_available_timeslots));
            holder.isAvailableStatus.setVisibility(View.GONE);
            holder.notificationBadge.setVisibility(View.VISIBLE);
            holder.notificationBadge.setText("" + thsAvailableProvider.getAvailableAppointmentTimeSlots().size());

            try {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.isAvailble.getLayoutParams();
                layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.notification_badge);
            }catch (Exception e){
                AmwellLog.i(THSProvidersListAdapter.TAG,e.getMessage());
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
                        (holder.providerImage.getResources().getDrawable(R.drawable.doctor_placeholder,holder.providerImage.getContext().getTheme())).
                        build().load();
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }
        }
        catch (Exception e){
            AmwellLog.e(THSProvidersListAdapter.TAG,e.toString());
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onProviderItemClickListener.onItemClick(thsProviderInfos.get(position));
            }
        };
        holder.relativeLayout.setOnClickListener(listener);

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
