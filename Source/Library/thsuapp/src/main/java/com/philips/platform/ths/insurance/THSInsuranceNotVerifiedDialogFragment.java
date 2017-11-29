package com.philips.platform.ths.insurance;
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

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.ths.visit.THSConfirmationDialogFragment;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_INSURANCE_VALIDATION;
import static com.philips.platform.ths.utility.THSConstants.THS_IN_APP_NOTIFICATION;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;


public class THSInsuranceNotVerifiedDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = THSConfirmationDialogFragment.class.getSimpleName();

    THSBasePresenter mPresenter;
    Label mTitleLabel;
    Label mMessageLabel;
    Button mPrimaryButton;
    Button mSecondaryButtonLabel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = inflater.cloneInContext(UIDHelper.getPopupThemedContext(this.getContext()));
        View view = layoutInflater.inflate(R.layout.ths_confirmation_dialog_fragment, container, false);
        mTitleLabel = (Label) view.findViewById(R.id.ths_confirmation_dialog_title_label);
        mTitleLabel.setText(getResources().getString(R.string.ths_insurance_not_verified_confirm_title));
        mMessageLabel = (Label) view.findViewById(R.id.ths_confirmation_dialog_message_label);
        mMessageLabel.setText(getResources().getString(R.string.ths_insurance_not_verified_confirm_message));
        mPrimaryButton = (Button) view.findViewById(R.id.ths_confirmation_dialog_primary_button);
        mPrimaryButton.setText(getResources().getString(R.string.ths_ok));
        mPrimaryButton.setOnClickListener(this);
        mSecondaryButtonLabel = (Button) view.findViewById(R.id.ths_confirmation_dialog_secondary_button);
        mSecondaryButtonLabel.setText(getResources().getString(R.string.ths_insurance_not_verified_confirm_secondary_button_label_text));
        mSecondaryButtonLabel.setOnClickListener(this);
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
            THSTagUtils.tagInAppNotification(THS_ANALYTICS_INSURANCE_VALIDATION,"Ok");
            mPresenter.onEvent(R.id.ths_confirmation_dialog_primary_button);
        }else if (v.getId()==R.id.ths_confirmation_dialog_secondary_button){
            dismiss();
            THSTagUtils.tagInAppNotification(THS_ANALYTICS_INSURANCE_VALIDATION,"Try again");
            mPresenter.onEvent(R.id.ths_confirmation_dialog_secondary_button);

        }
    }

}
