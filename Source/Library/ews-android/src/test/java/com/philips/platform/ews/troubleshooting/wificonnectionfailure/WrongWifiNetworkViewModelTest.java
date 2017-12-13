/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.troubleshooting.wificonnectionfailure;

import android.os.Bundle;

import com.philips.platform.ews.R;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class WrongWifiNetworkViewModelTest {

    private WrongWifiNetworkViewModel subject;

    @Mock private Navigator mockNavigator;

    @Mock private BaseContentConfiguration mockBaseContentConfiguration;
    @Mock private EWSTagger mockEWSTagger;

    @Before
    public void setUp() throws Exception {
        mockStatic(EWSTagger.class);
        initMocks(this);
        subject = new WrongWifiNetworkViewModel(mockNavigator,mockBaseContentConfiguration, mockEWSTagger);
    }

    @Test
    public void itShouldUpdateUpperDescription() throws Exception {
        String description = "Awesome desc";
        subject.setUpperDescription(description);

        assertEquals(description, subject.upperDescription.get());
    }

    @Test
    public void itShouldUpdateLowerDescription() throws Exception {
        String description = "Awesome desc";
        subject.setLowerDescription(description);

        assertEquals(description, subject.lowerDescription.get());
    }

    @Test
    public void itShouldUpdateStepFourText() throws Exception {
        String stepFour = "Awesome step four";
        subject.setStepFourText(stepFour);

        assertEquals(stepFour, subject.stepFourText.get());
    }

    @Test
    public void itShouldNavigateToConnectingDeviceWithWifiScreenWhenButtonClicked() throws
            Exception {
        subject.onButtonClick();

        verify(mockNavigator).navigateToConnectingDeviceWithWifiScreen(any(Bundle.class), eq(true));
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
        subject.trackPageName();
        verify(mockEWSTagger).trackPage("wrongWifiNetwork");
    }
}