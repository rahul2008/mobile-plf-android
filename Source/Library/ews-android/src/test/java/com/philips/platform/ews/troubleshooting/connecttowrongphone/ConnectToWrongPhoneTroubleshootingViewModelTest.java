package com.philips.platform.ews.troubleshooting.connecttowrongphone;

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
public class ConnectToWrongPhoneTroubleshootingViewModelTest {

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

    private ConnectToWrongPhoneTroubleshootingViewModel subject;



    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        when(mockTroubleShootContentConfiguration.getConnectWrongPhoneTitle()).thenReturn(1231231);
        when(mockTroubleShootContentConfiguration.getConnectWrongPhoneBody()).thenReturn(1231232);
        when(mockTroubleShootContentConfiguration.getConnectWrongPhoneQuestion()).thenReturn(1231233);
        when(mockTroubleShootContentConfiguration.getConnectWrongPhoneImage()).thenReturn(1231234);
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(1231235);
        subject = new ConnectToWrongPhoneTroubleshootingViewModel(mockNavigator, mockStringProvider,
                mockBaseContentConfiguration, mockTroubleShootContentConfiguration, mockEWSTagger);

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

    @Test
    public void itShouldVerifyThatGetTitleShouldBeCalledAtConstructorTime() throws Exception {
        verify(mockTroubleShootContentConfiguration).getConnectWrongPhoneTitle();
    }

    @Test
    public void itShouldVerifyThatGetBodyShouldBeCalledAtConstructorTime() throws Exception {
        verify(mockTroubleShootContentConfiguration).getConnectWrongPhoneBody();
    }

    @Test
    public void itShouldVerifyThatGetQuestionShouldBeCalledAtConstructorTime() throws Exception {
        verify(mockTroubleShootContentConfiguration).getConnectWrongPhoneQuestion();
    }

    @Test
    public void itShouldVerifyThatGetWrongPhoneImageShouldBeCalledAtConstructorTime() throws Exception {
        verify(mockTroubleShootContentConfiguration).getConnectWrongPhoneImage();
    }

    @Test
    public void itShouldGiveConnectWrongPhoneTitle() throws Exception {
        verify(mockStringProvider).getString(mockTroubleShootContentConfiguration.getConnectWrongPhoneTitle(), mockBaseContentConfiguration.getDeviceName());
    }

    @Test
    public void itShouldGiveConnectWrongPhoneBody() throws Exception {
        verify(mockStringProvider).getString(mockTroubleShootContentConfiguration.getConnectWrongPhoneBody(), mockBaseContentConfiguration.getDeviceName());
    }

    @Test
    public void itShouldGiveConnectWrongPhoneQuestion() throws Exception {
        verify(mockStringProvider).getString(mockTroubleShootContentConfiguration.getConnectWrongPhoneQuestion(), mockBaseContentConfiguration.getDeviceName());
    }

    @Test
    public void itShouldGiveConnectWrongPhoneImage() throws Exception {
        verify(mockStringProvider).getImageResource(mockTroubleShootContentConfiguration.getConnectWrongPhoneImage());
    }

    @Test
    public void itShouldVerifyTrackPageName() throws Exception {
        subject.trackPageName();
        verify(mockEWSTagger).trackPage("connectToWrongPhone");
    }

}
