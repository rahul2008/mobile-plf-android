/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.os.Bundle;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.health.Medication;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import static com.philips.platform.ths.R.id.ths_intake_medication_continue_button;
import static com.philips.platform.ths.R.id.ths_intake_medication_skip_step_label;

import static com.philips.platform.ths.R.id.ths_existing_medicine_footer_relative_layout;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_FETCH_MEDICATION;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_SAVE_MEDICATION;
import static com.philips.platform.ths.utility.THSConstants.MEDICATION_ON_ACTIVITY_RESULT;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;


public class THSMedicationPresenter implements THSBasePresenter, THSMedicationCallback.PTHGetMedicationCallback, THSMedicationCallback.PTHUpdateMedicationCallback {
    private THSBaseFragment mTHSBaseFragment;


    public THSMedicationPresenter(THSMedicationFragment tHSMedicationFragment) {
        this.mTHSBaseFragment = tHSMedicationFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == ths_intake_medication_continue_button) {
            updateMedication(((THSMedicationFragment) mTHSBaseFragment).mExistingMedication);
        } else if (componentID == ths_existing_medicine_footer_relative_layout) {
            THSSearchFragment tHSSearchFragment = new THSSearchFragment();
            tHSSearchFragment.setTargetFragment(mTHSBaseFragment, MEDICATION_ON_ACTIVITY_RESULT);
            tHSSearchFragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
            Bundle bundle = new Bundle();
            bundle.putInt(THSConstants.SEARCH_CONSTANT_STRING, THSConstants.MEDICATION_SEARCH_CONSTANT);
            mTHSBaseFragment.addFragment(tHSSearchFragment, THSSearchFragment.TAG, bundle, true);
        } else if (componentID == ths_intake_medication_skip_step_label) {
            final THSMedicalConditionsFragment fragment = new THSMedicalConditionsFragment();
            fragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
            mTHSBaseFragment.addFragment(fragment, THSMedicalConditionsFragment.TAG, null, true);
        }

    }


    protected void fetchMedication() {
        try {
            THSManager.getInstance().getMedication(mTHSBaseFragment.getContext(), this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }


    protected void updateMedication(THSMedication pTHMedication) {
        if (null != pTHMedication) {
            try {
                THSManager.getInstance().updateMedication(mTHSBaseFragment.getContext(), pTHMedication, this);

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
    public void onGetMedicationReceived(THSMedication thsMedication, SDKError sDKError) {
        if (null != mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            mTHSBaseFragment.hideProgressBar();
            if (null != sDKError && sDKError.getSDKErrorReason() != null) {
                mTHSBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_FETCH_MEDICATION,sDKError));
            } else {
                AmwellLog.i("onGetMedicationReceived", "Success");
                ((THSMedicationFragment) mTHSBaseFragment).showExistingMedicationList(thsMedication);
            }
        }

    }

    @Override
    public void onFailure(Throwable throwable) {
        if (null != mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            mTHSBaseFragment.showToast(R.string.ths_se_server_error_toast_message);
        }
    }
    //////////////// end of call backs for get existing medicines//////////////


    //////////////// start of call backs for update medicines//////////////


    @Override
    public void onUpdateMedicationSent(Void pVoid, SDKError sDKError) {

        if (null != mTHSBaseFragment && mTHSBaseFragment.isFragmentAttached()) {
            mTHSBaseFragment.hideProgressBar();
            if (null != sDKError) {
                mTHSBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_SAVE_MEDICATION,sDKError));
            } else {

                THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, "specialEvents", ((THSMedicationFragment) mTHSBaseFragment).tagAction);
                AmwellLog.i("onUpdateMedication", "success");
                // addF
                final THSMedicalConditionsFragment fragment = new THSMedicalConditionsFragment();
                fragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
                ((THSMedicationFragment) mTHSBaseFragment).addFragment(fragment, THSMedicalConditionsFragment.TAG, null, true);
            }
        }
    }
    //////////////// end of call backs for update medicines//////////////
}
