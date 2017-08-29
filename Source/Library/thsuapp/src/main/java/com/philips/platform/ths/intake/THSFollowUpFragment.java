/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBarButton;

public class THSFollowUpFragment extends THSBaseFragment implements View.OnClickListener {
    public static final String TAG = THSFollowUpFragment.class.getSimpleName();
    protected EditText mPhoneNumberEditText;
    private CheckBox mNoppAgreeCheckBox;
    ProgressBarButton mFollowUpContinueButton;
    private THSFollowUpPresenter mTHSFollowUpPresenter;
    private ActionBarListener actionBarListener;
    protected String updatedPhone;
    private Label nopp_label;
    private int REQUEST_LOCATION = 1001;
    private LocationManager mLocationManager = null;
    private String provider = null;
    protected Location updatedLocation = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_intake_follow_up, container, false);

        actionBarListener = getActionBarListener();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(R.string.ths_prepare_your_visit, true);
        }
        mTHSFollowUpPresenter = new THSFollowUpPresenter(this);
        mPhoneNumberEditText = (EditText) view.findViewById(R.id.pth_intake_follow_up_phone_number);
        mFollowUpContinueButton = (ProgressBarButton) view.findViewById(R.id.pth_intake_follow_up_continue_button);
        mFollowUpContinueButton.setOnClickListener(this);
        mFollowUpContinueButton.setEnabled(false);
        mNoppAgreeCheckBox = (CheckBox) view.findViewById(R.id.pth_intake_follow_up_nopp_agree_check_box);
        mNoppAgreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mFollowUpContinueButton.setEnabled(true);
                } else {
                    mFollowUpContinueButton.setEnabled(false);
                }
            }
        });
        nopp_label = (Label) view.findViewById(R.id.pth_intake_follow_up_i_agree_link_text);
        nopp_label.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        THSConsumer THSConsumer = THSManager.getInstance().getPTHConsumer();
        if (null != THSConsumer && null != THSConsumer.getConsumer() && null != THSConsumer.getConsumer().getPhone() && !THSConsumer.getConsumer().getPhone().isEmpty()) {
            mPhoneNumberEditText.setText(THSConsumer.getConsumer().getPhone());
        }
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        mTHSFollowUpPresenter.onEvent(v.getId());
    }

}
