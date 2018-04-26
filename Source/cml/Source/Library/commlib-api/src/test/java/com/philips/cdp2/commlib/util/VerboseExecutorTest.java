/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.util;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class VerboseExecutorTest {

    private VerboseExecutor executor;

    @Mock
    private VerboseLinkedBlockingQueueListener<Runnable> queueListenerMock;
    private CountDownLatch queueEmptyLatch;

    @Before
    public void setUp() {
        initMocks(this);

        queueEmptyLatch = new CountDownLatch(1);

        this.executor = new VerboseExecutor() {
            @Override
            VerboseLinkedBlockingQueueListener<Runnable> createQueueListener() {
                return queueListenerMock;
            }
        };
    }

    @Test
    public void givenQueueIsEmpty_whenOperationIsNotStarted_thenIsIdle() {
        assertThat(this.executor.isIdle()).isTrue();
    }

    @Test
    public void givenQueueIsEmpty_whenOperationIsStarted_thenIsNotIdle() throws InterruptedException {
        expectQueueTakes(never());

        executor.execute(generateAssertingTask());

        queueEmptyLatch.await(1L, TimeUnit.SECONDS);
    }

    @Test
    public void givenQueueIsNotEmpty_whenOperationIsNotStarted_thenIsNotIdle() {
        executor.getQueue().add(generateAssertingTask());

        assertThat(this.executor.isIdle()).isFalse();
    }

    @Test
    public void givenQueueIsNotEmpty_whenOperationIsStarted_thenIsNotIdle() throws InterruptedException {
        expectQueueTakes(times(1));

        executor.getQueue().add(generateAssertingTask());
        executor.execute(generateAssertingTask());

        queueEmptyLatch.await(1L, TimeUnit.SECONDS);
    }

    @Test
    public void givenEnoughFreeThreads_andATaskIsExecuted_thenListenerShouldNotBeNotified() throws InterruptedException {
        expectQueueTakes(never());

        executor.execute(generateAssertingTask());

        queueEmptyLatch.await(1L, TimeUnit.SECONDS);
    }

    @Test
    public void givenNotEnoughFreeThreads_andTasksAreExecuted_thenListenerShouldBeNotified() throws InterruptedException {
        expectQueueTakes(times(2));

        executor.getQueue().add(generateAssertingTask());
        executor.getQueue().add(generateAssertingTask());
        executor.execute(generateAssertingTask());

        queueEmptyLatch.await(1L, TimeUnit.SECONDS);
    }

    @NonNull
    private Runnable generateAssertingTask() {
        return new Runnable() {
            @Override
            public void run() {
                assertThat(executor.isIdle()).isFalse();
            }
        };
    }

    private void expectQueueTakes(final VerificationMode amount) {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                verify(queueListenerMock, amount).onBeforeTake(nullable(Runnable.class));
                queueEmptyLatch.countDown();

                return null;
            }
        }).when(queueListenerMock).onQueueEmpty();
    }
}
