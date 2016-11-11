package cdp.philips.com.mydemoapp.consents;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.ConsentDetailType;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.listener.DBChangeListener;
import cdp.philips.com.mydemoapp.listener.EventHelper;
import cdp.philips.com.mydemoapp.temperature.TemperaturePresenter;

/**
 * Created by sangamesh on 08/11/16.
 */

public class ConsentDialogFragment extends DialogFragment implements DBChangeListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private Button mBtnOk;
    private Button mBtnCancel;
    private ConsentDialogAdapter lConsentAdapter;
    ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_consent, container,
                false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lv_consent_detail);
        mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        mBtnOk.setEnabled(false);
        mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_consent);
        fetchConsent();
        showProgressBar();
        return rootView;

    }

    @Override
    public void onSuccess(ArrayList<? extends Object> data) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgressBar();
            }
        });
        if (data == null) {
            showProgressBar();
            createDefaultConsent();
        }
    }

    @Override
    public void onSuccess(Object data) {

        final OrmConsent ormConsent = (OrmConsent) data;
        if (ormConsent != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lConsentAdapter = new ConsentDialogAdapter(getActivity(), ormConsent);
                    mRecyclerView.setAdapter(lConsentAdapter);
                    lConsentAdapter.notifyDataSetChanged();
                    mBtnOk.setEnabled(true);
                    dismissProgressBar();
                }
            });
        }

    }

    @Override
    public void onFailure(Exception exception) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dismissProgressBar();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                lConsentAdapter.updateConsentDetails();
                getDialog().dismiss();
                break;
            case R.id.btnCancel:
                getDialog().dismiss();
                break;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventHelper.getInstance().unregisterEventNotification(EventHelper.CONSENT, this);
        dismissProgressBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventHelper.getInstance().registerEventNotification(EventHelper.CONSENT, this);
        Dialog dialog = getDialog();
        dialog.setTitle("Consents");
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public void createDefaultConsent() {
        DataServicesManager mDataServices = DataServicesManager.getInstance();
        Consent consent = mDataServices.createConsent();
        ConsentHelper consentHelper = new ConsentHelper(mDataServices);

        for (ConsentDetailType consentDetailType : ConsentDetailType.values()) {
            consentHelper.addConsent
                    (consent, consentDetailType, ConsentDetailStatusType.REFUSED,
                            Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);
        }

        consentHelper.createDeafultConsentRequest(consent);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void fetchConsent() {
        DataServicesManager.getInstance().fetchConsent();
    }


}
