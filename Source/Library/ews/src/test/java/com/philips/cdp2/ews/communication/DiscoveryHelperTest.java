package com.philips.cdp2.ews.communication;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiscoveryHelperTest {

    @InjectMocks private DiscoveryHelper subject;

    @Mock private CommCentral mockCommCentral;
    @Mock private DiscoveryHelper.DiscoveryCallback mockCallback;
    @Mock private ApplianceManager mockApplianceManager;
    @Mock private Appliance mockAppliance;

    @Captor private ArgumentCaptor<ApplianceManager.ApplianceListener<Appliance>> captor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(mockCommCentral.getApplianceManager()).thenReturn(mockApplianceManager);
    }

    @Ignore
    @Test
    public void itShouldAddListenerWhenDiscoveryIsStarted() throws Exception {
        subject.startDiscovery(mockCallback);

        verify(mockApplianceManager).addApplianceListener(any(ApplianceManager.ApplianceListener.class));
    }

    @Ignore
    @Test
    public void itShouldRemoveListenerWhenDiscoveryIsStopped() throws Exception {
        subject.stopDiscovery();

        verify(mockApplianceManager).removeApplianceListener(any(ApplianceManager.ApplianceListener.class));
    }

    @Ignore
    @Test
    public void itShouldStartDiscoveryForCommCentralWhenDiscoveryStarted() throws Exception {
        subject.startDiscovery(mockCallback);

        verify(mockCommCentral).startDiscovery();
    }

    @Ignore
    @Test
    public void itShouldStopDiscoveryForCommCentralWhenDiscoveryStops() throws Exception {
        subject.stopDiscovery();

        verify(mockCommCentral).stopDiscovery();
    }

    @Ignore
    @Test
    public void itShouldNotifyCallbackWhenApplianceIsFound() throws Exception {
        subject.startDiscovery(mockCallback);

        verify(mockApplianceManager).addApplianceListener(captor.capture());
        captor.getValue().onApplianceFound(mockAppliance);

        verify(mockCallback).onApplianceFound(any(Appliance.class));
    }
}