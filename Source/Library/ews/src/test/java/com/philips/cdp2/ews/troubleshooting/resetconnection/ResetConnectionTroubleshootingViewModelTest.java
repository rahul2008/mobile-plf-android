package com.philips.cdp2.ews.troubleshooting.resetconnection;

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

public class ResetConnectionTroubleshootingViewModelTest {

    @InjectMocks private ResetConnectionTroubleshootingViewModel subject;

    @Mock private Navigator mockNavigator;
    @Mock private TroubleShootContentConfiguration mockTroubleShootContentConfiguration;
    @Mock private BaseContentConfiguration mockBaseContentConfiguration;
    @Mock private StringProvider mockStringProvider;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(124234);
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

    @Test
    public void itShouldGiveResetConnectionImage() throws Exception {
        when(mockTroubleShootContentConfiguration.getResetConnectionImage()).thenReturn(234234);
        subject.getResetConnectionImage(mockTroubleShootContentConfiguration);
        verify(mockStringProvider).getImageResource(mockTroubleShootContentConfiguration.getResetConnectionImage());
    }

    @Test
    public void itShouldGiveResetConnectionTitle() throws Exception {
        when(mockTroubleShootContentConfiguration.getResetConnectionTitle()).thenReturn(234234);
        subject.getTitle(mockTroubleShootContentConfiguration, mockBaseContentConfiguration);
        verify(mockStringProvider).getString(mockTroubleShootContentConfiguration.getResetConnectionTitle(), mockBaseContentConfiguration.getDeviceName());
    }

    @Test
    public void itShouldGiveResetConnectionBody() throws Exception {
        when(mockTroubleShootContentConfiguration.getResetConnectionBody()).thenReturn(234234);
        subject.getNote(mockTroubleShootContentConfiguration, mockBaseContentConfiguration);
        verify(mockStringProvider).getString(mockTroubleShootContentConfiguration.getResetConnectionBody(), mockBaseContentConfiguration.getDeviceName());
    }

    @Test
    public void itShouldVerifyTrackPageName() throws Exception {
        verifyStatic();
        EWSTagger.trackPage("resetConnection");
    }


}