package com.philips.platform.ths.visit;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.visit.Appointment;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.settings.THSScheduledVisitsPresenter;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_CANCEL_VISIT;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_RESPONSE_CANCEL_VISIT;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_RESPONSE_DONT_CANCEL_VISIT;

public class THSConfirmationDialogFragment extends DialogFragment implements View.OnClickListener{
    public static final String TAG = THSConfirmationDialogFragment.class.getSimpleName();
    public static final String APPOINTMENT_DIALOG_TYPE = "APPOINTMENT_DIALOG_TYPE";

    THSBasePresenter mPresenter;
    Label mTitleLabel;
    Label mMessageLabel;
    Button mPrimaryButton;
    Button mSecondaryButtonButton;
    String dialogFragmentType=null;

    public String getDialogFragmentType() {
        return dialogFragmentType;
    }

    public void setDialogFragmentType(String dialogFragmentType) {
        this.dialogFragmentType = dialogFragmentType;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = inflater.cloneInContext(UIDHelper.getPopupThemedContext(this.getContext()));
        View view = layoutInflater.inflate(R.layout.ths_confirmation_dialog_fragment, container, false);
        mTitleLabel = (Label) view.findViewById(R.id.ths_confirmation_dialog_title_label);
        mMessageLabel = (Label) view.findViewById(R.id.ths_confirmation_dialog_message_label);
        mPrimaryButton = (Button) view.findViewById(R.id.ths_confirmation_dialog_primary_button);
        mPrimaryButton.setOnClickListener(this);
        mSecondaryButtonButton = (Button) view.findViewById(R.id.ths_confirmation_dialog_secondary_button);
        mSecondaryButtonButton.setOnClickListener(this);
        if(null!=getDialogFragmentType()&&getDialogFragmentType().equals(APPOINTMENT_DIALOG_TYPE)){
            mTitleLabel.setText(getString(R.string.ths_appointment_cancelTitle));
            mPrimaryButton.setText(getString(R.string.ths_appointment_cancelYes));
            mSecondaryButtonButton.setText(getString(R.string.ths_appointment_cancelNo));
            Appointment appointment = ( (THSScheduledVisitsPresenter)mPresenter).getAppointment();
            final Long scheduledStartTime = appointment.getSchedule().getScheduledStartTime();
            final Date dateScheduled = new Date(scheduledStartTime);
            final String date = new SimpleDateFormat(THSConstants.DATE_TIME_FORMATTER, Locale.getDefault()).format(dateScheduled).toString();
            final String providerName= appointment.getAssignedProvider().getFullName();
            mMessageLabel.setText(date +" "+ getString(R.string.ths_appointment_with)+" "+providerName);
        }
        return view;
    }


    public void setPresenter(THSBasePresenter presenter){
        mPresenter=presenter;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ths_confirmation_dialog_primary_button){
            dismiss();
            THSTagUtils.tagInAppNotification(THS_ANALYTICS_CANCEL_VISIT,THS_ANALYTICS_RESPONSE_CANCEL_VISIT);
            mPresenter.onEvent(R.id.ths_confirmation_dialog_primary_button);
        }else if (v.getId()==R.id.ths_confirmation_dialog_secondary_button){
            dismiss();
            THSTagUtils.tagInAppNotification(THS_ANALYTICS_CANCEL_VISIT,THS_ANALYTICS_RESPONSE_DONT_CANCEL_VISIT);
            mPresenter.onEvent(R.id.ths_confirmation_dialog_secondary_button);

        }

    }
}
