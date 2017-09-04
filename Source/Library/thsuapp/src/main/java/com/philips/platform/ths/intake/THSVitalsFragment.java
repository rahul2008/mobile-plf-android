/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;

import static com.philips.platform.ths.utility.THSConstants.THS_ADD_VITALS_PAGE;
import static com.philips.platform.ths.utility.THSConstants.THS_FLOATING_BUTTON;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SYMPTOMS_PAGE;

public class THSVitalsFragment extends THSBaseFragment implements View.OnClickListener {

    public static final String TAG = THSVitalsFragment.class.getSimpleName();
    protected THSVitalsPresenter mThsVitalsPresenter;
    protected EditText mSystolic;
    protected EditText mDiastolic;
    protected EditText mTemperature;
    protected EditText mWeight;
    protected Button mContinue;
    private THSVitals mTHSVitals;
    private Button mSkipLabel;
    private InputValidationLayout mSystolicInputValidationLayout;
    private InputValidationLayout mDiastolicInputValidationLayout;
    private InputValidationLayout mFarenheitInputLayoutContainer;
    private InputValidationLayout mWeightInputLayoutContainer;
    String tagActions="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_intake_vitals, container, false);
        mSystolic = (EditText) view.findViewById(R.id.systolic);
        mDiastolic = (EditText) view.findViewById(R.id.diastolic);
        mTemperature = (EditText) view.findViewById(R.id.edit_farenheit);
        mTemperature.setFilters(new InputFilter[]{new THSInputFilters(0.0,120.0)});
        mWeight = (EditText) view.findViewById(R.id.ponds);
        mWeight.setFilters(new InputFilter[]{new THSInputFilters(0,500)});
        mContinue = (Button) view.findViewById(R.id.vitals_continue_btn);
        mContinue.setOnClickListener(this);
        mSkipLabel = (Button) view.findViewById(R.id.vitals_skip);
        mSkipLabel.setOnClickListener(this);

        mSystolicInputValidationLayout = (InputValidationLayout) view.findViewById(R.id.intake_systolic_container);
        mSystolicInputValidationLayout.setValidator(new THSVitalsSystolicValidator());

        mDiastolicInputValidationLayout = (InputValidationLayout) view.findViewById(R.id.intake_diasystolic_container);
        mDiastolicInputValidationLayout.setValidator(new THSVitalsSystolicValidator());

        mFarenheitInputLayoutContainer = (InputValidationLayout) view.findViewById(R.id.intake_farenheit_container);
        mFarenheitInputLayoutContainer.setValidator(new THSVitalsTemperatureValidator());

        mWeightInputLayoutContainer = (InputValidationLayout) view.findViewById(R.id.pounds_container);
        mWeightInputLayoutContainer.setValidator(new THSVitalsWeightValidator());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mThsVitalsPresenter = new THSVitalsPresenter(this);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_prepare_your_visit), true);
        }
        try {
            mThsVitalsPresenter.getVitals();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.vitals_continue_btn) {
            THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA,THS_FLOATING_BUTTON,"vitalsContinue");
            mThsVitalsPresenter.onEvent(R.id.vitals_continue_btn);
        } else if (i == R.id.vitals_skip) {
            THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA,THS_FLOATING_BUTTON,"vitalsSkip");
            mThsVitalsPresenter.onEvent(R.id.vitals_skip);
        }
    }

    void setVitalsValues() {
        tagActions="";
        if (mThsVitalsPresenter.isTextValid(mSystolic)) {
            tagActions= THSTagUtils.addActions(tagActions,"bloodPressure");
            mTHSVitals.setSystolic(mThsVitalsPresenter.stringToInteger(mThsVitalsPresenter.getTextFromEditText(mSystolic)));
        }
        if (mThsVitalsPresenter.isTextValid(mDiastolic)) {
            tagActions= THSTagUtils.addActions(tagActions,"bloodPressure");
            mTHSVitals.setDiastolic(mThsVitalsPresenter.stringToInteger(mThsVitalsPresenter.getTextFromEditText(mDiastolic)));
        }
        if (mThsVitalsPresenter.isTextValid(mTemperature)) {
            tagActions= THSTagUtils.addActions(tagActions,"temperature");
            mTHSVitals.setTemperature(mThsVitalsPresenter.stringToDouble(mThsVitalsPresenter.getTextFromEditText(mTemperature)));
        }
        if (mThsVitalsPresenter.isTextValid(mWeight)) {
            tagActions= THSTagUtils.addActions(tagActions,"weight");
            mTHSVitals.setWeight(mThsVitalsPresenter.stringToInteger(mThsVitalsPresenter.getTextFromEditText(mWeight)));
        }
    }

    public void updateUI(THSVitals thsVitals) {
        mTHSVitals = thsVitals;

        if (mTHSVitals.getSystolic() != null)
            mSystolic.setText(mThsVitalsPresenter.integerToString(thsVitals.getSystolic()));
        if (mTHSVitals.getDiastolic() != null)
            mDiastolic.setText(mThsVitalsPresenter.integerToString(thsVitals.getDiastolic()));
        if (mTHSVitals.getTemperature() != null)
            mTemperature.setText(mThsVitalsPresenter.doubleToString(thsVitals.getTemperature()));
        if (mTHSVitals.getWeight() != null)
            mWeight.setText(mThsVitalsPresenter.integerToString(thsVitals.getWeight()));

        mContinue.setEnabled(true);
    }

    public THSVitals getTHSVitals() {
        return mTHSVitals;
    }

    public void setTHSVitals(THSVitals mTHSVitals) {
        this.mTHSVitals = mTHSVitals;
    }

    public boolean validate() {
        String systolic = mThsVitalsPresenter.getTextFromEditText(mSystolic);
        String diastolic = mThsVitalsPresenter.getTextFromEditText(mDiastolic);

        if (!mThsVitalsPresenter.isTextValid(mSystolic)) {
            showToast("Please Enter Valid Systolic Value");
            return false;
        } else if (!mThsVitalsPresenter.isTextValid(mDiastolic)) {
            showToast("Please Enter Valid Systolic Value");
            return false;
        } else if (mThsVitalsPresenter.stringToInteger(diastolic) > mThsVitalsPresenter.stringToInteger(systolic)) {
            showToast("Systolic Value should be higher than daistolic");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_ADD_VITALS_PAGE,null,null);
    }
}
