/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.core.events;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBFetchRequestListner;

public class LoadLastMomentRequest extends Event {
    @NonNull
    private final String type;
    private final DBFetchRequestListner<Moment> dbFetchRequestListener;

    public LoadLastMomentRequest(@NonNull String type, DBFetchRequestListner<Moment> dbRequestListener) {
        this.type = type;
        this.dbFetchRequestListener = dbRequestListener;
    }

    public DBFetchRequestListner<Moment> getDbFetchRequestListner() {
        return dbFetchRequestListener;
    }

    @NonNull
    public String getType() {
        return type;
    }
}
