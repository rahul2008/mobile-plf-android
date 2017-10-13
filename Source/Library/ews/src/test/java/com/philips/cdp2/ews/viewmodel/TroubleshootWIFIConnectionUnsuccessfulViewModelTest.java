/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.view.TroubleshootIncorrectPasswordFragment;
import com.philips.cdp2.ews.view.TroubleshootWrongWiFiFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TroubleshootWIFIConnectionUnsuccessfulViewModelTest {

    private TroubleshootConnectionUnsuccessfulViewModel subject;
    @Mock
    ScreenFlowController screenFlowControllerMock;

    @Before
    public void setup() {
        initMocks(this);
        subject = new TroubleshootConnectionUnsuccessfulViewModel(screenFlowControllerMock);
    }

    @Test
    public void shouldLaunchWrongWifiFragmentWhenUserSelectsIconBlicking() {
        subject.showConnectionErrorDifferentNetwork();

        verify(screenFlowControllerMock).showFragment(any(TroubleshootWrongWiFiFragment.class));
    }

    @Test
    public void shouldLaunchIncorrectPasswordWhenUserSelectsNoIconBlinking() {
        subject.showConnectionErrorDeviceNotFound();

        verify(screenFlowControllerMock).showFragment(any(TroubleshootIncorrectPasswordFragment.class));
    }
}