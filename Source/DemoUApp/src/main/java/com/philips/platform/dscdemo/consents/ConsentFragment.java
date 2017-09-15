/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.consents;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DSBaseFragment;
import com.philips.platform.dscdemo.R;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;

import java.util.ArrayList;
import java.util.List;

public class ConsentFragment extends DSBaseFragment implements
        DBRequestListener<ConsentDetail>, DBFetchRequestListner<ConsentDetail>, DBChangeListener, View.OnClickListener {

    private Context mContext;
    private ConsentAdapter mConsentAdapter;
    private ConsentPresenter mConsentPresenter;
    private DataServicesManager mDataServicesManager;
    private Button mBtnOk;
    private ProgressDialog mProgressDialog;

    @Override
    public int getActionbarTitleResId() {
        return R.string.consent_title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.consent_title);
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
        mConsentPresenter = new ConsentPresenter(this);
        ArrayList<? extends ConsentDetail> mConsentDetailList = new ArrayList<>();
        mConsentAdapter = new ConsentAdapter(mConsentDetailList, mConsentPresenter);
        mProgressDialog = new ProgressDialog(mContext);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lv_consent_detail);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mConsentAdapter);

        mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        mBtnOk.setEnabled(false);

        Button mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);

        fetchConsent();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDataServicesManager.registerDBChangeListener(this);
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
    public void onSuccess(final List<? extends ConsentDetail> data) {
        refreshUi((ArrayList<OrmConsentDetail>) data);
    }

    @Override
    public void onFailure(final Exception exception) {
        refreshOnFailure(exception);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnOK) {
            mConsentAdapter.updateConsent();
            dismissConsentDialog();
        } else if (i == R.id.btnCancel) {
            dismissConsentDialog();
        }
    }

    private void dismissConsentDialog() {
        getFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public void fetchConsent() {
        showProgressDialog();
        DataServicesManager.getInstance().fetchConsentDetail(this);
    }

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

    @Override
    public void onFetchSuccess(List<? extends ConsentDetail> data) {
        if (data == null || data.size() == 0) {
            mConsentPresenter.saveDefaultConsentDetails();
        } else {
            refreshUi((ArrayList<OrmConsentDetail>) data);
        }
    }

    private void refreshUi(ArrayList<OrmConsentDetail> data) {
        final ArrayList<OrmConsentDetail> ormConsents = data;
        if (getActivity() != null && ormConsents != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mConsentAdapter.setData(ormConsents);
                    mConsentAdapter.notifyDataSetChanged();
                    mBtnOk.setEnabled(true);
                    dismissProgressDialog();
                }
            });
        }
    }

    @Override
    public void onFetchFailure(final Exception exception) {
        refreshOnFailure(exception);
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
        if (mProgressDialog != null && !mProgressDialog.isShowing() && !(((Activity) mContext).isFinishing())) {
            mProgressDialog.setMessage(getString(R.string.fetching_consent));
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && !(((Activity) mContext).isFinishing())) {
            mProgressDialog.dismiss();
        }
    }
}
