/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.helper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class ScheduledThreadPoolExecutorTest {

    private MockedScheduledThreadPoolExecutor mockedScheduledThreadPoolExecutor;

    @Before
    public void setUp() {
        mockedScheduledThreadPoolExecutor = new MockedScheduledThreadPoolExecutor();
    }

    @Test
    public void testExecute() {
        final int[] count = {0};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                count[0]++;
            }
        };
        mockedScheduledThreadPoolExecutor.getMock().execute(runnable);
        assertEquals(1, count[0]);
    }

    @Test
    public void testSchedule() {
        final int[] count = {0};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                count[0]++;
            }
        };
        mockedScheduledThreadPoolExecutor.getMock().schedule(runnable, 1000l, TimeUnit.MILLISECONDS);
        assertEquals(0, count[0]);
        mockedScheduledThreadPoolExecutor.executeFirstScheduledExecution();
        assertEquals(1, count[0]);
    }

    @Test
    public void testScheduleWithDifferentTimeout() {
        final int[] count = {0};
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                count[0] += 1;
            }
        };
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                count[0] += 2;
            }
        };
        Runnable runnable4 = new Runnable() {
            @Override
            public void run() {
                count[0] += 4;
            }
        };
        mockedScheduledThreadPoolExecutor.getMock().schedule(runnable1, 1000l, TimeUnit.MILLISECONDS);
        mockedScheduledThreadPoolExecutor.getMock().schedule(runnable2, 500l, TimeUnit.MILLISECONDS);
        mockedScheduledThreadPoolExecutor.getMock().schedule(runnable4, 2000l, TimeUnit.MILLISECONDS);
        assertEquals(0, count[0]);
        mockedScheduledThreadPoolExecutor.executeFirstScheduledExecution();
        assertEquals(2, count[0]);
        mockedScheduledThreadPoolExecutor.executeFirstScheduledExecution();
        assertEquals(3, count[0]);
        mockedScheduledThreadPoolExecutor.executeFirstScheduledExecution();
        assertEquals(7, count[0]);
    }

    @Test
    public void testScheduleWithDifferentTimeoutAndCancel() {
        final int[] count = {0};
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                count[0] += 1;
            }
        };
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                count[0] += 2;
            }
        };
        Runnable runnable4 = new Runnable() {
            @Override
            public void run() {
                count[0] += 4;
            }
        };
        mockedScheduledThreadPoolExecutor.getMock().schedule(runnable1, 1000l, TimeUnit.MILLISECONDS);
        ScheduledFuture<?> scheduledFuture = mockedScheduledThreadPoolExecutor.getMock().schedule(runnable2, 500l, TimeUnit.MILLISECONDS);
        mockedScheduledThreadPoolExecutor.getMock().schedule(runnable4, 2000l, TimeUnit.MILLISECONDS);
        scheduledFuture.cancel(true);

        assertEquals(0, count[0]);
        mockedScheduledThreadPoolExecutor.executeFirstScheduledExecution();
        assertEquals(1, count[0]);
        mockedScheduledThreadPoolExecutor.executeFirstScheduledExecution();
        assertEquals(5, count[0]);
        mockedScheduledThreadPoolExecutor.executeFirstScheduledExecution();
        assertEquals(5, count[0]);
    }
}
