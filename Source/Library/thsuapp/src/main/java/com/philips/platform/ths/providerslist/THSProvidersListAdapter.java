package com.philips.platform.ths.providerslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.R;
import com.philips.platform.uid.view.widget.RatingBar;

import java.util.List;

public class THSProvidersListAdapter extends RecyclerView.Adapter<THSProvidersListAdapter.MyViewHolder> {
    private List<THSProviderInfo> thsProviderInfos;
    private OnProviderListItemClickListener onProviderItemClickListener;
    private List<AvailableProvider> mTHSAvailableProviderList;


    public void setOnProviderItemClickListener(OnProviderListItemClickListener onProviderItemClickListener) {
        this.onProviderItemClickListener = onProviderItemClickListener;
    }
    public THSProvidersListAdapter(List<THSProviderInfo> thsProviderInfos, List<AvailableProvider> availableProvidersList){
        this.thsProviderInfos = thsProviderInfos;
        mTHSAvailableProviderList = availableProvidersList;
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

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ths_provider_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final THSProviderInfo thsProviderInfo = thsProviderInfos.get(position);

        holder.providerRating.setRating(thsProviderInfo.getRating());
        holder.name.setText("Dr. " + thsProviderInfo.getProviderInfo().getFullName());
        holder.practice.setText(thsProviderInfo.getSpecialty().getName());
        holder.isAvailble.setText(""+thsProviderInfo.getVisibility());
        if(thsProviderInfo.hasImage()) {
            try {
                THSManager.getInstance().getAwsdk(holder.providerImage.getContext()).getPracticeProvidersManager().newImageLoader(thsProviderInfo.getProviderInfo(), holder.providerImage, ProviderImageSize.SMALL).placeholder(holder.providerImage.getResources().getDrawable(R.drawable.doctor_placeholder)).build().load();
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTHSAvailableProviderList==null) {
                    onProviderItemClickListener.onItemClick(thsProviderInfo,null);
                }else {
                    onProviderItemClickListener.onItemClick(thsProviderInfo, mTHSAvailableProviderList.get(position));
                }
            }
        };
        holder.relativeLayout.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return thsProviderInfos.size();
    }
}
