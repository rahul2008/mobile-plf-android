package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.health.Medication;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;

import static com.philips.platform.ths.R.id.pth_intake_medication_continue_button;
import static com.philips.platform.ths.R.id.pth_intake_medication_skip_step_label;

import static com.philips.platform.ths.R.id.ths_existing_medicine_footer_relative_layout;
import static com.philips.platform.ths.utility.THSConstants.MEDICATION_ON_ACTIVITY_RESULT;


public class THSMedicationPresenter implements THSBasePresenter, THSMedicationCallback.PTHGetMedicationCallback, THSMedicationCallback.PTHUpdateMedicationCallback {
    THSBaseFragment mTHSBaseFragment;


    public THSMedicationPresenter(THSMedicationFragment tHSMedicationFragment) {
        this.mTHSBaseFragment = tHSMedicationFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if(componentID == pth_intake_medication_continue_button){
            updateMedication( ((THSMedicationFragment )mTHSBaseFragment).mExistingMedication);
        } else if (componentID == ths_existing_medicine_footer_relative_layout){
            THSMedicationSearchFragment tHSMedicationSearchFragment= new THSMedicationSearchFragment();
            tHSMedicationSearchFragment.setTargetFragment(((THSMedicationFragment )mTHSBaseFragment), MEDICATION_ON_ACTIVITY_RESULT);
            tHSMedicationSearchFragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
            ((THSMedicationFragment )mTHSBaseFragment).addFragment(tHSMedicationSearchFragment, THSMedicationSearchFragment.TAG, null);
        }else if (componentID == pth_intake_medication_skip_step_label){
            final THSConditionsFragment fragment = new THSConditionsFragment();
            fragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
            mTHSBaseFragment.addFragment(fragment,THSConditionsFragment.TAG,null);
        }

    }


    protected void fetchMedication() {
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
        // addF
        final THSConditionsFragment fragment = new THSConditionsFragment();
        fragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
        ((THSMedicationFragment )mTHSBaseFragment).addFragment(fragment, THSConditionsFragment.TAG, null);

    }
    //////////////// end of call backs for update medicines//////////////
}
