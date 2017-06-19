package com.philips.cdp.wifirefuapp.consents;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.philips.cdp.wifirefuapp.R;
import com.philips.cdp.wifirefuapp.pojo.PairDevice;
import com.philips.cdp.wifirefuapp.states.CreateSubjectProfileState;
import com.philips.cdp.wifirefuapp.states.StateContext;
import com.philips.cdp.wifirefuapp.ui.DeviceStatusListener;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.ArrayList;
import java.util.List;


public class ConsentDialogFragment extends Fragment implements DBRequestListener<ConsentDetail>,
        DBFetchRequestListner<ConsentDetail>, DBChangeListener, View.OnClickListener {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private Button mBtnOk;
    private Button mBtnCancel;
    private ConsentDialogAdapter mConsentDialogAdapter;
    private ConsentDialogPresenter mConsentDialogPresenter;
    private ProgressDialog mProgressDialog;
    private DataServicesManager mDataServicesManager;
    private FragmentLauncher mFragmentLauncher;
    private PairDevice mPairDevice;
    private DeviceStatusListener mDeviceStatusListener;
    private ArrayList<? extends ConsentDetail> mConsentDetailList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_consent, container, false);

        mDataServicesManager = DataServicesManager.getInstance();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lv_consent_detail);

        mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        mBtnOk.setEnabled(false);

        mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mConsentDialogPresenter = new ConsentDialogPresenter(getActivity(), this);
        mProgressDialog = new ProgressDialog(getActivity());

        mConsentDetailList = new ArrayList<>();
        mConsentDialogAdapter = new ConsentDialogAdapter(getActivity(), mConsentDetailList, mConsentDialogPresenter);
        mRecyclerView.setAdapter(mConsentDialogAdapter);

        fetchConsent();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
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
        mFragmentLauncher.getActionbarListener().updateActionBar("Consent Detail", true);
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
            mConsentDialogAdapter.updateConsent();
            showProgressDialog();
        } else if (i == R.id.btnCancel) {
            dismissConsentDialog();
        }
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.mFragmentLauncher = fragmentLauncher;
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

    private void dismissConsentDialog() {
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
            stateContext.setState(new CreateSubjectProfileState(mPairDevice, mDeviceStatusListener, mFragmentLauncher));
            stateContext.start();
        } else {
            Toast.makeText(getActivity(), "Consents not accepted, please accept to continue", Toast.LENGTH_SHORT).show();
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
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.setMessage("Updating Consent Data. Please wait");
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
