/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

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
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class THSConfirmAppointmentFragment extends THSBaseFragment implements THSAppointmentInterface, View.OnClickListener {

    public static final String TAG = THSConfirmAppointmentFragment.class.getSimpleName();
    private THSProviderInfo mThsProviderInfo;
    private Date mAppointmentDate;
    private Label mProviderFullName, mPracticeNameLabel, mEmailSentMessage, mLabelDate, reminderTime;
    private CircularImageView mImageProviderImage;
    private Label mLabelIsAvailable;
    private ImageView mImageIsAvailable;
    private String reminderTimeString;
    private Button ok_got_it;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_confirm_appointment, container, false);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_confirm), true);
        }
        Bundle bundle = getArguments();
        mThsProviderInfo = bundle.getParcelable(THSConstants.THS_PROVIDER_INFO);
        mAppointmentDate = (Date) bundle.getSerializable(THSConstants.THS_DATE);
        reminderTimeString = bundle.getString(THSConstants.THS_SET_REMINDER_EXTRA_KEY);

        ok_got_it = (Button) view.findViewById(R.id.ok_got_it);
        ok_got_it.setOnClickListener(this);
        reminderTime = (Label) view.findViewById(R.id.reminderTime);
        if(reminderTimeString.contains(THSConstants.THS_NO_REMINDER_STRING)){
            reminderTime.setVisibility(View.GONE);
        }
        reminderTime.setText("Reminder set " + reminderTimeString + " before appointment");
        mProviderFullName = (Label) view.findViewById(R.id.details_providerNameLabel);
        mPracticeNameLabel = (Label) view.findViewById(R.id.details_practiceNameLabel);
        mEmailSentMessage = (Label) view.findViewById(R.id.email_sent);
        mLabelDate = (Label) view.findViewById(R.id.date);
        mImageProviderImage = (CircularImageView) view.findViewById(R.id.details_providerImage);

        mLabelIsAvailable = (Label) view.findViewById(R.id.details_isAvailableLabel);
        mLabelIsAvailable.setVisibility(View.GONE);
        mImageIsAvailable = (ImageView) view.findViewById(R.id.details_isAvailableImage);
        mImageIsAvailable.setVisibility(View.GONE);

        mProviderFullName.setText(mThsProviderInfo.getProviderInfo().getFullName());
        mPracticeNameLabel.setText(mThsProviderInfo.getProviderInfo().getSpecialty().getName());
        mEmailSentMessage.setText(getString(R.string.ths_email_sent) + " " + THSManager.getInstance().getPTHConsumer().getConsumer().getEmail());
        mLabelDate.setText(new SimpleDateFormat(THSConstants.DATE_TIME_FORMATTER, Locale.getDefault()).format(mAppointmentDate));

        if (mThsProviderInfo.getProviderInfo().hasImage()) {
            try {
                THSManager.getInstance().getAwsdk(getContext()).getPracticeProvidersManager().
                        newImageLoader(mThsProviderInfo.getProviderInfo(), mImageProviderImage,
                                ProviderImageSize.SMALL).placeholder(mImageProviderImage.getResources().
                        getDrawable(R.drawable.doctor_placeholder, getActivity().getTheme())).build().load();
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }
        return view;
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
    public void onClick(View v) {
        if(v.getId() == R.id.ok_got_it){
            popFragmentByTag(THSPracticeFragment.TAG, 0);
        }
    }
}
