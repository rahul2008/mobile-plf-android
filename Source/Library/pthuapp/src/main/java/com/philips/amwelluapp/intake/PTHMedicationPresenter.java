package com.philips.amwelluapp.intake;

import android.util.Log;
import android.widget.ListView;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.base.UIBasePresenter;
import com.philips.amwelluapp.base.UIBaseView;
import com.philips.amwelluapp.utility.PTHManager;

/**
 * Created by philips on 6/28/17.
 */

public class PTHMedicationPresenter implements UIBasePresenter, PTHMedicationCallback.PTHGetMedicationCallback {
    UIBaseView uiBaseView;




    public PTHMedicationPresenter(UIBaseView uiBaseView){
        this.uiBaseView = uiBaseView;
    }
    @Override
    public void onEvent(int componentID) {

    }


    protected void fetchMedication(){
        try{
            PTHManager.getInstance().getMedication(uiBaseView.getFragmentActivity(),PTHManager.getInstance().getPTHConsumer().getConsumer(),this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onGetMedicationReceived(PTHMedication pTHMedication, SDKError sDKError) {
       if( null!=pTHMedication.getMedicationList()){
           Log.v("onGetMedicationReceived","Success");
           ((PTHMedicationFragment)uiBaseView).showExistingMedicationList(pTHMedication);
       }else{
           Log.v("onGetMedicationReceived","failure");
       }


    }
}
