package com.philips.amwelluapp.intake;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.health.Medication;
import com.americanwell.sdk.exception.AWSDKInstantiationException;

import com.americanwell.sdk.manager.ValidationReason;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.base.PTHBaseView;
import com.philips.amwelluapp.utility.AmwellLog;
import com.philips.amwelluapp.utility.PTHManager;

import java.util.Map;


public class PTHMedicationPresenter implements PTHBasePresenter, PTHMedicationCallback.PTHGetMedicationCallback, PTHSDKValidatedCallback<PTHMedication, SDKError>, PTHMedicationCallback.PTHUpdateMedicationCallback {
    PTHBaseView uiBaseView;


    public PTHMedicationPresenter(PTHBaseView uiBaseView) {
        this.uiBaseView = uiBaseView;
    }

    @Override
    public void onEvent(int componentID) {
        uiBaseView.addFragment(new THSConditionsFragment(),THSConditionsFragment.TAG,null);
    }


    protected void fetchMedication() {
        try {
            PTHManager.getInstance().getMedication(uiBaseView.getFragmentActivity(), this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    protected void searchMedication(String medicineName) {
        try {
            PTHManager.getInstance().searchMedication(uiBaseView.getFragmentActivity(), medicineName,  this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    protected void updateMedication(PTHMedication pTHMedication) {
        if(null!=pTHMedication) {
            try {
                PTHManager.getInstance().updateMedication(uiBaseView.getFragmentActivity(), pTHMedication, this);

            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }

    }

    protected void addSearchedMedication(Medication medication) {
        ((PTHMedicationFragment) uiBaseView).addSearchedMedicineToExistingMedication(medication);
    }

    //////////////// start of call backs for get existing medicines//////////////
    @Override
    public void onGetMedicationReceived(PTHMedication pTHMedication, SDKError sDKError) {
        ((PTHMedicationFragment) uiBaseView).hideProgressBar();
        if (null != pTHMedication.getMedicationList() && !pTHMedication.getMedicationList().isEmpty()) {
            AmwellLog.i("onGetMedicationReceived","Success");
            ((PTHMedicationFragment) uiBaseView).showExistingMedicationList(pTHMedication);

        } else {
            AmwellLog.i("onGetMedicationReceived","failure");
        }


    }
    //////////////// end of call backs for get existing medicines//////////////


    //////////////// start of call backs for search medicines//////////////
    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {
        ((PTHMedicationFragment) uiBaseView).hideProgressBar();
        AmwellLog.i("onFetchMedication","failure");
    }

    @Override
    public void onResponse(PTHMedication pthMedication, SDKError sdkError) {
        ((PTHMedicationFragment) uiBaseView).hideProgressBar();
        if (null!=pthMedication && null != pthMedication.getMedicationList() &&  !pthMedication.getMedicationList().isEmpty()) {

            AmwellLog.i("onFetchMedication","success");
            ((PTHMedicationFragment) uiBaseView).showSearchedMedicationList(pthMedication);
        } else {
            AmwellLog.i("onFetchMedication","failure");
            ((PTHMedicationFragment) uiBaseView).showToast("No match found");
        }
    }

    @Override
    public void onFailure(Throwable throwable) {

        ((PTHMedicationFragment) uiBaseView).hideProgressBar();
        AmwellLog.i("onFetchMedication","failure");
        ((PTHMedicationFragment) uiBaseView).showToast("Search failure");
    }
    //////////////// end of call backs for search medicines//////////////




    //////////////// start of call backs for update medicines//////////////


    @Override
    public void onUpdateMedicationSent(Void pVoid, SDKError sDKError) {
        ((PTHMedicationFragment) uiBaseView).hideProgressBar();

        AmwellLog.i("onUpdateMedication","success");
        // TODO  call next intake fragment here

    }
    //////////////// end of call backs for update medicines//////////////
}
