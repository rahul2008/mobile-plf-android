/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

public class BackendMomentListSaveRequest extends ListEvent<Moment> {
    @Nullable
    private final DBRequestListener<Moment> dbRequestListener;
    @NonNull
    private final List<Moment> mMomentList;

    public BackendMomentListSaveRequest(@NonNull final List<Moment> dataList, @Nullable DBRequestListener<Moment> dbChangeListener) {
        super(dataList);
        this.dbRequestListener = dbChangeListener;
        this.mMomentList = dataList;
    }

    @Nullable
    public DBRequestListener<Moment> getDBRequestListener() {
        return dbRequestListener;
    }

    @NonNull
    public List<Moment> getMomentList() {
        return mMomentList;
    }
}
