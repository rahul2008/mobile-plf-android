package cdp.philips.com.mydemoapp.consents;

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

public class ConsentDialogFragment extends DialogFragment implements DBChangeListener,View.OnClickListener {

    private final TemperaturePresenter mTemperaturePresenter;
    private RecyclerView mRecyclerView;
    private Button mBtnOk;
    private Button mBtnCancel;
    private ConsentDialogAdapter lConsentAdapter;
    ProgressDialog mProgressBar;

    public ConsentDialogFragment() {
        mTemperaturePresenter = new TemperaturePresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_consent, container,
                false);
        EventHelper.getInstance().registerEventNotification(EventHelper.CONSENT, this);
        mTemperaturePresenter.fetchConsents();
        showProgressBar();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lv_consent_detail);
        mBtnOk=(Button)rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        mBtnOk.setEnabled(false);
        mBtnCancel=(Button)rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mProgressBar = new ProgressDialog(getActivity());
        return rootView;

    }

    @Override
    public void onSuccess(ArrayList<? extends Object> data) {
        dismissProgressBar();
      if(data==null){
          showProgressBar();
          createDefaultConsent();
      }
    }

    @Override
    public void onSuccess(Object data) {
        dismissProgressBar();
        OrmConsent ormConsent = (OrmConsent) data;
        if(ormConsent!=null){
            lConsentAdapter = new ConsentDialogAdapter(getActivity(),ormConsent, mTemperaturePresenter);
            mRecyclerView.setAdapter(lConsentAdapter);
            lConsentAdapter.notifyDataSetChanged();
            mBtnOk.setEnabled(true);
        }

    }

    @Override
    public void onFailure(Exception exception) {
     dismissProgressBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

    public void createDefaultConsent(){
        DataServicesManager mDataServices=DataServicesManager.getInstance();
        Consent consent = mDataServices.createConsent();
        ConsentHelper consentHelper = new ConsentHelper(mDataServices);
        consentHelper.addConsent
                (consent, ConsentDetailType.TEMPERATURE, ConsentDetailStatusType.REFUSED,
                        Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);
        consentHelper.addConsent
                (consent,ConsentDetailType.WEIGHT, ConsentDetailStatusType.REFUSED,
                        Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);
        consentHelper.addConsent
                (consent,ConsentDetailType.CRY_DURATION, ConsentDetailStatusType.REFUSED,
                        Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);

        consentHelper.addConsent
                (consent,ConsentDetailType.HEIGHT, ConsentDetailStatusType.REFUSED,
                        Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER);

        consentHelper.sendUpdateRequest(consent);
    }

    private void showProgressBar(){
        if(mProgressBar!=null) {
            mProgressBar.setCancelable(true);
            mProgressBar.setMessage("Fetching consents ...");
        }
    }

    private void dismissProgressBar(){
        if(mProgressBar!=null) {
            mProgressBar.dismiss();
        }
    }

}
