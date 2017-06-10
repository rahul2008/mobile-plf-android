package com.philips.cdp.wifirefuapp.ui;

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
import com.philips.cdp.wifirefuapp.pojo.PairDevicePojo;
import com.philips.cdp.wifirefuapp.pojo.SubjectProfilePojo;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class CreateSubjectProfileFragment extends Fragment implements View.OnClickListener,SubjectProfileViewListener {

    private View view;
    private EditText firstName,dob,gender,weight,creationDate;
    private Button createProfileButton;
    private  CreateSubjectProfileFragmentPresenter createProfilePresenter;
    private PairDevicePojo pairDevicePojo;
    private FragmentLauncher fragmentLauncher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_subject_profile_fragment, container, false);

        setUpViews();
        createProfileButton.setOnClickListener(this);
        createProfilePresenter = new CreateSubjectProfileFragmentPresenter(this);
        return view;
    }

    private void setUpViews() {
        firstName = (EditText) view.findViewById(R.id.subject_profile_first_name_edittext);
        dob = (EditText) view.findViewById(R.id.subject_date_of_birth_editText);
        gender = (EditText) view.findViewById(R.id.subject_gender_editText);
        weight = (EditText) view.findViewById(R.id.subject_weight_editText);
        creationDate = (EditText) view.findViewById(R.id.subject_creation_date_editText);
        createProfileButton = (Button) view.findViewById(R.id.create_subject_profile_button);
    }

    private boolean validate() {
        String firstNameText = firstName.getText().toString();
        String dobText = dob.getText().toString();
        String genderText = gender.getText().toString();
        String weightText = weight.getText().toString();
        String creationDateText = creationDate.getText().toString();
        if(firstNameText == null || dobText == null || genderText == null || weightText == null || creationDateText == null){
            return false;
        }else if(firstNameText.length() == 0 || dobText.length() == 0 || genderText.length() == 0 || weightText.length() == 0 || creationDateText.length() ==  0){
            return false;
        }else
            return true;
    }


    @Override
    public void onClick(View v) {
        if(v.equals(createProfileButton)){
                createProfilePresenter.createProfile();
        }
    }

    private SubjectProfilePojo createSubjectProfilePojo() {
        SubjectProfilePojo subjecProfilePojo = new SubjectProfilePojo();

        if(validate()){
            subjecProfilePojo.setFirstName(firstName.getText().toString());
            subjecProfilePojo.setDob(dob.getText().toString());
            subjecProfilePojo.setGender(gender.getText().toString());
            subjecProfilePojo.setWeight(Double.parseDouble(weight.getText().toString()));
            subjecProfilePojo.setCreationDate(creationDate.getText().toString());
            return subjecProfilePojo;
        }
        return null;
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher){
        this.fragmentLauncher = fragmentLauncher;
    }
    @Override
    public PairDevicePojo getDevicePojo() {
        pairDevicePojo.setDeviceID(this.getArguments().getString("DeviceID"));
        pairDevicePojo.setDeviceType(this.getArguments().getString("DeviceType"));
        return pairDevicePojo;
    }

    @Override
    public FragmentLauncher getFragmentLauncher() {
        return fragmentLauncher;
    }

    @Override
    public boolean validateViews() {
        return validate();
    }

    @Override
    public SubjectProfilePojo getSubjectProfilePojo() {
        return createSubjectProfilePojo();
    }

    @Override
    public void showToastMessage() {
        Toast.makeText(getActivity(),"Please check the values entered",Toast.LENGTH_SHORT).show();
    }


}
