package com.philips.platform.ews.troubleshooting.networknotlisted;

import com.philips.platform.ews.R;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class NetworkNotListedViewModelTest {

    private NetworkNotListedViewModel viewModel;
    @Mock
    private Navigator mockNavigator;
    @Mock
    private BaseContentConfiguration mockBaseContentConfiguration;
    @Mock
    private StringProvider mockStringProvider;
    @Mock
    private EWSTagger mockEWSTagger;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        viewModel = new NetworkNotListedViewModel(mockNavigator, mockBaseContentConfiguration, mockStringProvider, mockEWSTagger);
    }

    @Test
    public void itShouldGiveStepOneText() throws Exception {
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(2131362066);
        viewModel.getStepOneText(mockBaseContentConfiguration);
        verify(mockStringProvider).getString(R.string.label_ews_network_not_listed_instruction_1, mockBaseContentConfiguration.getDeviceName());
    }

    @Test
    public void itShouldGiveStepTwoText() throws Exception {
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(2131362066);
        viewModel.getStepTwoText(mockBaseContentConfiguration);
        verify(mockStringProvider).getString(R.string.label_ews_network_not_listed_instruction_2, mockBaseContentConfiguration.getDeviceName());
    }

    @Test
    public void itShouldNavigateBackWhenOkButtonIsClicked() throws Exception {
        viewModel.onOkClicked();

        verify(mockNavigator).navigateBack();
    }

    @Test
    public void itShouldVerifyTrackPageName() throws Exception {
        viewModel.trackPageName();

        verify(mockEWSTagger).trackPage("networkNotListed");
    }
}