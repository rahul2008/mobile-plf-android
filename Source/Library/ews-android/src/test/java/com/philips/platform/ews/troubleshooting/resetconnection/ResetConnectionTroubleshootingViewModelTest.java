package com.philips.platform.ews.troubleshooting.resetconnection;

import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.configuration.TroubleShootContentConfiguration;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.util.StringProvider;

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
public class ResetConnectionTroubleshootingViewModelTest {

    private ResetConnectionTroubleshootingViewModel subject;

    @Mock
    private Navigator mockNavigator;
    @Mock
    private TroubleShootContentConfiguration mockTroubleShootContentConfiguration;
    @Mock
    private BaseContentConfiguration mockBaseContentConfiguration;
    @Mock
    private StringProvider mockStringProvider;
    @Mock
    private EWSTagger mockEWSTagger;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(124234);
        subject = new ResetConnectionTroubleshootingViewModel(mockNavigator, mockStringProvider, mockBaseContentConfiguration,
                mockTroubleShootContentConfiguration, mockEWSTagger);
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
        subject.trackPageName();
        verify(mockEWSTagger).trackPage("resetConnection");
    }


}