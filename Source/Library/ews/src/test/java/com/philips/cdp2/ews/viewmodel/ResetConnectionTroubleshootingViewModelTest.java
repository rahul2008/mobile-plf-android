package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.navigation.Navigator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ResetConnectionTroubleshootingViewModelTest {

    @InjectMocks private ResetConnectionTroubleshootingViewModel subject;

    @Mock private Navigator mockNavigator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldNavigateToResetDeviceTroubleShootingScreenWhenYesClicked() throws Exception {
        subject.onYesButtonClicked();

        verify(mockNavigator).navigateToResetDeviceTroubleShootingScreen();
    }

    @Test
    public void itShouldNavigateToConnectToWrongPhoneTroubleShootingScreenWhenNoClicked() throws Exception {
        subject.onNoButtonClicked();

        verify(mockNavigator).navigateToConnectToWrongPhoneTroubleShootingScreen();
    }
}