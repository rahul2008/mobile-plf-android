package com.philips.amwelluapp.intake;

import android.util.Log;
import android.widget.ListView;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.health.Medication;
import com.americanwell.sdk.exception.AWSDKInstantiationException;

import com.americanwell.sdk.manager.ValidationReason;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.base.PTHBaseView;
import com.philips.amwelluapp.utility.PTHManager;

import java.util.Map;

/**
 * Created by philips on 6/28/17.
 */

public class PTHMedicationPresenter implements PTHBasePresenter, PTHMedicationCallback.PTHGetMedicationCallback , PTHSDKValidatedCallback<PTHMedication,SDKError>{
    PTHBaseView uiBaseView;




    public PTHMedicationPresenter(PTHBaseView uiBaseView){
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

    protected void searchMedication(String medicineName){
        try{
            PTHManager.getInstance().searchMedication(uiBaseView.getFragmentActivity(),medicineName,PTHManager.getInstance().getPTHConsumer().getConsumer(),this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    protected void addSearchedMedication(Medication medication){
        ((PTHMedicationFragment)uiBaseView).addSearchedMedicineToExistingMedication(medication);
    }

    //////////////// start of call backs for get existing medicines//////////////
    @Override
    public void onGetMedicationReceived(PTHMedication pTHMedication, SDKError sDKError) {
       if( null!=pTHMedication.getMedicationList()){
           Log.v("onGetMedicationReceived","Success");
           ((PTHMedicationFragment)uiBaseView).showExistingMedicationList(pTHMedication);
       }else{
           Log.v("onGetMedicationReceived","failure");
       }


    }
    //////////////// end of call backs for get existing medicines//////////////




    //////////////// start of call backs for search medicines//////////////
    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {
        Log.v("onFetchMedication","failure");
    }

    @Override
    public void onResponse(PTHMedication pthMedication, SDKError sdkError) {
        if( null!=pthMedication.getMedicationList()){
            Log.v("onFetchMedication","Success");
            ((PTHMedicationFragment)uiBaseView).showSearchedMedicationList(pthMedication);
        }else{
            Log.v("onFetchMedication","failure");
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        Log.v("onFetchMedication","failure");
    }

    //////////////// end of call backs for search medicines//////////////
}
