package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import android.os.Bundle;

import com.philips.cdp2.ews.navigation.Navigator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class WrongWifiNetworkViewModelTest {

    @InjectMocks private WrongWifiNetworkViewModel subject;

    @Mock private Navigator mockNavigator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldUpdateDescription() throws Exception {
        String description = "Awesome desc";
        subject.setDescription(description);

        assertEquals(description, subject.description.get());
    }

    @Test
    public void itShouldNavigateToConnectingDeviceWithWifiScreenWhenButtonClicked() throws
            Exception {
        subject.onButtonClick();

        verify(mockNavigator).navigateToConnectingDeviceWithWifiScreen(any(Bundle.class));
    }
}