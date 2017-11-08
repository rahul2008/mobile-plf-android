package com.philips.cdp2.ews.troubleshooting.setupaccesspointmode;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

public class SetupAccessPointModeTroubleshootingViewModelTest {

    @InjectMocks private SetupAccessPointModeTroubleshootingViewModel subject;

    @Mock private Navigator mockNavigator;
    @Mock private TroubleShootContentConfiguration mockTroubleShootContentConfiguration;
    @Mock private BaseContentConfiguration mockBaseContentConfiguration;
    @Mock private StringProvider mockStringProvider;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldNavigateToDevicePoweredOnConfirmationScreenWhenDoneClicked() throws Exception {
        subject.onDoneButtonClicked();

        verify(mockNavigator).navigateToCompletingDeviceSetupScreen();
    }

    @Test
    public void itShouldGiveResetConnectionImage() throws Exception {
        when(mockTroubleShootContentConfiguration.getSetUpAccessPointImage()).thenReturn(234234);
        subject.getsetupAccessPointImage(mockTroubleShootContentConfiguration);
        verify(mockStringProvider).getImageResource(mockTroubleShootContentConfiguration.getSetUpAccessPointImage());
    }

    @Test
    public void itShouldGiveResetConnectionTitle() throws Exception {
        when(mockTroubleShootContentConfiguration.getSetUpAccessPointTitle()).thenReturn(234234);
        subject.getTitle(mockTroubleShootContentConfiguration, mockBaseContentConfiguration);
        verify(mockStringProvider).getString(mockTroubleShootContentConfiguration.getSetUpAccessPointTitle(), mockBaseContentConfiguration.getDeviceName());
    }

    @Test
    public void itShouldGiveResetConnectionBody() throws Exception {
        when(mockTroubleShootContentConfiguration.getSetUpAccessPointBody()).thenReturn(234234);
        subject.getNote(mockTroubleShootContentConfiguration, mockBaseContentConfiguration);
        verify(mockStringProvider).getString(mockTroubleShootContentConfiguration.getSetUpAccessPointBody(), mockBaseContentConfiguration.getDeviceName());
    }

    @Test
    public void itShouldVerifyTrackPageName() throws Exception {
        verifyStatic();
        EWSTagger.trackPage("setupAccessPointMode");
    }

}