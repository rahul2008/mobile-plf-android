package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import com.philips.cdp2.ews.navigation.Navigator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class WIFIConnectionUnsuccessfulViewModelTest {

    @InjectMocks private WIFIConnectionUnsuccessfulViewModel subject;

    @Mock private Navigator mockNavigator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldUpdateDescription() throws Exception {
        String description = "Awesome description";
        subject.setDescription(description);

        assertEquals(description, subject.description.get());
    }

    @Test
    public void itShouldNavigateToWifiConfirmationScreenWhenTryAgainButtonIsClicked() throws Exception {
        subject.onTryAgainClicked();

        verify(mockNavigator).navigateToHomeNetworkConfirmationScreen();
    }
}
