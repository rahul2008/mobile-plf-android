package com.philips.platform.ths.appointment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class THSConfirmAppointmentFragment extends THSBaseFragment implements THSAppointmentInterface{

    public static final String TAG = THSConfirmAppointmentFragment.class.getSimpleName();
    THSProviderInfo mThsProviderInfo;
    Date mAppointmentDate;
    Label mProviderFullName;
    Label mPracticeNameLabel;
    Label mEmailSentMessage;
    Label mLabelDate;
    ImageView mImageProviderImage;
    Label mLabelIsAvailable;
    ImageButton mImageIsAvailable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_confirm_appointment, container, false);
        Bundle bundle = getArguments();
        mThsProviderInfo = bundle.getParcelable(THSConstants.THS_PROVIDER_INFO);
        mAppointmentDate = (Date) bundle.getSerializable(THSConstants.THS_DATE);

        mProviderFullName = (Label) view.findViewById(R.id.details_providerNameLabel);
        mPracticeNameLabel = (Label) view.findViewById(R.id.details_practiceNameLabel);
        mEmailSentMessage = (Label) view.findViewById(R.id.email_sent);
        mLabelDate = (Label) view.findViewById(R.id.date);
        mImageProviderImage = (ImageView)view.findViewById(R.id.details_providerImage);

        mLabelIsAvailable = (Label)view.findViewById(R.id.details_isAvailableLabel);
        mLabelIsAvailable.setVisibility(View.GONE);
        mImageIsAvailable = (ImageButton)view.findViewById(R.id.details_isAvailableImage);
        mImageIsAvailable.setVisibility(View.GONE);
        
        mProviderFullName.setText(mThsProviderInfo.getProviderInfo().getFullName());
        mPracticeNameLabel.setText(mThsProviderInfo.getProviderInfo().getSpecialty().getName());
        mEmailSentMessage.setText(getString(R.string.ths_email_sent) + " "+ THSManager.getInstance().getPTHConsumer().getConsumer().getEmail());
        mLabelDate.setText(new SimpleDateFormat(THSConstants.DATE_TIME_FORMATTER, Locale.getDefault()).format(mAppointmentDate));

        if(mThsProviderInfo.getProviderInfo().hasImage()) {
            try {
                THSManager.getInstance().getAwsdk(getContext()).getPracticeProvidersManager().
                        newImageLoader(mThsProviderInfo.getProviderInfo(), mImageProviderImage,
                                ProviderImageSize.SMALL).placeholder(mImageProviderImage.getResources().
                        getDrawable(R.drawable.doctor_placeholder)).build().load();
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        THSConfirmAppointmentPresenter thsConfirmAppointmentPresenter = new THSConfirmAppointmentPresenter(this,this);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_confirm), true);
        }
        super.onActivityCreated(savedInstanceState);
        try {
            thsConfirmAppointmentPresenter.scheduleAppointment();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public THSProviderInfo getTHSProviderInfo() {
        return mThsProviderInfo;
    }

    @Override
    public Date getAppointmentDate() {
        return mAppointmentDate;
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }
}
