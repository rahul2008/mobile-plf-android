/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.userregistration;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

public class UserRegistrationSettingsState extends UserRegistrationState {
    public static final String TAG = UserRegistrationSettingsState.class.getSimpleName();

    private String SETTINGS_LOGOUT = "logout";
    /**
     * AppFlowState constructor
     *
     */
    public UserRegistrationSettingsState() {
        super(AppStates.SETTINGS_REGISTRATION);
    }


   /* @Override
    public void onUserLogoutSuccess() {
        try {
            BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
            targetFlowManager.getBackState();
            BaseState baseState = targetFlowManager.getNextState(targetFlowManager.getState(AppStates.SETTINGS_REGISTRATION), SETTINGS_LOGOUT);
            if (baseState != null)
                baseState.navigate(new FragmentLauncher(getFragmentActivity(), R.id.frame_container, (ActionBarListener) getFragmentActivity()));
        } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            RALog.d(TAG, e.getMessage());
            Toast.makeText(getFragmentActivity(), getFragmentActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }*/

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) getFragmentActivity().getApplication();
    }

}
