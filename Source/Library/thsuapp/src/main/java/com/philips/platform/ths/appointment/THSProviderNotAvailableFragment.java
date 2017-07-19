package com.philips.platform.ths.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RatingBar;

public class THSProviderNotAvailableFragment extends THSAvailableProviderListBasedOnDateFragment{
    public static final String TAG = THSProviderNotAvailableFragment.class.getSimpleName();

    private Provider mProvider;
    private THSProviderEntity mThsProviderEntity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        mProvider = bundle.getParcelable(THSConstants.THS_PROVIDER);
        mThsProviderEntity = bundle.getParcelable(THSConstants.THS_PROVIDER_ENTITY);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_available_provider), true);
        }
    }

    public Provider getProvider() {
        return mProvider;
    }

    public void setProvider(Provider mProvider) {
        this.mProvider = mProvider;
    }

    public void updateProviderDetails(View view) {
        if(view == null)
            return;
        Label label = (Label) view.findViewById(R.id.schedule_appointment);
        label.setVisibility(View.GONE);
        RelativeLayout detailsContainer = (RelativeLayout) view.findViewById(R.id.details_container);
        detailsContainer.setVisibility(View.VISIBLE);

        ImageView imageView = (ImageView) view.findViewById(R.id.providerImage);

        if(mProvider.hasImage()) {
            try {
                THSManager.getInstance().getAwsdk(getContext()).getPracticeProvidersManager().
                        newImageLoader(((THSAvailableProvider) mThsProviderEntity).getProviderInfo(), imageView,
                                ProviderImageSize.SMALL).placeholder(imageView.getResources().
                        getDrawable(R.drawable.doctor_placeholder)).build().load();
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }

        Label providerName = (Label) view.findViewById(R.id.providerNameLabel);
        providerName.setText(mProvider.getFullName());
        Label practiceName = (Label) view.findViewById(R.id.practiceNameLabel);
        practiceName.setText(mProvider.getSpecialty().getName());
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.providerRating);
        ratingBar.setRating(mProvider.getRating());
        RelativeLayout noAvailableProviderContainer = (RelativeLayout) view.findViewById(R.id.no_available_container);
        noAvailableProviderContainer.setVisibility(View.VISIBLE);
    }

    public THSProviderEntity getThsProviderEntity() {
        return mThsProviderEntity;
    }

    public void setThsProviderEntity(THSProviderEntity mThsProviderEntity) {
        this.mThsProviderEntity = mThsProviderEntity;
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }
}
