package com.philips.cdp2.ews.troubleshooting.setupaccesspointmode;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class SetupAccessPointModeTroubleshootingViewModelTest {

    private SetupAccessPointModeTroubleshootingViewModel subject;

    @Mock private Navigator mockNavigator;
    @Mock private TroubleShootContentConfiguration mockTroubleShootContentConfiguration;
    @Mock private BaseContentConfiguration mockBaseContentConfiguration;
    @Mock private StringProvider mockStringProvider;
    @Mock private EWSTagger mockEWSTagger;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        subject = new SetupAccessPointModeTroubleshootingViewModel(mockNavigator, mockStringProvider, mockBaseContentConfiguration, mockTroubleShootContentConfiguration, mockEWSTagger);
    }

    @Test
    public void itShouldNavigateToDevicePoweredOnConfirmationScreenWhenDoneClicked() throws Exception {
        subject.onDoneButtonClicked();

        verify(mockNavigator).navigateToCompletingDeviceSetupScreen();
    }

    @Test
    public void itShouldGiveResetConnectionImage() throws Exception {
        when(mockTroubleShootContentConfiguration.getSetUpAccessPointImage()).thenReturn(234234);
        subject.getSetupAccessPointImage(mockTroubleShootContentConfiguration);
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
        subject.trackPageName();
        verify(mockEWSTagger).trackPage("setupAccessPointMode");
    }

    @Test
    public void itShouldNavigateOnYesButtonClicked() throws Exception {
        subject.onYesButtonClicked();
        verify(mockNavigator).navigateToResetDeviceTroubleShootingScreen();
    }

    @Test
    public void itShouldNavigateOnNoButtonClicked() throws Exception {
        subject.onNoButtonClicked();
        verify(mockNavigator).navigateToConnectToWrongPhoneTroubleShootingScreen();
    }
}