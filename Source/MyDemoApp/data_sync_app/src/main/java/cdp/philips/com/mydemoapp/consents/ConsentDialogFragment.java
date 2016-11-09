package cdp.philips.com.mydemoapp.consents;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.platform.core.datatypes.MomentType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetailType;
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
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lv_consent_detail);
        mBtnOk=(Button)rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        mBtnOk.setEnabled(false);
        mBtnCancel=(Button)rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        return rootView;

    }

    @Override
    public void onSuccess(ArrayList<? extends Object> data) {
        List<OrmConsent> ormConsents = (List<OrmConsent>) data;
        if(ormConsents==null || ormConsents.size()==0){
            mTemperaturePresenter.createDefaultConsent();
        }
        lConsentAdapter = new ConsentDialogAdapter(getActivity(),ormConsents, mTemperaturePresenter);
        mRecyclerView.setAdapter(lConsentAdapter);
        mBtnOk.setEnabled(true);
    }

    @Override
    public void onSuccess(Object data) {
    }

    @Override
    public void onFailure(Exception exception) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOK:
                lConsentAdapter.updateConsents();
                getDialog().dismiss();
                break;
            case R.id.btnCancel:
                getDialog().dismiss();
                break;

        }
    }
}
