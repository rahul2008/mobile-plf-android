/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dprdemo.consents;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dprdemo.R;
import com.philips.platform.dprdemo.pojo.PairDevice;
import com.philips.platform.dprdemo.states.CreateSubjectProfileState;
import com.philips.platform.dprdemo.states.StateContext;
import com.philips.platform.dprdemo.ui.DeviceStatusListener;
import com.philips.platform.dprdemo.utils.NetworkChangeListener;

import java.util.ArrayList;
import java.util.List;


public class ConsentDialogFragment extends Fragment implements DBRequestListener<ConsentDetail>,
        DBFetchRequestListner<ConsentDetail>, DBChangeListener, View.OnClickListener, NetworkChangeListener.INetworkChangeListener {

    public static String TAG = ConsentDialogFragment.class.getSimpleName();
    private Context mContext;
    private ConsentDialogAdapter mConsentDialogAdapter;
    private ConsentDialogPresenter mConsentDialogPresenter;
    private ProgressDialog mProgressDialog;
    private DataServicesManager mDataServicesManager;
    private PairDevice mPairDevice;
    private DeviceStatusListener mDeviceStatusListener;
    private NetworkChangeListener mNetworkChangeListener;
    private AlertDialog.Builder mAlertDialogBuilder;
    private AlertDialog mAlertDialog;
    private Button mBtnOk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_consent, container, false);

        mNetworkChangeListener = new NetworkChangeListener();

        mDataServicesManager = DataServicesManager.getInstance();

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lv_consent_detail);

        mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        mBtnOk.setEnabled(false);

        Button mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mConsentDialogPresenter = new ConsentDialogPresenter(this);
        mProgressDialog = new ProgressDialog(getActivity());

        ArrayList<? extends ConsentDetail> mConsentDetailList = new ArrayList<>();
        mConsentDialogAdapter = new ConsentDialogAdapter(getActivity(), mConsentDetailList, mConsentDialogPresenter);
        mRecyclerView.setAdapter(mConsentDialogAdapter);

        fetchConsent();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();

        mNetworkChangeListener.addListener(this);
        mContext.registerReceiver(mNetworkChangeListener, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        mNetworkChangeListener.removeListener(this);
        mContext.unregisterReceiver(mNetworkChangeListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Consent Details");
        mDataServicesManager.registerDBChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        DataServicesManager.getInstance().unRegisterDBChangeListener();
        dismissProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnOK) {
            if (mConsentDialogAdapter.isAllConsentsAccepted()) {
                mConsentDialogAdapter.updateConsent();
                showProgressDialog();
            } else {
                showAlertDialog("Please accept all the consents to pair device.");
            }
        } else if (i == R.id.btnCancel) {
            removeCurrentFragment();
        }
    }

    public void setDeviceDetails(PairDevice pairDeviceDetails) {
        this.mPairDevice = pairDeviceDetails;
    }

    public void setDeviceStatusListener(DeviceStatusListener deviceStatusListener) {
        mDeviceStatusListener = deviceStatusListener;
    }

    public void fetchConsent() {
        showProgressDialog();
        DataServicesManager.getInstance().fetchConsentDetail(this);
    }


    private boolean isConsentAccepted(List<? extends ConsentDetail> data) {
        boolean isAccepted = false;

        for (ConsentDetail ormConsentDetail : data) {
            if (ormConsentDetail.getStatus().toString().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name())) {
                isAccepted = true;
            } else {
                isAccepted = false;
                break;
            }
        }
        return isAccepted;
    }

    private void removeCurrentFragment() {
        getFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    //Update Listener
    @Override
    public void onSuccess(final List<? extends ConsentDetail> data) {
        refreshUi((ArrayList<OrmConsentDetail>) data);
        dismissProgressDialog();

        StateContext stateContext = new StateContext();
        if (isConsentAccepted(data)) {
            removeCurrentFragment();
            stateContext.setState(new CreateSubjectProfileState(mPairDevice, mDeviceStatusListener, getActivity()));
            stateContext.start();
        } else {
            showAlertDialog("Please accept all the consents to pair device.");
        }
    }

    @Override
    public void onFailure(final Exception exception) {
        refreshOnFailure(exception);
    }

    //DB Change Listener
    @Override
    public void dBChangeSuccess(SyncType type) {
        if (type != SyncType.CONSENT) return;
        DataServicesManager.getInstance().fetchConsentDetail(this);
    }

    @Override
    public void dBChangeFailed(final Exception e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Exception :" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //Fetch Listener
    @Override
    public void onFetchSuccess(List<? extends ConsentDetail> data) {
        if (data == null || data.size() == 0) {
            mConsentDialogPresenter.saveDefaultConsentDetails();
        } else {
            refreshUi((ArrayList<OrmConsentDetail>) data);
        }
    }

    @Override
    public void onFetchFailure(final Exception exception) {
        refreshOnFailure(exception);
    }

    private void refreshUi(ArrayList<OrmConsentDetail> data) {
        final ArrayList<OrmConsentDetail> ormConsents = data;

        if (getActivity() != null && ormConsents != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mConsentDialogAdapter.setData(ormConsents);
                    mConsentDialogAdapter.notifyDataSetChanged();
                    mBtnOk.setEnabled(true);
                    dismissProgressDialog();
                }
            });
        }
    }

    private void refreshOnFailure(final Exception exception) {
        if (getActivity() != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog != null && !mProgressDialog.isShowing() && !(getActivity().isFinishing())) {
            mProgressDialog.setMessage("Updating Consent Data. Please wait");
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && !(getActivity().isFinishing())) {
            mProgressDialog.dismiss();
        }
    }

    public void showAlertDialog(String message) {
        if (mAlertDialogBuilder == null) {
            mAlertDialogBuilder = new AlertDialog.Builder(mContext, R.style.alertDialogStyle);
            mAlertDialogBuilder.setCancelable(false);
            mAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
        if (mAlertDialog == null)
            mAlertDialog = mAlertDialogBuilder.create();

        if (!mAlertDialog.isShowing() && !(getActivity().isFinishing())) {
            mAlertDialog.setMessage(message);
            mAlertDialog.show();
        }
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
