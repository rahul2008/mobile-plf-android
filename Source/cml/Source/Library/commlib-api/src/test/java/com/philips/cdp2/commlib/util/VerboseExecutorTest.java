package com.philips.cdp2.commlib.util;

import android.support.annotation.NonNull;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VerboseExecutorTest {

    VerboseExecutor executor;

    @Before
    public void setUp() {
        this.executor = new VerboseExecutor();
    }

    @Test
    public void givenQueueIsEmpty_whenOperationIsNotStarted_thenIsIdle() {
        assertThat(this.executor.isIdle()).isTrue();
    }

    @Test
    public void givenQueueIsEmpty_whenOperationIsStarted_thenIsNotIdle() {
        executor.execute(generateTask());
    }

    @Test
    public void givenQueueIsNotEmpty_whenOperationIsNotStarted_thenIsNotIdle() {
        executor.getQueue().add(generateTask());

        assertThat(this.executor.isIdle()).isFalse();
    }

    @Test
    public void givenQueueIsNotEmpty_whenOperationIsStarted_thenIsNotIdle() {
        executor.getQueue().add(generateTask());

        executor.execute(generateTask());

        assertThat(this.executor.isIdle()).isFalse();
    }

    @NonNull
    private Runnable generateTask() {
        return new Runnable() {
            @Override
            public void run() {
                assertThat(executor.isIdle()).isFalse();
            }
        };
    }
}