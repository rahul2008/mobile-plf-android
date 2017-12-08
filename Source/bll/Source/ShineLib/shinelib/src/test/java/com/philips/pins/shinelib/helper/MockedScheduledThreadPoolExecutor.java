package com.philips.pins.shinelib.helper;

import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;

public class MockedScheduledThreadPoolExecutor {
    private final ScheduledThreadPoolExecutor mockedScheduledThreadPoolExecutor;
    private final List<ScheduledExecution> scheduledExecutions;

    public MockedScheduledThreadPoolExecutor() {
        scheduledExecutions = new ArrayList<>();
        mockedScheduledThreadPoolExecutor = mock(ScheduledThreadPoolExecutor.class, new ThrowsException(new RuntimeException("Function not stubbed with doReturn(..).when(mock).method();")));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((Runnable)invocation.getArguments()[0]).run();
                return null;
            }
        }).when(mockedScheduledThreadPoolExecutor).execute(any(Runnable.class));
        doAnswer(new Answer<ScheduledFuture<?>>() {
            @Override
            public ScheduledFuture<?> answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = (Runnable) invocation.getArguments()[0];
                long delay = (long) invocation.getArguments()[1];
                TimeUnit timeUnit = (TimeUnit) invocation.getArguments()[2];
                return addScheduledExecutionToList(runnable, delay, timeUnit);
            }
        }).when(mockedScheduledThreadPoolExecutor).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
    }
    public ScheduledThreadPoolExecutor getMock() {
        return mockedScheduledThreadPoolExecutor;
    }
    private ScheduledFuture<?> addScheduledExecutionToList(Runnable runnable, long delay, TimeUnit timeUnit) {
        ScheduledFuture<?> scheduledFuture = new ScheduledFuture<Object>() {
            public boolean cancelled;

            @Override
            public long getDelay(TimeUnit unit) {
                return 0;
            }

            @Override
            public int compareTo(Delayed another) {
                return 0;
            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                this.cancelled = true;
                return true;
            }

            @Override
            public boolean isCancelled() {
                return cancelled;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public Object get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };

        ScheduledExecution scheduledExecution = new ScheduledExecution(runnable, delay, timeUnit, scheduledFuture);
        scheduledExecutions.add(scheduledExecution);
        return scheduledFuture;
    }
    public void executeFirstScheduledExecution() {
        if (!scheduledExecutions.isEmpty()) {
            Collections.sort(scheduledExecutions);
            ScheduledExecution scheduledExecution = scheduledExecutions.remove(0);
            if (!scheduledExecution.scheduledFuture.isCancelled()) {
                scheduledExecution.runnable.run();
            } else {
                executeFirstScheduledExecution();
            }
        }
    }

    private static class ScheduledExecution implements Comparable<ScheduledExecution> {
        private final Runnable runnable;
        private final long delay;
        private final TimeUnit timeUnit;
        private final ScheduledFuture<?> scheduledFuture;

        public ScheduledExecution(Runnable runnable, long delay, TimeUnit timeUnit, ScheduledFuture<?> scheduledFuture) {
            this.runnable = runnable;
            this.delay = delay;
            this.timeUnit = timeUnit;
            this.scheduledFuture = scheduledFuture;
        }

        @Override
        public int compareTo(ScheduledExecution that) {
            if (this.delay < that.delay) return -1; // this before that
            if (this.delay > that.delay) return 1; // this after that
            return 0;
        }
    }
}

