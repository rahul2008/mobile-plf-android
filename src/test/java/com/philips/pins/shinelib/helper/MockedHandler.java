package com.philips.pins.shinelib.helper;

import android.os.Handler;

import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;

public class MockedHandler {
    private final Handler mockedHandler;
    private final List<ScheduledExecution> scheduledExecutions;

    public MockedHandler() {
        scheduledExecutions = new ArrayList<>();
        mockedHandler = mock(Handler.class, new ThrowsException(new RuntimeException("Function not stubbed with doReturn(..).when(mock).method();")));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((Runnable)invocation.getArguments()[0]).run();
                return null;
            }
        }).when(mockedHandler).post(any(Runnable.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = (Runnable) invocation.getArguments()[0];
                long delay = (long) invocation.getArguments()[1];
                addScheduledExecutionToList(runnable, delay);
                return null;
            }
        }).when(mockedHandler).postDelayed(any(Runnable.class), anyLong());
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = (Runnable) invocation.getArguments()[0];
                removeScheduledExecutionFromList(runnable);
                return null;
            }
        }).when(mockedHandler).removeCallbacks(any(Runnable.class));
    }

    public Handler getMock() {
        return mockedHandler;
    }

    private void addScheduledExecutionToList(Runnable runnable, long delay) {
        ScheduledExecution scheduledExecution = new ScheduledExecution(runnable, delay);
        scheduledExecutions.add(scheduledExecution);
    }

    private void removeScheduledExecutionFromList(Runnable runnable) {
        for (int index = 0; index < scheduledExecutions.size(); index++) {
            if (scheduledExecutions.get(index).runnable.equals(runnable)) {
                scheduledExecutions.remove(index);
                return;
            }
        }
    }

    public void executeFirstScheduledExecution() {
        if (!scheduledExecutions.isEmpty()) {
            Collections.sort(scheduledExecutions);
            ScheduledExecution scheduledExecution = scheduledExecutions.remove(0);
            scheduledExecution.runnable.run();
        }
    }

    public int getScheduledExecutionCount() {
        return scheduledExecutions.size();
    }

    private static class ScheduledExecution implements Comparable<ScheduledExecution> {
        private final Runnable runnable;
        private final long delay;

        public ScheduledExecution(Runnable runnable, long delay) {
            this.runnable = runnable;
            this.delay = delay;
        }

        @Override
        public int compareTo(ScheduledExecution that) {
            if (this.delay < that.delay) return -1; // this before that
            if (this.delay > that.delay) return 1; // this after that
            return 0;
        }
    }
}

