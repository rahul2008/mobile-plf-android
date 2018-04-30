package com.philips.cdp2.commlib.util;

import android.support.annotation.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class VerboseLinkedBlockingQueueTest {

    @Mock
    VerboseLinkedBlockingQueueListener listenerMock;

    VerboseLinkedBlockingQueue queue;

    @Before
    public void setUp() {
        initMocks(this);

        queue = new VerboseLinkedBlockingQueue<Runnable>();
    }

    @Test
    public void givenListenerIsNotSet_whenOperationIsTaken_thenListenerIsNotCalled() throws InterruptedException {
        Runnable element = generateTask();
        queue.add(element);
        queue.take();

        verify(listenerMock, never()).onBeforeTake(element);
    }

    @Test
    public void givenListenerIsSet_whenQueueIsEmpty_thenListenerIsCalled() throws InterruptedException {
        queue.take();

        verify(listenerMock, times(1)).onQueueEmpty();
    }

    @Test
    public void givenListenerIsSet_whenOperationIsTaken_thenListenerIsCalled() throws InterruptedException {
        queue.setListener(listenerMock);

        Runnable element = generateTask();
        queue.add(element);
        queue.take();

        verify(listenerMock, times(1)).onBeforeTake(element);
    }

    @NonNull
    private Runnable generateTask() {
        return new Runnable() {
            @Override
            public void run() {}
        };
    }
}