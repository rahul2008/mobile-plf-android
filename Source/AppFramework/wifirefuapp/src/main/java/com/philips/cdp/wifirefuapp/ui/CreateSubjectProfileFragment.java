package com.philips.cdp.wifirefuapp.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.philips.cdp.wifirefuapp.R;
import com.philips.cdp.wifirefuapp.pojo.PairDevice;
import com.philips.cdp.wifirefuapp.pojo.SubjectProfile;
import com.philips.cdp.wifirefuapp.states.PairDeviceState;
import com.philips.cdp.wifirefuapp.states.StateContext;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.List;

public class CreateSubjectProfileFragment extends Fragment implements View.OnClickListener, BackEventListener, CreateSubjectProfileViewListener {

    private View view;
    private EditText firstName, dob, gender, weight, creationDate;
    private Button createProfileButton;
    private CreateSubjectProfileFragmentPresenter createProfilePresenter;
    private PairDevice pairDevice;
    private FragmentLauncher fragmentLauncher;
    private DeviceStatusListener mDeviceStatusListener;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_subject_profile_fragment, container, false);

        setUpViews();
        createProfileButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentLauncher.getActionbarListener().updateActionBar("Subject Profile", true);
    }

    private void setUpViews() {
        firstName = (EditText) view.findViewById(R.id.subject_profile_first_name_edittext);
        dob = (EditText) view.findViewById(R.id.subject_date_of_birth_editText);
        gender = (EditText) view.findViewById(R.id.subject_gender_editText);
        weight = (EditText) view.findViewById(R.id.subject_weight_editText);
        creationDate = (EditText) view.findViewById(R.id.subject_creation_date_editText);
        createProfileButton = (Button) view.findViewById(R.id.create_subject_profile_button);
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
        if (firstNameText == null || dobText == null || genderText == null || weightText == null || creationDateText == null) {
            return false;
        } else if (firstNameText.length() == 0 ||
                dobText.length() == 0 ||
                genderText.length() == 0 ||
                weightText.length() == 0 ||
                creationDateText.length() == 0) {
            return false;
        } else
            return true;
    }


    @Override
    public void onClick(View v) {
        if (v.equals(createProfileButton)) {
            showProgressDialog();
            createProfilePresenter = new CreateSubjectProfileFragmentPresenter(this, mDeviceStatusListener);
            createProfilePresenter.createProfile();
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

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.fragmentLauncher = fragmentLauncher;
    }

    public void setDeviceStatusListener(DeviceStatusListener deviceStatusListener) {
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
    public void showToastMessage() {
        Toast.makeText(getActivity(), "Please check the values entered", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateSubjectProfile(List<UCoreSubjectProfile> list) {
        dismissProgressDialog();

        getFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();


        StateContext stateContext = new StateContext();
        stateContext.setState(new PairDeviceState(pairDevice,
                list, mDeviceStatusListener, fragmentLauncher));
        stateContext.start();
    }

    @Override
    public void onError(String message) {
        dismissProgressDialog();
        mDeviceStatusListener.onError(message);
    }

    private void showProgressDialog() {
        fragmentLauncher.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage("Creating subject profile. Please wait.");
                if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }

            }
        });
    }

    private void dismissProgressDialog() {
        fragmentLauncher.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

            }
        });
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }
}
