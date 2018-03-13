/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.R.id.systolic;
import static com.philips.platform.ths.utility.THSConstants.THS_ADD_VITALS_PAGE;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_SYSTOLIC_VALIDATION;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_TEMPERATURE_VALIDATION;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_WEIGHT_VALIDATION;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
@SuppressWarnings("serial")
public class THSVitalsFragment extends THSBaseFragment implements View.OnClickListener, THSVItalsUIInterface, View.OnFocusChangeListener, TextWatcher {

    public static final String TAG = THSVitalsFragment.class.getSimpleName();
    protected THSVitalsPresenter mThsVitalsPresenter;
    protected EditText mSystolic;
    protected EditText mDiastolic;
    protected EditText mTemperature;
    protected EditText mWeight;
    protected Button mContinue;
    private THSVitals mTHSVitals;
    private Label mLabelPatientName;
    String tagActions = "";
    InputValidationLayout mSystolicInputValidationLayout, mDiastolicInputValidationLayout, mFarenheitInputLayoutContainer, mWeightInputLayoutContainer;

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_intake_vitals, container, false);
        mSystolic = view.findViewById(systolic);
        mSystolic.setOnFocusChangeListener(this);
        mDiastolic = view.findViewById(R.id.diastolic);
        mDiastolic.setOnFocusChangeListener(this);
        mTemperature = view.findViewById(R.id.edit_farenheit);
        THSInputFilters<Integer> thsInputFiltersInt = new THSInputFilters<>(0, 500);
        mWeight = view.findViewById(R.id.ponds);
        mWeight.setFilters(new InputFilter[]{thsInputFiltersInt});
        mContinue = view.findViewById(R.id.vitals_continue_btn);
        mContinue.setOnClickListener(this);
        Button mSkipLabel = view.findViewById(R.id.vitals_skip);
        mSkipLabel.setOnClickListener(this);

        mSystolic.addTextChangedListener(this);
        mDiastolic.addTextChangedListener(this);
        mTemperature.addTextChangedListener(this);
        mWeight.addTextChangedListener(this);
        mLabelPatientName = view.findViewById(R.id.ths_vitals_patient_name);
        String name = getString(R.string.ths_dependent_name, THSManager.getInstance().getThsConsumer(getContext()).getFirstName());
        mLabelPatientName.setText(name);

        mSystolicInputValidationLayout = view.findViewById(R.id.intake_systolic_container);

        mDiastolicInputValidationLayout = view.findViewById(R.id.intake_diasystolic_container);

        mFarenheitInputLayoutContainer = view.findViewById(R.id.intake_farenheit_container);

        mWeightInputLayoutContainer = view.findViewById(R.id.pounds_container);

        mThsVitalsPresenter = new THSVitalsPresenter(this, this);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_prepare_your_visit), true);
        }
        getVitals();

        prepopulateData();

        return view;
    }

    private void getVitals() {
        if (getTHSVitals() == null) {
            mContinue.setEnabled(false);
            try {
                mThsVitalsPresenter.getVitals();
            } catch (AWSDKInstantiationException e) {

            }
        } else {
            mContinue.setEnabled(true);
        }
    }

    private void prepopulateData() {
        final THSConsumer thsConsumer = THSManager.getInstance().getThsConsumer(getContext());
        if (thsConsumer == null) {
            return;
        }

        if (thsConsumer.getBloodPressureSystolic() != null) {
            mSystolic.setText(thsConsumer.getBloodPressureSystolic());
        }
        if (thsConsumer.getBloodPressureDiastolic() != null) {
            mDiastolic.setText(thsConsumer.getBloodPressureDiastolic());
        }
        if (thsConsumer.getWeight() > 0) {
            mWeight.setText(String.valueOf(thsConsumer.getWeight()));
        }
        if (thsConsumer.getTemperature() > 0) {
            mTemperature.setText(String.valueOf(thsConsumer.getTemperature()));
        }
        setContinueButtonState();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.vitals_continue_btn) {
            if (validateFields()) {
                mThsVitalsPresenter.onEvent(R.id.vitals_continue_btn);
            }
        } else if (i == R.id.vitals_skip) {
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, "stepsSkipped", "vitals");
            mThsVitalsPresenter.onEvent(R.id.vitals_skip);
        }

    }

    @Override
    public void updateUI(THSVitals thsVitals) {
        mTHSVitals = thsVitals;

        if (mTHSVitals.getSystolic() != null && mTHSVitals.getSystolic() != 0) {
            mSystolic.setText(String.valueOf(thsVitals.getSystolic()));
        }
        if (mTHSVitals.getDiastolic() != null && mTHSVitals.getDiastolic() != 0) {
            mDiastolic.setText(String.valueOf(thsVitals.getDiastolic()));
        }
        if (mTHSVitals.getTemperature() != null && mTHSVitals.getTemperature() != 0) {
            mTemperature.setText(String.valueOf(thsVitals.getTemperature()));
        }
        if (mTHSVitals.getWeight() != null && mTHSVitals.getWeight() != 0) {
            mWeight.setText(String.valueOf(thsVitals.getWeight()));
        }
        setContinueButtonState();

    }

    private void setContinueButtonState() {
        if (!mThsVitalsPresenter.isTextValid(mSystolic) && !mThsVitalsPresenter.isTextValid(mDiastolic)
                && !mThsVitalsPresenter.isTextValid(mWeight) && !mThsVitalsPresenter.isTextValid(mTemperature)) {
            mContinue.setEnabled(false);
        } else if(validateFields()){
            mContinue.setEnabled(true);
        } else {
            mContinue.setEnabled(false);
        }
    }

    @Override
    public void updateVitalsData() {
        mTHSVitals.setSystolic(mThsVitalsPresenter.stringToInteger(mThsVitalsPresenter.getTextFromEditText(mSystolic)));
        if (mTHSVitals.getSystolic() > 0) {
            tagActions = THSTagUtils.addActions(tagActions, "bloodPressure");
        }
        mTHSVitals.setDiastolic(mThsVitalsPresenter.stringToInteger(mThsVitalsPresenter.getTextFromEditText(mDiastolic)));
        if (mTHSVitals.getDiastolic() > 0) {
            tagActions = THSTagUtils.addActions(tagActions, "bloodPressure");
        }
        mTHSVitals.setTemperature(mThsVitalsPresenter.stringToDouble(mThsVitalsPresenter.getTextFromEditText(mTemperature)));
        if (mTHSVitals.getTemperature() > 0.0d) {
            tagActions = THSTagUtils.addActions(tagActions, "temperature");
        }
        mTHSVitals.setWeight(mThsVitalsPresenter.stringToInteger(mThsVitalsPresenter.getTextFromEditText(mWeight)));
        if (mTHSVitals.getWeight() > 0) {
            tagActions = THSTagUtils.addActions(tagActions, "weight");
        }
        if (!tagActions.isEmpty()) {
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, "step2VitalsForVisit", tagActions);
        }
    }

    @Override
    public void launchMedicationFragment() {
        THSMedicationFragment fragment = new THSMedicationFragment();
        addFragment(fragment, THSMedicationFragment.TAG, null, true);
    }

    @Override
    public THSVitals getTHSVitals() {
        return mTHSVitals;
    }

    public void setTHSVitals(THSVitals mTHSVitals) {
        this.mTHSVitals = mTHSVitals;
    }

    @Override
    public void onResume() {
        super.onResume();
        tagActions = "";
        THSTagUtils.doTrackPageWithInfo(THS_ADD_VITALS_PAGE, null, null);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        validateFields();
    }

    public boolean validateFields() {
        return (validateBPField() && validateTmpField() && validateWeightField());
    }

    private boolean validateWeightField() {

        if (mThsVitalsPresenter.checkIfValueEntered(mWeight)) {
            validateWeightView();
            return !(mWeightInputLayoutContainer.isShowingError());
        } else {
            if (mWeightInputLayoutContainer.isShowingError()) {
                mWeightInputLayoutContainer.hideError();
                AmwellLog.v("","Testing");

            }
            return true;
        }
    }

    public boolean validateBPField() {
        if (mThsVitalsPresenter.checkIfValueEntered(mSystolic) && mThsVitalsPresenter.checkIfValueEntered(mDiastolic)) {
            validateDiastolicView();
            return !(mSystolicInputValidationLayout.isShowingError() || mDiastolicInputValidationLayout.isShowingError());
        } else if (mThsVitalsPresenter.checkIfValueEntered(mSystolic) || mThsVitalsPresenter.checkIfValueEntered(mDiastolic)) {
            mDiastolicInputValidationLayout.setErrorMessage(getString(R.string.ths_vitals_enter_bp_field_error));
            mDiastolicInputValidationLayout.showError();
            doTagging(THS_ANALYTICS_SYSTOLIC_VALIDATION, getString(R.string.ths_vitals_enter_bp_field_error), false);
            return false;
        } else {
            if (mSystolicInputValidationLayout.isShowingError()) {
                mSystolicInputValidationLayout.hideError();
            }
            if (mDiastolicInputValidationLayout.isShowingError()) {
                mDiastolicInputValidationLayout.hideError();
            }
            return true;
        }
    }

    private boolean validateTmpField() {
        if (mThsVitalsPresenter.checkIfValueEntered(mTemperature)) {
            validateTemperatureView();
            return !mFarenheitInputLayoutContainer.isShowingError();
        } else {
            if (mFarenheitInputLayoutContainer.isShowingError()) {
                mFarenheitInputLayoutContainer.hideError();
            }
            return true;
        }
    }

    public void validateTemperatureView() {
        if (!validateTemperature()) {
            mFarenheitInputLayoutContainer.setErrorMessage(R.string.ths_vitals_temperature_error);
            mFarenheitInputLayoutContainer.showError();
            doTagging(THS_ANALYTICS_TEMPERATURE_VALIDATION, getString(R.string.ths_vitals_temperature_error), false);
        } else {
            mFarenheitInputLayoutContainer.hideError();
        }
    }


    public boolean validateTemperature() {
        String temp = mTemperature.getText().toString();
        if (temp.length() <= 0 || temp.isEmpty()) {
            return false;
        } else {
            return (Double.parseDouble(temp) >= 60 && Double.parseDouble(temp) <= 120);
        }
    }

    public void validateWeightView() {
        if (!validateWeight()) {
            mWeightInputLayoutContainer.setErrorMessage(R.string.ths_vitals_weight_error);
            mWeightInputLayoutContainer.showError();
            doTagging(THS_ANALYTICS_WEIGHT_VALIDATION, getString(R.string.ths_vitals_weight_error), false);
        } else {
            mWeightInputLayoutContainer.hideError();
        }
    }

    private boolean validateWeight() {
        String weight = mWeight.getText().toString();
        if (weight.length() <= 0 || weight.isEmpty()) {
            return false;
        } else {
            return (Double.parseDouble(weight) <= 500 && Double.parseDouble(weight) >= 0);
        }
    }

    public void validateDiastolicView() {

        String systolic = mThsVitalsPresenter.getTextFromEditText(mSystolic);
        String diastolic = mThsVitalsPresenter.getTextFromEditText(mDiastolic);
        if (mThsVitalsPresenter.stringToInteger(systolic) > 250 || mThsVitalsPresenter.stringToInteger(diastolic) > 250) {
            mDiastolicInputValidationLayout.setErrorMessage(R.string.ths_vitals_diastolic_error_range);
            mDiastolicInputValidationLayout.showError();
            doTagging(THS_ANALYTICS_SYSTOLIC_VALIDATION, getString(R.string.ths_vitals_diastolic_error_range), false);
        } else if (mThsVitalsPresenter.stringToInteger(systolic) <= mThsVitalsPresenter.stringToInteger(diastolic)) {
            mDiastolicInputValidationLayout.setErrorMessage(R.string.ths_vitals_diastolic_error);
            mDiastolicInputValidationLayout.showError();
            doTagging(THS_ANALYTICS_SYSTOLIC_VALIDATION, getString(R.string.ths_vitals_diastolic_error), false);
        } else {
            mDiastolicInputValidationLayout.hideError();
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        setContinueButtonState();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
