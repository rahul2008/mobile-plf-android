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
import android.widget.Button;
import android.widget.Toast;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.ConsentDetailType;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.listener.DBChangeListener;
import cdp.philips.com.mydemoapp.listener.EventHelper;

/**
 * Created by sangamesh on 08/11/16.
 */

public class ConsentDialogFragment extends DialogFragment implements DBChangeListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private Button mBtnOk;
    private Button mBtnCancel;
    private ConsentDialogAdapter lConsentAdapter;
    ConsentDialogPresenter consentDialogPresenter;
    private ProgressDialog mProgressDialog;
    ArrayList<? extends ConsentDetail> consentDetails;

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
        consentDialogPresenter=new ConsentDialogPresenter(getActivity());
        mProgressDialog = new ProgressDialog(getActivity());
        consentDetails=new ArrayList<>();
        lConsentAdapter = new ConsentDialogAdapter(getActivity(),consentDetails);
        mRecyclerView.setAdapter(lConsentAdapter);
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        fetchConsent();
    }

    @Override
    public void onSuccess(final ArrayList<? extends Object> data) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                if (data == null) {
                    showProgressDialog();
                    createDefaultConsent();
                }
            }
        });
    }

    @Override
    public void onSuccess(Object data) {

        final OrmConsent ormConsent = (OrmConsent) data;
        if (ormConsent != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lConsentAdapter.setData(ormConsent);
                    lConsentAdapter.notifyDataSetChanged();
                    mBtnOk.setEnabled(true);
                    dismissProgressDialog();
                }
            });
        }

    }

    @Override
    public void onFailure(final Exception exception) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dismissProgressDialog();
                Toast.makeText(getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                lConsentAdapter.updateConsentDetails();
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
        EventHelper.getInstance().unregisterEventNotification(EventHelper.CONSENT, this);
        dismissProgressDialog();
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

    private void createDefaultConsent() {
        DataServicesManager mDataServices = DataServicesManager.getInstance();
        Consent consent = mDataServices.createConsent();
        boolean isSynchronized=true;
        mDataServices.createConsentDetail
                (consent, ConsentDetailType.SLEEP, ConsentDetailStatusType.REFUSED,
                        Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER,isSynchronized);
        mDataServices.createConsentDetail
                (consent, ConsentDetailType.TEMPERATURE, ConsentDetailStatusType.REFUSED,
                        Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER,isSynchronized);
        mDataServices.createConsentDetail
                (consent, ConsentDetailType.WEIGHT, ConsentDetailStatusType.REFUSED,
                        Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER,isSynchronized);
       mDataServices.save(consent);
    }

    private void showProgressDialog() {
        if(mProgressDialog!=null && !mProgressDialog.isShowing()) {
            mProgressDialog.setMessage("loading consents");
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
        DataServicesManager.getInstance().fetchConsent();
    }


}
