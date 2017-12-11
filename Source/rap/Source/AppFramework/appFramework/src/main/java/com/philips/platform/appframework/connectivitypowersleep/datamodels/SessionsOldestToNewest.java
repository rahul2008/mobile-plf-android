/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep.datamodels;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SessionsOldestToNewest {

    private final Comparator<? super Session> dateAsc = new Comparator<Session>() {
        @Override
        public int compare(final Session o1, final Session o2) {
            if (o1 == null || o2 == null) {
                throw new IllegalStateException("No null sessions allowed");
            }
            return o1.getDate().compareTo(o2.getDate());
        }
    };

    private List<Session> sortedSessions;

    public SessionsOldestToNewest(@NonNull final List<Session> sortedSessions) {
        Collections.sort(sortedSessions, dateAsc);
        this.sortedSessions = Collections.unmodifiableList(sortedSessions);
    }

    @NonNull
    public List<Session> getSortedList() {
        return sortedSessions;
    }

    public int size() {
        return sortedSessions.size();
    }

    public boolean isEmpty() {
        return sortedSessions.isEmpty();
    }

    /* THOSE ARE HANDY FOR TESTING, APPARENTLY */

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public SessionsOldestToNewest(Session... sessions) {
        this(sessions.length <= 0 ? Collections.<Session>emptyList() : Arrays.asList(sessions));
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public Session get(final int position) {
        return sortedSessions.get(position);
    }
}
