/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.util;

import android.support.annotation.NonNull;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Holds the AppId.
 * <p>
 * The AppId is a unique identifier for the app within the DiComm protocol.
 *
 * @publicApi
 */
public class AppIdProvider {

    /**
     * Indicates that the AppId has changed.
     */
    public interface AppIdListener {
        /**
         * Called when the AppId has changed.
         * @param appId String The appId after it was updated.
         */
        void onAppIdChanged(final String appId);
    }

    private String appId;

    private Set<AppIdListener> listeners = new CopyOnWriteArraySet<>();

    public AppIdProvider() {
        this.appId = String.format("deadbeef%08x", new Random().nextInt());
    }

    public void setAppId(@NonNull final String appId) {
        this.appId = appId;

        notifyAppIdChanged(appId);
    }

    @NonNull
    public String getAppId() {
        return this.appId;
    }

    public void addAppIdListener(final @NonNull AppIdListener listener) {
        listeners.add(listener);
    }

    public void removeAppIdListener(final @NonNull AppIdListener listener) {
        listeners.remove(listener);
    }

    private void notifyAppIdChanged(@NonNull String appId) {
        for (AppIdListener listener : listeners) {
            listener.onAppIdChanged(appId);
        }
    }
}
