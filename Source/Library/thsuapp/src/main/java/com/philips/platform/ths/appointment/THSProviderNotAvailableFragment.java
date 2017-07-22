package com.philips.platform.ths.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
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
import com.philips.platform.uid.view.widget.NotificationBadge;
import com.philips.platform.uid.view.widget.RatingBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class THSProviderNotAvailableFragment extends THSAvailableProviderListBasedOnDateFragment implements View.OnClickListener{
    public static final String TAG = THSProviderNotAvailableFragment.class.getSimpleName();

    private Provider mProvider;
    private THSProviderEntity mThsProviderEntity;
    private THSProviderNotAvailablePresenter mThsProviderNotAvailablePresenter;
    Label mChangeAppointDateView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_available_doctors_based_on_time, container, false);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_select_date_and_time), true);
        }

        mChangeAppointDateView = (Label) view.findViewById(R.id.calendar_view);
        mChangeAppointDateView.setOnClickListener(this);

        Bundle bundle = getArguments();
        mProvider = bundle.getParcelable(THSConstants.THS_PROVIDER);
        mThsProviderEntity = bundle.getParcelable(THSConstants.THS_PROVIDER_ENTITY);
        mThsProviderNotAvailablePresenter = new THSProviderNotAvailablePresenter(this);

        return view;
    }

    public Provider getProvider() {
        return mProvider;
    }

    public void setProvider(Provider mProvider) {
        this.mProvider = mProvider;
    }

    public void updateProviderDetails(THSAvailableProviderList availableProviders) {
        View view = getView();
        if(view == null)
            return;
        Label label = (Label) view.findViewById(R.id.schedule_appointment);
        label.setVisibility(View.GONE);
        RelativeLayout detailsContainer = (RelativeLayout) view.findViewById(R.id.details_container);
        detailsContainer.setVisibility(View.VISIBLE);

        mChangeAppointDateView.setText(new SimpleDateFormat(THSConstants.DATE_FORMATTER, Locale.getDefault()).format(mDate));

        View viewEndLayer = view.findViewById(R.id.end_layer);
        viewEndLayer.setVisibility(View.GONE);

        Label labelAvailableDocs = (Label) view.findViewById(R.id.isAvailableLabel);

        RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams)labelAvailableDocs.getLayoutParams();
        layoutParams.addRule(RelativeLayout.RIGHT_OF,R.id.notification_badge);

        labelAvailableDocs.setText("Available time slots");

        ImageView greenTick = (ImageView)view.findViewById(R.id.isAvailableImage);
        greenTick.setVisibility(View.GONE);

        NotificationBadge notificationBadge = (NotificationBadge) view.findViewById(R.id.notification_badge);
        notificationBadge.setVisibility(View.VISIBLE);

        notificationBadge.setText("" + availableProviders.getAvailableProvidersList().size());

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

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.calendar_view){
            mThsProviderNotAvailablePresenter.onEvent(R.id.calendar_view);
        }
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }
}
