/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dprdemo.ui;

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
import com.philips.platform.dprdemo.R;
import com.philips.platform.dprdemo.pojo.PairDevice;
import com.philips.platform.dprdemo.pojo.SubjectProfile;
import com.philips.platform.dprdemo.states.PairDeviceState;
import com.philips.platform.dprdemo.states.StateContext;
import com.philips.platform.dprdemo.utils.Utility;

import java.util.List;

public class CreateSubjectProfileFragment extends DevicePairingBaseFragment implements View.OnClickListener,
        ISubjectProfileListener {
    private View view;
    private EditText firstName, dob, gender, weight, creationDate;
    private Button createProfileButton;
    private PairDevice pairDevice;
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
        return false;
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

    public void setDeviceDetails(PairDevice pairDevice) {
        this.pairDevice = pairDevice;
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
                showProgressDialog(getString(R.string.creating_profile));
                CreateSubjectProfileFragmentPresenter createProfilePresenter = new CreateSubjectProfileFragmentPresenter(this);
                createProfilePresenter.createProfile();
            } else {
                showAlertDialog(getString(R.string.check_connection));
            }
        }
    }

    private SubjectProfile getSubjectProfileDetails() {
        SubjectProfile subjectProfile = new SubjectProfile();
        if (validate()) {
            subjectProfile.setFirstName(firstName.getText().toString());
            subjectProfile.setDob(dob.getText().toString());
            subjectProfile.setGender(gender.getText().toString());
            subjectProfile.setWeight(Double.parseDouble(weight.getText().toString()));
            subjectProfile.setCreationDate(creationDate.getText().toString());
            return subjectProfile;
        }
        return null;
    }

    public void setDeviceStatusListener(IDevicePairingListener deviceStatusListener) {
        mDeviceStatusListener = deviceStatusListener;
    }

    @Override
    public boolean validateViews() {
        return validate();
    }

    @Override
    public SubjectProfile getSubjectProfile() {
        return getSubjectProfileDetails();
    }

    @Override
    public void onCreateSubjectProfile(List<UCoreSubjectProfile> list) {
        dismissProgressDialog();
        removeCurrentFragment();
        pairDevice(list);
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

    @Override
    public void onInvalidInput() {
        showAlertDialog(getString(R.string.enter_valid_input));
    }

    private void pairDevice(List<UCoreSubjectProfile> list) {
        showProgressDialog(getString(R.string.pairing_device));
        StateContext stateContext = new StateContext();
        stateContext.setState(new PairDeviceState(pairDevice,
                list, mDeviceStatusListener, getActivity()));
        stateContext.start();
    }
}
