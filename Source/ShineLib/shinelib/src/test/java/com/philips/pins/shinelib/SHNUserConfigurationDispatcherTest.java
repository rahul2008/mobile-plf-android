package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNUserConfigurationDispatcherTest {

    public static final int HANDLER_THREAD_ID = 111;
    public static final int OTHER_THREAD_ID = 222;
    public int currentThreadId = HANDLER_THREAD_ID;

    @Mock
    private SHNUserConfiguration userConfigurationMock;

    @Mock
    private Handler internalHandlerMock;

    private FutureTask futureTaskMock;

    private SHNUserConfigurationDispatcher userConfiguration;
    private Callable lastCallback;

    @Before
    public void setUp() {
        initMocks(this);

        userConfiguration = new SHNUserConfigurationDispatcher(userConfigurationMock, internalHandlerMock) {

            @Override
            long getHandlerThreadId(@NonNull final Handler internalHandler) {
                assertThat(internalHandler).isSameAs(internalHandlerMock);
                return HANDLER_THREAD_ID;
            }

            @Override
            long getCurrentThreadId() {
                return currentThreadId;
            }

            @NonNull
            @Override
            <T> FutureTask<T> getFutureTask(final Callable<T> callback) {
                lastCallback = callback;
                return (FutureTask<T>) futureTaskMock;
            }
        };
    }

    @Test
    public void whenGetClockFormatIsCalledOnInternalThread_ThenCallIsDirectlyExecuted() {
        currentThreadId = HANDLER_THREAD_ID;

        userConfiguration.getClockFormat();

        verify(userConfigurationMock).getClockFormat();
    }

    @Test
    public void whenGetClockFormatIsCalledOnInternalThread_ThenValueIsReturned() {
        currentThreadId = HANDLER_THREAD_ID;
        SHNUserConfiguration.ClockFormat expected = SHNUserConfiguration.ClockFormat._12H;
        when(userConfigurationMock.getClockFormat()).thenReturn(expected);

        SHNUserConfiguration.ClockFormat clockFormat = userConfiguration.getClockFormat();

        assertThat(clockFormat).isEqualTo(expected);
    }

    @Test
    public void whenGetClockFormatIsCalledOnOtherThread_ThenCallIsPostedViaFuture() throws ExecutionException, InterruptedException {
        currentThreadId = OTHER_THREAD_ID;
        futureTaskMock = mock(FutureTask.class);

        userConfiguration.getClockFormat();

        verify(internalHandlerMock).post(futureTaskMock);
    }

    @Test
    public void whenGetClockFormatIsCalledOnOtherThread_ThenCallIsExecutedInCallback() throws Exception {
        currentThreadId = OTHER_THREAD_ID;
        futureTaskMock = mock(FutureTask.class);

        userConfiguration.getClockFormat();

        lastCallback.call();
        verify(userConfigurationMock).getClockFormat();
    }

    @Test
    public void whenGetClockFormatIsCalledOnOtherThread_ThenStillCorrectValueIsReturned() throws Exception {
        currentThreadId = OTHER_THREAD_ID;
        SHNUserConfiguration.ClockFormat expected = SHNUserConfiguration.ClockFormat._12H;
        futureTaskMock = mock(FutureTask.class);
        when(futureTaskMock.get()).thenReturn(expected);

        SHNUserConfiguration.ClockFormat clockFormat = userConfiguration.getClockFormat();

        assertThat(clockFormat).isEqualTo(expected);
    }

    @Test
    public void whenSetClockFormatIsCalledOnInternalThread_ThenCallIsDirectlyExecuted() {
        currentThreadId = HANDLER_THREAD_ID;
        SHNUserConfiguration.ClockFormat expected = SHNUserConfiguration.ClockFormat._12H;

        userConfiguration.setClockFormat(expected);

        verify(userConfigurationMock).setClockFormat(expected);
    }

    @Test
    public void whenClearIsCalledOnInternalThread_ThenCallIsDirectlyExecuted() {
        currentThreadId = HANDLER_THREAD_ID;

        userConfiguration.clear();

        verify(userConfigurationMock).clear();
    }

    @Test
    public void whenCallThrowsException_ThenRuntimeExceptionIsThrownOnInternalThead() throws Exception {
        currentThreadId = HANDLER_THREAD_ID;
        futureTaskMock = mock(FutureTask.class);

        RuntimeException causeException = new RuntimeException();
        when(userConfigurationMock.getClockFormat()).thenThrow(causeException);

        RuntimeException actualException = null;
        try {
            userConfiguration.getClockFormat();
        } catch (RuntimeException ex) {
            actualException = ex;
        }

        assertThat(actualException.getCause()).isEqualTo(causeException);
    }

    @Test
    public void whenCallThrowsException_ThenRuntimeExceptionIsThrownOnUserThead() throws Exception {
        currentThreadId = OTHER_THREAD_ID;
        SHNUserConfiguration.ClockFormat expected = SHNUserConfiguration.ClockFormat._12H;
        futureTaskMock = mock(FutureTask.class);
        RuntimeException causeException = new RuntimeException();
        when(futureTaskMock.get()).thenThrow(causeException);

        RuntimeException actualException = null;
        try {
            userConfiguration.getClockFormat();
        } catch (RuntimeException ex) {
            actualException = ex;
        }

        assertThat(actualException.getCause()).isEqualTo(causeException);
    }
}
