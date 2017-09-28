/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.devicepair.R;
import com.philips.platform.devicepair.pojo.SubjectProfile;
import com.philips.platform.devicepair.utils.Utility;

import java.util.List;

public class CreateSubjectProfileFragment extends DevicePairingBaseFragment implements View.OnClickListener,
        ISubjectProfileListener {
    private View view;
    private EditText firstName, dob, gender, weight, creationDate;
    private Button createProfileButton;
    private IDevicePairingListener mDeviceStatusListener;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public int getActionbarTitleResId() {
        return R.string.subject_profile_fragment_title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.subject_profile_fragment_title);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_subject_profile_fragment, container, false);
        setUpViews();
        createProfileButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setUpViews() {
        firstName = (EditText) view.findViewById(R.id.subject_profile_first_name_edittext);
        dob = (EditText) view.findViewById(R.id.subject_date_of_birth_editText);
        gender = (EditText) view.findViewById(R.id.subject_gender_editText);
        weight = (EditText) view.findViewById(R.id.subject_weight_editText);
        creationDate = (EditText) view.findViewById(R.id.subject_creation_date_editText);
        createProfileButton = (Button) view.findViewById(R.id.create_subject_profile_button);
        creationDate.setEnabled(false);
    }

    private boolean validate() {
        String firstNameText = firstName.getText().toString();
        String dobText = dob.getText().toString();
        String genderText = gender.getText().toString();
        String weightText = weight.getText().toString();
        String creationDateText = creationDate.getText().toString();

        return !(firstNameText.length() == 0 || dobText.length() == 0 || genderText.length() == 0 || weightText.length() == 0 || creationDateText.length() == 0);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(createProfileButton)) {
            if (Utility.isOnline(mContext)) {
                if (isValidInput()) {
                    SubjectProfile subjectProfile = getSubjectProfile();
                    showProgressDialog(getString(R.string.creating_profile));
                    CreateSubjectProfileFragmentPresenter createProfilePresenter = new CreateSubjectProfileFragmentPresenter(this);
                    createProfilePresenter.createProfile(subjectProfile);
                } else {
                    showAlertDialog(getString(R.string.enter_valid_input));
                }
            } else {
                showAlertDialog(getString(R.string.check_connection));
            }
        }
    }

    private boolean isValidInput() {
        return validate();
    }

    private SubjectProfile getSubjectProfile() {
        SubjectProfile subjectProfile = new SubjectProfile();
        subjectProfile.setFirstName(firstName.getText().toString());
        subjectProfile.setDob(dob.getText().toString());
        subjectProfile.setGender(gender.getText().toString());
        subjectProfile.setWeight(Double.parseDouble(weight.getText().toString()));
        subjectProfile.setCreationDate(creationDate.getText().toString());
        return subjectProfile;
    }

    public void setDeviceStatusListener(IDevicePairingListener deviceStatusListener) {
        mDeviceStatusListener = deviceStatusListener;
    }

    @Override
    public void onSuccess(List<UCoreSubjectProfile> subjectProfileList) {
        dismissProgressDialog();
        moveToPairingFragment();
        mDeviceStatusListener.onProfileCreated();
    }

    @Override
    public void onError(final String message) {
        dismissProgressDialog();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAlertDialog(message);
            }
        });
    }
}
