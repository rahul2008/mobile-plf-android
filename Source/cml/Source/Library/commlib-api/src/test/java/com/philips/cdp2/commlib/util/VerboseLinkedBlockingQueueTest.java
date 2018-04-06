package com.philips.cdp2.commlib.util;

import android.support.annotation.NonNull;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VerboseLinkedBlockingQueueTest {

    VerboseLinkedBlockingQueueListenerStub listenerStub;
    VerboseLinkedBlockingQueue queue;

    @Before
    public void setUp() {
        this.listenerStub = new VerboseLinkedBlockingQueueListenerStub();
        this.queue = new VerboseLinkedBlockingQueue<Runnable>();
        this.queue.listener = listenerStub;
    }

    @Test
    public void givenListenerIsNotSet_whenOperationIsTaken_thenListenerIsNotCalled() throws InterruptedException {
        queue.add(generateTask());
        queue.take();

        assertThat(this.listenerStub.beforeTakingOperationCalled).isTrue();
    }

    @Test
    public void givenListenerIsSet_whenOperationIsTaken_thenListenerIsCalled() throws InterruptedException {
        this.queue.listener = listenerStub;

        queue.add(generateTask());
        queue.take();

        assertThat(this.listenerStub.beforeTakingOperationCalled).isTrue();
    }

    @NonNull
    private Runnable generateTask() {
        return new Runnable() {
            @Override
            public void run() {}
        };
    }
}

class VerboseLinkedBlockingQueueListenerStub implements VerboseLinkedBlockingQueueListener {

    boolean beforeTakingOperationCalled = false;

    @Override
    public void beforeTakingOperation() {
        beforeTakingOperationCalled = true;
    }
}