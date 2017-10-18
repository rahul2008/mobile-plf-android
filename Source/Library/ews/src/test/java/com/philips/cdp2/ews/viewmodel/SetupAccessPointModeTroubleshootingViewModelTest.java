package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.troubleshooting.setupaccesspointmode.SetupAccessPointModeTroubleshootingViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SetupAccessPointModeTroubleshootingViewModelTest {

    @InjectMocks private SetupAccessPointModeTroubleshootingViewModel subject;

    @Mock private Navigator mockNavigator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldNavigateToDevicePoweredOnConfirmationScreenWhenDoneClicked() throws Exception {
        subject.onDoneButtonClicked();

        verify(mockNavigator).navigateToDevicePoweredOnConfirmationScreen();
    }

}