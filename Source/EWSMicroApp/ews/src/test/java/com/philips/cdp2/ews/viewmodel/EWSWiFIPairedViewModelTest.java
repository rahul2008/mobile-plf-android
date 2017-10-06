/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.common.callbacks.FragmentCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSCallbackNotifier.class})

public class EWSWiFIPairedViewModelTest {

    @InjectMocks private EWSWiFIPairedViewModel subject;

    @Mock private FragmentCallback mockFragmentCallback;
    @Mock private EWSCallbackNotifier callbackNotifierMock;

    @Before
    public void setup() {
        initMocks(this);

        PowerMockito.mockStatic(EWSCallbackNotifier.class);
        when(EWSCallbackNotifier.getInstance()).thenReturn(callbackNotifierMock);

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
}