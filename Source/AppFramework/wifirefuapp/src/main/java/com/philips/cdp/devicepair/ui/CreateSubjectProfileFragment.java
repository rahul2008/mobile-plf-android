package com.philips.cdp.devicepair.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.philips.cdp.devicepair.pojo.PairDevice;
import com.philips.cdp.devicepair.pojo.SubjectProfile;
import com.philips.cdp.devicepair.states.PairDeviceState;
import com.philips.cdp.devicepair.states.StateContext;
import com.philips.cdp.devicepair.utils.NetworkChangeListener;
import com.philips.cdp.devicepair.utils.Utility;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.cdp.devicepair.R;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.List;

public class CreateSubjectProfileFragment extends Fragment implements View.OnClickListener,
        BackEventListener, CreateSubjectProfileViewListener, NetworkChangeListener.INetworkChangeListener {

    private View view;
    private EditText firstName, dob, gender, weight, creationDate;
    private Button createProfileButton;
    private PairDevice pairDevice;
    private FragmentLauncher fragmentLauncher;
    private DeviceStatusListener mDeviceStatusListener;
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder mAlertDialog;
    private NetworkChangeListener mNetworkChangeListener;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_subject_profile_fragment, container, false);
        mNetworkChangeListener = new NetworkChangeListener();

        setUpViews();
        createProfileButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentLauncher.getActionbarListener().updateActionBar("Subject Profile", true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onPause() {
        super.onPause();
        mNetworkChangeListener.removeListener(this);
        mContext.unregisterReceiver(mNetworkChangeListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        mNetworkChangeListener.addListener(this);
        mContext.registerReceiver(mNetworkChangeListener, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
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
                showProgressDialog();
                CreateSubjectProfileFragmentPresenter createProfilePresenter = new CreateSubjectProfileFragmentPresenter(this);
                createProfilePresenter.createProfile();
            } else {
                showAlertDialog("Please check your connection and try again.");
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
    public void onError(final String message) {
        dismissProgressDialog();
        fragmentLauncher.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAlertDialog(message);

            }
        });

    }

    private void showProgressDialog() {
        fragmentLauncher.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setCancelable(true);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setMessage("Creating subject profile...");
                if (mProgressDialog != null && !mProgressDialog.isShowing() && !(getActivity().isFinishing())) {
                    mProgressDialog.show();
                }

            }
        });
    }

    private void dismissProgressDialog() {
        fragmentLauncher.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing() && !(getActivity().isFinishing())) {
                    mProgressDialog.dismiss();
                }

            }
        });
    }

    public void showAlertDialog(String message) {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(mContext, R.style.alertDialogStyle);
            mAlertDialog.setCancelable(false);
            mAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
        mAlertDialog.setMessage(message);
        AlertDialog alert = mAlertDialog.create();
        alert.show();
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    @Override
    public void onConnectionLost() {
        dismissProgressDialog();
        showAlertDialog("Please check your connection and try again.");
    }

    @Override
    public void onConnectionAvailable() {

    }
}
