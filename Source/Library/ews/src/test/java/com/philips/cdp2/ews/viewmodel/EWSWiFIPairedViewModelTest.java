/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.common.callbacks.FragmentCallback;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.connectionsuccessful.ConnectionSuccessfulViewModel;
import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSCallbackNotifier.class})

public class EWSWiFIPairedViewModelTest {

    @InjectMocks private ConnectionSuccessfulViewModel subject;

    @Mock private FragmentCallback mockFragmentCallback;
    @Mock private EWSCallbackNotifier callbackNotifierMock;
    @Mock
    private BaseContentConfiguration mockBaseContentConfig;

    @Mock
    private StringProvider mockStringProvider;

    @Before
    public void setup() {
        initMocks(this);

        PowerMockito.mockStatic(EWSCallbackNotifier.class);
        when(EWSCallbackNotifier.getInstance()).thenReturn(callbackNotifierMock);
        when(mockBaseContentConfig.getDeviceName()).thenReturn(R.string.ews_device_name_default);
        subject.setFragmentCallback(mockFragmentCallback);
    }

    @Test
    public void shouldGiveOnSuccessCallbackWhenClickedOnStartButton() throws Exception {
        subject.onStartClicked();

        verify(callbackNotifierMock).onSuccess();
    }

    @Test
    public void shouldFinishMicroAppWhenOnStartClicked() throws Exception {
        subject.onStartClicked();

        verify(mockFragmentCallback).finishMicroApp();
    }

    @Test
    public void shouldNotFinishMicroAppWhenOnStartClickedWhenCallbackIsNull() throws Exception {
        subject.setFragmentCallback(null);

        subject.onStartClicked();

        verify(mockFragmentCallback, never()).finishMicroApp();
    }

    @Test
    public void itShouldVerifyTitleForViewModel() throws Exception {
        subject.getTitle(mockBaseContentConfig);
        verify(mockStringProvider).getString(R.string.label_ews_succesful_body,
                mockBaseContentConfig.getDeviceName());

    }

    @Test
    public void itShouldVerifyTitleForViewMatches() throws Exception{
        when(mockStringProvider.getString(R.string.label_ews_succesful_body, mockBaseContentConfig.getDeviceName())).thenReturn("device name");
        assertEquals("device name", mockStringProvider.getString(R.string.label_ews_succesful_body,
                mockBaseContentConfig.getDeviceName()));
    }

}