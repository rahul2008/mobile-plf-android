package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.health.Medication;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.PTHManager;

import java.util.Map;


public class THSMedicationPresenter implements THSBasePresenter, THSMedicationCallback.PTHGetMedicationCallback, THSSDKValidatedCallback<THSMedication, SDKError>, THSMedicationCallback.PTHUpdateMedicationCallback {
    THSBaseView uiBaseView;


    public THSMedicationPresenter(THSBaseView uiBaseView) {
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

    protected void updateMedication(THSMedication pTHMedication) {
        if(null!=pTHMedication) {
            try {
                PTHManager.getInstance().updateMedication(uiBaseView.getFragmentActivity(), pTHMedication, this);

            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }

    }

    protected void addSearchedMedication(Medication medication) {
        ((THSMedicationFragment) uiBaseView).addSearchedMedicineToExistingMedication(medication);
    }

    //////////////// start of call backs for get existing medicines//////////////
    @Override
    public void onGetMedicationReceived(THSMedication pTHMedication, SDKError sDKError) {
        ((THSMedicationFragment) uiBaseView).hideProgressBar();
        if (null != pTHMedication.getMedicationList() && !pTHMedication.getMedicationList().isEmpty()) {
            AmwellLog.i("onGetMedicationReceived","Success");
            ((THSMedicationFragment) uiBaseView).showExistingMedicationList(pTHMedication);

        } else {
            AmwellLog.i("onGetMedicationReceived","failure");
        }


    }
    //////////////// end of call backs for get existing medicines//////////////


    //////////////// start of call backs for search medicines//////////////
    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {
        ((THSMedicationFragment) uiBaseView).hideProgressBar();
        AmwellLog.i("onFetchMedication","failure");
    }

    @Override
    public void onResponse(THSMedication THSMedication, SDKError sdkError) {
        ((THSMedicationFragment) uiBaseView).hideProgressBar();
        if (null!= THSMedication && null != THSMedication.getMedicationList() &&  !THSMedication.getMedicationList().isEmpty()) {

            AmwellLog.i("onFetchMedication","success");
            ((THSMedicationFragment) uiBaseView).showSearchedMedicationList(THSMedication);
        } else {
            AmwellLog.i("onFetchMedication","failure");
            ((THSMedicationFragment) uiBaseView).showToast("No match found");
        }
    }

    @Override
    public void onFailure(Throwable throwable) {

        ((THSMedicationFragment) uiBaseView).hideProgressBar();
        AmwellLog.i("onFetchMedication","failure");
        ((THSMedicationFragment) uiBaseView).showToast("Search failure");
    }
    //////////////// end of call backs for search medicines//////////////




    //////////////// start of call backs for update medicines//////////////


    @Override
    public void onUpdateMedicationSent(Void pVoid, SDKError sDKError) {
        ((THSMedicationFragment) uiBaseView).hideProgressBar();

        AmwellLog.i("onUpdateMedication","success");
        // TODO  call next intake fragment here

    }
    //////////////// end of call backs for update medicines//////////////
}
