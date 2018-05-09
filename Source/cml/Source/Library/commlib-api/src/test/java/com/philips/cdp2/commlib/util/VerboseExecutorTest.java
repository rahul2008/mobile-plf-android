/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.util;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class VerboseExecutorTest {

    private CountDownLatch latch;

    private VerboseExecutor executor;

    private Boolean idleness;

    @Before
    public void setUp() {
        initMocks(this);

        latch = new CountDownLatch(1);
        executor = new VerboseExecutor();
    }

    @Test
    public void givenNoWorkHasBeenScheduled_thenItIsIdle() {
        assertThat(this.executor.isIdle()).isTrue();
    }

    @Test
    public void givenNoWorkHasBeenScheduled_whenWorkIsScheduled_thenTheExecutorIsNotIdle() throws InterruptedException {

        executor.execute(generateTaskThatSetsIdleness());

        latch.await(10, TimeUnit.SECONDS);

        assertThat(idleness).isFalse();
    }

    @Test
    public void givenWorkHasBeenScheduled_whenThatWorkCompletes_thenTheExecutorIsIdle() throws Exception {
        executor = new VerboseExecutor() {

            @Override
            protected void afterExecute(final Runnable r, final Throwable t) {
                super.afterExecute(r, t);
                idleness = executor.isIdle();
                latch.countDown();
            }
        };

        executor.execute(generateEmptyTask());

        latch.await(10, TimeUnit.SECONDS);

        assertThat(idleness).isTrue();
    }

    @Test
    public void givenWorkHasBeenScheduledTwice_whenFirstOperationFinishesAndSecondIsNotYetStarted_thenTheExecutorIsNotIdle() throws Exception {
        //GIVEN

        //latch used to ensure that second Runnable will get scheduled, before the first runnable completes
        final CountDownLatch task1latch = new CountDownLatch(1);

        //latch will be counted down twice, because we execute two runnables
        latch = new CountDownLatch(2);
        executor = new VerboseExecutor() {

            @Override
            protected void afterExecute(final Runnable r, final Throwable t) {
                super.afterExecute(r, t);
                if (latch.getCount() == 2) { //here we know that we finished the first runnable, but not the second
                    idleness = executor.isIdle();
                }
                latch.countDown();
            }
        };

        final Runnable task1 = new Runnable() {

            @Override
            public void run() {
                try {
                    task1latch.await(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                }
            }
        };
        executor.execute(task1);
        executor.execute(generateEmptyTask());

        //WHEN
        task1latch.countDown();
        latch.await(10, TimeUnit.SECONDS);

        //THEN
        assertThat(idleness).isFalse();
    }

    @NonNull
    Runnable generateEmptyTask() {
        return new Runnable() {
            @Override
            public void run() {
            }
        };
    }

    @NonNull
    private Runnable generateTaskThatSetsIdleness() {
        return new Runnable() {
            @Override
            public void run() {
                idleness = executor.isIdle();
                latch.countDown();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        };
    }
}
