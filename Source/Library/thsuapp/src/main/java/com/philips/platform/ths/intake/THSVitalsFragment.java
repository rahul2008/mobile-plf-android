package com.philips.platform.ths.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.Label;

public class THSVitalsFragment extends THSBaseFragment implements BackEventListener, View.OnClickListener {

    public static final String TAG = THSVitalsFragment.class.getSimpleName();
    THSVitalsPresenter mThsVitalsPresenter;
    EditText mSystolic;
    EditText mDiastolic;
    EditText mTemperature;
    EditText mWeight;
    Button mContinue;
    THSVitals mTHSVitals;
    Label mSkipLabel;
    InputValidationLayout mSystolicInputValidationLayout;
    InputValidationLayout mDiastolicInputValidationLayout;
    InputValidationLayout mFarenheitInputLayoutContainer;
    InputValidationLayout mWeightInputLayoutContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_intake_vitals, container, false);
        mSystolic = (EditText) view.findViewById(R.id.systolic);
        mDiastolic = (EditText) view.findViewById(R.id.diastolic);
        mTemperature = (EditText) view.findViewById(R.id.edit_farenheit);
        mWeight = (EditText) view.findViewById(R.id.ponds);
        mContinue = (Button) view.findViewById(R.id.vitals_continue_btn);
        mContinue.setOnClickListener(this);
        mSkipLabel = (Label) view.findViewById(R.id.vitals_skip);
        mSkipLabel.setOnClickListener(this);
        mSystolicInputValidationLayout = (InputValidationLayout) view.findViewById(R.id.intake_systolic_container);
        mSystolicInputValidationLayout.setValidator(new THSVitalsSystolicValidator());

        mDiastolicInputValidationLayout = (InputValidationLayout) view.findViewById(R.id.intake_diastolic_container);
        mDiastolicInputValidationLayout.setValidator(new THSVitalsSystolicValidator());

        mFarenheitInputLayoutContainer = (InputValidationLayout) view.findViewById(R.id.intake_farenheit_container);
        mFarenheitInputLayoutContainer.setValidator(new THSVitalsTemperatureValidator());

        mWeightInputLayoutContainer = (InputValidationLayout) view.findViewById(R.id.ponds_container);
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
    public boolean handleBackEvent() {
        return false;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.vitals_continue_btn) {
            mThsVitalsPresenter.onEvent(R.id.vitals_continue_btn);
        }else if(i==R.id.vitals_skip){
            mThsVitalsPresenter.onEvent(R.id.vitals_skip);
        }
    }

    void setVitalsValues() {
        if (mThsVitalsPresenter.isTextValid(mSystolic)) {
            mTHSVitals.setSystolic(mThsVitalsPresenter.stringToInteger(mThsVitalsPresenter.getTextFromEditText(mSystolic)));
        }
        if (mThsVitalsPresenter.isTextValid(mDiastolic)) {
            mTHSVitals.setDiastolic(mThsVitalsPresenter.stringToInteger(mThsVitalsPresenter.getTextFromEditText(mDiastolic)));
        }
        if (mThsVitalsPresenter.isTextValid(mTemperature)) {
            mTHSVitals.setTemperature(mThsVitalsPresenter.stringToDouble(mThsVitalsPresenter.getTextFromEditText(mTemperature)));
        }
        if (mThsVitalsPresenter.isTextValid(mWeight)) {
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

    public boolean validate(){
        String systolic = mThsVitalsPresenter.getTextFromEditText(mSystolic);
        String diastolic = mThsVitalsPresenter.getTextFromEditText(mDiastolic);

        if(!mThsVitalsPresenter.isTextValid(mSystolic)){
            showToast("Please Enter Valid Systolic Value");
            return false;
        }else if(!mThsVitalsPresenter.isTextValid(mDiastolic)){
            showToast("Please Enter Valid Systolic Value");
            return false;
        }else if(mThsVitalsPresenter.stringToInteger(diastolic)>mThsVitalsPresenter.stringToInteger(systolic)){
            showToast("Systolic Value should be higher than daistolic");
            return false;
        }else {
            return true;
        }
    }
}
