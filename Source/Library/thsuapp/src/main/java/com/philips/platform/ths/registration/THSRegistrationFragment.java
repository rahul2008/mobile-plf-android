package com.philips.platform.ths.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Country;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.pharmacy.THSSpinnerAdapter;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class THSRegistrationFragment extends THSBaseFragment implements BackEventListener,View.OnClickListener {
    public static final String TAG = THSRegistrationFragment.class.getSimpleName();
    THSRegistrationPresenter mThsRegistrationPresenter;
    private RelativeLayout mRelativeLayout;
    private Button mContinueButton;
    EditText mEditTextFirstName;
    EditText mEditTextLastName;
    Label mDateOfBirth;
    CheckBox mCheckBoxMale;
    CheckBox mCheckBoxFemale;
    AppCompatSpinner mEditTextState;
    private THSSpinnerAdapter spinnerAdapter;
    List<State> mValidStates = null;
    private Date mDob;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.registration_form, container, false);
        setView(view);
        getActionBarListener().updateActionBar("Registration form", false);
        mThsRegistrationPresenter = new THSRegistrationPresenter(this);
        return view;
    }

    private void setView(ViewGroup view) {
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.registration_form_container);
        mContinueButton = (Button) view.findViewById(R.id.ths_continue);
        mContinueButton.setOnClickListener(this);
        mEditTextFirstName = (EditText) view.findViewById(R.id.ths_edit_first_name);
        mEditTextLastName = (EditText) view.findViewById(R.id.ths_edit_last_name);
        mDateOfBirth = (Label) view.findViewById(R.id.ths_edit_dob);
        mDateOfBirth.setOnClickListener(this);
        mCheckBoxMale = (CheckBox) view.findViewById(R.id.ths_checkbox_male);
        mCheckBoxFemale = (CheckBox) view.findViewById(R.id.ths_checkbox_female);
        mEditTextState = (AppCompatSpinner) view.findViewById(R.id.ths_edit_location);

        try {
            final List<Country> supportedCountries = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getSupportedCountries();
            mValidStates = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getConsumerManager().getValidPaymentMethodStates(supportedCountries.get(0));
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }


        spinnerAdapter = new THSSpinnerAdapter(getActivity(), R.layout.ths_pharmacy_spinner_layout, mValidStates);
        mEditTextState.setAdapter(spinnerAdapter);
        mEditTextState.setSelection(0);
    }

    @Override
    public void finishActivityAffinity() {
        getActivity().finishAffinity();
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }


    @Override
    public boolean handleBackEvent() {
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.ths_continue){
            mThsRegistrationPresenter.enrollUser(mDob,mEditTextFirstName.getText().toString(),
                    mEditTextLastName.getText().toString(), Gender.MALE,mValidStates.get(mEditTextState.getSelectedItemPosition()));
        }if(id == R.id.ths_edit_dob){
            mThsRegistrationPresenter.onEvent(R.id.ths_edit_dob);
        }
    }

    public void updateDobView(Date date) {
        mDob = date;
        mDateOfBirth.setText(new SimpleDateFormat(THSConstants.DATE_FORMATTER, Locale.getDefault()).
                format(date));
    }
}
