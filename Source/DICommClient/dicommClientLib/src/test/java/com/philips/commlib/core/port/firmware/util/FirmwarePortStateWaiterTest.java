/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */

package com.philips.commlib.core.port.firmware.util;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.commlib.core.port.firmware.FirmwarePort;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.DOWNLOADING;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.IDLE;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.PROGRAMMING;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwarePortStateWaiterTest {

    @Mock
    private ExecutorService executorMock;
    @Mock
    private FirmwarePort portMock;
    @Mock
    private FirmwarePortProperties portPropertiesMock;
    @Mock
    private CountDownLatch mockCountDownLatch;

    private FirmwarePortStateWaiter firmwarePortStateWaiter;
    private static final long TIMEOUT_MILLIS = 1000L;

    @Captor
    private ArgumentCaptor<DICommPortListener<FirmwarePort>> updateListenerArgumentCaptor;

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();

        doAnswer(new Answer<Future<FirmwarePortState>>() {
            @Override
            public Future<FirmwarePortState> answer(InvocationOnMock invocation) throws Throwable {
                Future future = mock(Future.class);
                try {
                    FirmwarePortState newState = (FirmwarePortState) invocation.getArgumentAt(0, Callable.class).call();
                    when(future.get()).thenReturn(newState);
                } catch (Exception e) {
                    when(future.get()).thenThrow(new ExecutionException(e));
                }

                return future;
            }
        }).when(executorMock).submit(any(Callable.class));

        when(portMock.getPortProperties()).thenReturn(portPropertiesMock);

        when(portPropertiesMock.getState()).thenReturn(IDLE);

        this.firmwarePortStateWaiter = new FirmwarePortStateWaiter(executorMock, portMock);
    }

    @Test
    public void whenPortIsInOtherStateThenWaiterShouldReturnNewState() throws StateWaitException {
        when(portPropertiesMock.getState()).thenReturn(PROGRAMMING);
        FirmwarePortState state = firmwarePortStateWaiter.waitForNextState(IDLE, TIMEOUT_MILLIS);

        assertFalse(state == IDLE);
    }

    @Test
    public void whenPortIsInOtherStateThenSubscribeIsNotCalled() throws StateWaitException {
        when(portPropertiesMock.getState()).thenReturn(PROGRAMMING);
        firmwarePortStateWaiter.waitForNextState(IDLE, TIMEOUT_MILLIS);

        verify(portMock, times(0)).subscribe();
    }

    @Test
    public void whenPortIsNotInOtherStateThenSubscribeIsCalled() throws StateWaitException {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                when(portPropertiesMock.getState()).thenReturn(DOWNLOADING);
                invocation.getArgumentAt(0, DICommPortListener.class).onPortUpdate(portMock);
                return null;
            }
        }).when(portMock).addPortListener(any(DICommPortListener.class));

        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                when(mockCountDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)).thenReturn(true);
                return null;
            }
        }).when(mockCountDownLatch).countDown();

        firmwarePortStateWaiter.waitForNextState(IDLE, TIMEOUT_MILLIS);

        verify(portMock).subscribe();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenStateChangedThenReturnsNewState() throws StateWaitException {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                when(portPropertiesMock.getState()).thenReturn(DOWNLOADING);
                invocation.getArgumentAt(0, DICommPortListener.class).onPortUpdate(portMock);
                return null;
            }
        }).when(portMock).addPortListener(any(DICommPortListener.class));

        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                when(mockCountDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)).thenReturn(true);
                return null;
            }
        }).when(mockCountDownLatch).countDown();

        FirmwarePortState state = firmwarePortStateWaiter.waitForNextState(IDLE, TIMEOUT_MILLIS);

        assertTrue(state == DOWNLOADING);
    }

    @Test(expected = StateWaitException.class)
    public void whenTimeoutThenThrowsException() throws StateWaitException {
        firmwarePortStateWaiter.waitForNextState(IDLE, TIMEOUT_MILLIS);
    }

    private FirmwarePortStateWaiter createFirmwarePortStateWaiter() {
        return new FirmwarePortStateWaiter(this.executorMock, this.portMock) {
            @NonNull
            @Override
            CountDownLatch createCountDownLatch() {
                return mockCountDownLatch;
            }
        };
    }
}
