package com.philips.cdp2.ews.troubleshooting.resetconnection;

import android.graphics.drawable.Drawable;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.troubleshooting.resetconnection.ResetConnectionTroubleshootingViewModel;
import com.philips.cdp2.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ResetConnectionTroubleshootingViewModelTest {

    private ResetConnectionTroubleshootingViewModel subject;

    @Mock
    TroubleShootContentConfiguration mockTroubleShootContentConfiguration;

    @Mock
    BaseContentConfiguration mockBaseContentConfiguration;

    @Mock
    StringProvider mockStringProvider;

    @Mock private Navigator mockNavigator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new ResetConnectionTroubleshootingViewModel(mockNavigator, mockStringProvider, mockBaseContentConfiguration, mockTroubleShootContentConfiguration);
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


}