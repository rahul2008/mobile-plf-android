package com.philips.platform.ths.intake;

import android.app.Fragment;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.health.Medication;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.appinfra.tagging.AppTaggingConstants;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;

import java.util.Map;

import static com.philips.platform.ths.R.id.pth_intake_medication_continue_button;
import static com.philips.platform.ths.R.id.pth_search_medication_relative_layout;


public class THSMedicationPresenter implements THSBasePresenter, THSMedicationCallback.PTHGetMedicationCallback, THSMedicationCallback.PTHUpdateMedicationCallback {
    THSBaseFragment mTHSBaseFragment;


    public THSMedicationPresenter(THSMedicationFragment tHSMedicationFragment) {
        this.mTHSBaseFragment = tHSMedicationFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if(componentID == pth_intake_medication_continue_button){
            mTHSBaseFragment.showProgressBar();
            updateMedication( ((THSMedicationFragment )mTHSBaseFragment).mExistingMedication);
        } else if (componentID == pth_search_medication_relative_layout){
            THSMedicationSearchFragment tHSMedicationSearchFragment= new THSMedicationSearchFragment();
            tHSMedicationSearchFragment.setTargetFragment(((THSMedicationFragment )mTHSBaseFragment), 123);
            ((THSMedicationFragment )mTHSBaseFragment).addFragment(tHSMedicationSearchFragment, THSMedicationSearchFragment.TAG, null);
        }
       // mTHSBaseFragment.addFragment(new THSConditionsFragment(),THSConditionsFragment.TAG,null);
    }


    protected void fetchMedication() {
        mTHSBaseFragment.showProgressBar();
        try {
            THSManager.getInstance().getMedication(mTHSBaseFragment.getFragmentActivity(), this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }



    protected void updateMedication(THSMedication pTHMedication) {
        if(null!=pTHMedication) {
            try {
                THSManager.getInstance().updateMedication(mTHSBaseFragment.getFragmentActivity(), pTHMedication, this);

            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }

    }

    protected void addSearchedMedication(Medication medication) {
        ((THSMedicationFragment) mTHSBaseFragment).addSearchedMedicineToExistingMedication(medication);
    }

    //////////////// start of call backs for get existing medicines//////////////
    @Override
    public void onGetMedicationReceived(THSMedication pTHMedication, SDKError sDKError) {
        ((THSMedicationFragment) mTHSBaseFragment).hideProgressBar();
            AmwellLog.i("onGetMedicationReceived","Success");
            ((THSMedicationFragment) mTHSBaseFragment).showExistingMedicationList(pTHMedication);




    }
    //////////////// end of call backs for get existing medicines//////////////






    //////////////// start of call backs for update medicines//////////////


    @Override
    public void onUpdateMedicationSent(Void pVoid, SDKError sDKError) {
        ((THSMedicationFragment) mTHSBaseFragment).hideProgressBar();

        AmwellLog.i("onUpdateMedication","success");
        // TODO  call next intake fragment here

    }
    //////////////// end of call backs for update medicines//////////////
}
