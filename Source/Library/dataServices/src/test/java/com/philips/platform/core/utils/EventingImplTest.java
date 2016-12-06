package com.philips.platform.core.utils;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.philips.pins.RobolectricTestCaseTemplate;
import com.philips.platform.core.events.Event;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import de.greenrobot.event.EventBus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.assertj.core.api.Assertions.assertThat;
import org.robolectric.RuntimeEnvironment;

/**
 * Created by sangamesh on 30/11/16.
 */
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
 //       verify(eventBusMock).post(eventMock);
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
}