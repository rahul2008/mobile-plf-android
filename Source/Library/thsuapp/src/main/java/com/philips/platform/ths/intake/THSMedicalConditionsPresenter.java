/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_SAVE_MEDICAL_CONDITION;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;

public class THSMedicalConditionsPresenter implements THSBasePresenter, THSConditionsCallBack<THSConditionsList, THSSDKError>, THSUpdateConditionsCallback<Void, THSSDKError> {
    private THSBaseFragment thsBaseFragment;

    public THSMedicalConditionsPresenter(THSBaseFragment THSBaseFragment) {
        this.thsBaseFragment = THSBaseFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.continue_btn) {
            try {
                THSManager.getInstance().updateConditions(thsBaseFragment.getContext(), ((THSMedicalConditionsFragment) thsBaseFragment).getTHSConditions(), this);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }

        } else if (componentID == R.id.conditions_skip) {
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA,"stepsSkipped","medicalConditions");
            launchFollowUpFragment();
        }
    }

    private void launchFollowUpFragment() {
        final THSFollowUpFragment fragment = new THSFollowUpFragment();
        fragment.setFragmentLauncher(thsBaseFragment.getFragmentLauncher());
        thsBaseFragment.addFragment(fragment, THSFollowUpFragment.TAG, null, true);
    }

    public void getConditions() throws AWSDKInstantiationException {
        THSManager.getInstance().getConditions(thsBaseFragment.getFragmentActivity(), this);
    }

    @Override
    public void onResponse(THSConditionsList thsConditions, THSSDKError THSSDKError) {
        if (null != thsBaseFragment && thsBaseFragment.isFragmentAttached()) {

            final List<Condition> conditions = thsConditions.getConditions();

            List<THSCondition> THSConditionsList = new ArrayList<>();
            for (Condition condition : conditions) {
                THSCondition THSConditions = new THSCondition();
                THSConditions.setCondition(condition);
                THSConditionsList.add(THSConditions);
            }
            ((THSMedicalConditionsFragment) thsBaseFragment).setConditions(THSConditionsList);
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if (null != thsBaseFragment && thsBaseFragment.isFragmentAttached()) {
            thsBaseFragment.showToast(R.string.ths_se_server_error_toast_message);
        }
    }


    @Override
    public void onUpdateConditonResponse(Void aVoid, THSSDKError sdkError) {
        if (null != thsBaseFragment && thsBaseFragment.isFragmentAttached()) {
            if (null != sdkError.getSdkError()) {
                thsBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_SAVE_MEDICAL_CONDITION,sdkError.getSdkError()));
            } else {
                if(((THSMedicalConditionsFragment) thsBaseFragment).NumberOfConditionSelected>0) {
                    THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, "specialEvents", "step4MedicalConditionsAdded");
                }
                //Spoorti - This has no implementation as the UI would have got updated and we are sending the result to server.
                //On response, as of now no need to handle
                //Keeping this for future use
                launchFollowUpFragment();
            }

        }

    }

    @Override
    public void onUpdateConditionFailure(Throwable throwable) {
        if (null != thsBaseFragment && thsBaseFragment.isFragmentAttached()) {
            thsBaseFragment.showToast(R.string.ths_se_server_error_toast_message);
        }
    }
}
