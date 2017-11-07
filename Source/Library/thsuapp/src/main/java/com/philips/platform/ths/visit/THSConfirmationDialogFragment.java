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
import android.widget.ImageView;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.utility.THSConstants.THS_IN_APP_NOTIFICATION;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;

public class THSConfirmationDialogFragment extends DialogFragment implements View.OnClickListener{
    public static final String TAG = THSConfirmationDialogFragment.class.getSimpleName();

    THSBasePresenter mPresenter;
    Label mTitleLabel;
    Label mMessageLabel;
    ImageView mTitleImage;
    Button mPrimaryButton;
    Label mSecondaryButtonLabel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = inflater.cloneInContext(UIDHelper.getPopupThemedContext(this.getContext()));
        View view = layoutInflater.inflate(R.layout.ths_confirmation_dialog_fragment, container, false);
        mTitleLabel = (Label) view.findViewById(R.id.ths_confirmation_dialog_title_label);
        mTitleImage = (ImageView) view.findViewById(R.id.ths_confirmation_dialog_title_image);
        mMessageLabel = (Label) view.findViewById(R.id.ths_confirmation_dialog_message_label);
        mPrimaryButton = (Button) view.findViewById(R.id.ths_confirmation_dialog_primary_button);
        mPrimaryButton.setOnClickListener(this);
        mSecondaryButtonLabel = (Label) view.findViewById(R.id.ths_confirmation_dialog_secondary_button_label);
        mSecondaryButtonLabel.setOnClickListener(this);
        THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_IN_APP_NOTIFICATION, "statusNotification" , "Do you really want to cancel your visit");
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
            mPresenter.onEvent(R.id.ths_confirmation_dialog_primary_button);
        }else if (v.getId()==R.id.ths_confirmation_dialog_secondary_button_label){
            dismiss();
            mPresenter.onEvent(R.id.ths_confirmation_dialog_secondary_button_label);

        }

    }
}
