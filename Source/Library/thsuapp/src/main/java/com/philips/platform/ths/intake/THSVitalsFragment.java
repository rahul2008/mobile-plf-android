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
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.R.id.systolic;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_UPDATE_VITALS;
import static com.philips.platform.ths.utility.THSConstants.THS_ADD_VITALS_PAGE;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;

public class THSVitalsFragment extends THSBaseFragment implements View.OnClickListener, THSVItalsUIInterface, View.OnFocusChangeListener {

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
        mSystolic = (EditText) view.findViewById(systolic);
        mDiastolic = (EditText) view.findViewById(R.id.diastolic);
        mTemperature = (EditText) view.findViewById(R.id.edit_farenheit);
        mSystolic.setOnFocusChangeListener(this);
        mDiastolic.setOnFocusChangeListener(this);
        mTemperature.setOnFocusChangeListener(this);
        THSInputFilters<Double> thsInputFilters = new THSInputFilters<>(0.0, 120.0);
        THSInputFilters<Integer> thsInputFiltersInt = new THSInputFilters<>(0, 500);
        mWeight = (EditText) view.findViewById(R.id.ponds);
        mWeight.setFilters(new InputFilter[]{thsInputFiltersInt});
        mContinue = (Button) view.findViewById(R.id.vitals_continue_btn);
        mContinue.setOnClickListener(this);
        Button mSkipLabel = (Button) view.findViewById(R.id.vitals_skip);
        mSkipLabel.setOnClickListener(this);

        mLabelPatientName = (Label) view.findViewById(R.id.ths_vitals_patient_name);
        String name = getString(R.string.ths_dependent_name, THSManager.getInstance().getThsConsumer(getContext()).getFirstName());
        mLabelPatientName.setText(name);

        mSystolicInputValidationLayout = (InputValidationLayout) view.findViewById(R.id.intake_systolic_container);
        mSystolicInputValidationLayout.setValidator(new THSVitalsSystolicValidator());

        mDiastolicInputValidationLayout = (InputValidationLayout) view.findViewById(R.id.intake_diasystolic_container);
        mDiastolicInputValidationLayout.setValidator(new THSVitalsSystolicValidator());

        mFarenheitInputLayoutContainer = (InputValidationLayout) view.findViewById(R.id.intake_farenheit_container);
        mFarenheitInputLayoutContainer.setValidator(new THSVitalsTemperatureValidator());

        mWeightInputLayoutContainer = (InputValidationLayout) view.findViewById(R.id.pounds_container);
        mWeightInputLayoutContainer.setValidator(new THSVitalsWeightValidator());

        mThsVitalsPresenter = new THSVitalsPresenter(this,this);
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
                e.printStackTrace();
            }
        } else {
            mContinue.setEnabled(true);
        }
    }

    private void prepopulateData() {
        final THSConsumer thsConsumer = THSManager.getInstance().getThsConsumer(getContext());
        if(thsConsumer==null){
            return;
        }

        if (thsConsumer.getBloodPressureSystolic() != null) {
            mSystolic.setText(thsConsumer.getBloodPressureSystolic());
        }
        if (thsConsumer.getBloodPressureDiastolic() != null) {
            mDiastolic.setText(thsConsumer.getBloodPressureDiastolic());
        }
        mWeight.setText(String.valueOf(thsConsumer.getWeight()));
        mTemperature.setText(String.valueOf(thsConsumer.getTemperature()));
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if(validateFields()) {
            if (i == R.id.vitals_continue_btn) {
                //THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, THS_FLOATING_BUTTON, "vitalsContinue");
                mThsVitalsPresenter.onEvent(R.id.vitals_continue_btn);
            } else if (i == R.id.vitals_skip) {
                THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, "stepsSkipped", "vitals");
                mThsVitalsPresenter.onEvent(R.id.vitals_skip);
            }
        }
    }


    @Override
    public boolean validate() {
        boolean enableContinue;
        if (mThsVitalsPresenter.checkIfValueEntered(mTemperature) || mThsVitalsPresenter.checkIfValueEntered(mWeight)) {
            enableContinue = !mThsVitalsPresenter.checkIfValueEntered(mSystolic) && !mThsVitalsPresenter.checkIfValueEntered(mDiastolic) || validateBloodPressure();

        } else if (validateBloodPressure()) {
            enableContinue = true;
        } else {
            showToast("Please enter the values");
            enableContinue = false;
        }
        return enableContinue;
    }

    @Override
    public void updateUI(THSVitals thsVitals) {
        mTHSVitals = thsVitals;

        if (mTHSVitals.getSystolic() != null)
            mSystolic.setText(String.valueOf(thsVitals.getSystolic()));
        if (mTHSVitals.getDiastolic() != null)
            mDiastolic.setText(String.valueOf(thsVitals.getDiastolic()));
        if (mTHSVitals.getTemperature() != null)
            mTemperature.setText(String.valueOf(thsVitals.getTemperature()));
        if (mTHSVitals.getWeight() != null)
            mWeight.setText(String.valueOf(thsVitals.getWeight()));

        mContinue.setEnabled(true);
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
            THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, "step2VitalsForVisit", tagActions);
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


    public boolean validateBloodPressure() {

        if (mThsVitalsPresenter.checkIfValueEntered(mSystolic) || mThsVitalsPresenter.checkIfValueEntered(mDiastolic)) {
            String systolic = mThsVitalsPresenter.getTextFromEditText(mSystolic);
            String diastolic = mThsVitalsPresenter.getTextFromEditText(mDiastolic);
            return mThsVitalsPresenter.stringToInteger(diastolic) <= mThsVitalsPresenter.stringToInteger(systolic);

        } else {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        tagActions = "";
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_ADD_VITALS_PAGE, null, null);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        validateFields();
    }

    public boolean validateFields() {
        validateSystolicView();
        validateDiastolicView();
        validateTemperatureView();
        if(mFarenheitInputLayoutContainer.isShowingError() || mSystolicInputValidationLayout.isShowingError() || mDiastolicInputValidationLayout.isShowingError()){
            return false;
        }else {
            return true;
        }
    }

    public void validateTemperatureView() {
        if(!validateTemperature()){
            mFarenheitInputLayoutContainer.setErrorMessage(R.string.ths_vitals_temperature_error);
            mFarenheitInputLayoutContainer.showError();
            doTagging(ANALYTICS_UPDATE_VITALS,getString(R.string.ths_vitals_temperature_error),false);
        }else {
            mFarenheitInputLayoutContainer.hideError();
        }
    }

    public void validateDiastolicView() {
        if(!validateBloodPressure()){
            mDiastolicInputValidationLayout.setErrorMessage(R.string.ths_vitals_diastolic_error);
            mDiastolicInputValidationLayout.showError();
            doTagging(ANALYTICS_UPDATE_VITALS,getString(R.string.ths_vitals_diastolic_error),false);
        }else {
            mDiastolicInputValidationLayout.hideError();
        }
    }

    public void validateSystolicView() {
        if(!validateBloodPressure()){
            mSystolicInputValidationLayout.setErrorMessage(R.string.ths_vitals_diastolic_error);
            mSystolicInputValidationLayout.showError();
            doTagging(ANALYTICS_UPDATE_VITALS,getString(R.string.ths_vitals_diastolic_error),false);
        }else {
            mSystolicInputValidationLayout.hideError();
        }
    }

    private boolean validateTemperature() {
        if(mTemperature.getText().toString().length() <= 0 || mTemperature.getText().toString().isEmpty()){
            return false;
        }else return Double.parseDouble(mTemperature.getText().toString()) <= 120;
    }
}
