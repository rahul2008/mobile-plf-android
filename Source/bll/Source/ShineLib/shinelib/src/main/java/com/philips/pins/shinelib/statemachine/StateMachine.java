package com.philips.pins.shinelib.statemachine;

import android.support.annotation.NonNull;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class StateMachine<T extends State> {

    private T state;

    private Set<StateChangedListener<T>> stateChangedListeners = new CopyOnWriteArraySet<>();

    public synchronized void setState(@NonNull  T newState) {
        if (state == newState) return;

        final T oldState = state;

        if (state != null) {
            this.state.onExit();
        }

        this.state = newState;
        this.state.onEnter();

        for (StateChangedListener<T> listener: stateChangedListeners) {
            listener.onStateChanged(oldState, newState);
        }
    }

    public T getState() {
        return state;
    }

    public void addStateListener(@NonNull final StateChangedListener<T> listener) {
        stateChangedListeners.add(listener);
    }

    public void removeStateListener(final StateChangedListener<T> listener) {
        stateChangedListeners.remove(listener);
    }
}
