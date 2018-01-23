/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.insurance;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.RadioButton;
import com.philips.platform.uid.view.widget.RadioGroup;

import static com.philips.platform.ths.utility.THSConstants.IS_LAUNCHED_FROM_COST_SUMMARY;
import static com.philips.platform.ths.utility.THSConstants.THS_INSURANCE_CONFIRM;


public class THSInsuranceConfirmationFragment extends THSBaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    public static final String TAG = THSInsuranceConfirmationFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    protected THSInsuranceConfirmationPresenter mPresenter;
    protected RadioGroup mConfirmationRadioGroup;
    private int mConfirmationRadioButtonSelectedID;
    private Button confirmationContinueButton;
    boolean isLaunchedFromCostSummary = false;
    private RelativeLayout mProgressbarContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_insurance_confirmation, container, false);
        Bundle bundle = getArguments();
        if (null != bundle) {
            isLaunchedFromCostSummary = bundle.getBoolean(IS_LAUNCHED_FROM_COST_SUMMARY);
        }
        mPresenter = new THSInsuranceConfirmationPresenter(this);
        mConfirmationRadioGroup = (RadioGroup) view.findViewById(R.id.pth_insurance_confirmation_radio_group);
        mConfirmationRadioGroup.setOnCheckedChangeListener(this);
        confirmationContinueButton = (Button) view.findViewById(R.id.pth_insurance_confirmation_continue_button);
        confirmationContinueButton.setOnClickListener(this);
        mProgressbarContainer = (RelativeLayout) view.findViewById(R.id.ths_insurance_confirmation_container);
        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        THSTagUtils.doTrackPageWithInfo(THS_INSURANCE_CONFIRM,null,null);
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Insurance", true);
        }
    }

    /**
     * <p>Called when the checked radio button has changed. When the
     * selection is cleared, checkedId is -1.</p>
     *
     * @param radioGroup the group in which the checked radio button has changed
     * @param checkedId  the unique identifier of the newly checked radio button
     */
    @Override
    public void onCheckedChanged(android.widget.RadioGroup radioGroup, @IdRes int checkedId) {
        RadioButton radioButton = (RadioButton) radioGroup.findViewById(checkedId);
        if (null != radioButton && checkedId > -1) {
            if (radioButton.getId() == R.id.pth_insurance_confirmation_radio_option_yes) {
                mConfirmationRadioButtonSelectedID = R.id.pth_insurance_confirmation_radio_option_yes;
            } else if (radioButton.getId() == R.id.pth_insurance_confirmation_radio_option_no) {
                mConfirmationRadioButtonSelectedID = R.id.pth_insurance_confirmation_radio_option_no;
            }

        }
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            confirmationContinueButton.setEnabled(false);
        } else {
            confirmationContinueButton.setEnabled(true);
        }
    }


    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pth_insurance_confirmation_continue_button) {
            if (mConfirmationRadioButtonSelectedID == R.id.pth_insurance_confirmation_radio_option_yes) {
                mPresenter.onEvent(R.id.pth_insurance_confirmation_radio_option_yes);
            } else if (mConfirmationRadioButtonSelectedID == R.id.pth_insurance_confirmation_radio_option_no) {
                mPresenter.onEvent(R.id.pth_insurance_confirmation_radio_option_no);
            }
        }

    }

    protected void showProgressbar(){
        createCustomProgressBar(mProgressbarContainer, BIG);
    }

}
