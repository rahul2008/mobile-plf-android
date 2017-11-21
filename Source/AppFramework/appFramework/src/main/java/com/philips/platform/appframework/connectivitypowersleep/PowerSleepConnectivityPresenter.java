/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPort;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.UIView;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import static com.janrain.android.engage.JREngage.getApplicationContext;

public class PowerSleepConnectivityPresenter extends AbstractUIBasePresenter implements ConnectivityPowerSleepContract.UserActionsListener {
    public static final String TAG = PowerSleepConnectivityPresenter.class.getSimpleName();
    private ConnectivityPowerSleepContract.View connectivityViewListener;
    private Context context;

    public PowerSleepConnectivityPresenter(Context context, final ConnectivityPowerSleepContract.View connectivityViewListener, UIView uiView) {
        super(uiView);
        this.context = context;
        this.connectivityViewListener = connectivityViewListener;
    }

    @Override
    public void removeSessionPortListener(RefAppBleReferenceAppliance appliance) {
        if (appliance != null) {
            appliance.getSessionDataPort().removePortListener(diCommPortListener);
        }
    }

    @Override
    public void setUpApplicance(@NonNull RefAppBleReferenceAppliance appliance) {
        if (appliance == null) {
            throw new IllegalArgumentException("Cannot create bleReferenceAppliance for provided NetworkNode.");
        }

        appliance.getSessionDataPort().addPortListener(diCommPortListener);
    }

    DICommPortListener diCommPortListener = new DICommPortListener<SessionDataPort>() {
        @Override
        public void onPortUpdate(SessionDataPort diCommPort) {
            connectivityViewListener.updateSessionData(diCommPort.getPortProperties().getTotalSleepTime(), diCommPort.getPortProperties().getNumberOfInterruptions(), diCommPort.getPortProperties().getDeepSleepTime());

        }

        @Override
        public void onPortError(SessionDataPort diCommPort, Error error, @Nullable String s) {
            connectivityViewListener.showError(error, s);
        }
    };

    @Override
    public void onEvent(int componentID) {
        BaseFlowManager targetFlowManager = ((AppFrameworkApplication) context.getApplicationContext()).getTargetFlowManager();

        try {
            BaseState baseState = targetFlowManager.getNextState(targetFlowManager.getState(AppStates.POWER_SLEEP_CONNECTIVITY), AppStates.INSIGHTS);
            if (null != baseState) {
                baseState.navigate(new FragmentLauncher((FragmentActivity) context, R.id.frame_container, (ActionBarListener) ((FragmentActivity) context)));
            }
        } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            RALog.d(TAG, e.getMessage());
            Toast.makeText(getApplicationContext(), context.getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
        }

    }
}
