package com.philips.platform.ths.appointment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerslist.OnProviderListItemClickListener;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.RatingBar;

import java.util.List;

public class THSAvailableProviderListBasedOnDateAdapter extends RecyclerView.Adapter<THSAvailableProviderListBasedOnDateAdapter.MyViewHolder>{

    private THSAvailableProviderList availableProvidersList;
    private OnProviderListItemClickListener onProviderItemClickListener;
    THSAvailableProviderListBasedOnDatePresenter mThsAvailableProviderListBasedOnDatePresenter;

    public THSAvailableProviderListBasedOnDateAdapter(THSAvailableProviderList providerInfos, THSAvailableProviderListBasedOnDatePresenter thsAvailableProviderListBasedOnDatePresenter) {
        availableProvidersList = providerInfos;
        mThsAvailableProviderListBasedOnDatePresenter = thsAvailableProviderListBasedOnDatePresenter;
    }

    public void setOnProviderItemClickListener(OnProviderListItemClickListener onProviderItemClickListener) {
        this.onProviderItemClickListener = onProviderItemClickListener;
    }

    @Override
    public THSAvailableProviderListBasedOnDateAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ths_provider_list, parent, false);

        return new THSAvailableProviderListBasedOnDateAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(THSAvailableProviderListBasedOnDateAdapter.MyViewHolder holder, int position) {
        final AvailableProvider availableProvider = availableProvidersList.getAvailableProvidersList().get(position);

        holder.providerRating.setRating(availableProvider.getProviderInfo().getRating());
        holder.name.setText("Dr. " + availableProvider.getProviderInfo().getFullName());
        holder.practice.setText(availableProvider.getProviderInfo().getSpecialty().getName());
        holder.isAvailble.setText(""+availableProvider.getProviderInfo().getVisibility());
        if(availableProvider.getProviderInfo().hasImage()) {
            try {
                THSManager.getInstance().getAwsdk(holder.providerImage.getContext()).getPracticeProvidersManager().newImageLoader(availableProvider.getProviderInfo(), holder.providerImage, ProviderImageSize.SMALL).placeholder(holder.providerImage.getResources().getDrawable(R.drawable.doctor_placeholder)).build().load();
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProviderItemClickListener.onItemClick(availableProvider.getProviderInfo());
            }
        };
        holder.relativeLayout.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return availableProvidersList.getAvailableProvidersList().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, practice, isAvailble;
        public RatingBar providerRating;
        public ImageView providerImage;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            practice = (TextView) view.findViewById(R.id.practiceNameLabel);
            name = (TextView) view.findViewById(R.id.providerNameLabel);
            isAvailble = (TextView) view.findViewById(R.id.isAvailableLabel);
            providerRating = (RatingBar) view.findViewById(R.id.providerRating);
            providerImage = (ImageView) view.findViewById(R.id.providerImage);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.providerListItemLayout);

        }
    }
}
