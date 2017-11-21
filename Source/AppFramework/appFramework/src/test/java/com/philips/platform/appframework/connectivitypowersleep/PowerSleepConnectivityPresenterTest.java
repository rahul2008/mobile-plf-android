/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep;

import android.content.Context;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.TestActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPort;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.insights.InsightsFragmentState;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.UIView;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 8/30/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class PowerSleepConnectivityPresenterTest {

    @Mock
    private ConnectivityPowerSleepContract.View view;

    private Context context;

    @Mock
    private UIView uiView;

    PowerSleepConnectivityPresenter powerSleepConnectivityPresenter;

    @Captor
    private ArgumentCaptor<DICommPortListener<SessionDataPort>> portListenerArgumentCaptor;

    @Mock
    private RefAppBleReferenceAppliance bleReferenceAppliance;

    @Mock
    private SessionDataPort sessionDataPort;

    @Mock
    private SessionDataPortProperties sessionDataPortProperties;

    DICommPortListener<SessionDataPort> portListener;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setUp() {
        context = mock(TestActivity.class);
        powerSleepConnectivityPresenter = new PowerSleepConnectivityPresenter(context, view, uiView);
        when(bleReferenceAppliance.getSessionDataPort()).thenReturn(sessionDataPort);
        when(sessionDataPort.getPortProperties()).thenReturn(sessionDataPortProperties);
        powerSleepConnectivityPresenter.setUpApplicance(bleReferenceAppliance);
        verify(sessionDataPort).addPortListener(portListenerArgumentCaptor.capture());
        portListener = portListenerArgumentCaptor.getValue();


    }

    @Test
    public void onPortUpdateTest() {
        portListener.onPortUpdate(sessionDataPort);
        verify(view).updateSessionData(anyLong(), anyLong(), anyLong());
    }
    @Test
    public void onPortErrorTest() {
        portListener.onPortError(sessionDataPort, Error.CANNOT_CONNECT, "");
        verify(view).showError(Error.CANNOT_CONNECT, "");
    }

    @Test
    public void removeSessionPortListenerTest() {
        powerSleepConnectivityPresenter.removeSessionPortListener(bleReferenceAppliance);
        verify(sessionDataPort).removePortListener(portListener);
    }

    @Test
    public void setUpApplicanceWithNullValueTest() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(equalTo("Cannot create bleReferenceAppliance for provided NetworkNode."));
        powerSleepConnectivityPresenter.setUpApplicance(null);
    }

    @Test
    public void onEventTest() {
        final InsightsFragmentState insightsFragmentState = mock(InsightsFragmentState.class);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        FlowManager uiFlowManagerMock = mock(FlowManager.class);
        when(context.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        when(appFrameworkApplicationMock.getTargetFlowManager()).thenReturn(uiFlowManagerMock);
        when(uiFlowManagerMock.getNextState(uiFlowManagerMock.getState(AppStates.POWER_SLEEP_CONNECTIVITY),AppStates.INSIGHTS)).thenReturn(insightsFragmentState);
        powerSleepConnectivityPresenter.onEvent(R.id.insights);
        verify(insightsFragmentState).navigate(any(FragmentLauncher.class));
    }

    @After
    public void tearDown() {
        view = null;
        context = null;
        uiView = null;
        powerSleepConnectivityPresenter = null;
        portListenerArgumentCaptor = null;
        portListener = null;
        bleReferenceAppliance = null;
        sessionDataPort = null;
        sessionDataPortProperties = null;
    }

}
