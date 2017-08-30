/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 8/30/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class PowerSleepConnectivityPresenterTest {

    @Mock
    private ConnectivityPowerSleepContract.View view;

    PowerSleepConnectivityPresenter powerSleepConnectivityPresenter;

    @Captor
    private ArgumentCaptor<DICommPortListener<SessionDataPort>> captor;

    @Mock
    private BleReferenceAppliance bleReferenceAppliance;

    @Mock
    private SessionDataPort sessionDataPort;

    @Mock
    private SessionDataPortProperties sessionDataPortProperties;

    DICommPortListener<SessionDataPort> value;


    @Before
    public void setUp() {
        powerSleepConnectivityPresenter = new PowerSleepConnectivityPresenter(view);
        when(bleReferenceAppliance.getSessionDataPort()).thenReturn(sessionDataPort);
        when(sessionDataPort.getPortProperties()).thenReturn(sessionDataPortProperties);
        powerSleepConnectivityPresenter.setUpApplicance(bleReferenceAppliance);
        verify(sessionDataPort).addPortListener(captor.capture());
        value= captor.getValue();


    }

    @Test
    public void onPortUpdateTest() throws Exception {
        value.onPortUpdate(sessionDataPort);
        verify(view).updateSessionData(anyLong(), anyLong(), anyLong());
    }
    @Test
    public void onPortErrorTest() throws Exception {
        value.onPortError(sessionDataPort, Error.CANNOT_CONNECT, "");
        verify(view).showError(Error.CANNOT_CONNECT, "");
    }

    @Test
    public void removeSessionPortListenerTest() {
        powerSleepConnectivityPresenter.removeSessionPortListener(bleReferenceAppliance);
        verify(sessionDataPort).removePortListener(value);
    }

    @After
    public void tearDown() {
        view = null;
        powerSleepConnectivityPresenter = null;
        captor = null;
        value = null;
        bleReferenceAppliance = null;
        sessionDataPort = null;
        sessionDataPortProperties = null;
    }

}
