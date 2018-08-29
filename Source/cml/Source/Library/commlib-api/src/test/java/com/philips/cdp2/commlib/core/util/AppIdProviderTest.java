package com.philips.cdp2.commlib.core.util;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AppIdProviderTest {

    private static final String AWESOME_APP_ID = "AwesomeApp";

    private AppIdProvider provider;

    @Mock
    private AppIdProvider.AppIdListener mockedListener;

    @Mock
    private Handler mockedHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        HandlerProvider.enableMockedHandler(mockedHandler);
        provider = new AppIdProvider();
    }

    @Test
    public void givenAppIdListenerIsRegistered_whenAppIdIsSet_thenListenerIsNotifiedOnMainThread() throws Exception {
        provider.addAppIdListener(mockedListener);

        provider.setAppId(AWESOME_APP_ID);

        ArgumentCaptor<Runnable> notifyRunnable = ArgumentCaptor.forClass(Runnable.class);
        verify(mockedHandler).post(notifyRunnable.capture());
        notifyRunnable.getValue().run();
        verify(mockedListener).onAppIdChanged(AWESOME_APP_ID);
    }
}