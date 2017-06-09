package com.philips.cdp.wifirefuapp.states;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.philips.cdp.wifirefuapp.consents.ConsentDialogFragment;
import com.philips.cdp.wifirefuapp.consents.OrmConsentDetail;
import com.philips.cdp.wifirefuapp.pojo.PairDevicePojo;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philips on 6/7/17.
 */

public class CheckConsentState extends BaseState implements DBRequestListener<ConsentDetail>,DBFetchRequestListner<ConsentDetail>,DBChangeListener{

    private Context context;
    private ProgressDialog mProgressDialog;
    List<OrmConsentDetail> list;
    StateContext stateContext;
    private PairDevicePojo pairDevicePojo;

    public CheckConsentState(PairDevicePojo pairDevicePojo,Context context) {
        super(context);
        this.context = context;
        this.pairDevicePojo = pairDevicePojo;
    }

    @Override
    public void start(StateContext stateContext) {
        mProgressDialog = new ProgressDialog(context);
        //((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().add(new ConsentDialogFragment(),"ConsentFragment").commit();
        fetchConsent();
    }

    public void fetchConsent(){
        showProgressDialog();
        DataServicesManager.getInstance().fetchConsentDetail(this);
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
    @Override
    public void dBChangeSuccess(SyncType syncType) {

    }

    @Override
    public void dBChangeFailed(Exception e) {

    }

    @Override
    public void onFetchSuccess(List<? extends ConsentDetail> list) {
        dismissProgressDialog();
        stateContext = new StateContext();
        boolean accepted = false;
        for (OrmConsentDetail ormConsentDetail: (ArrayList<OrmConsentDetail>) list) {
            if(ormConsentDetail.getStatus().toString().equals(ConsentDetailStatusType.ACCEPTED.name())){
                accepted = true;
            }
            else {
                accepted = false;
            }
        }
        if(accepted){
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().add(new ConsentDialogFragment(), "ConsentFragment").commit();
        }else {
            stateContext.setState(new CreateSubjectProfileState(pairDevicePojo,context));
            stateContext.start();
        }


    }

    @Override
    public void onFetchFailure(Exception e) {

    }

    @Override
    public void onSuccess(List<? extends ConsentDetail> list) {

    }

    @Override
    public void onFailure(Exception e) {

    }
}
