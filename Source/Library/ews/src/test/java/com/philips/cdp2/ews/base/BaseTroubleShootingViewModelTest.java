package com.philips.cdp2.ews.base;

import com.philips.cdp2.ews.navigation.Navigator;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BaseTroubleShootingViewModelTest {
    @Test
    public void itShouldNavigateToDevicePoweredOnConfirmationScreen() throws Exception {
        Navigator mockNavigator = mock(Navigator.class);
        BaseTroubleShootingViewModel subject = new BaseTroubleShootingViewModel(mockNavigator);
        subject.onCancelButtonClicked();
        verify(mockNavigator).navigateToCompletingDeviceSetupScreen();
    }
}
