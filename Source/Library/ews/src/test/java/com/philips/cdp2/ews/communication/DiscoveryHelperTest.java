package com.philips.cdp2.ews.communication;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.ews.logger.EWSLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CommCentral.class, EWSLogger.class})
public class DiscoveryHelperTest {

    private DiscoveryHelper subject;

    @Mock private CommCentral mockCommCentral;
    @Mock private DiscoveryHelper.DiscoveryCallback mockCallback;
    @Mock private ApplianceManager mockApplianceManager;
    @Mock private Appliance mockAppliance;
    @Mock private EWSLogger mockEWSLogger;

    @Captor private ArgumentCaptor<ApplianceManager.ApplianceListener<Appliance>> captor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(CommCentral.class);
        PowerMockito.mockStatic(EWSLogger.class);
        subject = new DiscoveryHelper(mockCommCentral);
        when(mockCommCentral.getApplianceManager()).thenReturn(mockApplianceManager);
    }

    @Test
    public void itShouldAddListenerWhenDiscoveryIsStarted() throws Exception {
        subject.startDiscovery(mockCallback);

        verify(mockApplianceManager).addApplianceListener(any(ApplianceManager.ApplianceListener.class));
    }

    @Test
    public void itShouldStartDiscoveryForCommCentralWhenDiscoveryStarted() throws Exception {
        subject.startDiscovery(mockCallback);

        verify(mockCommCentral).startDiscovery();
    }

    @Test
    public void itShouldStopDiscoveryForCommCentralWhenDiscoveryStops() throws Exception {
        subject.stopDiscovery();

        verify(mockCommCentral).stopDiscovery();
    }

    @Test
    public void itShouldNotifyCallbackWhenApplianceIsFound() throws Exception {
        subject.startDiscovery(mockCallback);

        verify(mockApplianceManager).addApplianceListener(captor.capture());
        captor.getValue().onApplianceFound(mockAppliance);

        verify(mockCallback).onApplianceFound(any(Appliance.class));
    }

    @Test
    public void itShouldVerifyMissingPermissionException() throws Exception{
        Mockito.doThrow(new MissingPermissionException("some message")).when(mockCommCentral).startDiscovery();
        subject.startDiscovery(mockCallback);
        verify(mockEWSLogger).e(eq("DiscoveryHelper"), anyString());
    }
}