package com.pins.philips.shinelib.framework;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SingleThreadEventDispatcher {
    private static final String TAG = SingleThreadEventDispatcher.class.getSimpleName();

//    public static final String ALL_EVENT_HANDLER_LIST_NAME = BaseEvent.class.getCanonicalName();

    public static class BaseEvent {
    }

    public interface EventHandler {
        public boolean handleEvent(BaseEvent event);
    }

    private boolean running;
    private boolean stopRequested;

    private Map<String, List<EventHandler>> eventNameToEventHandlersMap;
    private Set<EventHandler> eventHandlerSet;
    private Map<EventHandler, String> eventHandlerToEventNameMap;
    private BlockingQueue<BaseEvent> eventQueue;
    private Thread thread;

    public SingleThreadEventDispatcher () {
        eventNameToEventHandlersMap = new HashMap<>();
        eventHandlerToEventNameMap = new HashMap<>();
//        eventHandlerMap.put(ALL_EVENT_HANDLER_LIST_NAME, new ArrayList<EventHandler>());
        eventHandlerSet = new HashSet<>();
        eventQueue = new LinkedBlockingQueue<>();
    }

    public boolean isRunning() {
        return running;
    }

    public void eventLoop() {
        synchronized (this) {
            running = true;
            notify();
        }
        while(!stopRequested) {
            try {
                boolean handled = false;
                BaseEvent event = eventQueue.take();
                List<EventHandler> eventHandlers = eventNameToEventHandlersMap.get(event.getClass().getCanonicalName());
                if (eventHandlers == null) {
                    Log.e(TAG, "No event handler registered for: " + event.getClass().getCanonicalName());
                    continue;
                }
                for (EventHandler handler: eventHandlers) {
                    handled = handler.handleEvent(event);
                    if (handled) {
                        break; // Stop calling handlers when the event is processed
                    }
                }
//                if (!handled) {
//                    eventHandlers = eventHandlerMap.get(ALL_EVENT_HANDLER_LIST_NAME);
//                    for (EventHandler handler: eventHandlers) {
//                        handled = handler.handleEvent(event);
//                        if (handled) {
//                            break; // Stop calling handlers when the event is processed
//                        }
//                    }
//                }
            } catch (InterruptedException e) {
                // No problem just go into the loop again
                e.printStackTrace();
            }
        }
        running = false;
    }

    public synchronized boolean start() {
        if (running) {
            return false;
        }
        stopRequested = false;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                eventLoop();
            }
        });
        thread.start();
        while(!running) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean stop() {
        if (!running) {
            return false;
        }
        stopRequested = true;
        thread.interrupt();
        boolean joined = false;
        while (!joined) {
            try {
                thread.join();
                joined = true;
            } catch (InterruptedException e) {
            }
        }
        return true;
    }

    public boolean register(EventHandler eventHandler, Class<? extends BaseEvent> eventClass) {
        if (eventHandlerSet.contains(eventHandler)) {
            return false;
        }
        List<EventHandler> eventHandlers = eventNameToEventHandlersMap.get(eventClass.getCanonicalName());
        if (eventHandlers == null) {
            eventHandlers = new ArrayList<>();
            eventNameToEventHandlersMap.put(eventClass.getCanonicalName(), eventHandlers);
        }
        eventHandlerSet.add(eventHandler);
        eventHandlerToEventNameMap.put(eventHandler, eventClass.getCanonicalName());
        boolean result = eventHandlers.add(eventHandler);
        return result;
    }

    public boolean unregister(EventHandler eventHandler) {
        String eventName = eventHandlerToEventNameMap.get(eventHandler);
        if (eventName == null) {
            return false;
        }
        List<EventHandler> eventHandlers = eventNameToEventHandlersMap.get(eventName); //eventHandlerMap.get(ALL_EVENT_HANDLER_LIST_NAME);
        if (eventHandlers == null) {
            return false;
        }
        eventHandlerToEventNameMap.remove(eventHandler);
        eventHandlerSet.remove(eventHandler);
        boolean result = eventHandlers.remove(eventHandler);
        return result;
    }

    public boolean queueEvent(BaseEvent event) {
        if (eventQueue == null) {
            return false;
        }
        return eventQueue.offer(event);
    }
}
