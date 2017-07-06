package com.philips.amwelluapp.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.utility.PTHConstants;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;

public class THSVitalsFragment extends PTHBaseFragment implements BackEventListener, View.OnClickListener {

    public static final String TAG = THSVitalsFragment.class.getSimpleName();
    THSVitalsPresenter mThsVitalsPresenter;
    EditText mSystolic;
    EditText mDiastolic;
    EditText mTemperature;
    EditText mWeight;
    Button mContinue;
    THSVitals mTHSVitals;
    Label mSkipLabel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.pth_intake_vitals, container, false);
        mSystolic = (EditText) view.findViewById(R.id.systolic);
        mDiastolic = (EditText) view.findViewById(R.id.diastolic);
        mTemperature = (EditText) view.findViewById(R.id.edit_farenheit);
        mWeight = (EditText) view.findViewById(R.id.ponds);
        mContinue = (Button) view.findViewById(R.id.vitals_continue_btn);
        mContinue.setOnClickListener(this);
        mSkipLabel = (Label) view.findViewById(R.id.vitals_skip);
        mSkipLabel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mThsVitalsPresenter = new THSVitalsPresenter(this);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.pth_prepare_your_visit), true);
        }
        try {
            mThsVitalsPresenter.getVitals();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup) getView().getParent()).getId();
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
        mTHSVitals.setSystolic(Integer.parseInt(mSystolic.getText().toString()));
        mTHSVitals.setDiastolic(Integer.parseInt(mDiastolic.getText().toString()));
        mTHSVitals.setTemperature(Double.parseDouble(mTemperature.getText().toString()));
        mTHSVitals.setWeight(Integer.parseInt(mWeight.getText().toString()));
    }

    public void updateUI(THSVitals thsVitals) {
        mTHSVitals = thsVitals;
        if (mTHSVitals.getDiastolic() != null)
            mSystolic.setText(String.valueOf(thsVitals.getSystolic()));
        if (mTHSVitals.getDiastolic() != null)
            mDiastolic.setText(String.valueOf(thsVitals.getDiastolic()));
        if (mTHSVitals.getTemperature() != null)
            mTemperature.setText(String.valueOf(thsVitals.getTemperature()));
        if (mTHSVitals.getWeight() != null)
            mWeight.setText(String.valueOf(thsVitals.getWeight()));

        mContinue.setEnabled(true);
    }

    public THSVitals getTHSVitals() {
        return mTHSVitals;
    }

    public void setTHSVitals(THSVitals mTHSVitals) {
        this.mTHSVitals = mTHSVitals;
    }
}
