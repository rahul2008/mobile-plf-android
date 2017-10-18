package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.troubleshooting.connecttowrongphone.ConnectToWrongPhoneTroubleshootingViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConnectToWrongPhoneTroubleshootingViewModelTest {

    @InjectMocks private ConnectToWrongPhoneTroubleshootingViewModel subject;

    @Mock private Navigator mockNavigator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldNavigateSetupAccessPointModeScreenWhenYesClicked() throws Exception {
        subject.onYesButtonClicked();

        verify(mockNavigator).navigateSetupAccessPointModeScreen();
    }

    @Test
    public void itShouldNavigateToResetConnectionTroubleShootingScreenWhenNoClicked() throws Exception {
        subject.onNoButtonClicked();

        verify(mockNavigator).navigateToResetConnectionTroubleShootingScreen();
    }
}
