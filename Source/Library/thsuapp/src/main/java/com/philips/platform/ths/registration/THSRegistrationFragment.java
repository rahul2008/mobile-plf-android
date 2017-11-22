/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Country;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.pharmacy.THSSpinnerAdapter;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.ProgressBarButton;
import com.philips.platform.uid.view.widget.RadioButton;
import com.philips.platform.uid.view.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.philips.platform.ths.utility.THSConstants.THS_ADD_DETAILS;

public class THSRegistrationFragment extends THSBaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {
    public static final String TAG = THSRegistrationFragment.class.getSimpleName();
    private THSRegistrationPresenter mThsRegistrationPresenter;
    private RelativeLayout mRelativeLayout;
    protected ProgressBarButton mContinueButton;
    private EditText mEditTextFirstName;
    private EditText mEditTextLastName;
    private EditText mDateOfBirth;
    private EditText mEditTextStateSpinner;
    private RadioButton mCheckBoxMale;
    private RadioButton mCheckBoxFemale;
    private CustomSpinner mStateSpinner;
    private THSSpinnerAdapter spinnerAdapter;
    private List<State> mValidStates = null;
    private Date mDob;
    private RadioGroup radio_group_single_line;
    protected int mLaunchInput = -1;
    private InputValidationLayout firstNameValidationLayout, lastNameValidationLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_registration_form, container, false);

        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_your_details), true);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            mLaunchInput = bundle.getInt(THSConstants.THS_LAUNCH_INPUT, -1);
        }
        mThsRegistrationPresenter = new THSRegistrationPresenter(this);
        setView(view);
        return view;
    }

    private void setView(ViewGroup view) {
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.registration_form_container);
        mContinueButton = (ProgressBarButton) view.findViewById(R.id.ths_continue);
        mContinueButton.setOnClickListener(this);
        mEditTextFirstName = (EditText) view.findViewById(R.id.ths_edit_first_name);
        mEditTextFirstName.setOnFocusChangeListener(this);
        mEditTextLastName = (EditText) view.findViewById(R.id.ths_edit_last_name);
        mEditTextLastName.setOnFocusChangeListener(this);
        mDateOfBirth = (EditText) view.findViewById(R.id.ths_edit_dob);
        firstNameValidationLayout = (InputValidationLayout) view.findViewById(R.id.ths_edit_first_name_container);
        lastNameValidationLayout = (InputValidationLayout) view.findViewById(R.id.ths_edit_last_name_container);
        mDateOfBirth.setFocusable(false);
        mDateOfBirth.setClickable(true);
        mDateOfBirth.setOnClickListener(this);
        mEditTextStateSpinner = (EditText) view.findViewById(R.id.ths_edit_location_container);
        radio_group_single_line = (RadioGroup) view.findViewById(R.id.radio_group_single_line);
        mEditTextStateSpinner.setFocusable(false);
        mEditTextStateSpinner.setClickable(true);
        mEditTextStateSpinner.setOnClickListener(this);
        mCheckBoxMale = (RadioButton) view.findViewById(R.id.ths_checkbox_male);
        mCheckBoxFemale = (RadioButton) view.findViewById(R.id.ths_checkbox_female);
        mStateSpinner = new CustomSpinner(getContext(), null);

        try {
            final List<Country> supportedCountries = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getSupportedCountries();
            mValidStates = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getConsumerManager().getValidPaymentMethodStates(supportedCountries.get(0));
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

        spinnerAdapter = new THSSpinnerAdapter(getActivity(), R.layout.ths_pharmacy_spinner_layout, mValidStates);
        mStateSpinner.setAdapter(spinnerAdapter);
        mStateSpinner.setSelection(0);
        mStateSpinner.setOnItemSelectedEvenIfUnchangedListener(this);
        prePopulateData();
    }

    private void prePopulateData() {

        THSConsumer user = THSManager.getInstance().getThsConsumer(getContext());
        if (user.getFirstName() != null) {
            mEditTextFirstName.setText(user.getFirstName());
        }
        if (user.getLastName() != null) {
            mEditTextLastName.setText(user.getLastName());
        }
        if (user.getDob() != null) {
            mDob = user.getDob();
            setDate(user.getDob());
        }
        final com.philips.cdp.registration.ui.utils.Gender gender = user.getGender();
        if (gender == null)
            return;
        if (gender == com.philips.cdp.registration.ui.utils.Gender.FEMALE) {
            mCheckBoxFemale.setSelected(true);
            mCheckBoxFemale.setChecked(true);
        } else {
            mCheckBoxMale.setSelected(true);
            mCheckBoxMale.setChecked(true);
        }

    }

    protected boolean validateNameFields() {
        validateFirstNameField();
        validateLastNameField();
        return !(firstNameValidationLayout.isShowingError() || lastNameValidationLayout.isShowingError());
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
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ths_continue) {
            if (validateUserDetails()) {

                mContinueButton.showProgressIndicator();

                if (THSManager.getInstance().getThsConsumer(getContext()).isDependent()) {
                    mThsRegistrationPresenter.enrollDependent(mDob, mEditTextFirstName.getText().toString(),
                            mEditTextLastName.getText().toString(), Gender.MALE, mValidStates.get(mStateSpinner.getSelectedItemPosition()));
                } else {
                    mThsRegistrationPresenter.enrollUser(mDob, mEditTextFirstName.getText().toString(),
                            mEditTextLastName.getText().toString(), Gender.MALE, mValidStates.get(mStateSpinner.getSelectedItemPosition()));
                }
            }

        }
        if (id == R.id.ths_edit_dob) {
            mThsRegistrationPresenter.onEvent(R.id.ths_edit_dob);
        }
        if (id == R.id.ths_edit_location_container) {
            mStateSpinner.performClick();
        }
    }

    private boolean validateUserDetails() {
        if (validateNameFields()) {
            if (mThsRegistrationPresenter.validateDOB(mDob)) {
                if (mThsRegistrationPresenter.validateLocation(mEditTextStateSpinner.getText().toString())) {
                    showError(getString(R.string.ths_registration_location_validation_error));
                    return false;
                } else {
                    return true;
                }
            } else {
                showError(getString(R.string.ths_registration_dob_validation_error));
                return false;
            }
        } else {
            return false;
        }
    }

    public void updateDobView(Date date) {
        mDob = date;
        setDate(date);
    }

    private void setDate(Date date) {
        mDateOfBirth.setText(new SimpleDateFormat(THSConstants.DATE_FORMATTER, Locale.getDefault()).
                format(date));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mEditTextStateSpinner.setText(mValidStates.get(i).getName());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onResume() {
        super.onResume();
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_ADD_DETAILS, null, null);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (view.getId() == R.id.ths_edit_first_name && !hasFocus) {
            validateFirstNameField();
        } else if (view.getId() == R.id.ths_edit_last_name && !hasFocus) {
            validateLastNameField();
        }
    }

    public void validateLastNameField() {
        if (!mThsRegistrationPresenter.validateName(mEditTextLastName.getText().toString())) {
            setInLineErrorMessageLastName();
            setInLineErrorVisibilityLN(true);
        } else {
            setInLineErrorVisibilityLN(false);
        }
    }

    public void validateFirstNameField() {
        if (!mThsRegistrationPresenter.validateName(mEditTextFirstName.getText().toString())) {
            setInLineErrorMessageFirstName();
            setInLineErrorVisibilityFN(true);
        } else {
            setInLineErrorVisibilityFN(false);
        }
    }

    private String errorString = "";

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public void setInLineErrorMessageFirstName() {
        firstNameValidationLayout.setErrorMessage(errorString);
    }

    public void setInLineErrorVisibilityFN(boolean show) {
        if (show) {
            firstNameValidationLayout.showError();
        } else {
            firstNameValidationLayout.hideError();
        }
    }

    public void setInLineErrorMessageLastName() {
        lastNameValidationLayout.setErrorMessage(errorString);
    }

    public void setInLineErrorVisibilityLN(boolean show) {
        if (show) {
            lastNameValidationLayout.showError();
        } else {
            lastNameValidationLayout.hideError();
        }
    }

}
