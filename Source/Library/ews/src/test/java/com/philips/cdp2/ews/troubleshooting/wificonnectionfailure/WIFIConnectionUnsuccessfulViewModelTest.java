package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

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
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
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

    @Test
    public void itShouldVerifyTrackPageName() throws Exception {
        verifyStatic();
        EWSTagger.trackPage("connectionUnsuccessful");
    }
}
