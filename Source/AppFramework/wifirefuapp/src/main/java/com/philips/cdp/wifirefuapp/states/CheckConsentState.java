package com.philips.cdp.wifirefuapp.states;

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
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.List;

/**
 * Created by philips on 6/7/17.
 */

public class CheckConsentState extends BaseState implements DBRequestListener<ConsentDetail>,DBFetchRequestListner<ConsentDetail>,DBChangeListener{

    private FragmentLauncher context;
    //private ProgressDialog mProgressDialog;
    List<OrmConsentDetail> list;
    StateContext stateContext;
    private PairDevicePojo pairDevicePojo;

    public CheckConsentState(PairDevicePojo pairDevicePojo,FragmentLauncher context) {
        super(context);
        this.context = context;
        this.pairDevicePojo = pairDevicePojo;
    }

    @Override
    public void start(StateContext stateContext) {
       // mProgressDialog = new ProgressDialog(context);
        //((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().add(new ConsentDialogFragment(),"ConsentFragment").commit();
        fetchConsent();
    }

    public void fetchConsent(){
        showProgressDialog();
        DataServicesManager.getInstance().fetchConsentDetail(this);
    }

    private void showProgressDialog() {
//        if(mProgressDialog!=null && !mProgressDialog.isShowing()) {
//            mProgressDialog.show();
//        }
    }

    private void dismissProgressDialog() {
//        if(mProgressDialog!=null && mProgressDialog.isShowing()) {
//            mProgressDialog.dismiss();
//        }
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
        for (ConsentDetail ormConsentDetail: list) {
            if(ormConsentDetail.getStatus().toString().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name())){
                accepted = true;
            }
            else {
                accepted = false;
                break;
            }
        }
        if(!accepted){
            context.getFragmentActivity().getSupportFragmentManager().beginTransaction().replace(context.getParentContainerResourceID(),new ConsentDialogFragment(), "ConsentFragmentUApp").commit();
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
