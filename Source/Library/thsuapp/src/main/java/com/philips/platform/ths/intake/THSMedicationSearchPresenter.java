package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;

import java.util.Map;

/**
 * Created by philips on 7/11/17.
 */

public class THSMedicationSearchPresenter implements THSBasePresenter, THSSDKValidatedCallback<THSMedication, SDKError> {

    THSBaseView uiBaseView;


public THSMedicationSearchPresenter(THSBaseView uiBaseView) {
    this.uiBaseView = uiBaseView;
}


    //////////////// start of call backs for search medicines//////////////
    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {

        AmwellLog.i("onFetchMedication","failure");
    }

    @Override
    public void onResponse(THSMedication THSMedication, SDKError sdkError) {

        if (null!= THSMedication && null != THSMedication.getMedicationList() &&  !THSMedication.getMedicationList().isEmpty()) {

            AmwellLog.i("onFetchMedication","success");
            // if user deletes string to less than 3 character before response comes then response should not be shown
            if(((THSMedicationSearchFragment) uiBaseView).searchBox.getSearchTextView().getText().length()>2) {
                ((THSMedicationSearchFragment) uiBaseView).searchedMedicines=THSMedication;
                ((THSMedicationSearchFragment) uiBaseView).mTHSSearchedMedicationListAdapter.setData( ((THSMedicationSearchFragment) uiBaseView).searchedMedicines);
            }
        } else {

            AmwellLog.i("onFetchMedication","failure");
            //((THSMedicationSearchFragment) uiBaseView).showToast("No match found");
        }
    }

    @Override
    public void onFailure(Throwable throwable) {


        AmwellLog.i("onFetchMedication","failure");
        ((THSMedicationFragment) uiBaseView).showToast("Search failure");
    }


    //////////////// end of call backs for search medicines//////////////


    @Override
    public void onEvent(int componentID) {

    }

    void searchMedication(String medication){
        try {
            THSManager.getInstance().searchMedication(uiBaseView.getFragmentActivity(), medication,  this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }
}
