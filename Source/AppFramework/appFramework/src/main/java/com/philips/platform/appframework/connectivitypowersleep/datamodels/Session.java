/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep.datamodels;

import android.os.Parcel;
import android.support.annotation.NonNull;

import java.util.Date;

public class Session implements Comparable<Session> {

    @NonNull private final Summary summary;


    public Session(@NonNull Summary summary) {
        this.summary = summary;
    }


    @NonNull
    public Summary getSummary() {
        return summary;
    }

    @NonNull
    public Date getDate() {
        return getSummary().getDate();
    }


    @Override
    public int compareTo(@NonNull Session o) {
        return getDate().compareTo(o.getDate());
    }


    protected Session(Parcel in) {
        this.summary = in.readParcelable(Summary.class.getClassLoader());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (!summary.equals(session.summary)) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = summary.hashCode();
        result = 31 * result ;
        return result;
    }
}
