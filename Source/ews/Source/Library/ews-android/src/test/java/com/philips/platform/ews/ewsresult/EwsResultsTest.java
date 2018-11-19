/**
 * Copyright (c) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.platform.ews.ewsresult;

import android.content.Intent;

import com.philips.platform.ews.EWSActivity;
import com.philips.platform.ews.connectionsuccessful.ConnectionSuccessfulFragment;
import com.philips.platform.ews.microapp.EwsResultListener;
import com.philips.platform.ews.startconnectwithdevice.StartConnectWithDeviceFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class EwsResultsTest {

    @Mock
    private EwsResultListener mockEwsResultListener;
    private EWSActivity ewsActivitySpy;
    @InjectMocks
    private StartConnectWithDeviceFragment mockStartConnectWithDeviceFragment;
    @InjectMocks
    private ConnectionSuccessfulFragment mockConnectionSuccessfulFragment;

    @Before
    public void setup() {
        initMocks(this);
        ewsActivitySpy = spy(new EWSActivity());
    }

    @Test
    public void itShouldCallOnEWSCancelledWhenStartConnectWithDeviceFragmentCallsPerformEWSCancelAndEwsResultListenerIsNotNull() {
        mockStartConnectWithDeviceFragment.performEWSCancel(mockEwsResultListener);
        verify(mockEwsResultListener).onEWSCancelled();
    }

    @Test
    public void itShouldCallTheResultListenerWhenfinishMicroAppIsCalled() {
        mockConnectionSuccessfulFragment.finishMicroApp(mockEwsResultListener);
        verify(mockEwsResultListener, times(1)).onEWSFinishSuccess();
    }

    @Test
    public void ewsActivityWillPassResultBackToTheCallingActivityOnEwsFinishSuccess() {
        ewsActivitySpy.onEWSFinishSuccess();
        verify(ewsActivitySpy, times(1)).setResult(EwsResultListener.EWS_RESULT_SUCCESS, null);
    }

    @Test
    public void ewsActivityWillPassResultBackToTheCallingActivityOnEwsError() {
        ewsActivitySpy.onEWSError(12345);
        verify(ewsActivitySpy, times(1)).setResult(eq(EwsResultListener.EWS_RESULT_FAILURE), any(Intent.class));
    }

    @Test
    public void ewsActivityWillPassResultBackToTheCallingActivityOnEWSCancelled() {
        ewsActivitySpy.onEWSCancelled();
        verify(ewsActivitySpy, times(1)).setResult(EwsResultListener.EWS_RESULT_CANCEL, null);
    }
}
