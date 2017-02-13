package com.philips.platform.baseapp.screens.dataservices.consents;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;

public class ConsentDialogFragment extends DialogFragment implements DBRequestListener,DBChangeListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private Button mBtnOk;
    private Button mBtnCancel;
    private ConsentDialogAdapter lConsentAdapter;
    ConsentDialogPresenter consentDialogPresenter;
    private ProgressDialog mProgressDialog;
    ArrayList<? extends ConsentDetail> consentDetails;
    private Context mContext;
    private DataServicesManager mDataServicesManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_consent, container,
                false);

        mDataServicesManager= DataServicesManager.getInstance();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lv_consent_detail);
        mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        mBtnOk.setEnabled(false);
        mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        consentDialogPresenter=new ConsentDialogPresenter(getActivity(), this);
        mProgressDialog = new ProgressDialog(getActivity());
        consentDetails=new ArrayList<>();
        lConsentAdapter = new ConsentDialogAdapter(getActivity(),consentDetails, consentDialogPresenter);
        mRecyclerView.setAdapter(lConsentAdapter);
        fetchConsent();
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess(final ArrayList<? extends Object> data) {

        final ArrayList<OrmConsentDetail> ormConsents = (ArrayList<OrmConsentDetail>) data;

        if (getActivity()!=null && ormConsents != null ) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lConsentAdapter.setData(ormConsents);
                    lConsentAdapter.notifyDataSetChanged();
                    mBtnOk.setEnabled(true);
                    dismissProgressDialog();
                }
            });
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    @Override
    public void onSuccess(Object data) {


    }

    @Override
    public void onFailure(final Exception exception) {

        if(getActivity()!=null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                lConsentAdapter.updateConsent();
                dismissConsentDialog(getDialog());
                break;
            case R.id.btnCancel:
                dismissConsentDialog(getDialog());
                break;

        }
    }

    private void dismissConsentDialog(Dialog dialog){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mDataServicesManager.registerDBChangeListener(this);
        Dialog dialog = getDialog();
        dialog.setTitle(R.string.consent);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void showProgressDialog() {
        if(mProgressDialog!=null && !mProgressDialog.isShowing()) {
             mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if(mProgressDialog!=null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void fetchConsent() {
        showProgressDialog();
        DataServicesManager.getInstance().fetchConsentDetail(this);
    }

    @Override
    public void dBChangeSuccess() {
        DataServicesManager.getInstance().fetchConsentDetail(this);
    }

    @Override
    public void dBChangeFailed(final Exception e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity(),"Exception :"+e.getMessage() ,Toast.LENGTH_LONG).show();
            }
        });
    }
}
