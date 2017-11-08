package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import android.os.Bundle;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class WrongWifiNetworkViewModelTest {

    @InjectMocks private WrongWifiNetworkViewModel subject;

    @Mock private Navigator mockNavigator;

    @Mock private BaseContentConfiguration mockBaseContentConfiguration;

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

    @Test
    public void itShouldSetTheBundle() throws Exception {
        subject.setBundle(mock(Bundle.class));

        assertNotNull(subject.getBundle());
    }

    @Test
    public void itShouldReturnIntForAppName() throws Exception {
        when(mockBaseContentConfiguration.getAppName()).thenReturn(R.string.app_name);
        assertEquals(R.string.app_name, subject.getAppName());
    }

    @Test
    public void itShouldVerifyTrackPageName() throws Exception {
        verifyStatic();
        EWSTagger.trackPage("wrongWifiNetwork");
    }
}