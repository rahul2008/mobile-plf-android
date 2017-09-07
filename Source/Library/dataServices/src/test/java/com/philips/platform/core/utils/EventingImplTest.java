package com.philips.platform.core.utils;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.philips.platform.core.events.Event;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EventingImplTest {

    @Mock
    private EventBus eventBusMock;

    @Mock
    private Event eventMock;

    private EventingImpl eventingImpl;

    @Mock
    private Handler handlerMock;

    private Object subscriber = new Object();

    @Before
    public void setUp() {
        initMocks(this);

        eventingImpl = new EventingImpl(eventBusMock, handlerMock);
    }

    @Test
    public void ShouldForwardPostOnMainThread_WhenPostIsCalled() {
        eventingImpl.post(eventMock);
        // verify(eventBusMock).post(eventMock);
    }

    @Test
    public void ShouldForwardRegister_WhenRegisterIsCalled() {
        eventingImpl.register(subscriber);

        verify(eventBusMock).register(subscriber);
    }

    @Test
    public void ShouldForwardUnregister_WhenUnregisterIsCalled() {
        eventingImpl.unregister(subscriber);

        verify(eventBusMock).unregister(subscriber);
    }

    @Test
    public void ShouldForwardIsRegistered_WhenIsRegisteredIsCalled() {
        when(eventBusMock.isRegistered(subscriber)).thenReturn(true);

        boolean registered = eventingImpl.isRegistered(subscriber);

        assertThat(registered).isTrue();
    }

    @Test
    public void ShouldPostSticky_WhenPostStickyIsCalled() {
        eventingImpl.postSticky(eventMock);
    }

    @Test
    public void ShouldregisterSticky_WhenregisterStickyIsCalled() {
        eventingImpl.register(subscriber);
    }

    @Test
    public void ShouldremoveSticky_WhenremoveStickyIsCalled() {
        eventingImpl.removeSticky(eventMock);
    }

    // -------------

    @NonNull
    private Handler getHandler() {
        final Application application = RuntimeEnvironment.application;
        final Looper mainLooper = application.getMainLooper();
        return new Handler(mainLooper);
    }
}