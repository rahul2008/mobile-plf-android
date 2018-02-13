/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.ths.utility.THSUtilities;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBarButton;
import com.philips.platform.uid.view.widget.RadioButton;
import com.philips.platform.uid.view.widget.RadioGroup;
import com.philips.platform.uid.view.widget.UIPicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_FETCH_STATES;
import static com.philips.platform.ths.utility.THSConstants.THS_ADD_DETAILS;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_ENROLLMENT_MISSING;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SERVER_ERROR;

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
    //private CustomSpinner mStateSpinner;
    protected UIPicker uiPicker;
    private Label anchorUIPicker, dependantEmailAddress;
    protected State mCurrentSelectedState;

    private THSSpinnerAdapter spinnerAdapter;
    private List<State> mValidStates = null;
    private Date mDob;
    private RadioGroup radio_group_single_line;
    protected int mLaunchInput = -1;
    private InputValidationLayout firstNameValidationLayout, lastNameValidationLayout, ths_edit_dob_container, ths_edit_location_container;
    private boolean isLocationValid;
    private RelativeLayout mLocationCantainer;
    private Label mStateLabel;
    static final long serialVersionUID = 127L;

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
        mRelativeLayout = view.findViewById(R.id.registration_form_container);
        mLocationCantainer = view.findViewById(R.id.ths_edit_location_container_layout);
        mStateLabel = view.findViewById(R.id.ths_label);
        mContinueButton = view.findViewById(R.id.ths_continue);
        dependantEmailAddress = view.findViewById(R.id.ths_dependant_email_address);
        mContinueButton.setOnClickListener(this);
        mEditTextFirstName = view.findViewById(R.id.ths_edit_first_name);
        mEditTextFirstName.setOnFocusChangeListener(this);
        mEditTextLastName = view.findViewById(R.id.ths_edit_last_name);
        mEditTextLastName.setOnFocusChangeListener(this);
        mDateOfBirth = view.findViewById(R.id.ths_edit_dob);
        firstNameValidationLayout = view.findViewById(R.id.ths_edit_first_name_container);
        lastNameValidationLayout = view.findViewById(R.id.ths_edit_last_name_container);
        ths_edit_dob_container = view.findViewById(R.id.ths_edit_dob_container);
        ths_edit_location_container = view.findViewById(R.id.ths_edit_location_container);
        mDateOfBirth.setFocusable(false);
        mDateOfBirth.setClickable(true);
        mDateOfBirth.setOnClickListener(this);
        mEditTextStateSpinner = view.findViewById(R.id.ths_edit_location);
        int defaultLocationIcoColor = THSUtilities.getAttributeColor(getContext(), R.attr.uidButtonPrimaryNormalBackgroundColor);
        Drawable drawableInt = THSUtilities.getGpsDrawableFromFontIcon(getContext(), R.string.dls_location, defaultLocationIcoColor, 24);

        mEditTextStateSpinner.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableInt, null);
        radio_group_single_line = view.findViewById(R.id.radio_group_single_line);
        mEditTextStateSpinner.setClickable(true);
        mEditTextStateSpinner.setOnClickListener(this);
        mCheckBoxMale = view.findViewById(R.id.ths_checkbox_male);
        mCheckBoxFemale = view.findViewById(R.id.ths_checkbox_female);
        anchorUIPicker = view.findViewById(R.id.ths_label);


        try {
            final List<Country> supportedCountries = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getSupportedCountries();
            mValidStates = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getConsumerManager().getValidPaymentMethodStates(supportedCountries.get(0));
        } catch (Exception e) {
            final String errorTag = THSTagUtils.createErrorTag(ANALYTICS_FETCH_STATES, e.getMessage());
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SERVER_ERROR,errorTag);
        }

        spinnerAdapter = new THSSpinnerAdapter(getActivity(), R.layout.ths_pharmacy_spinner_layout, mValidStates);


        Context popupThemedContext = UIDHelper.getPopupThemedContext(getContext());
        uiPicker = new UIPicker(popupThemedContext);
        uiPicker.setAdapter(spinnerAdapter);
        uiPicker.setAnchorView(anchorUIPicker);
        uiPicker.setModal(true);
        uiPicker.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mCurrentSelectedState = mValidStates.get(position);
                        mEditTextStateSpinner.setText(mCurrentSelectedState.getName());
                        uiPicker.setSelection(position);
                        uiPicker.dismiss();
                    }
                }
        );
        prePopulateData();

        if(THSManager.getInstance().getThsConsumer(getContext()).isDependent()){
            mStateLabel.setVisibility(View.GONE);
            mLocationCantainer.setVisibility(View.GONE);
        }
    }

    private void prePopulateData() {

        THSConsumer user = THSManager.getInstance().getThsConsumer(getContext());

        if(user.getEmail() != null){
            dependantEmailAddress.setText(user.getEmail());
        }
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
                            mEditTextLastName.getText().toString(), Gender.MALE, mCurrentSelectedState);
                } else {
                    mThsRegistrationPresenter.enrollUser(mDob, mEditTextFirstName.getText().toString(),
                            mEditTextLastName.getText().toString(), Gender.MALE, mCurrentSelectedState);
                }
            }

        }
        if (id == R.id.ths_edit_dob) {
            mThsRegistrationPresenter.onEvent(R.id.ths_edit_dob);
        }
        if (id == R.id.ths_edit_location) {
            uiPicker.show();
            updateUiPickerSelection();
        }
    }

    private void updateUiPickerSelection() {
        if (null != mCurrentSelectedState) {
            int currentStateindex = mValidStates.indexOf(mCurrentSelectedState);
            if (currentStateindex > -1) {
                uiPicker.setSelection(currentStateindex);
            }
        }
    }

    private boolean validateUserDetails() {
        if (validateNameFields()) {
            boolean isValidDOB = mThsRegistrationPresenter.validateDOB(mDob);
            if (isValidDOB) {
                if (ths_edit_dob_container.isShowingError()) {
                    ths_edit_dob_container.hideError();
                }
                if(THSManager.getInstance().getThsConsumer(getContext()).isDependent()){
                    isLocationValid = false;
                }else {
                    isLocationValid = mThsRegistrationPresenter.validateLocation(mEditTextStateSpinner.getText().toString());
                }
                if (isLocationValid) {
                    doTagging(THS_ANALYTICS_ENROLLMENT_MISSING,getString(R.string.ths_registration_location_validation_error),false);
                    ths_edit_location_container.setErrorMessage(R.string.ths_registration_location_validation_error);
                    ths_edit_location_container.showError();
                    return false;
                } else {
                    if (ths_edit_location_container.isShowingError()) {
                        ths_edit_location_container.hideError();
                    }
                    return true;
                }
            } else {
                doTagging(THS_ANALYTICS_ENROLLMENT_MISSING,getString(R.string.ths_registration_dob_validation_error),false);
                ths_edit_dob_container.setErrorMessage(R.string.ths_registration_dob_validation_error);
                ths_edit_dob_container.showError();
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
        THSTagUtils.doTrackPageWithInfo(THS_ADD_DETAILS, null, null);
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
        if (!mThsRegistrationPresenter.validateName(mEditTextLastName.getText().toString(), false)) {
            setInLineErrorMessageLastName();
            setInLineErrorVisibilityLN(true);
        } else {
            setInLineErrorVisibilityLN(false);
        }
    }

    public void validateFirstNameField() {
        if (!mThsRegistrationPresenter.validateName(mEditTextFirstName.getText().toString(), true)) {
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

    @Override
    public boolean handleBackEvent() {
        THSTagUtils.doExitToPropositionWithCallBack();
        return true;
    }

}
