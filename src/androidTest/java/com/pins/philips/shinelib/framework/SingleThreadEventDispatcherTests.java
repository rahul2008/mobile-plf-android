package com.pins.philips.shinelib.framework;

import android.test.AndroidTestCase;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SingleThreadEventDispatcherTests extends AndroidTestCase {
    private SingleThreadEventDispatcher singleThreadEventDispatcher;

    private class TestEventHandler implements SingleThreadEventDispatcher.EventHandler {
        private int handlerCalledCount;
        @Override
        public boolean handleEvent(SingleThreadEventDispatcher.BaseEvent event) {
            synchronized (SingleThreadEventDispatcherTests.this) {
                handlerCalledCount++;
                SingleThreadEventDispatcherTests.this.notify();
            }
            return true;
        }

        public void resetHandlerCalledCount() {
            handlerCalledCount = 0;
        }
        public int getHandlerCalledCount() {
            return handlerCalledCount;
        }
    }

    @Override
    public void setUp() {
        singleThreadEventDispatcher = new SingleThreadEventDispatcher();
    }

    public void testCreateAnInstance() {
        assertNotNull(singleThreadEventDispatcher);
    }

    public void testStartTheInstance() {
        singleThreadEventDispatcher.start();
        assertTrue(singleThreadEventDispatcher.isRunning());
    }

    public void testStopTheInstance() {
        singleThreadEventDispatcher.start();
        singleThreadEventDispatcher.stop();
        assertFalse(singleThreadEventDispatcher.isRunning());
    }

    public void testRegisterHandler() {
        TestEventHandler eventHandler = new TestEventHandler();
        assertTrue(singleThreadEventDispatcher.register(eventHandler, SingleThreadEventDispatcher.BaseEvent.class));
    }

    public void testCannotRegisterHandlerTwice() {
        TestEventHandler eventHandler = new TestEventHandler();
        assertTrue(singleThreadEventDispatcher.register(eventHandler, SingleThreadEventDispatcher.BaseEvent.class));
        assertFalse(singleThreadEventDispatcher.register(eventHandler, SingleThreadEventDispatcher.BaseEvent.class));
    }

    public void testUnregisterHandler() {
        TestEventHandler eventHandler = new TestEventHandler();
        singleThreadEventDispatcher.register(eventHandler, SingleThreadEventDispatcher.BaseEvent.class);
        assertTrue(singleThreadEventDispatcher.unregister(eventHandler));
    }

    public void testCannotUnregisterHandlerUnregisteredHandler() {
        TestEventHandler eventHandler = new TestEventHandler();
        assertFalse(singleThreadEventDispatcher.unregister(eventHandler));
    }

    public void testCannotUnregisterHandlerTwice() {
        TestEventHandler eventHandler = new TestEventHandler();
        singleThreadEventDispatcher.register(eventHandler, SingleThreadEventDispatcher.BaseEvent.class);
        assertTrue(singleThreadEventDispatcher.unregister(eventHandler));
        assertFalse(singleThreadEventDispatcher.unregister(eventHandler));
    }

    public void testQueueAnEvent() {
        SingleThreadEventDispatcher.BaseEvent event = new SingleThreadEventDispatcher.BaseEvent();
        assertTrue(singleThreadEventDispatcher.queueEvent(event));
    }

    public void testEventIsDispatched() {
        TestEventHandler eventHandler = new TestEventHandler();

        singleThreadEventDispatcher.start();

        singleThreadEventDispatcher.register(eventHandler, SingleThreadEventDispatcher.BaseEvent.class);

        SingleThreadEventDispatcher.BaseEvent event = new SingleThreadEventDispatcher.BaseEvent();
        singleThreadEventDispatcher.queueEvent(event);

        synchronized (this) {
            if (eventHandler.getHandlerCalledCount() == 0) {
                try {
                    this.wait(10000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        assertEquals(1, eventHandler.getHandlerCalledCount());
    }

    public class ThreadVerifyingEventHandler extends TestEventHandler {
        private long threadId;
        @Override
        public boolean handleEvent(SingleThreadEventDispatcher.BaseEvent event) {
            boolean result = super.handleEvent(event);
            if (threadId == 0) {
                threadId = Thread.currentThread().getId();
            } else {
                assertEquals(threadId, Thread.currentThread().getId());
            }
            return result;
        }
    }

    public void testMultipleEventsAreDispatchedOnTheSameThread() {
        ThreadVerifyingEventHandler threadVerifyingEventHandler = new ThreadVerifyingEventHandler();
        singleThreadEventDispatcher.register(threadVerifyingEventHandler, SingleThreadEventDispatcher.BaseEvent.class);

        SingleThreadEventDispatcher.BaseEvent event = new SingleThreadEventDispatcher.BaseEvent();
        singleThreadEventDispatcher.queueEvent(event);
        singleThreadEventDispatcher.queueEvent(event);
        singleThreadEventDispatcher.queueEvent(event);

        singleThreadEventDispatcher.start(); // Three events are queued now lets verify that they execute on the same thread

        synchronized (this) {
            long startTime = System.currentTimeMillis();
            while (threadVerifyingEventHandler.getHandlerCalledCount() != 3 && System.currentTimeMillis() - startTime < 10000l) {
                try {
                    this.wait(10000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        assertEquals(3, threadVerifyingEventHandler.getHandlerCalledCount());
    }

    public class TestEvent extends SingleThreadEventDispatcher.BaseEvent {
    }

    public void testRegisteringForASpecificEventClass() {
        TestEventHandler eventHandler = new TestEventHandler();
        TestEventHandler testEventHandler = new TestEventHandler();

        singleThreadEventDispatcher.start();

        singleThreadEventDispatcher.register(eventHandler, SingleThreadEventDispatcher.BaseEvent.class);
        singleThreadEventDispatcher.register(testEventHandler, TestEvent.class);

        SingleThreadEventDispatcher.BaseEvent event = new SingleThreadEventDispatcher.BaseEvent();
        singleThreadEventDispatcher.queueEvent(event);
        event = new TestEvent();
        singleThreadEventDispatcher.queueEvent(event);

        synchronized (this) {
            long startTime = System.currentTimeMillis();
            while (testEventHandler.getHandlerCalledCount() == 0 && System.currentTimeMillis() - startTime < 10000l) {
                try {
                    this.wait(10000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        assertEquals(1, eventHandler.getHandlerCalledCount());
        assertEquals(1, testEventHandler.getHandlerCalledCount());
    }

    public void testDontReceiveEventsAfterUnregister() {
        TestEventHandler eventHandler = new TestEventHandler();
        TestEventHandler testEventHandler = new TestEventHandler();

        singleThreadEventDispatcher.start();

        singleThreadEventDispatcher.register(eventHandler, SingleThreadEventDispatcher.BaseEvent.class);
        singleThreadEventDispatcher.register(testEventHandler, TestEvent.class);
        singleThreadEventDispatcher.unregister(testEventHandler);

        SingleThreadEventDispatcher.BaseEvent event = new SingleThreadEventDispatcher.BaseEvent();
        singleThreadEventDispatcher.queueEvent(event);
        event = new TestEvent();
        singleThreadEventDispatcher.queueEvent(event);
        event = new SingleThreadEventDispatcher.BaseEvent();
        singleThreadEventDispatcher.queueEvent(event);

        synchronized (this) {
            long startTime = System.currentTimeMillis();
            while (eventHandler.getHandlerCalledCount() != 2 && System.currentTimeMillis() - startTime < 10000l) {
                try {
                    this.wait(10000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        assertEquals(2, eventHandler.getHandlerCalledCount());
        assertEquals(0, testEventHandler.getHandlerCalledCount());
    }
}
