package com.philips.platform.ths.appointment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerslist.OnProviderListItemClickListener;
import com.philips.platform.ths.providerslist.THSProvidersListAdapter;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.RatingBar;

import java.util.List;

public class THSAvailableProviderListBasedOnDateAdapter extends RecyclerView.Adapter<THSAvailableProviderListBasedOnDateAdapter.MyViewHolder>{

    private List<ProviderInfo> providerList;
    private OnProviderListItemClickListener onProviderItemClickListener;
    THSAvailableProviderListBasedOnDatePresenter mThsAvailableProviderListBasedOnDatePresenter;

    public THSAvailableProviderListBasedOnDateAdapter(List<ProviderInfo> providerInfos, THSAvailableProviderListBasedOnDatePresenter thsAvailableProviderListBasedOnDatePresenter) {
        providerList = providerInfos;
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
        final ProviderInfo provider = providerList.get(position);

        holder.providerRating.setRating(provider.getRating());
        holder.name.setText("Dr. " + provider.getFullName());
        holder.practice.setText(provider.getSpecialty().getName());
        holder.isAvailble.setText(""+provider.getVisibility());
        if(provider.hasImage()) {
            try {
                THSManager.getInstance().getAwsdk(holder.providerImage.getContext()).getPracticeProvidersManager().newImageLoader(provider, holder.providerImage, ProviderImageSize.SMALL).placeholder(holder.providerImage.getResources().getDrawable(R.drawable.doctor_placeholder)).build().load();
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProviderItemClickListener.onItemClick(provider);
            }
        };
        holder.relativeLayout.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return 0;
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
