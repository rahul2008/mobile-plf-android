/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.devicepair.R;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;

import java.util.ArrayList;
import java.util.List;

public class ConsentsFragment extends DevicePairingBaseFragment implements DBRequestListener<ConsentDetail>,
        DBFetchRequestListner<ConsentDetail>, DBChangeListener, View.OnClickListener {
    private ConsentsAdapter mConsentDialogAdapter;
    private ConsentsPresenter mConsentDialogPresenter;
    private DataServicesManager mDataServicesManager;
    private IDevicePairingListener mDeviceStatusListener;
    private Button mBtnOk;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getActionbarTitleResId() {
        return R.string.consent_fragment_title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.consent_fragment_title);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_consent, container, false);

        mDataServicesManager = DataServicesManager.getInstance();

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lv_consent_detail);

        mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        mBtnOk.setEnabled(false);

        Button mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mConsentDialogPresenter = new ConsentsPresenter(this);

        ArrayList<? extends ConsentDetail> mConsentDetailList = new ArrayList<>();
        mConsentDialogAdapter = new ConsentsAdapter(getActivity(), mConsentDetailList, mConsentDialogPresenter);
        mRecyclerView.setAdapter(mConsentDialogAdapter);

        fetchConsent();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
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
                showProgressDialog(getString(R.string.update_consents));
                mConsentDialogAdapter.updateConsent();
            } else {
                showAlertDialog(getString(R.string.accept_consents));
            }
        } else if (i == R.id.btnCancel) {
            removeCurrentFragment();
        }
    }

    public void setDeviceStatusListener(IDevicePairingListener deviceStatusListener) {
        mDeviceStatusListener = deviceStatusListener;
    }

    public void fetchConsent() {
        showProgressDialog(getString(R.string.get_consents));
        DataServicesManager.getInstance().fetchConsentDetail(this);
    }

    private boolean isConsentAccepted(List<? extends ConsentDetail> data) {
        boolean isAccepted = false;
        for (ConsentDetail ormConsentDetail : data) {
            if (ormConsentDetail.getStatus().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name())) {
                isAccepted = true;
            } else {
                isAccepted = false;
                break;
            }
        }
        return isAccepted;
    }

    //Update Listener
    @Override
    public void onSuccess(final List<? extends ConsentDetail> data) {
        refreshUi((ArrayList<OrmConsentDetail>) data);
        dismissProgressDialog();

        if (isConsentAccepted(data)) {
            mDeviceStatusListener.onConsentsAccepted();
        } else {
            showAlertDialog(getString(R.string.accept_consents));
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
                showAlertDialog(e.getMessage());
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
                    showAlertDialog(exception.getMessage());
                }
            });
        }
    }
}
