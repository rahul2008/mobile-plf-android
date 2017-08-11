/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.core;

import android.support.annotation.NonNull;

import com.philips.platform.core.events.Event;

public interface Eventing {
    void post(@NonNull final Event event);

    void postSticky(@NonNull final Event event);

    void register(@NonNull final Object subscriber);

    void unregister(@NonNull final Object subscriber);

    boolean isRegistered(@NonNull final Object subscriber);

    void removeSticky(@NonNull final Event event);
}
