/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBarButton;


import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_PHONE_NUMBER_VALIDATION;
import static com.philips.platform.ths.utility.THSConstants.THS_FOLLOW_UP_PAGE;

public class THSFollowUpFragment extends THSBaseFragment implements View.OnClickListener,THSFollowUpViewInterface {
    public static final String TAG = THSFollowUpFragment.class.getSimpleName();
    protected EditText mPhoneNumberEditText;
    private CheckBox mNoppAgreeCheckBox;
    ProgressBarButton mFollowUpContinueButton;
    private THSFollowUpPresenter mTHSFollowUpPresenter;
    private ActionBarListener actionBarListener;
    private Label nopp_label;
    private Label mLabelPatientName;
    private InputValidationLayout ths_intake_follow_up_phone_number_container;
    static final long serialVersionUID = 151L;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_intake_follow_up, container, false);

        actionBarListener = getActionBarListener();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(R.string.ths_prepare_your_visit, true);
        }
        mTHSFollowUpPresenter = new THSFollowUpPresenter(this, this);
        mPhoneNumberEditText = (EditText) view.findViewById(R.id.pth_intake_follow_up_phone_number);
        mFollowUpContinueButton = (ProgressBarButton) view.findViewById(R.id.pth_intake_follow_up_continue_button);
        mFollowUpContinueButton.setOnClickListener(this);
        mFollowUpContinueButton.setEnabled(false);
        mNoppAgreeCheckBox = (CheckBox) view.findViewById(R.id.pth_intake_follow_up_nopp_agree_check_box);
        ths_intake_follow_up_phone_number_container = view.findViewById(R.id.ths_intake_follow_up_phone_number_container);
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

        mLabelPatientName = (Label) view.findViewById(R.id.ths_follow_up_patient_name);
        String name = getString(R.string.ths_dependent_name, THSManager.getInstance().getThsConsumer(getContext()).getFirstName());
        mLabelPatientName.setText(name);
        THSTagUtils.doTrackPageWithInfo(THS_FOLLOW_UP_PAGE,null,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        THSConsumerWrapper THSConsumerWrapper = THSManager.getInstance().getPTHConsumer(getContext());
       /* removing pre population of phone number in align with IOS (Bug 100462)
        if (null != THSConsumerWrapper && null != THSConsumerWrapper.getConsumer() && null != THSConsumerWrapper.getConsumer().getPhone() && !THSConsumerWrapper.getConsumer().getPhone().isEmpty()) {
            mPhoneNumberEditText.setText(THSConsumerWrapper.getConsumer().getPhone());
        }*/
    }

    @Override
    public boolean validatePhoneNumber() {
        String pattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
        String phoneNumber = mPhoneNumberEditText.getText().toString();
        return phoneNumber.matches(pattern);

    }

    @Override
    public void showNoticeOfPrivacyFragment() {
        final THSNoticeOfPrivacyPracticesFragment fragment = new THSNoticeOfPrivacyPracticesFragment();
        addFragment(fragment, THSNoticeOfPrivacyPracticesFragment.TAG, null, true);
    }

    @Override
    public String getConsumerPhoneNumber() {
        return mPhoneNumberEditText.getText().toString().trim();
    }

    @Override
    public void startProgressButton() {
        mFollowUpContinueButton.showProgressIndicator();
    }

    @Override
    public void hideProgressButton() {
        mFollowUpContinueButton.hideProgressIndicator();
    }

    @Override
    public void showProviderDetailsFragment() {
        THSProviderDetailsFragment pthProviderDetailsFragment = new THSProviderDetailsFragment();
        addFragment(pthProviderDetailsFragment, THSProviderDetailsFragment.TAG, null, true);
    }

    @Override
    public void showConditionsFragment() {
        addFragment(new THSCheckPharmacyConditionsFragment(), THSCheckPharmacyConditionsFragment.TAG, null, true);
    }

    @Override
    public void showInvalidPhoneNumberToast(String message) {
        showError(message);
    }

    @Override
    public void showInlineError() {
        ths_intake_follow_up_phone_number_container.setErrorMessage(R.string.ths_invalid_phone_number);
        ths_intake_follow_up_phone_number_container.showError();
        doTagging(THS_ANALYTICS_PHONE_NUMBER_VALIDATION,getString(R.string.ths_invalid_phone_number),false);
    }

    @Override
    public void hideInlineError() {
        if(ths_intake_follow_up_phone_number_container.isShowingError()){
           ths_intake_follow_up_phone_number_container.hideError();
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
